package GameLogic.Player

import Config.Config
import Coordinate.Coordinate
import State.UniversalPlayerState


object HumanPlayer extends Player {

  override def attack(attackCoordinate: Coordinate, playerState: UniversalPlayerState)(implicit config: Config): (UniversalPlayerState, Boolean) =
    super.attack(attackCoordinate, playerState)

}
