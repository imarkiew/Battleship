package BoardTest

import Board.{Board, Cell}
import Config.Config
import Coordinate.Coordinate
import Ship.{Ship, ShipPosition}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers


class CreateBoard extends AnyFlatSpec with Matchers {

  implicit val config = Config(" ", 2, 2, 2)
  val ships = Seq(Ship(ShipPosition(Coordinate(2, 1), 2, ShipPosition.South)), Ship(ShipPosition(Coordinate(1, 2), 1, ShipPosition.South)))
  val changes = Map(Coordinate(1, 1) -> Board.MissCell, Coordinate(2, 1) -> Board.HitCell, Coordinate(1, 2) -> Board.WreckCell)

  val humanBoard = Board(Seq(
    Seq(Cell(Coordinate(0, 0), Board.EmptyCell), Cell(Coordinate(1, 0), Board.XIdCell(0)), Cell(Coordinate(2, 0), Board.XIdCell(1))),
    Seq(Cell(Coordinate(0, 1), Board.YIdCell(0)), Cell(Coordinate(1, 1), Board.WaterCell), Cell(Coordinate(2, 1), Board.ShipCell)),
    Seq(Cell(Coordinate(0, 2), Board.YIdCell(1)), Cell(Coordinate(1, 2), Board.ShipCell), Cell(Coordinate(2, 2), Board.ShipCell))
  ), false)

  val changedHumanBoard = Board(Seq(
    Seq(Cell(Coordinate(0, 0), Board.EmptyCell), Cell(Coordinate(1, 0), Board.XIdCell(0)), Cell(Coordinate(2, 0), Board.XIdCell(1))),
    Seq(Cell(Coordinate(0, 1), Board.YIdCell(0)), Cell(Coordinate(1, 1), Board.MissCell), Cell(Coordinate(2, 1), Board.HitCell)),
    Seq(Cell(Coordinate(0, 2), Board.YIdCell(1)), Cell(Coordinate(1, 2), Board.WreckCell), Cell(Coordinate(2, 2), Board.ShipCell))
  ), false)

  val aiBoard = Board(Seq(
    Seq(Cell(Coordinate(0, 0), Board.EmptyCell), Cell(Coordinate(1, 0), Board.XIdCell(0)), Cell(Coordinate(2, 0), Board.XIdCell(1))),
    Seq(Cell(Coordinate(0, 1), Board.YIdCell(0)), Cell(Coordinate(1, 1), Board.WaterCell), Cell(Coordinate(2, 1), Board.WaterCell)),
    Seq(Cell(Coordinate(0, 2), Board.YIdCell(1)), Cell(Coordinate(1, 2), Board.WaterCell), Cell(Coordinate(2, 2), Board.WaterCell))
  ), true)

  val changedAiBoard = Board(Seq(
    Seq(Cell(Coordinate(0, 0), Board.EmptyCell), Cell(Coordinate(1, 0), Board.XIdCell(0)), Cell(Coordinate(2, 0), Board.XIdCell(1))),
    Seq(Cell(Coordinate(0, 1), Board.YIdCell(0)), Cell(Coordinate(1, 1), Board.MissCell), Cell(Coordinate(2, 1), Board.HitCell)),
    Seq(Cell(Coordinate(0, 2), Board.YIdCell(1)), Cell(Coordinate(1, 2), Board.WreckCell), Cell(Coordinate(2, 2), Board.WaterCell))
  ), true)

  "Board" should "be created properly from Seq[Ship] for human" in {

    Board(ships, false) shouldBe humanBoard

  }

  "Board" should "be created properly from old board and changed cells for human" in {

    Board(humanBoard, changes) shouldBe changedHumanBoard

  }

  "Board" should "be created properly from Seq[Ship] for AI" in {

    Board(ships, true) shouldBe aiBoard

  }

  "Board" should "be created properly from old board and changed cells for AI" in {

    Board(aiBoard, changes) shouldBe changedAiBoard

  }

}
