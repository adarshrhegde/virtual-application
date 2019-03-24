package com.uic.chessvap.service

import org.springframework.web.bind.annotation.{RequestBody, ResponseBody}


trait IChessEngine {


  @throws[Exception]
  def newGame(newGameRequest: NewGameRequest): Response

  @throws[Exception]
  def move(move : Move): Response

  @throws[Exception]
  def quit(session: String): Response
}
