package com.example

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

// Planta ilustrativa do terminal. Posições em frações da largura (x) e da altura (y) do mapa.

private const val FIRST_PLATFORM = 10
private const val PLATFORM_COUNT = 6

private const val WALL_LEFT = 0.03f
private const val WALL_TOP = 0.03f
private const val WALL_RIGHT = 0.97f
private const val WALL_BOTTOM = 0.93f
private const val ENTRANCE_HALF_WIDTH = 0.08f

private const val APRON_INSET = 0.015f
private const val APRON_TOP = 0.05f
private const val APRON_BOTTOM = 0.25f
private const val BAYS_LEFT = 0.06f
private const val BAYS_RIGHT = 0.94f
private const val BAY_TOP = 0.075f
private const val BAY_BOTTOM = 0.235f
private const val BAY_GAP = 0.014f
private const val BAY_NUMBER_Y = 0.11f

private const val ENTRY_X = 0.5f
private const val ENTRY_Y = 0.885f
private const val CORRIDOR_Y = 0.29f
private const val ROUTE_END_Y = 0.2f

/** Ampliação do botão "Ampliar": aproxima a região da plataforma de destino. */
private const val ZOOM_SCALE = 1.8f
private const val ZOOM_FOCUS_Y = 0.3f

/** Altura do pin (da ponta ao centro da cabeça) em múltiplos do raio da cabeça. */
private const val PIN_HEIGHT_RATIO = 2.4f
private val PIN_RADIUS = 11.dp
private val CHIP_PADDING_H = 8.dp
private val CHIP_PADDING_V = 4.dp

val MapCardBackground = Color(0xFFE9ECF1)
private val MapFloor = Color(0xFFF7F8FA)
private val MapWall = Color(0xFF94A3B8)
private val MapApron = Color(0xFF3F4652)
private val MapBay = Color(0xFF5A626F)
private val MapBayText = Color(0xFFD1D5DB)
private val MapCurb = Color(0xFFFACC15)
private val MapLabel = Color(0xFF475569)

private val LabelStyle = TextStyle(color = MapLabel, fontSize = 12.sp, fontWeight = FontWeight.Medium)
private val BayNumberStyle = TextStyle(color = MapBayText, fontSize = 13.sp, fontWeight = FontWeight.Bold)
private val ChipStyle = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold)

private data class Facility(
    val name: String,
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float,
    val color: Color
)

private val FACILITIES = listOf(
    Facility("Bilheteria", 0.07f, 0.41f, 0.33f, 0.52f, Color(0xFFE0E7FF)),
    Facility("Sanitários", 0.67f, 0.41f, 0.93f, 0.52f, Color(0xFFDBEAFE)),
    Facility("Alimentação", 0.07f, 0.60f, 0.33f, 0.72f, Color(0xFFFEF3C7)),
    Facility("Lojas", 0.67f, 0.60f, 0.93f, 0.72f, Color(0xFFFCE7F3))
)

private const val BAY_WIDTH = (BAYS_RIGHT - BAYS_LEFT) / PLATFORM_COUNT

private fun bayCenterX(index: Int): Float = BAYS_LEFT + (index + 0.5f) * BAY_WIDTH

/** Índice da baia no desenho, ou null se a plataforma não estiver no mapa. */
private fun bayIndexOf(platform: String): Int? =
    platform.toIntOrNull()?.minus(FIRST_PLATFORM)?.takeIf { it in 0 until PLATFORM_COUNT }

/** Deslocamento que leva [focus] (fração) ao centro da vista com a escala [scale]. */
private fun focusTranslation(focus: Float, extent: Float, scale: Float): Float =
    (0.5f - focus) * extent * scale

/** Translação final, limitada para o mapa sempre cobrir a área visível. */
private fun zoomTranslation(focus: Float, extent: Float, scale: Float, pan: Float): Float {
    val limit = (scale - 1f) * extent / 2f
    return (focusTranslation(focus, extent, scale) + pan).coerceIn(-limit, limit)
}

