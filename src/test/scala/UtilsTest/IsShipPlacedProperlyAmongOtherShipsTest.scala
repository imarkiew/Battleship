package UtilsTest

import Utils.Utils.isShipPlacedProperlyAmongOtherShips
import Coordinate.Coordinate
import Ship.{Ship, ShipPosition}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers


class IsShipPlacedProperlyAmongOtherShipsTest extends AnyFlatSpec with Matchers {

  val placedShips = Seq(
    Ship(ShipPosition(Coordinate(2, 3), 2, ShipPosition.North)),
    Ship(ShipPosition(Coordinate(3, 4), 3, ShipPosition.East)),
  )

  "isShipPlacedProperlyAmongOtherShips" should "return false for improperly placed ship" in {
    isShipPlacedProperlyAmongOtherShips(Ship(ShipPosition(Coordinate(2, 3), 2, ShipPosition.North)), placedShips) shouldBe false
    isShipPlacedProperlyAmongOtherShips(Ship(ShipPosition(Coordinate(3, 2), 3, ShipPosition.West)), placedShips) shouldBe false
    isShipPlacedProperlyAmongOtherShips(Ship(ShipPosition(Coordinate(3, 1), 4, ShipPosition.South)), placedShips) shouldBe false
  }

  "isShipPlacedProperlyAmongOtherShips" should "return true for properly placed ship" in {
    isShipPlacedProperlyAmongOtherShips(Ship(ShipPosition(Coordinate(2, 1), 2, ShipPosition.North)), placedShips) shouldBe true
  }

}
