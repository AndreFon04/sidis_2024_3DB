# SIDIS Library Project

Project for the SIDIS Subject in the LETI Course

---
## Initial Setup

<p>For Rabbit DB initialization run this command on the console:</p>

``` docker run -d --hostname my-rabbit --name some-rabbit -e RABBITMQ_DEFAULT_USER=user -e RABBITMQ_DEFAULT_PASS=password -p 8056:15672 -p 5672:5672 rabbitmq:3-management ```


### Ports for the Spring Instances:

* **Book**
    * **Command [1]:** 7070
    * **Command [2]:** 7071
    * **Command [3]:** 7072
    * **Query [1]:** 7075
    * **Query [2]:** 7076
    * **Query [3]:** 7077


* **User**
    * **Command [1]:** 7080
    * **Command [2]:** 7081
    * **Command [3]:** 7082
    * **Query [1]:** 7085
    * **Query [2]:** 7086
    * **Query [3]:** 7087


* **Lending**
    * **Command [1]:** 7090
    * **Command [2]:** 7091
    * **Command [3]:** 7092
    * **Query [1]:** 7095
    * **Query [2]:** 7096
    * **Query [3]:** 7097



## Documentation

Documentation will be on docs folder in the base directory:
1. [First Delivery](docs/Entrega1)
    *  [N1](docs/Entrega1/nivel_1)
    *  [N2](docs/Entrega1/nivel_2)
    *  [N3](docs/Entrega1/nivel_3)


2. [Second Delivery](docs/Entrega2)
    *  [N1](docs/Entrega2/nivel_1)
    *  [N2](docs/Entrega2/nivel_2)
    *  [N3](docs/Entrega2/nivel_3)


3. [Postman Collection](docs/Entrega2/SIDIS.postman_collection.json)



### Database Urls:

1. **Book:**
    * **Command [1]:** jdbc:h2:file:./bookCommand/src/main/resources/db/bookCommand_DB_1
    * **Command [2]:** jdbc:h2:file:./bookCommand/src/main/resources/db/bookCommand_DB_2
    * **Command [3]:** jdbc:h2:file:./bookCommand/src/main/resources/db/bookCommand_DB_3
    * **Query [1]:** jdbc:h2:file:./bookQuery/src/main/resources/db/bookQuery_DB_1
    * **Query [2]:** jdbc:h2:file:./bookQuery/src/main/resources/db/bookQuery_DB_2
    * **Query [3]:** jdbc:h2:file:./bookQuery/src/main/resources/db/bookQuery_DB_3


2. **User:**
    * **Command [1]:** jdbc:h2:file:./userCommand/src/main/resources/db/userCommand_DB_1
    * **Command [2]:** jdbc:h2:file:./userCommand/src/main/resources/db/userCommand_DB_2
    * **Command [3]:** jdbc:h2:file:./userCommand/src/main/resources/db/userCommand_DB_3
    * **Query [1]:** jdbc:h2:file:./userQuery/src/main/resources/db/userQuery_DB_1
    * **Query [2]:** jdbc:h2:file:./userQuery/src/main/resources/db/userQuery_DB_2
    * **Query [3]:** jdbc:h2:file:./userQuery/src/main/resources/db/userQuery_DB_3


3. **Lending:**
   * **Command [1]:** jdbc:h2:file:./lendingCommand/src/main/resources/db/lendingCommand_DB_1
   * **Command [2]:** jdbc:h2:file:./lendingCommand/src/main/resources/db/lendingCommand_DB_2
   * **Command [3]:** jdbc:h2:file:./lendingCommand/src/main/resources/db/lendingCommand_DB_3
   * **Query [1]:** jdbc:h2:file:./lendingQuery/src/main/resources/db/lendingQuery_DB_1
   * **Query [2]:** jdbc:h2:file:./lendingQuery/src/main/resources/db/lendingQuery_DB_2
   * **Query [3]:** jdbc:h2:file:./lendingQuery/src/main/resources/db/lendingQuery_DB_3


### Tabelas de Assinaturas e Publicações

As tabelas abaixo descrevem como cada serviço interage com o Message Broker:
- **Assinaturas:** Indica quais mensagens cada serviço consome.
- **Publicações:** Indica quais mensagens cada serviço publica no broker.



### Assinaturas

|                    | Author | Book | User | Reader | Lending |
|--------------------|--------|------|------|--------|---------|
| **bookCommand**    | x      | x    |      |        |         |
| **bookQuery**      | x      | x    |      |        |         |
| **userCommand**    | x      | x    | x    | x      |         |
| **userQuery**      | x      | x    | x    | x      |         |
| **lendingCommand** | x      | x    | x    | x      |   x     |
| **lendingQuery**   | x      | x    | x    | x      |   x     |



### Publicações (Publish)

|                    | Author | Book | User | Reader | Lending |
|--------------------|--------|------|------|--------|--------|
| **bookCommand**    |   x    |   x  |      |        |        |
| **bookQuery**      |        |      |      |        |        |
| **userCommand**    |        |      |   x  |   x    |        |
| **userQuery**      |        |      |      |        |        |
| **lendingCommand** |     |      |      |        |   x    |
| **lendingQuery**   |     |      |      |        |        |



Esta tabela representará o processo que ocorre depois da chegada ao Message Broker nos diagramas da vista de processo.
