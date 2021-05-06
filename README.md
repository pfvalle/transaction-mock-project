Transaction Mock
------------------------------------------------------

Esse projeto consiste em um exercício para aprimorar conhecimento.

###Porta: 
```
localhost:8080
```
###Construção:
```
gradlew clean build
```
-------------------------------------------------------
Features
-------------------------------------------------------
###Version - 0.0.1

***GET - TRANSAÇÕES***
```
/v1/{id}/transacoes/{year}/{month}
```
####Descrição:
```
Consulta transacoes salvas em um arquivo Json. Caso não haja arquivos
ele cria transações com valores aleatórios salva em um arquivo(.Json)
e retorna.
```
-------------------------------------------------------
Library:
-------------------------------------------------------
* spring-boot-web
* spring-boot-validation
* spring-boot-test
* ktlint 
* mockk 
* jacoco

### Referências

https://pt.stackoverflow.com/
https://docs.gradle.org/current/userguide/jacoco_plugin.html
https://ktlint.github.io/