package State

import Board.Board
import Ship.Ship

case class PlayerState(enemyPlayerBoard: Board, enemyPlayerShips: Seq[Ship], numberOfShipsDestroyedByPlayer: Int)
