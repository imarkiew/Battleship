package GameLogic.Player

import Board.Board._
import Board.Board
import Config.Config
import Coordinate.Coordinate
import Ship.Ship
import State.UniversalPlayerState


class Player {

  def attack(attackCoordinate: Coordinate, playerState: UniversalPlayerState)(implicit config: Config): (UniversalPlayerState, Boolean) = {

    val isAnyShipShot = playerState.enemyPlayerShips.exists(_.isShot(attackCoordinate))

    if(isAnyShipShot){
      val updatedShips = playerState.enemyPlayerShips.map {ship =>
        val coordinates = ship.shipCoordinates.map{
          case (coordinateInShip, wasShot) =>
            if(coordinateInShip == attackCoordinate && !wasShot){
              Coordinate(coordinateInShip.x, coordinateInShip.y) -> true
            } else {
              Coordinate(coordinateInShip.x, coordinateInShip.y) -> wasShot
            }
        }

        Ship(coordinates)
      }

      updatedShips.find(_.shipCoordinates.getOrElse(attackCoordinate, false)) match {
        case Some(ship) if ship.isSunk =>
          val newBoard = Board(playerState.enemyPlayerBoard, ship.shipCoordinates.keySet.map((_, WreckCell)).toMap)
          (UniversalPlayerState(newBoard, updatedShips, playerState.numberOfShipsDestroyedByPlayer + 1), true)
        case _ =>
          val newBoard = Board(playerState.enemyPlayerBoard, Map(attackCoordinate -> HitCell))
          (UniversalPlayerState(newBoard, updatedShips, playerState.numberOfShipsDestroyedByPlayer), true)
      }

    } else if(playerState.enemyPlayerShips.exists(_.shipCoordinates.keySet.exists(_ == attackCoordinate))) {
      (playerState, false)
    } else {
      val newBoard = Board(playerState.enemyPlayerBoard, Map(attackCoordinate -> MissCell))
      (UniversalPlayerState(newBoard, playerState.enemyPlayerShips, playerState.numberOfShipsDestroyedByPlayer), false)
    }

  }

}
