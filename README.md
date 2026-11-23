<img src="docs/logo/boardly-logo-horizontal.png" alt="Boardly" width="320">

# Boardly — Challenge ClickBus 2026

MVP Android (Kotlin + Jetpack Compose) que conecta o app ao terminal rodoviário: orienta o passageiro do bilhete até o portão de embarque e reage a eventos do terminal, como a troca de plataforma.

> **Protótipo.** Dados da viagem e eventos do terminal são simulados. Não há integração com a ClickBus, com o terminal, GPS, câmera ou biometria.

## O que o app faz

| Tela | Conteúdo |
|---|---|
| Início | Viagem do dia, status do embarque, portão e plataforma atuais |
| Status | Etapas do embarque, minutos para a saída, alertas e instruções até o portão |
| Mapa | Mapa ilustrativo do terminal com a rota até a plataforma e zoom |
| Validação / Check-in | Bilhete para mostrar no totem ou ao motorista e confirmação simulada |
| Confirmação | Embarque registrado, com "Voltar ao início" ou "Repetir validação" |
| Viagens / Informações | Lista de viagens e explicação da demonstração |

**Simulação do terminal:** a cada 10 s chega um novo evento:

1. Aguardando ônibus.
2. Troca de plataforma de 12 para 14.
3. Ônibus no portão.
4. Embarque aberto.

Todas as telas acompanham o evento atual, e a troca de plataforma gera um alerta. Na tela de Status, "Avançar simulação do terminal" adianta o próximo evento.

## Executar

Requisitos: JDK 17 ou mais novo e Android SDK 36 (o Android Studio instala os dois). O app roda em Android 7.0 (API 24) ou superior.

```bash
./gradlew assembleDebug        # APK em app/build/outputs/apk/debug/
./gradlew testDebugUnitTest    # testes de unidade
```

Ou abra a pasta no Android Studio, sincronize o Gradle e execute o módulo `app`.

## Roteiro de demonstração

1. Início → **Iniciar jornada de embarque**.
2. Status → toque uma vez em **Avançar simulação do terminal**. O alerta de troca de plataforma aparece.
3. **Ver mapa do terminal** → **Ampliar**.
4. **Validar embarque** → **Simular confirmação de embarque**.
5. **Voltar ao início**. A simulação recomeça.

## Estrutura

```
app/src/main/java/com/example/
├── MainActivity.kt          # Activity, Scaffold e grafo de navegação
├── Navigation.kt            # Rotas e barra inferior
├── Trip.kt                  # Dados da viagem de demonstração
├── BoardingTimeline.kt      # Roteiro de eventos do terminal (estado imutável)
├── BoardingViewModel.kt     # Avanço automático/manual da simulação
├── BoardingComponents.kt    # Componentes compartilhados (alerta de plataforma etc.)
├── ScreenHeader.kt          # Cabeçalho das telas internas
└── *Screen.kt               # Uma tela por arquivo
app/src/test/java/com/example/
├── BoardingStateTest.kt
└── BoardingViewModelTest.kt
```

## Próximos passos (além do protótipo)

- Fonte autorizada de dados: viagens do passageiro e painel do terminal em tempo real.
- Validação real do bilhete (QR lido pelo totem) e persistência local.
- Notificações push para troca de plataforma e abertura do embarque.
- Mapa real do terminal e validação da proposta com passageiros.

## Equipe

<!-- Preencher: nome do time e integrantes -->
