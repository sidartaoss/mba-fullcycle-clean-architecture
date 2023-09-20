# MBA Full Cycle - De Arquitetura Hexagonal para Clean Architecture

Esta é a continuação de um projeto prático, aonde, em sua versão anterior, é refatorada uma aplicação implementada com arquitetura _MVC_ para utilizar uma arquitetura Hexagonal. Na versão atual do projeto, a arquitetura da aplicação é refatorada para _Clean Architecture_.

## Em que consiste a aplicação?

- Qual é o problema de negócios que a aplicação propõe resolver?

  - Gerenciar a venda de ingressos para eventos.

- Qual é a dinâmica de negócios?

  1. Um evento é criado por um parceiro, como, por exemplo, uma casa de _shows_ interessada na realização do evento;
  2. Um ingresso de determinado evento é comprado por um cliente.

- Nesse sentido, qual é a dinâmica da aplicação?

  1. Cadastrar um cliente;
  2. Cadastrar um parceiro;
  3. Cadastrar um evento;
  4. Inscrever um cliente em determinado evento.

## Motivação

A preocupação maior da arquitetura Hexagonal é com a separação do _core_ da aplicação, ou seja, das regras de negócio definidas nas entidades de domínio e nos casos de uso do restante da infraestrutura que mantém a aplicação.

Nesse sentido, é comum nos depararmos com a afirmação de que toda aplicação _Clean Architecture_ é Hexagonal, mas nem toda aplicação Hexagonal é _Clean Architecture_ (Rodrigo Branas, 2023). Por quê? Porque, enquanto a _Clean Architecture_ também segue uma abordagem _middle-out_, ela impõe mais conceitos e adiciona uma maior granularidade na separação das camadas, de forma a evidenciar uma arquitetura gritante (_Screaming Architecture_).

Assim sendo, no cenário em que uma aplicação tiver que lidar com muitas regras de negócio e o negócio em si exigir poder acrescentar ou remover diversas funcionalidades todos os dias, de forma a manter a competitividade de mercado da organização, aplicar uma burocracia maior na organização do _design_ da aplicação que favoreça a manutenabilidade pode ser uma opção bastante considerável.

## Clean Architecture versus Arquitetura Hexagonal

A _Clean Architecture_:

- Define 4 camadas principais: _Enterprise Business Rules, Application Business Rules, Interface Adapters_ e _Frameworks and Drives_. Na arquitetura Hexagonal, a preocupação maior é com a camada de _Application_;

- Salienta uma arquitetura gritante (_Screaming Architecture_), principalmente na separação das camadas, na nomenclatura e na exposição de diretórios ou de módulos da aplicação;

- Frisa em não utilizar _frameworks_ e bibliotecas em casos de uso e entidades de domínio e, quando possível, em _Interface Adapters_, como os _Controllers_;

- Utiliza o conceito de _Gateway_, ao invés de _Repository_. Neste caso, _Gateway_ é um conceito mais amplo, que abrange acesso a qualquer recurso externo. Na verdade, é possível utilizar os dois conceitos dentro de uma aplicação, com o conceito de _Repository_ sendo utilizado para gerenciar a persistência de agregados;

- Traz o conceito de _Presenter_: um manipulador da resposta dos casos de uso;

- Separa camadas específicas de _Entities_ e de _Use Cases_; já, na arquitetura Hexagonal, as entidades e os casos de uso ficam dentro da mesma camada de _Application_;

- Separa _Frameworks and Drivers_ de _Interface Adapters_. Essa separação nem sempre apresenta uma vantagem, dependendo da linguagem de programação e do seu ecossistema de bibliotecas e _frameworks_. Isso pode apresentar uma vantagem real, a depender da linguagem de programação, quando, por exemplo, separamos o _framework_ de roteamento _REST_ (localizado na camada de _Framework and Drivers_) dos _Controllers_ (localizados na camada de _Interface Adpaters_). Por quê? Porque vai nos permitir trocar de roteador conforme demandar a necessidade de desempenho da aplicação.

## Use Cases

### O caso de uso deve interagir com outros casos de uso?

O caso de uso pode interagir com outro caso de uso, mas a _Clean Architecture_ recomenda que um caso de uso não deveria estar acoplado a outro caso de uso. Lembrando que quaisquer tipo de acoplamento pode gerar fragilidades no sistema.

Normalmente, interagir com outro caso de uso está associado à intenção de evitar duplicidade de código.

Por exemplo, entre um caso de uso de Criação da Nota Fiscal e um caso de uso de Envio do _Email_ ao Comprador. Poderíamos realizar a chamada do caso de uso de Envio de _Email_ dentro do caso de uso de Emissão da Nota Fiscal ou deveríamos duplicar o código sempre que surgir a necessidade de enviar um _email_ ao comprador?

Primeiramente, a _Clean Architecture_ recomenda verificar se a duplicidade, neste caso, é real ou acidental. Caso seja acidental, não há problema em duplicar o código temporariamente.

Devemos lembrar que duplicidade real se percebe quando devemos alterar, a qualquer momento, o mesmo código em mais partes do sistema; já a duplicidade acidental é quando existe um código momentaneamente duplicado, mas que, ao longo do tempo, sabemos que pode ser alterado por razões diferentes, conforme o Princípio da Responsabilidade Única (_Single-Responsibility Principle_).

