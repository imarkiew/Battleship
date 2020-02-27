package State

import Board.Board
import Coordinate.Coordinate
import Ship.Ship

case class AIPlayerState(enemyPlayerBoard: Board, enemyPlayerShips: Seq[Ship], attackCoordinates: Seq[Coordinate], numberOfShipsDestroyedByPlayer: Int)
  extends PlayerState

object AIPlayerState {

  def apply(universalAiPlayerState: UniversalPlayerState, attackPoints: Seq[Coordinate]): AIPlayerState =
    AIPlayerState(universalAiPlayerState.enemyPlayerBoard, universalAiPlayerState.enemyPlayerShips, attackPoints, universalAiPlayerState.numberOfShipsDestroyedByPlayer)

  def apply(aIPlayerState: AIPlayerState, attackPoints: Seq[Coordinate]): AIPlayerState = {
    AIPlayerState(aIPlayerState.enemyPlayerBoard, aIPlayerState.enemyPlayerShips, attackPoints, aIPlayerState.numberOfShipsDestroyedByPlayer)
  }

}
