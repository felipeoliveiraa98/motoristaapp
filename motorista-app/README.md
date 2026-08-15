# Motorista Finanças — MVP

App Android nativo (Kotlin + Jetpack Compose + Material 3 + Room), 100% offline,
para motoristas de Uber/99/InDrive controlarem ganhos, despesas, metas e lucro real.

## O que já está funcionando nesta Etapa 1 (MVP)

- **Dashboard**: ganhos por plataforma, despesas por categoria, faturamento, lucro
  líquido, margem, ganho/lucro por hora e por km, km rodados, horas trabalhadas,
  progresso da meta mensal e alertas — com filtro Hoje/Semana/Mês.
- **Ganhos**: cadastro com plataforma, valor, horário inicial/final, km inicial/final;
  cálculo automático de horas, km, ganho/hora e ganho/km. Listar e excluir.
- **Despesas**: 14 categorias, forma de pagamento, km do veículo; campos extras de
  litros/preço por litro/tipo quando a categoria é Combustível. Listar e excluir.
- **Metas**: meta de faturamento e de lucro mensal, meta diária manual opcional,
  seleção dos dias da semana trabalhados, cálculo automático de quanto falta e
  quanto é necessário ganhar por dia.
- **Banco Room local** com 5 entidades (Earning, Expense, Goal, Vehicle, WorkSession),
  DAOs, Repositories e um `AppContainer` simples de injeção de dependência.
- Validações: valores vazios/inválidos, km final < inicial, horário final < inicial,
  listas vazias tratadas com mensagens amigáveis.

## O que ainda falta (Etapa 2 — próxima entrega)

Relatórios com gráficos, comparação Uber x 99, seção "Meu carro" (custo real do
veículo), calendário financeiro, previsão de fechamento do mês, tela de
configurações, fluxo rápido "Começar/Finalizar trabalho", backup/exportação
JSON/CSV e o assistente de primeiro acesso. A entidade `WorkSession` e a tela de
Relatórios já existem como placeholder para isso ser plugado sem retrabalho.

## Como gerar o APK sem instalar nada (GitHub Actions)

O projeto já inclui `.github/workflows/build-apk.yml`, que compila o APK
automaticamente na nuvem. Passo a passo:

1. Crie uma conta gratuita em [github.com](https://github.com) (se ainda não tiver).
2. Crie um repositório novo (pode ser privado), ex: `motorista-financas`.
3. Suba a pasta `motorista-app` inteira para esse repositório. O jeito mais fácil:
   - Na página do repositório recém-criado, clique em **"uploading an existing file"**
   - Arraste todos os arquivos/pastas do projeto (inclusive a pasta `.github`) e
     clique em **Commit changes**
   - (Alternativa via linha de comando: `git init`, `git add .`,
     `git commit -m "primeiro commit"`, `git remote add origin <url-do-repo>`,
     `git push -u origin main`)
4. Assim que o push terminar, vá na aba **Actions** do repositório no GitHub.
5. Vai aparecer um workflow chamado **"Build APK"** rodando (ícone amarelo/laranja
   girando). Espere terminar — leva de 3 a 6 minutos. Quando ficar com um ✔️ verde,
   terminou.
6. Clique em cima da execução concluída, role até **Artifacts** (embaixo da página)
   e clique em **motorista-financas-apk** para baixar um `.zip` contendo o
   `app-debug.apk`.
7. Extraia o `.zip`, copie o `app-debug.apk` para o celular e instale (ative
   "Fontes desconhecidas" nas configurações do Android se pedir).

Cada vez que você (ou eu) alterar o código e enviar (`push`) para o GitHub, um novo
APK é gerado automaticamente — não precisa repetir os passos manualmente.

## Como abrir e rodar no Android Studio

1. Abra o Android Studio (recomendado: versão Koala/2024.1 ou mais recente).
2. **File → Open** e selecione a pasta `motorista-app` (a raiz do projeto, que
   contém `settings.gradle.kts`).
3. Aguarde o Gradle Sync (primeira vez baixa as dependências — precisa de internet
   só nesse passo; o app em si roda 100% offline).
4. Conecte um celular Android (modo desenvolvedor + depuração USB) ou crie um
   emulador (**Tools → Device Manager**), Android 7.0 (API 24) ou superior.
5. Clique em **Run ▶** (ou Shift+F10).

## Como gerar o APK

- **APK de teste (debug)**: `Build → Build App Bundle(s) / APK(s) → Build APK(s)`.
  O arquivo fica em `app/build/outputs/apk/debug/app-debug.apk`.
- **APK assinado para distribuição**: `Build → Generate Signed Bundle / APK`,
  escolha APK, crie ou selecione um keystore, e siga o assistente. O resultado fica
  em `app/build/outputs/apk/release/`.

Ou via linha de comando, na raiz do projeto:

```
./gradlew assembleDebug
```

## Estrutura de pastas

```
app/src/main/java/com/motoristaapp/financas/
  data/
    entity/        Earning, Expense, Goal, Vehicle, WorkSession
    dao/            DAOs Room
    repository/     Repositories + AppContainer (injeção manual)
    AppDatabase.kt  Configuração do Room
    Converters.kt   TypeConverters (enums)
  ui/
    dashboard/      Tela e ViewModel do Dashboard
    earnings/        Listagem + formulário de ganhos
    expenses/        Listagem + formulário de despesas
    goals/           Tela e ViewModel de metas
    reports/         Placeholder da Etapa 2
    navigation/      NavGraph + Bottom Navigation + FAB
    components/      Cards reutilizáveis
    theme/           Material 3 (light/dark)
  MainActivity.kt
  MotoristaApp.kt   Application (cria o AppContainer)
```
