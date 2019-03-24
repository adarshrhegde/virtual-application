package com.uic.chessvap.service

trait Response {

}


class ChessResponse(message : String) extends Response {

  def getMessage() = message

}

class NewGameResponse(playerColor : String, session : String, response: Response) extends Response {

  def getPlayerColor = playerColor
  def getSession = session
  def getResponse = response
}