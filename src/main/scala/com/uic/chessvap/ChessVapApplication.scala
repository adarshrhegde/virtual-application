package com.uic.chessvap

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.boot.test.web.client.TestRestTemplate

object ChessVapApplication {
  def main(args: Array[String]) : Unit = SpringApplication.run(classOf[ChessVapApplication], args :_ *)
}


/**
  * The spring boot application
  */
@SpringBootApplication
class ChessVapApplication {}
