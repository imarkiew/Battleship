package UtilsTest

import Config.Config
import Coordinate.Coordinate
import Exceptions.NotValidUserInputForAttackCoordinate
import Utils.Utils.parseUserInputToAttackCoordinate
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scala.util.{Failure, Success}


class ParseUserInputToAttackCoordinateTest extends AnyFlatSpec with Matchers {

  implicit val config = Config(" ", 6, 6, 4)

  "parseUserInputToAttackCoordinate" should "return Failure(NotValidUserInputForAttackCoordinate) for too few arguments" in {
    parseUserInputToAttackCoordinate("4") shouldBe Failure(NotValidUserInputForAttackCoordinate)
    parseUserInputToAttackCoordinate("4 ") shouldBe Failure(NotValidUserInputForAttackCoordinate)
    parseUserInputToAttackCoordinate("") shouldBe Failure(NotValidUserInputForAttackCoordinate)
    parseUserInputToAttackCoordinate(" ") shouldBe Failure(NotValidUserInputForAttackCoordinate)
  }

  "parseUserInputToAttackCoordinate" should "return Failure(NotValidUserInputForAttackCoordinate) for too many arguments" in {
    parseUserInputToAttackCoordinate("4 2 x") shouldBe Failure(NotValidUserInputForAttackCoordinate)
  }

  "parseUserInputToShipPosition" should "return Failure(NotValidUserInputForAttackCoordinate) for coordinate which isn't in map" in {
    parseUserInputToAttackCoordinate("-1 3") shouldBe Failure(NotValidUserInputForAttackCoordinate)
    parseUserInputToAttackCoordinate("4 -3") shouldBe Failure(NotValidUserInputForAttackCoordinate)
    parseUserInputToAttackCoordinate("6 3") shouldBe Failure(NotValidUserInputForAttackCoordinate)
    parseUserInputToAttackCoordinate("4 7") shouldBe Failure(NotValidUserInputForAttackCoordinate)
  }

  "parseUserInputToShipPosition" should "return Failure(NotValidUserInputForAttackCoordinate) for coordinate which isn't integer" in {
    parseUserInputToAttackCoordinate("1.5 3") shouldBe Failure(NotValidUserInputForAttackCoordinate)
    parseUserInputToAttackCoordinate("4 3.76") shouldBe Failure(NotValidUserInputForAttackCoordinate)
  }

  "parseUserInputToShipPosition" should "return Failure(NotValidUserInputForAttackCoordinate) when data is provided in wrong form" in {
    parseUserInputToAttackCoordinate("3,2") shouldBe Failure(NotValidUserInputForAttackCoordinate)
  }

  "parseUserInputToShipPosition" should "Success(Coordinate) for proper input data" in {
    parseUserInputToAttackCoordinate("3 2") shouldBe Success(Coordinate(3 + 1, 2 + 1))
  }

}
