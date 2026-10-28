# RescueStock

Aplicação de linha de comando para organizar doações, controlar o estoque de um depósito e atender solicitações de famílias. O projeto foi desenvolvido em Java puro, sem dependências externas, e mantém os dados somente durante a execução.

## Visão geral

O RescueStock centraliza quatro tarefas operacionais:

- registrar doações de alimentos, itens de higiene e vestuário;
- localizar e retirar produtos do estoque;
- cadastrar famílias e priorizá-las pela composição familiar;
- criar, validar e atender solicitações, atualizando o estoque de forma consistente.

O depósito tem uma grade de 4 × 4 posições, portanto comporta até **16 produtos distintos**. Novas doações compatíveis com um produto já cadastrado apenas aumentam sua quantidade e não ocupam outra posição.

## Funcionalidades

| Área | Recursos |
| --- | --- |
| Doações | Cadastro de alimento, higiene e vestuário; consolidação de lotes compatíveis |
| Estoque | Listagem, pesquisa parcial, retirada parcial ou exclusão de produto |
| Validade | Consulta de alimentos que vencem dentro de um número informado de dias |
| Depósito | Posição de cada produto, mapa visual e percentual de ocupação |
| Famílias | Cadastro com endereço e ordenação por prioridade |
| Solicitações | Registro de itens, checagem de disponibilidade e atendimento integral |
| Relatórios | Resumo de estoque, alertas, famílias, solicitações e mapa do depósito |

## Tecnologias e requisitos

- Java 17 ou superior (o código utiliza `record` e `switch` com setas);
- terminal com `javac` e `java` disponíveis no `PATH`;
- nenhuma biblioteca, banco de dados ou serviço externo.

## Como executar

No PowerShell, a partir da raiz do projeto:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse -Filter *.java src | ForEach-Object FullName)
java -cp out Main
```

No IntelliJ IDEA, abra o projeto, selecione o JDK 17+ e execute a classe `Main`.

> Os dados não são persistidos. Ao encerrar o programa, cadastros, estoque e solicitações são descartados.

## Fluxo recomendado de operação

1. Registre as doações no menu **1**.
2. Cadastre as famílias no menu **5** e anote o ID informado.
3. Crie solicitações no menu **7**, usando o ID da família e os nomes exatos dos produtos cadastrados.
4. Consulte as pendências e realize o atendimento no menu **8**.
5. Acompanhe alertas com os menus **9**, **10** e **12**.

Consulte o [Manual de UI](docs/MANUAL_UI.md) para o passo a passo completo de cada tela do terminal.

## Regras de negócio

### Produtos e lotes

- Nome, quantidade e peso unitário são obrigatórios; quantidade e peso devem ser positivos.
- Alimentos precisam informar dias para vencer, sem valores negativos.
- Produtos com o mesmo nome são somados apenas se forem do mesmo tipo, tiverem o mesmo peso e as mesmas características específicas:
  - alimento: mesmos dias para vencer;
  - higiene: mesmo indicador de uso infantil;
  - vestuário: mesmo tamanho.
- Se existir um item homônimo incompatível, o sistema solicita outro nome para separar o lote.
- Um produto é removido do depósito automaticamente quando sua quantidade chega a zero.

### Capacidade e localização

- O depósito padrão possui 16 posições (4 linhas × 4 colunas).
- Cada produto distinto ocupa uma posição; a primeira posição livre é atribuída no cadastro.
- A posição é exibida no formato `[linha][coluna]`, com índices iniciando em zero.

### Famílias e prioridade

- Cada família deve ter responsável, endereço e pelo menos uma pessoa entre adultos e crianças.
- A prioridade é calculada por `adultos + (2 × crianças)`.
- Listas de famílias e solicitações pendentes são ordenadas da maior para a menor prioridade.

### Solicitações

- Uma solicitação pertence a uma família já cadastrada e deve conter ao menos um item.
- Antes do atendimento, o sistema verifica todos os itens. Se faltar qualquer um, nenhuma retirada é feita.
- Uma solicitação atendida não pode ser atendida novamente.
- Para evitar divergências, informe o nome do produto exatamente como cadastrado (a busca não diferencia maiúsculas de minúsculas, mas não corrige grafias diferentes).

## Estrutura do projeto

```text
src/
├── Main.java              # ponto de entrada
├── model/                 # entidades e regras próprias dos objetos
│   ├── Item.java
│   ├── Alimento.java
│   ├── Higiene.java
│   ├── Vestuario.java
│   ├── Familia.java
│   ├── Solicitacao.java
│   └── ...
├── service/               # coordenação do domínio e depósito
│   ├── Sistema.java
│   ├── Estoque.java
│   └── Deposito.java
└── ui/
    └── Menu.java          # interface de terminal e validação de entrada

docs/
├── adr/                   # decisões arquiteturais
└── MANUAL_UI.md           # guia operacional da CLI
```

## Arquitetura

O projeto segue uma separação simples em camadas:

```text
Usuário → Menu (UI) → Sistema / Estoque / Depósito (serviços) → Modelos
```

- `ui.Menu` lê entradas, apresenta mensagens e encaminha ações.
- `service.Sistema` coordena famílias, solicitações e relatórios.
- `service.Estoque` e `service.Deposito` controlam itens e suas posições físicas.
- `model` representa os conceitos do domínio e suas validações.

A decisão arquitetural principal está documentada em [ADR-001](docs/adr/ADR-001-arquitetura-em-camadas-e-memoria.md).

## Menu da aplicação

| Opção | Ação |
| ---: | --- |
| 1 | Registrar doação |
| 2 | Listar estoque |
| 3 | Procurar produto |
| 4 | Remover produto ou retirar quantidade |
| 5 | Registrar família |
| 6 | Listar famílias por prioridade |
| 7 | Criar solicitação |
| 8 | Atender solicitação |
| 9 | Consultar alimentos próximos do vencimento |
| 10 | Consultar produtos com estoque baixo |
| 11 | Exibir mapa do depósito |
| 12 | Gerar relatório geral |
| 0 | Sair |

## Limitações atuais

- Não há persistência de dados, autenticação, histórico de movimentações ou integração com banco de dados.
- A capacidade do depósito é fixa na execução padrão (4 × 4).
- O relatório considera estoque baixo como quantidade menor ou igual a 5 e validade próxima como até 2 dias.
- A ordenação de alimentos por validade deve ser revisada antes de uso produtivo: a implementação atual de ordenação possui um erro de controle de laço que pode falhar para listas com mais de um alimento.
- Ao repetir o mesmo produto dentro de uma solicitação, a intenção é consolidar quantidades; contudo, a implementação atual também mantém a entrada original. Evite itens repetidos na mesma solicitação até a correção.
- Alguns textos exibidos pelo código-fonte atual podem aparecer com caracteres de codificação incorretos se ele for compilado/aberto fora de UTF-8.

## Documentação complementar

- [Manual de Interface](docs/MANUAL_UI.md)
- [ADR-001 — Arquitetura em camadas e armazenamento em memória](docs/adr/ADR-001-arquitetura-em-camadas-e-memoria.md)

## Licença

Não há licença declarada no repositório. Antes de redistribuir ou reutilizar o projeto, defina uma licença apropriada.
