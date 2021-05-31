# eng-zap-challenge-groovy
Repositório da API do Desafio Zap

Este projeto foi desenvolvido para resolver o desafio Zap para desenvolvedor de software.

O desafio encontra-se no link abaixo:
https://olxbr.github.io/cultura/challenges/engineering.html

Apesar de o desafio sugerir utilizar algumas linguagens, dentre elas o Java, escolhi utilizar a linguagem Groovy, que é
baseada no Java e acredito ser aceitável para o objetivo do teste.

Foi construída uma API utilizando Spring Boot, Spock Framework (testes) e bibliotecas para validação de requests através
de anotações, que deixam o código mais limpo.


# Funcionamento

O projeto basicamente busca os dados JSON do arquivo ao inicializar e os deixa em memória.
Quando as requests são executadas ele aplica as regras de negócio para o tipo de request.

Obs.: Poderia até ficar já em memória com as regras basicas aplicadas e com os resultados filtrados, para ser mais performático,
mas como quis deixar o código pronto a inserção de um banco de dados, deixei o tratamento na hora da realização da request

# Instruções

### Rodar o projeto localmente
mvn spring-boot:run

### Rodar os testes
mvn test

### Deploy
Não implementado - falta de conhecimento até o momento, procuro oportunidades de aprender e trabalhar melhor essa e
outras skills de DevOps.
