package com.uic.chessvap.service
import com.uic.chessvap.ChessGame
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._

/**
  * ChessEngine defines the services for interacting with the ChessVap
  */
@Controller class ChessEngine extends IChessEngine {


  /**
    * New Game request sent by end user
    * Example:
    * POST http://localhost:8080/new-game
    * Content Type : application/json
    * {"firstMove" : "false", "playerName" : "Adarsh"}
    * @param newGameRequest
    * @return
    */
  @PostMapping(path=Array("/new-game"), consumes = Array("application/json"),
    produces = Array("application/json"))
  @ResponseBody override def newGame(@RequestBody newGameRequest: NewGameRequest): Response = {

    if(null != newGameRequest.getPlayerName && newGameRequest.getPlayerName != ""){
      ChessGame.setupGame(newGameRequest.getFirstMove,
        newGameRequest.getPlayerName)
    }
    else {
      new ChessResponse("Improper request")
    }

  }


  /**
    * Quit Game
    * Example:
    * POST http://localhost:8080/quit-game
    * Content Type : application/json
    * Adarsh-BLACK
    *
    * @param session
    * @return
    */
  @PostMapping(Array("/quit-game"))
  @ResponseBody override def quit(@RequestBody session: String): Response = {

    if(ChessGame.quitGame(session))
      new ChessResponse("You have left the game")
    else
      new ChessResponse("Unauthorized request. Check your session key.")

  }


  /**
    * Perform move by end user
    * Example:
    * POST http://localhost:8080/move
    * Content Type : application/json
    * {"startSquare" : "d7", "endSquare" : "d6", "session":"Adarsh-BLACK", "playerColor" : "BLACK"}
    *
    * @param move
    * @return
    */
  @PostMapping(Array("/move"))
  @ResponseBody override def move(@RequestBody move : Move): Response = {

    (ChessGame.performMove(move.startSquare, move.endSquare, move.session))

  }

}


/**
  * NewGameRequest : Used to accept user request to connect to Chess Vap
  *
  */
class NewGameRequest{

  var firstMove : Boolean = _
  var playerName : String = _

  def getFirstMove = firstMove
  def getPlayerName = playerName
}