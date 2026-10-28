# Manual de Interface — RescueStock

Este manual orienta a operação do RescueStock pela interface de terminal. Os dados existem apenas enquanto o programa está aberto.

## Antes de começar

Execute o projeto conforme descrito no [README](../README.md). Ao iniciar, o sistema mostra o menu principal e aguarda uma opção em `Escolha:`.

```text
=== RESCUESTOCK ===
1  - Registrar doação
...
0  - Sair
Escolha:
```

Digite apenas o número desejado e pressione `Enter`. Depois de cada operação, o menu é mostrado novamente.

## Mapa de navegação

```text
Menu principal
├── 1  Registrar doação
├── 2  Listar estoque
├── 3  Procurar produto
├── 4  Remover produto
├── 5  Registrar família
├── 6  Listar famílias
├── 7  Criar solicitação
├── 8  Atender solicitação
├── 9  Produtos próximos do vencimento
├── 10 Produtos com estoque baixo
├── 11 Mapa do depósito
├── 12 Relatório geral
└── 0  Sair
```

## Operações

### 1 — Registrar doação

Use esta opção para incluir itens no estoque.

| Campo | Como preencher |
| --- | --- |
| Nome | Nome identificável do produto, por exemplo `Arroz` |
| Quantidade | Número inteiro maior que zero |
| Peso unitário (kg) | Número positivo; aceita vírgula ou ponto, por exemplo `1,5` |
| Tipo | `1` alimento, `2` higiene, `3` vestuário |

Campos adicionais por tipo:

- **Alimento:** dias para vencer (zero ou inteiro positivo).
- **Higiene:** uso infantil (`s` para sim ou `n` para não).
- **Vestuário:** tamanho, por exemplo `M`, `G` ou `infantil 8`.

Exemplo:

```text
Escolha: 1
Nome: Arroz
Quantidade: 20
Peso unitário (kg): 1
Tipo (1-Alimento, 2-Higiene, 3-Vestuário): 1
Dias para vencer: 90
Doação registrada.
```

Se o nome já existir, o sistema soma a quantidade somente quando tipo, peso e característica do lote forem iguais. Para um lote diferente, use outro nome, como `Arroz - lote outubro`.

### 2 — Listar estoque

Exibe todos os produtos com tipo, quantidade, peso, prioridade e posição no depósito. Quando não houver itens, a mensagem será `Estoque vazio.`

### 3 — Procurar produto

Digite o nome completo ou apenas um trecho. A pesquisa não diferencia maiúsculas de minúsculas.

```text
Produto ou trecho: arroz
```

O resultado mostra todos os nomes que contêm o termo. Se não houver correspondência, será exibido `Produto não encontrado.`

### 4 — Remover produto

Primeiro, informe o nome do produto. Depois escolha:

| Escolha | Resultado |
| ---: | --- |
| `1` | Retira uma quantidade específica; informe um inteiro positivo disponível no estoque |
| `2` | Remove o produto inteiro, independentemente da quantidade |

Ao retirar toda a quantidade disponível, a posição do produto é liberada automaticamente no depósito.

### 5 — Registrar família

Informe responsável, total de adultos, total de crianças, rua, cidade e bairro. Adultos e crianças podem ser zero isoladamente, porém a soma deve ser pelo menos 1.

```text
Responsável: Ana Silva
Adultos: 1
Crianças: 2
Rua: Rua das Flores, 20
Cidade: São Paulo
Bairro: Centro
```

Guarde o ID exibido ao final; ele será necessário para criar uma solicitação.

### 6 — Listar famílias

Exibe as famílias ordenadas pela prioridade. A pontuação é `adultos + 2 × crianças`; maior pontuação aparece primeiro.

### 7 — Criar solicitação

Informe o ID de uma família previamente cadastrada, quantos tipos de produto serão solicitados e, para cada tipo, nome e quantidade.

```text
ID da família: 1
Quantos tipos de produto? 2
Produto 1: Arroz
Quantidade: 5
Produto 2: Sabonete
Quantidade: 4
```

Os nomes serão conferidos no atendimento. Para evitar pendências desnecessárias, utilize os mesmos nomes registrados no estoque e não repita um produto dentro da mesma solicitação.

### 8 — Atender solicitação

O sistema lista primeiro as solicitações pendentes em ordem de prioridade. Informe o ID desejado.

- Se todos os itens estiverem disponíveis, a solicitação é marcada como atendida e as quantidades são retiradas do estoque.
- Se faltar algo, o sistema mostra produto, quantidade solicitada, disponível e faltante. Nenhum item é retirado nesse caso.

Exemplo de indisponibilidade:

```text
Itens insuficientes:
Arroz | Solicitado: 10 | Disponível: 5 | Faltante: 5
```

### 9 — Produtos próximos do vencimento

Informe o número máximo de dias para vencer. São exibidos somente alimentos cuja validade seja menor ou igual ao limite.

```text
Vencimento em até quantos dias? 7
```

### 10 — Produtos com estoque baixo

Informe o limite de unidades. O resultado inclui produtos cuja quantidade seja menor ou igual ao valor informado.

```text
Limite de estoque: 5
```

### 11 — Mapa do depósito

Mostra uma grade 4 × 4:

- `[x]` indica posição ocupada;
- `[ ]` indica posição livre.

No rodapé, o sistema informa a ocupação percentual e a razão de posições ocupadas sobre a capacidade total, por exemplo `25% (4/16)`.

### 12 — Relatório geral

Gera um resumo com:

- número de produtos distintos e total de unidades;
- itens com até 5 unidades;
- alimentos a vencer em até 2 dias;
- total de famílias;
- solicitações pendentes e atendidas;
- mapa do depósito.

Os limites de 5 unidades e 2 dias são fixos para este relatório.

### 0 — Sair

Encerra a aplicação. Como não há persistência, faça registros externos dos dados relevantes antes de sair.

## Mensagens e correção de erros

| Mensagem ou situação | O que fazer |
| --- | --- |
| `Campo obrigatório.` | Preencha o texto solicitado. |
| `Digite um número inteiro válido.` | Use somente inteiro, sem casas decimais. |
| `Digite um número maior que zero.` | Informe valor positivo para quantidades e IDs. |
| `Digite zero ou um número positivo.` | Não use valores negativos em dias, adultos, crianças ou limites. |
| `Digite um peso positivo válido.` | Use um número maior que zero; vírgula e ponto são aceitos. |
| `Digite s ou n.` | Responda exatamente `s` ou `n` para uso infantil. |
| Depósito cheio | Remova um produto inteiro ou zere sua quantidade antes de cadastrar outro produto distinto. |
| Família não encontrada | Cadastre a família e use o ID retornado na operação 5. |
| Solicitação não encontrada | Consulte a lista de pendentes na operação 8 e informe um ID válido. |

## Boas práticas operacionais

- Padronize nomes de produtos (`Arroz 1 kg`, por exemplo) e use a mesma grafia nas solicitações.
- Registre lotes com características diferentes sob nomes distintos.
- Confira produtos próximos do vencimento antes de criar uma distribuição.
- Use o relatório geral no fim do turno para acompanhar pendências e ocupação.
