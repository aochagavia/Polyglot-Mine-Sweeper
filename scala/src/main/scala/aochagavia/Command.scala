package aochagavia

import scala.util.Try

sealed trait Command {
  def run(board: Board): Board
}

case class Reveal(pos: Position) extends Command {
  def run(board: Board): Board = board.reveal(pos)
}

case class ToggleMark(pos: Position) extends Command {
  def run(board: Board): Board = board.toggleMark(pos)
}

object Command {
  def parse(cmd: String): Option[Command] = {
    val parts = cmd.split(" ")
    if (parts.length < 3) {
      return None
    }

    val targetPos = for {
      x <- Try(parts(1).toInt).map(_ - 1)
      y <- Try(parts(2).toInt).map(_ - 1)
    } yield Position(x, y)

    targetPos.toOption.flatMap(pos => {
      parts(0) match {
        case "m" => Some(ToggleMark(pos))
        case "r" => Some(Reveal(pos))
        case _ => None
      }
    })
  }
}
