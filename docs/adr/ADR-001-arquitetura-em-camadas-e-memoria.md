# ADR-001 — Arquitetura em camadas e armazenamento em memória

- **Status:** Aceito
- **Data:** 2026-10-28
- **Decisores:** Equipe RescueStock
- **Contexto:** arquitetura inicial da aplicação

## Contexto

O RescueStock é uma aplicação de terminal voltada ao controle básico de doações, estoque, famílias e solicitações. O escopo atual privilegia aprendizagem, execução local simples e regras de negócio visíveis no código, sem exigir infraestrutura externa.

A solução precisava evitar que a interface de terminal concentrasse também regras de estoque, prioridade e atendimento. Ao mesmo tempo, uma base de dados ou framework adicionaria complexidade incompatível com o estágio e o escopo atuais.

## Decisão

Adotamos uma arquitetura Java em três responsabilidades, com dados mantidos em memória durante a execução:

```text
UI (ui.Menu)
       ↓
Serviços (Sistema, Estoque, Deposito)
       ↓
Domínio (model.*)
```

1. **UI:** `Menu` coleta e valida entradas básicas, apresenta resultados e converte a escolha do usuário em chamadas de serviço.
2. **Serviços:** `Sistema` orquestra famílias e solicitações; `Estoque` centraliza as movimentações; `Deposito` gerencia posições físicas da grade.
3. **Domínio:** as classes do pacote `model` representam itens, categorias, famílias, endereços, solicitações e posições, mantendo invariantes próprias.
4. **Armazenamento:** `ArrayList` e matriz `Item[][]` mantêm o estado em memória. O ponto de entrada cria uma instância de `Sistema` por execução.

## Consequências

### Positivas

- Código de UI permanece mais simples e concentrado na interação por terminal.
- Regras críticas, como retirada integral para atendimento de solicitação, ficam centralizadas e reutilizáveis.
- O projeto executa com apenas um JDK, sem configuração de banco de dados ou dependências.
- A separação torna possível trocar a CLI ou introduzir persistência com menor impacto nas entidades do domínio.

### Negativas

- Todos os dados são perdidos ao encerrar o processo.
- Não há controle de concorrência, auditoria ou recuperação de falhas.
- Serviços expõem coleções mutáveis em alguns pontos, o que exige disciplina caso a aplicação cresça.
- A capacidade física está representada por uma matriz fixa criada pelo construtor padrão de `Estoque`.

## Alternativas consideradas

### 1. Lógica integral no `Menu`

Rejeitada. Misturaria fluxo de terminal, validação, estoque e regras de solicitação no mesmo componente, reduzindo testabilidade e legibilidade.

### 2. Banco de dados relacional desde o início

Rejeitada para o escopo atual. Resolveria persistência e consulta, mas exigiria esquema, driver, configuração e tratamento de conexão antes de validar o fluxo principal.

### 3. Arquivo local (JSON/CSV)

Adiada. É uma evolução apropriada se for necessária persistência leve, mas requer definição de formato, migração e tratamento de falhas de leitura/gravação.

## Evolução prevista

Quando a persistência se tornar necessária, introduzir interfaces de repositório para itens, famílias e solicitações. As implementações em memória podem continuar servindo a testes, enquanto uma implementação baseada em arquivo ou banco de dados substitui o armazenamento de produção sem alterar a UI.

## Referências

- [README do projeto](../../README.md)
- [Manual de Interface](../MANUAL_UI.md)
