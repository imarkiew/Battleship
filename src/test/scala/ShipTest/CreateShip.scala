package ShipTest

import Coordinate.Coordinate
import Ship.{Ship, ShipPosition}
import _root_.Ship.ShipPosition.{North, East, South, West}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers


class CreateShip extends AnyFlatSpec with Matchers {

  "Ship" should "be created properly from ShipPosition in north direction" in {
    val shipPosition = ShipPosition(Coordinate(2, 3), 2, North)
    val ship = Ship(Map(Coordinate(2, 3) -> false, Coordinate(2, 2) -> false))
    Ship(shipPosition) shouldBe ship
  }

  "Ship" should "be created properly from ShipPosition in east direction" in {
    val shipPosition = ShipPosition(Coordinate(2, 3), 2, East)
    val ship = Ship(Map(Coordinate(2, 3) -> false, Coordinate(3, 3) -> false))
    Ship(shipPosition) shouldBe ship
  }

  "Ship" should "be created properly from ShipPosition in south direction" in {
    val shipPosition = ShipPosition(Coordinate(2, 3), 2, South)
    val ship = Ship(Map(Coordinate(2, 3) -> false, Coordinate(2, 4) -> false))
    Ship(shipPosition) shouldBe ship
  }

  "Ship" should "be created properly from ShipPosition in west direction" in {
    val shipPosition = ShipPosition(Coordinate(2, 3), 2, West)
    val ship = Ship(Map(Coordinate(2, 3) -> false, Coordinate(1, 3) -> false))
    Ship(shipPosition) shouldBe ship
  }

}
