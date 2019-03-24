package com.uic.chessvap.service
import com.uic.chessvap.ChessGame
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._

@Controller class ChessEngine extends IChessEngine {


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


  @PostMapping(Array("/quit-game"))
  @ResponseBody override def quit(@RequestBody session: String): Response = {

    if(ChessGame.quitGame(session))
      new ChessResponse("You have left the game")
    else
      new ChessResponse("Unauthorized request. Check your session key.")

  }

  @PostMapping(Array("/move"))
  @ResponseBody override def move(@RequestBody move : Move): Response = {

    (ChessGame.performMove(move.startSquare, move.endSquare, move.session))

  }

}


class NewGameRequest{

  var firstMove : Boolean = _
  var playerName : String = _

  def getFirstMove = firstMove
  def getPlayerName = playerName
}