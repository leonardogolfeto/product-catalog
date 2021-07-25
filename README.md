[![Java CI](https://github.com/leonardogolfeto/product-catalog/actions/workflows/build.yml/badge.svg?branch=master)](https://github.com/leonardogolfeto/product-catalog/actions/workflows/build.yml)
# Catálogo De Produtos

## Pré-requisitos

- Docker
- Docker Compose

## Tecnologias utilizadas

- Java 11
- Postgres
- Spring Boot
- Spring Cloud Gateway
- Spring Cloud Netflix Eureka
- Spring Cloud Sleuth
- Liquibase
- Docker
- Docker Compose
- Testcontainers

## Importando projeto no Intellij

 1. Clonar o projeto em um diretorio de sua preferência.
 2. Abrir o intellij
 3. No menu superior, acessar **File** > **Open**
 4. Navegar até a pasta onde o projeto foi clonado
 5. Double click no arquivo **pom.xml**
 6. Selecionar a opção **Open as Project**

## Testes

Para executar os testes entre na pasta raiz do projeto e execute:

```shell
./mvnw clean test
```

## Empacotando e executando a aplicação

Com docker rodando, execute o comando na pasta raíz do projeto:

```shell
./mvnw clean package
```

Após o término do comando, serão criadas 3 imagens docker, sendo elas: 

- **br.com.uol.compasso/catalog** (Contendo o micro serviço do catálogo de produtos)
- **br.com.uol.compasso/discovery** (Contendo um micro serviço de service discovery)
- **br.com.uol.compasso/gateway** (Contendo um micro serviço de gateway)

Tendo essas imagens criadas com sucesso, basta executar o comando na pasta raíz do projeto:

```shell
docker-compose up
```

É importante ressaltar que para o funcionamento correto da aplicação as portas **5432** e **9999** devem estar disponíveis.

## Eureka UI
O Projeto conta com um dashboard do Eureka, onde estão presentes todos os serviços e suas respectivas instâncias registradas. 

Para visualizar acesse: _*http://localhost:9999/eureka-ui/*_

## Swagger UI

O Projeto conta com uma interface visual do Swagger, onde está presente toda a documentação dos endpoints. 

Para visualizar acesse: _*http://localhost:9999/swagger-ui/*_


