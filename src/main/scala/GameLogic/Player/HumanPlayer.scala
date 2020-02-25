package GameLogic.Player

import Config.Config
import Coordinate.Coordinate
import State.PlayerState


object HumanPlayer extends Player {

  override def attack(attackCoordinate: Coordinate, playerState: PlayerState)(implicit config: Config): (PlayerState, Boolean) =
    super.attack(attackCoordinate, playerState)

}
