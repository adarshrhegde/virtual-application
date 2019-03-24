package com.uic.chessvap

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

object ChessVapApplication {
  def main(args: Array[String]) : Unit = SpringApplication.run(classOf[ChessVapApplication], args :_ *)
}

@SpringBootApplication
class ChessVapApplication {}
