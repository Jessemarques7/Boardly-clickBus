package com.example

/** Dados fixos da viagem usada na demonstração. Única fonte para todas as telas. */
data class Trip(
    val originCity: String,
    val originTerminal: String,
    val destinationCity: String,
    val destinationTerminal: String,
    val dateLabel: String,
    val departureTime: String,
    val duration: String,
    val company: String,
    val ticketCode: String
) {
    val routeShort: String get() = "$originCity → $destinationCity"
}

val DEMO_TRIP = Trip(
    originCity = "São Paulo",
    originTerminal = "Terminal Tietê",
    destinationCity = "Rio de Janeiro",
    destinationTerminal = "Novo Rio",
    dateLabel = "Hoje",
    departureTime = "18:57",
    duration = "6h00",
    company = "Viação 1001",
    ticketCode = "CSG-4567-UVDU-56789"
)
