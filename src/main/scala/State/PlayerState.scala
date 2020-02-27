package State

import Board.Board
import Ship.Ship

trait PlayerState {
  def enemyPlayerBoard: Board
  def enemyPlayerShips: Seq[Ship]
  def numberOfShipsDestroyedByPlayer: Int
}