@Composable
fun TerminalMap(platform: String, gate: String, isZoomed: Boolean, modifier: Modifier = Modifier) {
    val bayIndex = bayIndexOf(platform)
    val endX = bayIndex?.let(::bayCenterX) ?: ENTRY_X
    val textMeasurer = rememberTextMeasurer()

    val zoom by animateFloatAsState(if (isZoomed) 1f else 0f, label = "mapZoom")
    var pan by remember { mutableStateOf(Offset.Zero) }
    LaunchedEffect(isZoomed) { if (!isZoomed) pan = Offset.Zero }

    val transition = rememberInfiniteTransition(label = "mapAnimations")
    val pulse by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1600, easing = LinearEasing), RepeatMode.Restart),
        label = "pulse"
    )
    val flow by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(900, easing = LinearEasing), RepeatMode.Restart),
        label = "routeFlow"
    )

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .clipToBounds()
            .semantics {
                contentDescription =
                    "Mapa ilustrativo do terminal: rota da entrada do metrô até a plataforma $platform, portão $gate"
            }
            .pointerInput(isZoomed, endX) {
                if (!isZoomed) return@pointerInput
                // Ampliado: arrastar move o mapa, sem sair dos limites.
                detectDragGestures { change, drag ->
                    change.consume()
                    val w = size.width.toFloat()
                    val h = size.height.toFloat()
                    val tx = zoomTranslation(endX, w, ZOOM_SCALE, pan.x + drag.x)
                    val ty = zoomTranslation(ZOOM_FOCUS_Y, h, ZOOM_SCALE, pan.y + drag.y)
                    pan = Offset(
                        tx - focusTranslation(endX, w, ZOOM_SCALE),
                        ty - focusTranslation(ZOOM_FOCUS_Y, h, ZOOM_SCALE)
                    )
                }
            }
            .graphicsLayer {
                val scale = 1f + (ZOOM_SCALE - 1f) * zoom
                scaleX = scale
                scaleY = scale
                translationX = zoomTranslation(endX, size.width, scale, pan.x * zoom)
                translationY = zoomTranslation(ZOOM_FOCUS_Y, size.height, scale, pan.y * zoom)
            }
    ) {
        drawFloorPlan(textMeasurer)
        drawPlatforms(textMeasurer, bayIndex, pulse)
        val entry = at(ENTRY_X, ENTRY_Y)
        val end = at(endX, ROUTE_END_Y)
        drawRoute(listOf(entry, at(ENTRY_X, CORRIDOR_Y), at(endX, CORRIDOR_Y), end), flow)
        drawCircle(Color.White, 6.dp.toPx(), end)
        drawCircle(ClickBusPurple, 3.dp.toPx(), end)
        drawUserPin(entry, pulse)
        drawRouteLabels(textMeasurer, entry, end, gate)
    }
}

private fun DrawScope.at(x: Float, y: Float) = Offset(size.width * x, size.height * y)

private fun DrawScope.sizeOf(width: Float, height: Float) = Size(size.width * width, size.height * height)

private fun DrawScope.drawCenteredText(textMeasurer: TextMeasurer, text: String, center: Offset, style: TextStyle) {
    val layout = textMeasurer.measure(text, style)
    drawText(layout, topLeft = center - Offset(layout.size.width / 2f, layout.size.height / 2f))
}

