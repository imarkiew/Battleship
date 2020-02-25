package Utils

import Config.Config
import Coordinate.Coordinate
import Exceptions.{NotValidUserInputForAttackCoordinate, NotValidUserInputForShipPlacement}
import Ship.{Ship, ShipPosition}
import _root_.Ship.ShipPosition._

import scala.annotation.tailrec
import scala.io.StdIn
import scala.util.{Failure, Success, Try}


object Utils {

  def parseUserInputToShipPosition(input: String, shipLength: Int)(implicit config: Config): Try[ShipPosition] = {
    input.split(config.userInputSeparator) match {
      case Array(xStart, yStart, direction) if isProperDirection(direction) =>
        val xStartInt = Try(xStart.toInt + 1).getOrElse(-1)
        val yStartInt = Try(yStart.toInt + 1).getOrElse(-1)
        val directionInstance = buildDirectionFromString(direction).get
        if(areBoundariesForShipMet(xStartInt, yStartInt, shipLength, directionInstance))
          Success(ShipPosition(Coordinate(xStartInt, yStartInt), shipLength, directionInstance))
        else
          Failure(NotValidUserInputForShipPlacement)
      case _ => Failure(NotValidUserInputForShipPlacement)
    }
  }

  def parseUserInputToAttackCoordinate(input: String)(implicit config: Config): Try[Coordinate] = {
    input.split(config.userInputSeparator) match {
      case Array(x, y) =>
        val xAttack = Try(x.toInt + 1)
        val yAttack = Try(y.toInt + 1)
        if(xAttack.isSuccess && yAttack.isSuccess){
          val attackCoordinate = Coordinate(xAttack.get, yAttack.get)
          if(isCoordinateInsideCoreBoard(attackCoordinate))
            Success(attackCoordinate)
          else
            Failure(NotValidUserInputForAttackCoordinate)
        } else {
          Failure(NotValidUserInputForAttackCoordinate)
        }
      case _ => Failure(NotValidUserInputForAttackCoordinate)
    }
  }

  def isCoordinateInsideCoreBoard(coordinate: Coordinate)(implicit config: Config): Boolean = {
    if (coordinate.x > 0 && coordinate.x <= config.xLengthOfCoreBoard &&
      coordinate.y > 0 && coordinate.y <= config.yLengthOfCoreBoard)
      true
    else
      false
  }

  def areBoundariesForShipMet(xStart: Int, yStart: Int, shipLength: Int, direction: Direction)(implicit config: Config): Boolean = {
    val startingPointPredicate = isCoordinateInsideCoreBoard(Coordinate(xStart, yStart))

    if (startingPointPredicate) {
      if (direction == North) {
        yStart - shipLength + 1 > 0
      } else if (direction == West) {
        xStart - shipLength + 1 > 0
      } else if (direction == South) {
        yStart + shipLength - 1 <= config.yLengthOfCoreBoard
      } else {
        xStart + shipLength - 1 <= config.xLengthOfCoreBoard
      }
    } else false
  }

  def isShipPlacedProperlyAmongOtherShips(ship: Ship, placedShips: Seq[Ship]): Boolean = {
    val coordinates = ship.shipCoordinates.keySet
    placedShips.find(_.shipCoordinates.keySet.intersect(coordinates).nonEmpty) match {
      case Some(_: Ship) => false
      case _ => true
    }
  }

  def getUserInputWhileIsImproperForAttackCoordinate(implicit config: Config): Coordinate = {

    val userInputParser = parseUserInputToAttackCoordinate(_: String)

    println("Type in coordinate for attack - \"x y\"")

    @tailrec
    def getUserInputWhileIsImproperForAttackCoordinateHelper: Coordinate = {

      userInputParser(StdIn.readLine) match {
        case Success(attackCoordinate: Coordinate) => attackCoordinate
        case Failure(exception: NotValidUserInputForAttackCoordinate.type) =>
          println(exception.message + "\n")
          getUserInputWhileIsImproperForAttackCoordinateHelper
        case _ =>
          println("Unknown exception ! Try again \n")
          getUserInputWhileIsImproperForAttackCoordinateHelper
      }

    }

    getUserInputWhileIsImproperForAttackCoordinateHelper
  }

  def getUserInputWhileIsImproperForPlacingAShip(ships: Seq[Ship], currentShipSize: Int)(implicit config: Config): Seq[Ship] = {

    println(s"""Put $currentShipSize fields ship on your board - type in \"xStartCoordinate yStartCoordinate direction\" \n""")

    val userInputParser = parseUserInputToShipPosition(_: String, _: Int)

    @tailrec
    def getUserInputWhileIsImproperForPlacingShipsHelper: Seq[Ship] = {
      userInputParser(StdIn.readLine, currentShipSize) match {
        case Success(shipPosition: ShipPosition) =>
          val ship = Ship(shipPosition)
          if(isShipPlacedProperlyAmongOtherShips(ship, ships)) {
            ships :+ ship
          } else {
            println("Ship.Ship is not properly placed among other ships ! Try again \n")
            getUserInputWhileIsImproperForPlacingShipsHelper
          }
        case Failure(exception: NotValidUserInputForShipPlacement.type) =>
          println(exception.message + "\n")
          getUserInputWhileIsImproperForPlacingShipsHelper
        case _ =>
          println("Unknown exception ! Try again \n")
          getUserInputWhileIsImproperForPlacingShipsHelper
      }
    }

    getUserInputWhileIsImproperForPlacingShipsHelper
  }

}
