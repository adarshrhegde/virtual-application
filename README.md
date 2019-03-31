Docker SBT Packager :  https://www.scala-sbt.org/sbt-native-packager/formats/docker.html

spring boot scala : https://github.com/bjoernjacobs/spring-boot-scala

capstan install : https://github.com/cloudius-systems/capstan/wiki/Capstan-Installation

SBT : https://stackoverflow.com/questions/45410630/spring-boot-how-can-i-build-a-runnable-jar-with-sbt

https://stackoverflow.com/questions/38792031/springboot-making-jar-files-no-auto-configuration-classes-found-in-meta-inf


sbt docker:publishLocal

sbt docker:publish

docker-compose up


New Game : 

POST http://localhost:8080/new-game

Content Type : application/json

{"firstMove" : "false", "playerName" : "Adarsh"}


Move :

POST http://localhost:8080/move

Content Type : application/json

{"startSquare" : "d7", "endSquare" : "d6", "session":"Adarsh-BLACK", "playerColor" : "BLACK"}

Quit :

POST http://localhost:8080/quit-game

Content Type : application/json

Adarsh-BLACK


Add lib jar to classpath

curl -i -H "Content-Type:application/json" -X POST -d '{"firstMove" : "false", "playerName" : "Adarsh"}' http://localhost:8080/new-game

curl -i -H "Content-Type:application/json" -X POST -d '{"startSquare" : "d7", "endSquare" : "d6", "session":"Adarsh-BLACK", "playerColor" : "BLACK"}' http://localhost:8080/move

curl -i -H "Content-Type:text/plain" -X POST -d 'Adarsh-BLACK' http://localhost:8080/quit-game