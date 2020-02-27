package State

import Board.Board
import Ship.Ship

case class UniversalPlayerState(enemyPlayerBoard: Board, enemyPlayerShips: Seq[Ship], numberOfShipsDestroyedByPlayer: Int) extends PlayerState

object UniversalPlayerState {

  def apply(aIPlayerState: AIPlayerState): UniversalPlayerState =
    UniversalPlayerState(aIPlayerState.enemyPlayerBoard, aIPlayerState.enemyPlayerShips, aIPlayerState.numberOfShipsDestroyedByPlayer)
}