package com.uic.chessvap.service

/**
  * Response from the web service
  */
trait Response {

}

/**
  * ChessResponse : Used for general response from the service
  * @param message
  */
class ChessResponse(message : String) extends Response {

  def getMessage() = message

}

/**
  * NewGameResponse : Used to respond to New Game Request
  * @param playerColor
  * @param session
  * @param response
  */
class NewGameResponse(playerColor : String, session : String, response: Response) extends Response {

  def getPlayerColor = playerColor
  def getSession = session
  def getResponse = response
}