package UtilsTest

import Config.Config
import Coordinate.Coordinate
import Exceptions.NotValidUserInputForShipPlacement
import Ship.ShipPosition
import Ship.ShipPosition.South
import Utils.Utils.parseUserInputToShipPosition
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scala.util.{Failure, Success}


class ParseUserInputToShipPositionTest extends AnyFlatSpec with Matchers {

  implicit val config = Config(" ", 6, 6, 4)

  "parseUserInputToShipPosition" should "return Failure(NotValidUserInputForShipPlacement) for too few arguments" in {
    parseUserInputToShipPosition("4 3", 4) shouldBe Failure(NotValidUserInputForShipPlacement)
    parseUserInputToShipPosition("4", 4) shouldBe Failure(NotValidUserInputForShipPlacement)
    parseUserInputToShipPosition("", 4) shouldBe Failure(NotValidUserInputForShipPlacement)
    parseUserInputToShipPosition(" ", 4) shouldBe Failure(NotValidUserInputForShipPlacement)
  }

  "parseUserInputToShipPosition" should "return Failure(NotValidUserInputForShipPlacement) for too many arguments" in {
    parseUserInputToShipPosition("4 3 north 6", 4) shouldBe Failure(NotValidUserInputForShipPlacement)
  }

  "parseUserInputToShipPosition" should "return Failure(NotValidUserInputForShipPlacement) for improper direction" in {
    parseUserInputToShipPosition("4 3 nort", 4) shouldBe Failure(NotValidUserInputForShipPlacement)
    parseUserInputToShipPosition("4 3 5", 4) shouldBe Failure(NotValidUserInputForShipPlacement)
  }

  "parseUserInputToShipPosition" should "return Failure(NotValidUserInputForShipPlacement) for coordinate which isn't in map" in {
    parseUserInputToShipPosition("-1 3 north", 4) shouldBe Failure(NotValidUserInputForShipPlacement)
    parseUserInputToShipPosition("4 -3 north", 4) shouldBe Failure(NotValidUserInputForShipPlacement)
    parseUserInputToShipPosition("6 3 north", 4) shouldBe Failure(NotValidUserInputForShipPlacement)
    parseUserInputToShipPosition("4 7 north", 4) shouldBe Failure(NotValidUserInputForShipPlacement)
  }

  "parseUserInputToShipPosition" should "return Failure(NotValidUserInputForShipPlacement) for coordinate which isn't integer" in {
    parseUserInputToShipPosition("1.5 3 north", 4) shouldBe Failure(NotValidUserInputForShipPlacement)
    parseUserInputToShipPosition("4 3.76 north", 4) shouldBe Failure(NotValidUserInputForShipPlacement)
  }

  "parseUserInputToShipPosition" should "return Failure(NotValidUserInputForShipPlacement) when ship is supposed to be placed off the map" in {
    parseUserInputToShipPosition("1 2 north", 4) shouldBe Failure(NotValidUserInputForShipPlacement)
    parseUserInputToShipPosition("5 2 east", 2) shouldBe Failure(NotValidUserInputForShipPlacement)
    parseUserInputToShipPosition("5 4 south", 4) shouldBe Failure(NotValidUserInputForShipPlacement)
    parseUserInputToShipPosition("1 2 west", 3) shouldBe Failure(NotValidUserInputForShipPlacement)
  }

  "parseUserInputToShipPosition" should "return Failure(NotValidUserInputForShipPlacement) when data is provided in wrong form" in {
    parseUserInputToShipPosition("4,3 south", 3) shouldBe Failure(NotValidUserInputForShipPlacement)
  }

  "parseUserInputToShipPosition" should "return Success(Ship) for proper input data" in {
    parseUserInputToShipPosition("4 3 south", 3) shouldBe Success(ShipPosition(Coordinate(4 + 1, 3 + 1), 3, South))
  }

}
