package aochagavia

import scala.annotation.tailrec

object Main {
  def main(args: Array[String]): Unit = {
    println("Welcome to mine sweeper!")
    println()
    println("You may enter the following commands:")
    println("r x y: reveal the cell at coordinates (x, y)")
    println("m x y: toggle a mark on the cell at coordinates (x, y)")
    println()

    play(board = ImmutableBoard.create(10, 10))
  }

  // A single iteration of the game loop
  @tailrec def play(board: Board): Unit = {
    print(board.display())
    print("Enter a command: ")
    System.out.flush()

    val line = scala.io.StdIn.readLine()
    if (line == null) {
      // No more lines from stdin
      return
    }

    val newBoard = Command.parse(line) match {
      case Some(cmd) =>
        cmd.run(board)
      case None =>
        println("Invalid command, try again.")
        board
    }

    newBoard.outcome() match {
      case Victory =>
        print(newBoard.display())
        println("Congratulations, you won!")
      case Defeat =>
        print(newBoard.display())
        println("Too bad, you lost!")
      case Ongoing =>
        play(newBoard)
    }
  }
}
