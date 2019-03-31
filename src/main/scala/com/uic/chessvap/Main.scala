package com.uic.chessvap

import pl.art.lach.mateusz.javaopenchess.core.ai.AIFactory
import pl.art.lach.mateusz.javaopenchess.core.moves.MovesHistory
import pl.art.lach.mateusz.javaopenchess.core.pieces.implementation.King
import pl.art.lach.mateusz.javaopenchess.core.players.implementation.{ComputerPlayer, HumanPlayer}
import pl.art.lach.mateusz.javaopenchess.core.players.{Player, PlayerType}
import pl.art.lach.mateusz.javaopenchess.core.{Chessboard, Colors, Game, Square}
import pl.art.lach.mateusz.javaopenchess.utils.Settings

class Main {

}

/**
  * Test class used to test JavaChessOpen directly without services
  */
object Main extends App {

  val p1 : Player = new HumanPlayer("Adarsh", Colors.WHITE.getColorName)
  val p2 : Player = new ComputerPlayer("Tron", Colors.BLACK.getColorName)
  val settings : Settings = new Settings(p1, p2)

  val game : Game = new Game()
  game.setSettings(settings)
  game.setAi(AIFactory.getAI(2))
  val moves : MovesHistory = game.getMoves
  val chessBoard : Chessboard = game.getChessboard//new Chessboard(settings, moves)

  //game.newGame()
  chessBoard.setPieces4NewGame(settings.getPlayerWhite, settings.getPlayerBlack)

  val activePlayer : Player = settings.getPlayerWhite
  game.setActivePlayer(activePlayer)

 /* chessBoard.getSquares.foreach(item => {
    item.foreach(sq => println(sq.piece +
      ": " + sq.getPozX.toString + ", " + sq.getPozY.toString))
  })*/



  val startSquare : Square = chessBoard.getSquare(4, 1)
  val endSquare : Square = chessBoard.getSquare(4, 2)
  chessBoard.setActiveSquare(startSquare)

  println("Active player : " + game.getActivePlayer.getName)
  if(cannotInvokeMoveAction(endSquare)){
    println("Cannot move")
  } else {
    if(endSquare == startSquare){
      println("No move detected")
    } else {
      if(canInvokeMoveAction(endSquare)){

        println("Performing move")
        chessBoard.move(startSquare,endSquare)

        println("Switching players")
        game.nextMove()

        println("Active player : " + game.getActivePlayer.getName)

        var king : King = null
        if (game.getActivePlayer == game.getSettings.getPlayerWhite)
          king = chessBoard.getKingWhite
        else king = chessBoard.getKingBlack

        king.isCheckmatedOrStalemated match {
          case 1 =>
            println(String.format("Checkmate! %s player lose!", king.getPlayer.getColor.toString))
          case 2 =>
            println("Stalemate! Draw!")
          case _ =>
            println("The King is safe...We live to fight another day!")
        }

        if(canDoComputerMove){
          println("Performing computer move")
          game.doComputerMove()
        }

      } else {
        println("Illegal move ")
      }
    }

  }



  chessBoard.getSquares.foreach(item => {
    item.foreach(sq => println(sq.piece +
      ": " + sq.getPozX.toString + ", " + sq.getPozY.toString))
  })

  private def cannotInvokeMoveAction (sq: Square): Boolean = {
    return (sq == null && sq.piece == null && chessBoard.getActiveSquare == null) ||
      (chessBoard.getActiveSquare == null && sq.piece != null && (sq.getPiece.getPlayer ne game.getActivePlayer) )
  }

  private def canInvokeMoveAction(sq: Square) = {
    val activeSq = chessBoard.getActiveSquare
    activeSq != null && activeSq.piece != null && activeSq.getPiece.getAllMoves.contains(sq)
  }

  private def canDoComputerMove = game.getSettings.isGameAgainstComputer &&
    (game.getActivePlayer.getPlayerType eq PlayerType.COMPUTER) && null != game.getAi



}


