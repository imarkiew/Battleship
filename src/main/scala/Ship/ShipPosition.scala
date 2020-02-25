package Ship

import Coordinate.Coordinate
import ShipPosition.Direction

case class ShipPosition(startingPoint: Coordinate, length: Int, direction: Direction)

object ShipPosition {

  sealed trait Direction {val DIRECTION: String}
  case object North extends Direction {val DIRECTION = "north"}
  case object West extends Direction {val DIRECTION = "west"}
  case object South extends Direction {val DIRECTION = "south"}
  case object East extends Direction {val DIRECTION = "east"}

  val directions: Seq[Direction] = Seq(North, West, South, East)

  def isProperDirection(direction: String): Boolean = directions.exists(_.DIRECTION == direction)
  def buildDirectionFromString(direction: String): Option[Direction] = directions.find(_.DIRECTION == direction)

}
