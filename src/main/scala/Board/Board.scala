package Board

import Board._
import Config.Config
import Coordinate.Coordinate
import Ship.Ship


case class Board(grid: gridType, isAiBoard: Boolean)(implicit config: Config) {

  def drawBoard: Unit = {
    grid.foreach { row =>
      val rowToString = row.map {
        case Cell(_, WaterCell) => "\u001b[1;34m W \u001b[0m"
        case Cell(_, ShipCell) => "\u001b[1;32m S \u001b[0m"
        case Cell(_, MissCell) => "\u001b[1;37m M \u001b[0m"
        case Cell(_, HitCell) => "\u001b[1;41m H \u001b[0m"
        case Cell(_, WreckCell) => "\u001b[1;30m X \u001b[0m"
        case Cell(_, EmptyCell) => " "
        case Cell(_, XIdCell(x)) => s" $x "
        case Cell(_, YIdCell(y)) => s"$y"
        case _ => ""
      }.mkString("")

      println(rowToString)
    }
  }
}


object Board {

  sealed trait CellTypeInBoard
  case object WaterCell extends CellTypeInBoard
  case object ShipCell extends CellTypeInBoard
  case object MissCell extends CellTypeInBoard
  case object HitCell extends CellTypeInBoard
  case object WreckCell extends CellTypeInBoard
  case object EmptyCell extends CellTypeInBoard
  case class XIdCell(value: Int) extends CellTypeInBoard
  case class YIdCell(value: Int) extends CellTypeInBoard

  type gridType = Seq[Seq[Cell]]

  def apply(ships: Seq[Ship], isAiBoard: Boolean)(implicit config: Config, d: DummyImplicit): Board = {

    val grid = (0 to config.yLengthOfCoreBoard).foldLeft(Seq[Seq[Cell]]()) {
         (row, rowIndex) => row :+ (0 to config.xLengthOfCoreBoard).foldLeft(Seq[Cell]()){
           (column, columnIndex) =>

            val coordinate = Coordinate(columnIndex, rowIndex)

            if(rowIndex == 0 && columnIndex == 0)
              column :+ Cell(coordinate, EmptyCell)
            else if(rowIndex == 0 && columnIndex > 0 && columnIndex <= config.xLengthOfCoreBoard)
              column :+ Cell(coordinate, XIdCell(columnIndex - 1))
            else if(rowIndex > 0 && columnIndex == 0){
              column :+ Cell(coordinate, YIdCell(rowIndex - 1))}
            else if(isAiBoard)
              column :+ Cell(coordinate, WaterCell)
            else {
              ships.find(_.shipCoordinates.keySet.exists(_ == Coordinate(columnIndex, rowIndex))) match {
                case Some(_: Ship) => column :+ Cell(coordinate, ShipCell)
                case _ => column :+ Cell(coordinate, WaterCell)
              }
            }
        }
      }

    Board(grid, isAiBoard)
  }

  def apply(board: Board, changes: Map[Coordinate, CellTypeInBoard])(implicit config: Config): Board = {
    val newGrid = board.grid.foldLeft(Seq[Seq[Cell]]()) {
      (rowAcc, row) => rowAcc :+ row.foldLeft(Seq[Cell]()) {
        (columnAcc, cell) =>
          changes.getOrElse(cell.coordinates, false) match {
            case cellTypeInBoard: CellTypeInBoard => columnAcc :+ _root_.Board.Cell(cell.coordinates, cellTypeInBoard)
            case _ => columnAcc :+ _root_.Board.Cell(cell.coordinates, cell.cellTypeInBoard)
          }
      }
    }

    Board(newGrid, board.isAiBoard)
  }

}
