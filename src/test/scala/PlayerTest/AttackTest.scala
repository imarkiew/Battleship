package PlayerTest

import Board.Board
import Config.Config
import Coordinate.Coordinate
import GameLogic.Player.Player
import Ship.{Ship, ShipPosition}
import State.PlayerState
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers


class AttackTest extends AnyFlatSpec with Matchers {

  implicit val config = Config(" ", 2, 2, 2)
  val player = new Player
  val enemyPlayerShips = Seq(Ship(ShipPosition(Coordinate(2, 1), 2, ShipPosition.South)), Ship(ShipPosition(Coordinate(1, 2), 1, ShipPosition.South)))
  val enemyPlayerBoard = Board(enemyPlayerShips, 0, false)
  val playerState = PlayerState(enemyPlayerBoard, enemyPlayerShips, 0)

  "Player" should "return proper player state after attack, in case of a miss" in {
    val enemyPlayerBoardAfterAttack = Board(enemyPlayerBoard, 0, Map(Coordinate(1, 1) -> Board.MissCell))
    val playerStateAfterAttack = PlayerState(enemyPlayerBoardAfterAttack, enemyPlayerShips, 0)
    player.attack(Coordinate(1, 1), playerState, 0) shouldBe (playerStateAfterAttack, false)
  }

  "Player" should "return proper player state after attack, in case of a hit" in {
    val enemyPlayerBoardAfterAttack = Board(enemyPlayerBoard, 0, Map(Coordinate(2, 1) -> Board.HitCell))
    val enemyPlayerShipPositionsAfterAttack = Seq(Ship(Map(Coordinate(2, 1) -> true, Coordinate(2, 2) -> false)), Ship(Map(Coordinate(1, 2) -> false)))
    val playerStateAfterAttack = PlayerState(enemyPlayerBoardAfterAttack, enemyPlayerShipPositionsAfterAttack, 0)
    player.attack(Coordinate(2, 1), playerState, 0) shouldBe (playerStateAfterAttack, true)
  }

  "Player" should "return proper player state after attack, in case of sinking a ship" in {
    val enemyPlayerBoardAfterAttack = Board(enemyPlayerBoard, 0, Map(Coordinate(1, 2) -> Board.WreckCell))
    val enemyPlayerShipPositionsAfterAttack = Seq(Ship(Map(Coordinate(2, 1) -> false, Coordinate(2, 2) -> false)), Ship(Map(Coordinate(1, 2) -> true)))
    val playerStateAfterAttack = PlayerState(enemyPlayerBoardAfterAttack, enemyPlayerShipPositionsAfterAttack, 1)
    player.attack(Coordinate(1, 2), playerState, 0) shouldBe (playerStateAfterAttack, true)
  }

}
