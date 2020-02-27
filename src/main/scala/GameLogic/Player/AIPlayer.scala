package GameLogic.Player

import Board.Board.{ShipCell, WaterCell}
import Board.Board
import Config.Config
import Coordinate.Coordinate
import Ship._
import State.{AIPlayerState, UniversalPlayerState}
import Utils.Utils._
import scala.annotation.tailrec
import scala.util.Random.nextInt


object AIPlayer extends Player {

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

  def attack(aiPlayerState: AIPlayerState)(implicit config: Config): (AIPlayerState, Boolean) = {

    aiPlayerState.attackCoordinates match {
      case Nil =>
        val waterOrShipCells = aiPlayerState.enemyPlayerBoard.grid.flatMap(_.filter(x => x.cellTypeInBoard == WaterCell || x.cellTypeInBoard == ShipCell))
        val randomWaterOrShipCellCoordinate = waterOrShipCells(nextInt(waterOrShipCells.length)).coordinates
        val (newAiUniversalPlayerState, isHitOrWreck) = super.attack(randomWaterOrShipCellCoordinate, UniversalPlayerState(aiPlayerState))
        if(isHitOrWreck)
          (AIPlayerState(newAiUniversalPlayerState, getNeighboursOfCoordinate(randomWaterOrShipCellCoordinate, aiPlayerState.enemyPlayerBoard)), isHitOrWreck)
        else
          (AIPlayerState(newAiUniversalPlayerState, Seq()), isHitOrWreck)
      case attackCoordinates @ _::_ =>
        val randomNeighbour = attackCoordinates(nextInt(attackCoordinates.length))
        val newAttackCoordinates = attackCoordinates.filterNot(_ == randomNeighbour)
        val (newAiUniversalPlayerState, isHitOrWreck) = super.attack(randomNeighbour, UniversalPlayerState(aiPlayerState))
        if(isHitOrWreck) {
          val newNeighbours = (newAttackCoordinates ++ getNeighboursOfCoordinate(randomNeighbour, aiPlayerState.enemyPlayerBoard)).distinct
          (AIPlayerState(newAiUniversalPlayerState, newNeighbours), isHitOrWreck)
        } else
          (AIPlayerState(newAiUniversalPlayerState, newAttackCoordinates), isHitOrWreck)
    }
  }

  private def getNeighboursOfCoordinate(coordinate: Coordinate, board: Board)(implicit config: Config): Seq[Coordinate] = {
    val north = if(coordinate.y > 1) Coordinate(coordinate.x, coordinate.y - 1) else Coordinate(-1, -1)
    val east = if(coordinate.x < config.xLengthOfCoreBoard - 1) Coordinate(coordinate.x + 1, coordinate.y) else Coordinate(-1, -1)
    val south = if(coordinate.y < config.yLengthOfCoreBoard - 1) Coordinate(coordinate.x, coordinate.y + 1) else Coordinate(-1, -1)
    val west = if(coordinate.x > 1) Coordinate(coordinate.x - 1, coordinate.y) else Coordinate(-1, -1)
    val grid = board.grid

    Seq(north, east, south, west).filterNot(_ == Coordinate(-1, -1)).filter { x =>
      val cellType = board.findCellByCoordinate(x).get.cellTypeInBoard
      cellType == WaterCell || cellType == ShipCell
    }
  }
}
