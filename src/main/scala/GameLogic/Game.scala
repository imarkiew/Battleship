package GameLogic

import Board.Board
import Config.Config
import GameLogic.Player.AiPlayer.placeShipsRandomly
import GameLogic.Player.{AiPlayer, HumanPlayer}
import Ship.Ship
import State.{GameState, PlayerState}
import scala.annotation.tailrec
import scala.util.Random.nextBoolean
import Utils.Utils.{getUserInputWhileIsImproperForPlacingAShip, getUserInputWhileIsImproperForAttackCoordinate}


object Game {

  def startGame: Unit = {

    implicit val config = Config(" ", 6, 6, 4)

    println(s"The Battleship game has been started on ${config.xLengthOfCoreBoard} x ${config.yLengthOfCoreBoard} grid \n")

    val humanShips = 1.to(config.numberOfShips).reverse
      .foldLeft(Seq[Ship]())((ships, currentShipSize) => getUserInputWhileIsImproperForPlacingAShip(ships, currentShipSize))

    val humanBoard = Board(humanShips, false)

    println("-------------------------------------------------------------------")

    val aiShips = placeShipsRandomly
    val aiBoard = Board(aiShips, true)

    val initialGameState = GameState(PlayerState(aiBoard, aiShips, 0), PlayerState(humanBoard, humanShips, 0))
    val isHumanTurn = nextBoolean

    loopUntilEnd(initialGameState, isHumanTurn)
  }

  @tailrec
  private def loopUntilEnd(gameState: GameState, isHumanPlayerTurn: Boolean)(implicit config: Config): GameState = {

    println(s"You have destroyed ${gameState.humanPlayerState.numberOfShipsDestroyedByPlayer} out of ${config.numberOfShips} ships")
    gameState.aiPlayerState.enemyPlayerBoard.drawBoard

    println(s"Computer has destroyed ${gameState.aiPlayerState.numberOfShipsDestroyedByPlayer} out of ${config.numberOfShips} ships")
    gameState.humanPlayerState.enemyPlayerBoard.drawBoard

    if(gameState.aiPlayerState.numberOfShipsDestroyedByPlayer == config.numberOfShips) {
      println("You lost ! \n")
      gameState
    } else if(gameState.humanPlayerState.numberOfShipsDestroyedByPlayer == config.numberOfShips) {
      println("You won ! \n")
      gameState
    } else if(isHumanPlayerTurn){
        println("It's your turn - provide point coordinates \n")
        val attackCoordinate = getUserInputWhileIsImproperForAttackCoordinate
        val (newPlayerState, isHitOrWreck) = HumanPlayer.attack(attackCoordinate, gameState.humanPlayerState)
        val newGameState = GameState(newPlayerState, gameState.aiPlayerState)
        loopUntilEnd(newGameState, isHitOrWreck)
      } else {
        println("Computer's turn \n")
        val (newPlayerState, isHitOrWreck) = AiPlayer.attack(gameState.aiPlayerState)
        val newGameState = GameState(gameState.humanPlayerState, newPlayerState)
        loopUntilEnd(newGameState, !isHitOrWreck)
      }
    }
}

