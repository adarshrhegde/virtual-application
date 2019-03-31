Chess VAP Application:

This project exposes web services using which the end user can play the chess game with a computer. The underlying chess game is an open source project on Github (JavaChessOpen). 

We have defined the following Web Services(RESTful) in the IChessEngine class using which the user can interact with the app:

1. New Game Request : The end user sends a new game Request (POST)

    
    POST http://localhost:8080/new-game
    
    Content Type : application/json
    
    {"firstMove" : "false", "playerName" : "Adarsh"} // firstMove is used to specify if player wants to play first

   The Computer responds with the created session which the user is expected to use on all further requests. 
    
  

2. Move Request : 
       
    
    POST http://localhost:8080/move
    
    Content Type : application/json
    
    {"startSquare" : "d7", "endSquare" : "d6", "session":"Adarsh-BLACK", "playerColor" : "BLACK"}
    
   Here the user specifies the start and end square location for the piece being moved. The user also mentions the session provided by the app.
    
    
3. Quit Game Request :

    
    POST http://localhost:8080/quit-game
    
    Content Type : application/json
    
    Adarsh-BLACK
    
   Here the user provides the session to the Chess app. As a result, app ends the game.


Environment:

1. Build Tool : SBT . Plugin sbt-native-packager is used to package the app and generate the docker image.

2. Docker


Instructions:

    Add jChess-1.5.jar within lib directory to classpath


How to Run :

1. Generate Docker image: 
    Following configurations have been made for docker:
    
        packageName in Docker := "chessgame" // name of the image
        
        version in Docker := "1.2.0" // image version 
        
        dockerBaseImage := "anapsix/alpine-java" // the Docker image to base on 
        
        dockerRepository := Some("adarsh23") // Repository name
        
        dockerUpdateLatest := true // creates tag 'latest' as well when publishing

    
    // The following generates a docker image locally
    sbt docker:publishLocal
    
    // The following publishes a docker image to the hub (Repository adarsh23)
    sbt docker:publish
    
    // Creates a container when run within the project root directory
    docker-compose up
    
2. Launch Amazon EC2 instance:
     
    a. Install docker
    b. docker pull adarsh23/chessgame
    c. docker run -p 8080:8080 adarsh23/chessgame
    
    The above command will execute the app 


3. OSV with Capstan: (Not implemented successfully)
    
    Challenges faced with Capstan:
    a. Running Capstan on Windows : Known issue not fixed in current build
    b. Linux Subsystem for Windows : Issue with KVM
    c. Amazon EC2 : Amazon uses its own package manager. Required dependencies were not available. Tried manually downloading each but failed due to version requirements.
    d. Virtual Box : Issue with KVM. Later found out virtualization within Vbox is not supported as mentioned in some forums.
	e. Local Ubuntu : Local installation of Ubuntu couldn't connect to wifi . This is a known issue related to LAN drivers for my machine.


Testing the Services:

1. Run unit tests implemented in the project

2. Test using CURL or Rest client


    Following are the curl commands that can be used to test the services :
    
    curl -i -H "Content-Type:application/json" -X POST -d '{"firstMove" : "false", "playerName" : "Adarsh"}' http://localhost:8080/new-game
    
    curl -i -H "Content-Type:application/json" -X POST -d '{"startSquare" : "d7", "endSquare" : "d6", "session":"Adarsh-BLACK", "playerColor" : "BLACK"}' http://localhost:8080/move
    
    curl -i -H "Content-Type:text/plain" -X POST -d 'Adarsh-BLACK' http://localhost:8080/quit-game
    
    
Results:

Please refer to Screenshots.docx for results of the execution on Amazon EC2. I have attached screenshots for testing with CURL as well as Rest Client.