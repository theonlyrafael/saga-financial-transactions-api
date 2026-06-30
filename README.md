# Sistema Distribuído de Transações Financeiras

## 🎯 Objetivo e Motivação do Sistema

No ecossistema financeiro atual, a disponibilidade e a integridade dos dados são inegociáveis. Diante disso, em arquiteturas monolíticas tradicionais, a falha em um único módulo (como o sistema de custódia) pode derrubar toda a operação do cliente (como a criação de uma conta ou a emissão de uma ordem de compra). Por esse motivo, o objetivo deste projeto é resolver esse problema implementando uma Arquitetura de Microsserviços Orientada a Eventos.

Nesse sentido, o sistema simula a criação e o processamento de ordens de investimentos utilizando o **Padrão Saga (Coreografado)** para garantir a consistência dos dados. Dessa forma, os serviços operam de maneira totalmente desacoplada, o que significa que a porta de entrada não engargala esperando o fim do processamento. Sendo assim, caso o sistema final fique indisponível, as transações não são perdidas, sendo mantidas seguras na fila de mensageria até a normalização.

## 🛠️ Stack Tecnológica Principal

- **Linguagem:** Java 17
- **Framework:** Spring Boot (Web, Data JPA, AMQP)
- **Mensageria:** RabbitMQ (Message Broker)
- **Banco de Dados:** H2 Database (In-Memory)
- **Infraestrutura:** Docker
- **Ferramentas de Teste:** VS Code Thunder Client / Postman / Insomnia

## 🚀 Manual de Utilização

Siga o passo a passo abaixo para rodar o ecossistema localmente e acompanhar a lógica distribuída em ação.

### 1. Subindo a Infraestrutura de Mensageria

Primeiramente, certifique-se de ter o Docker rodando em sua máquina e execute o comando abaixo no terminal para levantar o servidor do RabbitMQ com o painel de gerenciamento:

\`\`\`bash
docker run -d --name rabbitmq-server -p 5672:5672 -p 15672:15672 rabbitmq:3-management
\`\`\`

Adicionalmente, você pode visualizar a interface gráfica do broker através do seu navegador (Opcional: acesse `http://localhost:15672` utilizando as credenciais `guest`/`guest` para monitorar as filas).

### 2. Iniciando os Microsserviços

Em seguida, abra terminais separados na raiz de cada projeto e inicie as aplicações Spring Boot. Vale ressaltar que, graças à resiliência da arquitetura, a ordem de inicialização não interfere no funcionamento do ecossistema:

- **conta-service** (Porta `8081`) — Porta de entrada da aplicação.
- **custodia-service** (Porta `8082`) — Operário final e armazenamento da custódia.
- **orquestrador-service** (Porta `8083`) — Cérebro da Saga, responsável por rastrear o estado da transação.

### 3. Executando o Fluxo na Prática

Posteriormente, com os serviços rodando, faça uma requisição HTTP via Thunder Client, Postman ou terminal. Para isso, crie uma nova ordem de compra (`POST`) informando o endpoint e o corpo da requisição:

**URL:** `http://localhost:8081/api/ordens`

**Body (JSON):**

\`\`\`json
{
  "cpfCliente": "12345678900",
  "valor": 25000.00
}
\`\`\`

Ao disparar a requisição, você receberá um status `200 OK` imediato. Logo após, olhe para os terminais das aplicações, pois você verá os logs rastreando a mensagem saindo da Conta, sendo interceptada pelo Orquestrador e finalmente sendo salva pela Custódia.

### 4. Auditoria e Rastreabilidade

Por fim, para validar a persistência da Saga e rastrear os dados, siga o roteiro de inspeção:

1. Acesse o banco de dados do Orquestrador no navegador através da URL `http://localhost:8083/h2-console` (JDBC URL: `jdbc:h2:mem:orquestradordb`, Usuário: `sa`).
2. Consulte a tabela `transacao_saga` para visualizar a ordem gravada e copie seu `ordem_id` (UUID).
3. Na sequência, faça uma requisição `GET` para consultar o status público da transação:

**URL:** `http://localhost:8083/api/sagas/{COLE-O-UUID-AQUI}`

## 📌 Conclusão e Aprendizados

Esse projeto nasceu da vontade de sair da teoria e trabalhar com uma arquitetura parecida com o que existe no mercado financeiro. O maior desafio foi garantir comunicação assíncrona confiável entre os serviços.

No início, os serviços caíam na inicialização ao tentar consumir filas que ainda não existiam no RabbitMQ (erros 404 Not Found). A solução foi abandonar ouvintes passivos e declarar as filas ativamente com `queuesToDeclare`, fazendo cada microsserviço montar sua própria infraestrutura no broker sob demanda.

Outro problema recorrente foi manter os `application.properties` consistentes entre os projetos, especialmente credenciais do AMQP e portas dos servidores. Qualquer divergência gerava erros difíceis de rastrear.

O resultado é um sistema onde os serviços não derrubam um ao outro e nenhuma transação se perde, mesmo com partes temporariamente fora do ar.