package Ship

import Coordinate.Coordinate
import ShipPosition.{North, South, West}


case class Ship(shipCoordinates: Map[Coordinate, Boolean]) {

  def isSunk: Boolean = shipCoordinates.forall(_._2)
  def isShot(coordinates: Coordinate): Boolean = shipCoordinates.get(coordinates) match {
    case Some(wasShot) if !wasShot => true
    case _ => false
  }

}

object Ship {

  def apply(position: ShipPosition): Ship = _root_.Ship.Ship(getAllCellPositionsForShip(position).getOrElse(Map()))

  private def getAllCellPositionsForShip(shipPosition: ShipPosition): Option[Map[Coordinate, Boolean]] = {
    shipPosition match {
      case ShipPosition(Coordinate(xStart, yStart), length, direction) =>
        if (direction == North) {
          val yPositions = (yStart - length + 1).to(yStart)
          Some(makeShipCoordinates(Seq(xStart), yPositions))
        } else if (direction == West) {
          val xPositions = (xStart - length + 1).to(xStart)
          Some(makeShipCoordinates(xPositions, Seq(yStart)))
        } else if (direction == South) {
          val yPositions =  yStart to (yStart + length - 1)
          Some(makeShipCoordinates(Seq(xStart), yPositions))
        } else {
          val xPositions = xStart.to(xStart + length - 1)
          Some(makeShipCoordinates(xPositions, Seq(yStart)))
        }

      case _ => None
    }
  }

  private def makeShipCoordinates(xPositions: Seq[Int], yPositions: Seq[Int]): Map[Coordinate, Boolean] =
    xPositions
      .flatMap(x => yPositions.map(Coordinate(x, _)))
      .map((_, false))
      .toMap
}
