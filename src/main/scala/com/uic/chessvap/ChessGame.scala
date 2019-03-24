package com.uic.chessvap

import com.uic.chessvap.service.{ChessResponse, NewGameResponse, Response}
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.boot.autoconfigure.web.ServerProperties.Session
import pl.art.lach.mateusz.javaopenchess.core.ai.AIFactory
import pl.art.lach.mateusz.javaopenchess.core.{Chessboard, Colors, Game, Square}
import pl.art.lach.mateusz.javaopenchess.core.moves.{Move, MovesHistory}
import pl.art.lach.mateusz.javaopenchess.core.pieces.implementation.King
import pl.art.lach.mateusz.javaopenchess.core.players.{Player, PlayerType}
import pl.art.lach.mateusz.javaopenchess.core.players.implementation.{ComputerPlayer, HumanPlayer}
import pl.art.lach.mateusz.javaopenchess.utils.Settings
import Move._

class ChessGame {}


object ChessGame {

  private val ASCII_OFFSET = 97
  private val LAST_SQUARE = 7

  var isGameInProgress : Boolean = false
  var p1 : Player = _
  var p2 : Player = _

  var settings : Settings = _

  var session : String = _
  var game : Game = _
  var moves : MovesHistory = _
  var chessBoard : Chessboard = _
  val logger : Logger = LoggerFactory.getLogger(classOf[ChessGame])

  def setupGame(isWhitePlayer : Boolean, playerName : String) : Response = {

    if(!isGameInProgress){

      var color : String = ""
      if(isWhitePlayer){

        p1 = new HumanPlayer(playerName, Colors.WHITE.getColorName)
        p2 = new ComputerPlayer("matrix", Colors.BLACK.getColorName)
        color = "WHITE"
      } else {

        p1 = new ComputerPlayer("matrix", Colors.WHITE.getColorName)
        p2 = new HumanPlayer(playerName, Colors.BLACK.getColorName)
        color = "BLACK"
      }

      logger.info(s"Initiating game for $playerName")
      game = new Game()
      settings = new Settings(p1, p2)
      game.setSettings(settings)
      game.setAi(AIFactory.getAI(2))
      game.setActivePlayer(p1)

      moves = game.getMoves
      chessBoard = game.getChessboard
      chessBoard.setPieces4NewGame(settings.getPlayerWhite, settings.getPlayerBlack)

      isGameInProgress = true
      session = s"$playerName-$color"
      logger.info(s"Game has started for $playerName")

      if(canDoComputerMove){
        logger.info("Performing computer move")
        val move = doComputerMove()
        val start = getAlgebraicNotation(move.getFrom.getPozX, move.getFrom.getPozY)
        val end = getAlgebraicNotation(move.getTo.getPozX, move.getTo.getPozY)

        new NewGameResponse(color, session, service.Move(start, end, session, "WHITE"))

      } else {
        new NewGameResponse(color, session, new ChessResponse("Make next move"))
      }

    } else {

      logger.info(s"New player $playerName has been blocked as game is already in progress")
      new ChessResponse("A game is in progress already. Try again after some time.")
    }
  }

  def quitGame(userSession : String) : Boolean = {

    if(session.equals(userSession)){
      p1 = null
      p2 = null
      game = null
      isGameInProgress = false
      session = null
      true
    } else
      false


  }

  def performMove(startSquareString : String, endSquareString : String, userSession : String): Response = {

    if(!session.equals(userSession))
      return new ChessResponse("Unauthorized user. Use the correct session key.")

    logger.info("Request to perform move")
    if(isGameInProgress){

      logger.info(s"Active player : ${game.getActivePlayer.getName}")
      logger.info(s"Move -> from $startSquareString to $endSquareString")

      val startSquare : Square = fromAlgebraicNotationToSquare(startSquareString)
      val endSquare : Square = fromAlgebraicNotationToSquare(endSquareString)

      logger.info(s"Active player color : ${game.getActivePlayer.getColor}")

      chessBoard.setActiveSquare(startSquare)
      logger.info(s"Active square color : ${chessBoard.getActiveSquare.piece.getPlayer.getColor}")

      if(game.getActivePlayer.getColor != chessBoard.getActiveSquare.piece.getPlayer.getColor)
        return new ChessResponse("Illegal move. Play according to the rules.")

      if(cannotInvokeMoveAction(endSquare)){

        logger.info("Cannot make this move")
        return new ChessResponse("Cannot perform the move")

      } else {

        if(endSquare == startSquare){

          logger.info("No move detected")
          return new ChessResponse("No move detected")

        } else {

          if(canInvokeMoveAction(endSquare)){

            logger.info("Performing move")
            chessBoard.move(startSquare,endSquare)

            logger.info("Switching players")
            game.nextMove()

            logger.info("Active player : " + game.getActivePlayer.getName)

            var king : King = null

            if (game.getActivePlayer == game.getSettings.getPlayerWhite)
              king = chessBoard.getKingWhite

            else
              king = chessBoard.getKingBlack

            // Check if checkmate
            king.isCheckmatedOrStalemated match {
              case 1 =>
                logger.info(String.format("Checkmate! %s player has lost!", king.getPlayer.getColor.toString))
                new ChessResponse(String.format("Checkmate! %s has lost!", king.getPlayer.getColor.toString))
              case 2 =>
                logger.info("Stalemate! Draw!")
                new ChessResponse("Stalemate! Draw!")
              case _ =>
                logger.info("The King is safe...We live to fight another day!")
            }

            if(canDoComputerMove){
              logger.info("Performing computer move")
              val move = doComputerMove()
              val start = getAlgebraicNotation(move.getFrom.getPozX, move.getFrom.getPozY)
              val end = getAlgebraicNotation(move.getTo.getPozX, move.getTo.getPozY)

              return service.Move(start, end, session, move.getTo.getPiece.getPlayer.getColor.getColorName)
            }

          } else {
            logger.info("Player has performed illegal move ")
            return new ChessResponse("Illegal move. Play according to the rules.")
          }
        }

      }
    }

    printChessboard
    return new ChessResponse("Game is not in progress")
  }


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

  private def printChessboard = {
    chessBoard.getSquares.foreach(item => {
      item.foreach(sq => logger.info(sq.piece +
        ": " + sq.getPozX.toString + ", " + sq.getPozY.toString))
    })
  }

  private def doComputerMove(): Move = {

    val move = game.getAi.getMove(game, game.getMoves.getLastMoveFromHistory)
    game.getChessboard.move(move.getFrom, move.getTo)

    if (null != move.getPromotedPiece)
      move.getTo.setPiece(move.getPromotedPiece)

    game.nextMove()

    move

  }

  def getAlgebraicNotation(x : Int, y : Int): String = {
    val letter = String.valueOf((x + ASCII_OFFSET).toChar)
    val result = letter + (Math.abs(LAST_SQUARE - y) + 1)
    logger.debug(s"Converted from ($x,$y) to $result")
    result
  }

  def fromAlgebraicNotationToSquare(str : String) : Square = {

    val x = Integer.valueOf(str.charAt(0)) - ASCII_OFFSET
    val y = Math.abs(LAST_SQUARE - (Integer.parseInt(str.charAt(1) + "") - 1))
    logger.debug(s"Converted from $str to ($x,$y)")
    chessBoard.getSquare(x, y)
  }

}


object PlayerColor extends Enumeration {

  val WHITE, BLACK, BLOCKED = Value

  def apply(typeString : String): Value = {

    PlayerColor.withName(typeString)
  }
}