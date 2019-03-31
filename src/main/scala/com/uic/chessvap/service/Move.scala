package com.uic.chessvap.service


/**
  * The Move type represents a move by a player
  */
class Move extends Response {

  var startSquare : String = _
  var endSquare : String = _
  var session : String = _
  var playerColor : String = _

  // getters are used by the Spring boot web for REST calls
  def getStartSquare = startSquare
  def getEndSquare = endSquare
  def getSession = session
  def getPlayerColor = playerColor

}

object Move {

  def apply(startSquare :String, endSquare : String, session : String, playerColor : String): Move = {

    var move = new Move()
    move.startSquare = startSquare
    move.endSquare = endSquare
    move.session = session
    move.playerColor = playerColor

    move
  }
}
