package com.uic.chessvap

import java.net.URI

import com.uic.chessvap.service._
import io.restassured
import io.restassured.RestAssured
import org.junit.{Before, FixMethodOrder, Test}
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

import org.springframework.http.{HttpEntity, HttpHeaders, MediaType, ResponseEntity}
import org.springframework.test.context.junit4.SpringRunner

import org.springframework.boot.context.embedded.LocalServerPort

/**
  * This is the test class used to test the Chess Vap services
  */
@RunWith(classOf[SpringRunner])
@SpringBootTest(classes = Array(classOf[ChessVapApplication]), webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ChessVapTest {



  @Autowired
  var chessEngine : ChessEngine = _



  @LocalServerPort
  private var port :Int = 0


  /**
    * Test if chess engine bean is created when the Spring Boot Context is loaded
    * @throws java.lang.Exception
    */
  @Test
  @throws(classOf[Exception])
  def testChessEngineShouldBeLoadedOnStartup() : Unit = {

    assert(chessEngine != null)
  }

  /**
    * Test the response for a new game web service request
    */
  @Test
  def testZNewGameRequestResponse() : Unit = {

    val url: URI = new URI(s"http://localhost:$port/new-game")
    val newGameReq = new NewGameRequest()
    newGameReq.playerName = "Adarsh"
    newGameReq.firstMove = true // White player

    val response : restassured.response.Response = RestAssured.given()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(newGameReq)
        .post(url)

    assert(response.statusCode() == 200)
    val newGameResponse : String = (response.getBody.print())
    assert(newGameResponse.contains("WHITE"))
    assert(newGameResponse.contains("Adarsh-WHITE"))
    assert(newGameResponse.contains("Make next move"))

  }

  /**
    * Test the response for a new game web service request when a
    * game is already in progress
    */
  @Test
  def testNewGameRequestResponseInprogressGame() : Unit = {

    val url: URI = new URI(s"http://localhost:$port/new-game")
    val newGameReq = new NewGameRequest()
    newGameReq.playerName = "Adarsh"
    newGameReq.firstMove = true // White player

    val response : restassured.response.Response = RestAssured.given()
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .body(newGameReq)
      .post(url)

    val response2 : restassured.response.Response = RestAssured.given()
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .body(newGameReq)
      .post(url)

    assert(response.statusCode() == 200)
    val newGameResponse : String = (response.getBody.print())
    assert(newGameResponse.contains("A game is in progress already. Try again after some time."))


  }

  /**
    * Test the response for a move request
    */
  @Test
  def testMoveRequestResponse() : Unit = {

    val url1: URI = new URI(s"http://localhost:$port/new-game")
    val newGameReq = new NewGameRequest()
    newGameReq.playerName = "Adarsh"
    newGameReq.firstMove = false // Black player

    val response : restassured.response.Response = RestAssured.given()
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .body(newGameReq)
      .post(url1)

    val url2: URI = new URI(s"http://localhost:$port/move")
    val move = new Move()
    move.playerColor = "BLACK"
    move.startSquare = "d7"
    move.endSquare = "d6"
    move.session = "Adarsh-BLACK"
//"startSquare" : "d7", "endSquare" : "d6", "session":"Adarsh-BLACK", "playerColor" : "BLACK"

    val response2 : restassured.response.Response = RestAssured.given()
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .body(move)
      .post(url2)

    val moveResponse : String = (response2.getBody.print())
    // check if white player has made move in response
    assert(moveResponse.contains("white"))
  }

  /**
    * Test the response for a quit game request
    */
  @Test
  def testQuitRequestResponse() : Unit = {

    val url1: URI = new URI(s"http://localhost:$port/new-game")
    val newGameReq = new NewGameRequest()
    newGameReq.playerName = "Adarsh"
    newGameReq.firstMove = false

    RestAssured.given()
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .body(newGameReq)
      .post(url1)

    val url2: URI = new URI(s"http://localhost:$port/quit-game")

    val response2 : restassured.response.Response = RestAssured.given()
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .body("Adarsh-BLACK")
      .post(url2)

    // confirm quit game
    assert(response2.getBody.print().contains("You have left the game"))
  }



}
