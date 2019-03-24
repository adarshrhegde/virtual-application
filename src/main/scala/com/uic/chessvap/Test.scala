package com.uic.chessvap

import pl.art.lach.mateusz.javaopenchess.core.players.Player

object Test extends App {


  /*var a : Player = _
  print("-" + a + "-")*/

  //ChessGame.getAlgebraicNotation(6,7)

  ChessGame.fromAlgebraicNotationToSquare(ChessGame.getAlgebraicNotation(0,5))
  ChessGame.fromAlgebraicNotationToSquare(ChessGame.getAlgebraicNotation(5,5))

}
