---
format: 1920x1080
duration: 120s
message: "O Boardly leva o passageiro do bilhete ao portão de embarque e avisa na hora quando o terminal muda a plataforma."
arc: Gancho → Problema → Desafio → Solução → Demonstração → Valor → Próximos passos → Encerramento
audience: banca da FIAP e profissionais da ClickBus
mode: collaborative
---

## Frame 1 — Abertura

- scene: Vinheta da logo do Boardly
- duration: 5s
- transition_in: cut
- status: built
- blueprint: logo-assemble-lockup
- src: compositions/01-abertura.html

Pin cai, ônibus aparece, nome e frase "Do bilhete ao portão de embarque". Reaproveita a vinheta pronta.

## Frame 2 — Gancho

- scene: "A passagem já está no celular. O embarque ainda depende do painel do terminal."
- duration: 8s
- transition_in: crossfade
- status: built
- blueprint: kinetic-type-beats
- src: compositions/02-gancho.html

Contraste entre a compra digital e o embarque físico. Abre a tensão que o vídeo resolve.

## Frame 3 — Problema

- scene: Três dúvidas do passageiro no terminal
- duration: 12s
- transition_in: crossfade
- status: built
- blueprint: overwhelm-surround
- src: compositions/03-problema.html

"Qual é a minha plataforma?", "Mudou e ninguém me avisou.", "Para que lado fica o portão?". As dúvidas cercam um passageiro no centro. Sem números.

## Frame 4 — Desafio

- scene: O desafio da ClickBus, citado do regulamento
- duration: 7s
- transition_in: crossfade
- status: built
- blueprint: titlecard-reveal
- src: compositions/04-desafio.html

"Conectar o mundo digital (app) com o mundo físico (terminal rodoviário)." Challenge ClickBus 2026. Mostra alinhamento com o desafio.

## Frame 5 — Solução

- scene: Boardly no celular, tela Início
- duration: 8s
- transition_in: crossfade
- status: built
- blueprint: device-surface-showcase
- src: compositions/05-solucao.html

"Boardly: do bilhete ao portão de embarque." Celular entra com a tela Início; três palavras ao lado: acompanha, avisa, guia.

## Frame 6 — Demonstração: acompanhar

- scene: Início e Status com as etapas do embarque
- duration: 14s
- transition_in: crossfade
- status: built
- blueprint: device-surface-showcase
- src: compositions/06-demo-status.html

Gravação real: Iniciar jornada → Status. Legenda: "Viagem do dia, etapas do embarque e minutos para a saída."

## Frame 7 — Demonstração: troca de plataforma

- scene: Alerta "Plataforma alterada: 12 → 14"
- duration: 14s
- transition_in: crossfade
- status: built
- blueprint: device-surface-showcase
- src: compositions/07-demo-alerta.html

Momento central. Gravação real do alerta chegando; destaque ampliado do aviso. Legenda: "O terminal mudou a plataforma. O app avisa na hora, em todas as telas."

## Frame 8 — Demonstração: mapa

- scene: Mapa do terminal com pin e rota até a plataforma
- duration: 14s
- transition_in: crossfade
- status: built
- blueprint: device-surface-showcase
- src: compositions/08-demo-mapa.html

Gravação real: Ver mapa → Ampliar → arrastar. Legenda: "Você está aqui. A rota leva até a plataforma certa."

## Frame 9 — Demonstração: validação

- scene: Bilhete e confirmação de embarque
- duration: 12s
- transition_in: crossfade
- status: built
- blueprint: device-surface-showcase
- src: compositions/09-demo-validacao.html

Gravação real: Validar embarque → Simular confirmação. Legenda: "Bilhete pronto para o totem ou o motorista. Embarque confirmado."

## Frame 10 — Como foi feito

- scene: Tecnologia do MVP e aviso de simulação
- duration: 8s
- transition_in: crossfade
- status: built
- blueprint: titlecard-reveal
- src: compositions/10-mvp.html

"MVP Android em Kotlin e Jetpack Compose." "Nesta versão, os eventos do terminal são simulados."

## Frame 11 — Valor

- scene: Três cartões de proposta de valor
- duration: 10s
- transition_in: crossfade
- status: built
- blueprint: grid-card-assemble
- src: compositions/11-valor.html

Passageiro: sabe para onde ir e o que mudou. Terminal: informação chega a quem precisa. ClickBus: o app continua útil depois da compra. Sem percentuais.

## Frame 12 — Próximos passos

- scene: Lista do que vem depois do protótipo
- duration: 8s
- transition_in: crossfade
- status: built
- blueprint: grid-card-assemble
- src: compositions/12-proximos-passos.html

Dados do terminal em tempo real; QR lido pelo totem; notificações push; mapa real do terminal.

## Frame 13 — Encerramento

- scene: Logo, frase e equipe
- duration: 6s
- transition_in: crossfade
- status: built
- blueprint: logo-assemble-lockup
- src: compositions/13-encerramento.html

Logo do Boardly, "Do bilhete ao portão de embarque", nome do time e integrantes, Challenge ClickBus 2026.