Caso verifiquemos que estamos lidando, de fato, com duplicidade real, devemos analisar também se existe uma casualidade de ação e reação entre os casos de uso.

No exemplo acima, verificamos que sim; caso a nota fiscal seja emitida com sucesso, um _email_ deveria ser enviado ao comprador. Neste caso, então, podemos fazê-los interagir entre si de maneira desacoplada através de eventos.

Assim, podemos, então, introduzir o conceito de eventos de domínio do _Domain-Driven Design_ (_DDD_). Neste caso, eventos de domínio são gerados a partir de mutações; quando se manipulam agregados, eles podem produzir eventos de domínio, que podem ser propagados para outras partes da aplicação, de forma a notificar e interagir com outros agregados e casos de uso sem gerar um acoplamento.

## Repositórios & Use Cases

### Como o caso de uso deve trabalhar com múltiplos repositórios/gateways?

Caso múltiplos repositórios ou _gateways_ estejam sendo utilizados em um caso de uso para a obtenção de dados, isso não fere as boas práticas. Mas, se múltiplos repositórios estiverem sendo utilizados para persistir dados, isso pode ser um indicativo de que o caso de uso pode estar assumindo muitas responsabilidades, ferindo o Princípio da Responsabilidade Única e tornando-se um anti-padrão de _Big Ball of Mud_.

Neste caso, também podemos fazer a interação entre diferentes casos de uso baseando-se em eventos de domínio e com consistência eventual.

## Tratamento de Eventos

Normalmente, os eventos de domínio podem ser publicados a partir de 3 maneiras diferentes:

- _Publish righ away_: logo que acontece uma alteração no agregado, é publicado um evento a partir do _Domain Event Publisher_. Não é uma abordagem muito utilizada, porque o evento é disparado antes de o estado do agregado estar persistido;

- _Publish after persistence_: logo que o estado do agregado é persistido, é publicado o evento. Não é garantido que o evento seja publicado no caso de um _bug_ do sistema, aonde se esquece de fazer a chamada para o _Domain Event Publisher_. Também não é garantido que o evento chegue até o sistema de mensageria, por exemplo, caso o _broker_ ou a própria aplicação esteja fora do ar;

- _Publish through persistence_ (_Job_ ou _CDC_): consiste em usar a camada de persistência para persistir, juntamente com o agregado, os eventos de domínio e, logo após, obter esses eventos de domínio da camada de persistência a partir de um _job_ ou de um mecanismo mais sofisticado de _message relay_, como um _Change Data Capture_ (_CDC_) - o _Oracle GoldenGate_, por exemplo - e publicar para o _message broker_.


## Geração do Ingresso

Na versão anterior da aplicação, o caso de uso _SubscribeCustomerToEventUseCase_ fazia uso de múltiplos repositórios para persistir os dados; primeiramente, era gerado o ingresso e, a seguir, era atualizado o evento.

Conforme vimos, múltiplos repositórios para persistir dados em um caso de uso é um indicativo de excesso de responsabilidades. Vimos também que uma estratégia muito pertinente para resolver esse acoplamento é criar um novo caso de uso e interagir os casos de uso por meio de eventos de domínio e consistência eventual. É o que fazemos na versão atual da aplicação, iniciando por uma readequação na modelagem dos agregados.


## Event-Driven Architecture

O caso de uso de _SubscribeCustomerToEventUseCase_ é refatorado para remover a criação do ingresso e, ao invés disso, criar um evento de reserva do ingresso. Assim, quando o ingresso é reservado, de fato é gerado uma reserva, mas do ponto de vista do domínio do evento.

Assim, é gerado um evento de domínio, o _EventTicketReserved_, que é armazenado em uma tabela chamada _outbox_, já que estamos lidando com o padrão _Transactional Outbox_. Assim, um _message relay_, implementado aqui como um _job_, é responsável por ler da tabela _outbox_ a cada 2 segundos e publicar, caso haja, um evento de domínio para o _message broker_.

A seguir, uma aplicação, que poderia ser essa mesma, escuta da fila do _broker_ e verifica, na mensagem, que trata-se de um evento de ingresso reservado (_EventTicketReserved_) e que deve ser criado um novo ingresso para esse evento.

Então, é chamado o caso de uso de Criar Ingresso e um novo evento é devolvido de ingresso criado (_TicketCreated_), que é publicado novamente no _message broker_ e a aplicação novamente escuta da fila do _broker_, formando, assim, um processo conforme arquitetura _Event-Driven_, aonde o sistema começa a ter eventos de domínio conversando com as aplicações e as aplicações vão tornando-se desacopladas.

Referências

MBA ARQUITETURA FULL CYCLE. Arquitetura Hexagonal e Clean Architecture. 2023. Disponível em: https://plataforma.fullcycle.com.br. Acesso em: 20 set. 2023.

MICROSERVICE ARCHITECTURE. Pattern: Transactional outbox. 2023. Disponível em: https://microservices.io/patterns/data/transactional-outbox.html. Acesso em: 20 set. 2023.