private fun DrawScope.drawFloorPlan(textMeasurer: TextMeasurer) {
    val wallTopLeft = at(WALL_LEFT, WALL_TOP)
    val wallSize = sizeOf(WALL_RIGHT - WALL_LEFT, WALL_BOTTOM - WALL_TOP)
    val wallCorner = CornerRadius(12.dp.toPx())
    drawRoundRect(MapFloor, wallTopLeft, wallSize, wallCorner)
    drawRoundRect(MapWall, wallTopLeft, wallSize, wallCorner, style = Stroke(2.dp.toPx()))

    // Entrada: abertura na parede de baixo, com batentes.
    val gapLeft = ENTRY_X - ENTRANCE_HALF_WIDTH
    val gapRight = ENTRY_X + ENTRANCE_HALF_WIDTH
    drawLine(MapFloor, at(gapLeft, WALL_BOTTOM), at(gapRight, WALL_BOTTOM), strokeWidth = 4.dp.toPx())
    listOf(gapLeft, gapRight).forEach { x ->
        drawLine(
            MapWall,
            at(x, WALL_BOTTOM - 0.015f),
            at(x, WALL_BOTTOM + 0.015f),
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
    drawCenteredText(textMeasurer, "Entrada · Metrô Tietê", at(ENTRY_X, (WALL_BOTTOM + 1f) / 2f), LabelStyle)

    FACILITIES.forEach { facility ->
        drawRoundRect(
            facility.color,
            at(facility.left, facility.top),
            sizeOf(facility.right - facility.left, facility.bottom - facility.top),
            CornerRadius(8.dp.toPx())
        )
        drawCenteredText(
            textMeasurer,
            facility.name,
            at((facility.left + facility.right) / 2f, (facility.top + facility.bottom) / 2f),
            LabelStyle
        )
    }
}

private fun DrawScope.drawPlatforms(textMeasurer: TextMeasurer, targetBay: Int?, pulse: Float) {
    drawRoundRect(
        MapApron,
        at(WALL_LEFT + APRON_INSET, APRON_TOP),
        sizeOf(WALL_RIGHT - WALL_LEFT - 2 * APRON_INSET, APRON_BOTTOM - APRON_TOP),
        CornerRadius(8.dp.toPx())
    )
    // Faixa amarela do meio-fio, onde o passageiro embarca.
    drawLine(
        MapCurb,
        at(WALL_LEFT + APRON_INSET, APRON_BOTTOM),
        at(WALL_RIGHT - APRON_INSET, APRON_BOTTOM),
        strokeWidth = 3.dp.toPx()
    )

    val baySize = sizeOf(BAY_WIDTH - BAY_GAP, BAY_BOTTOM - BAY_TOP)
    val bayCorner = 6.dp.toPx()
    repeat(PLATFORM_COUNT) { index ->
        val topLeft = at(BAYS_LEFT + index * BAY_WIDTH + BAY_GAP / 2f, BAY_TOP)
        val isTarget = index == targetBay
        if (isTarget) {
            val grow = 6.dp.toPx() * pulse
            drawRoundRect(
                HighlightYellow.copy(alpha = 1f - pulse),
                topLeft - Offset(grow, grow),
                Size(baySize.width + 2 * grow, baySize.height + 2 * grow),
                CornerRadius(bayCorner + grow),
                style = Stroke(3.dp.toPx())
            )
        }
        drawRoundRect(if (isTarget) ClickBusPurple else MapBay, topLeft, baySize, CornerRadius(bayCorner))
        drawCenteredText(
            textMeasurer,
            "${FIRST_PLATFORM + index}",
            at(bayCenterX(index), BAY_NUMBER_Y),
            if (isTarget) BayNumberStyle.copy(color = Color.White) else BayNumberStyle
        )
    }
}

/** Polilinha com cantos arredondados (o raio é reduzido em trechos curtos). */
private fun roundedPath(points: List<Offset>, radius: Float): Path {
    val distinct = points.filterIndexed { i, p -> i == 0 || p != points[i - 1] }
    val path = Path()
    path.moveTo(distinct.first().x, distinct.first().y)
    for (i in 1 until distinct.size - 1) {
        val prev = distinct[i - 1]
        val corner = distinct[i]
        val next = distinct[i + 1]
        val r = minOf(radius, (corner - prev).getDistance() / 2f, (next - corner).getDistance() / 2f)
        val toPrev = (prev - corner) / (prev - corner).getDistance()
        val toNext = (next - corner) / (next - corner).getDistance()
        val a = corner + toPrev * r
        val b = corner + toNext * r
        path.lineTo(a.x, a.y)
        path.quadraticTo(corner.x, corner.y, b.x, b.y)
    }
    path.lineTo(distinct.last().x, distinct.last().y)
    return path
}

private fun DrawScope.drawRoute(points: List<Offset>, flow: Float) {
    val path = roundedPath(points, 18.dp.toPx())
    drawPath(path, Color.White, style = Stroke(11.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(path, ClickBusPurple, style = Stroke(7.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))

    // Setas brancas que andam no sentido da caminhada.
    val measure = PathMeasure().apply { setPath(path, false) }
    val spacing = 20.dp.toPx()
    val arm = 2.5.dp.toPx()
    val chevronStroke = Stroke(1.8.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
    var distance = spacing * (0.5f + flow)
    while (distance < measure.length - spacing / 2f) {
        val position = measure.getPosition(distance)
        val tangent = measure.getTangent(distance)
        val normal = Offset(-tangent.y, tangent.x)
        val chevron = Path().apply {
            val left = position - tangent * arm + normal * arm
            val right = position - tangent * arm - normal * arm
            moveTo(left.x, left.y)
            lineTo(position.x, position.y)
            lineTo(right.x, right.y)
        }
        drawPath(chevron, Color.White, style = chevronStroke)
        distance += spacing
    }
}

/** Pin em gota com a ponta em [tip]. */
private fun pinPath(tip: Offset, radius: Float): Path {
    val distance = radius * PIN_HEIGHT_RATIO
    val center = Offset(tip.x, tip.y - distance)
    val tangentDegrees = Math.toDegrees(acos(radius / distance).toDouble()).toFloat()
    val startDegrees = 90f + tangentDegrees
    val startRadians = Math.toRadians(startDegrees.toDouble())
    return Path().apply {
        moveTo(tip.x, tip.y)
        lineTo(center.x + radius * cos(startRadians).toFloat(), center.y + radius * sin(startRadians).toFloat())
        arcTo(Rect(center, radius), startDegrees, 360f - 2 * tangentDegrees, false)
        close()
    }
}

private fun DrawScope.drawUserPin(tip: Offset, pulse: Float) {
    val halo = 6.dp.toPx() + 14.dp.toPx() * pulse
    drawOval(
        ClickBusPurple.copy(alpha = 0.35f * (1f - pulse)),
        topLeft = tip - Offset(halo, halo * 0.45f),
        size = Size(halo * 2, halo * 0.9f)
    )
    val radius = PIN_RADIUS.toPx()
    val pin = pinPath(tip, radius)
    translate(top = 1.5.dp.toPx()) { drawPath(pin, Color.Black.copy(alpha = 0.2f)) }
    drawPath(pin, ClickBusPurple)
    drawPath(pin, Color.White, style = Stroke(2.dp.toPx(), join = StrokeJoin.Round))
    drawCircle(Color.White, radius * 0.4f, Offset(tip.x, tip.y - radius * PIN_HEIGHT_RATIO))
}

private fun DrawScope.chipSize(layout: TextLayoutResult) = Size(
    layout.size.width + 2 * CHIP_PADDING_H.toPx(),
    layout.size.height + 2 * CHIP_PADDING_V.toPx()
)

private fun DrawScope.drawChip(layout: TextLayoutResult, topLeft: Offset, background: Color, border: Color?) {
    val chip = chipSize(layout)
    val corner = CornerRadius(chip.height / 2f)
    drawRoundRect(Color.Black.copy(alpha = 0.12f), topLeft + Offset(0f, 1.dp.toPx()), chip, corner)
    drawRoundRect(background, topLeft, chip, corner)
    border?.let { drawRoundRect(it, topLeft, chip, corner, style = Stroke(1.5.dp.toPx())) }
    drawText(layout, topLeft = topLeft + Offset(CHIP_PADDING_H.toPx(), CHIP_PADDING_V.toPx()))
}

private fun DrawScope.drawRouteLabels(textMeasurer: TextMeasurer, entry: Offset, end: Offset, gate: String) {
    val gap = 8.dp.toPx()

    // "Você está aqui" ao lado da cabeça do pin.
    val youLayout = textMeasurer.measure("Você está aqui", ChipStyle.copy(color = ClickBusPurple))
    val pinRadius = PIN_RADIUS.toPx()
    val headY = entry.y - pinRadius * PIN_HEIGHT_RATIO
    drawChip(
        youLayout,
        Offset(entry.x + pinRadius + 6.dp.toPx(), headY - chipSize(youLayout).height / 2f),
        Color.White,
        ClickBusPurple
    )

    // Portão logo abaixo do corredor, do lado da plataforma, sem cruzar a rota.
    val gateLayout = textMeasurer.measure("Portão $gate", ChipStyle.copy(color = Color.White))
    val gateWidth = chipSize(gateLayout).width
    val preferredLeft = end.x - gateWidth / 2f
    val left = if (end.x >= entry.x) {
        maxOf(preferredLeft, entry.x + gap)
    } else {
        minOf(preferredLeft, entry.x - gap - gateWidth)
    }.coerceIn(gap, size.width - gap - gateWidth)
    drawChip(gateLayout, Offset(left, size.height * CORRIDOR_Y + gap), ClickBusPurple, null)
}
