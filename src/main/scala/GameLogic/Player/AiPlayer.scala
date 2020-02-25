package GameLogic.Player

import Config.Config
import Coordinate.Coordinate
import Ship._
import State.PlayerState
import Utils.Utils._
import scala.annotation.tailrec
import scala.util.Random.nextInt


object AiPlayer extends Player {

  def placeShipsRandomly(implicit config: Config): Seq[Ship] = {

    @tailrec
    def generateShipUntilIsProperlyPlaced(shipLength: Int, ships: Seq[Ship])(implicit config: Config): Seq[Ship] = {
      val x = 1 + nextInt(config.xLengthOfCoreBoard)
      val y = 1 + nextInt(config.yLengthOfCoreBoard)
      val directions = ShipPosition.directions
      val direction = directions(nextInt(directions.length))
      val basicBoundaries = areBoundariesForShipMet(x, y, shipLength, direction)
      val ship = Ship(ShipPosition(Coordinate(x, y), shipLength, direction))
      val boundariesAmongOtherShips = isShipPlacedProperlyAmongOtherShips(ship, ships)

      if(basicBoundaries && boundariesAmongOtherShips)
        ships :+ ship
      else generateShipUntilIsProperlyPlaced(shipLength, ships)
    }

    1.to(config.numberOfShips)
      .foldLeft(Seq[Ship]())((shipsAcc, currentShipLength) => generateShipUntilIsProperlyPlaced(currentShipLength, shipsAcc))
  }

  def attack(playerState: PlayerState)(implicit config: Config): (PlayerState, Boolean) = attackEnemy(playerState, randomAttackPolicy)

  private def randomAttackPolicy(config: Config): Coordinate = {
    val x = 1 + nextInt(config.xLengthOfCoreBoard)
    val y = 1 + nextInt(config.yLengthOfCoreBoard)

    Coordinate(x, y)
  }

  private def attackEnemy(playerState: PlayerState, attackPolicy: Config => Coordinate)(implicit config: Config): (PlayerState, Boolean) = super.attack(attackPolicy(config), playerState)

}
