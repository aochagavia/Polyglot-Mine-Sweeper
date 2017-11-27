package aochagavia

import scala.annotation.tailrec

trait Board {
  val mines: Int
  val width: Int
  def height: Int

  // An iterable through all cells in the board
  protected def cells: Iterable[Cell]

  // A getter for Cells based on their position. Throws an exception when the position is invalid
  protected def getCell(pos: Position): Cell

  // True only after the mines have been spawned
  // Fixme: it feels wrong to have these two things here...
  protected def minesSpawned: Boolean
  protected def setMinesSpawned(): Board

  // Updates the cell in the given position using the updater function. Throws an exception when the position is invalid
  protected def updateCell(pos: Position, update: Cell => Cell): Board

  // Methods for free!

  // Places or removes a mark in the given position
  final def toggleMark(pos: Position): Board =
    if (Position.isValid(pos, width, height)) updateCell(pos, c => c.copy(marked = !c.marked))
    else this

  // Reveals the cells at the given position and its surroundings
  final def reveal(pos: Position): Board = {
    if (!Position.isValid(pos, width, height))
      this
    else {
      // Spawn mines the first time a reveal happens, so the player never loses the first turn
      val board =
        if (minesSpawned) this
        else spawnMines(pos)

      // Show this and surrounding cells
      board.showCells(List(pos))
    }
  }

  final def outcome(): Outcome = {
    // Lose condition: a mine is shown
    val defeat = cells.filter(!_.hidden).exists(_.kind match {
      case Mine => true
      case _ => false
    })

    // Win condition: everything is shown besides the mines
    val victory = cells.count(_.hidden) == mines

    if (defeat) Defeat
    else if (victory) Victory
    else Ongoing
  }

  final def display(): String = {
    val buf = new StringBuilder

    // First line displays column numbers
    buf.append("     ")
    for (x <- 0 until width) {
      val padding = if (x < 9) { "  " } else { " " }
      buf.append(padding)
      buf.append(x + 1)
    }

    // Horizontal line to separate column numbers from the rest of the board
    buf.append("\n     ")
    buf.append("_" * 3 * width)
    buf.append("\n")

    // Following lines begin with their row number
    for (y <- 0 until height) {
      val padding = if (y < 9) { "  " } else { " " }
      buf.append(padding)
      buf.append(y + 1)
      buf.append(" |")

      // Cells
      for (x <- 0 until width) {
        val cell = getCell(Position(x, y))
        buf.append(Cell.display(cell))
      }

      buf.append("\n\n")
    }

    buf.toString
  }

  // Private stuff

  @tailrec private final def showCells(positions: List[Position]): Board = {
    positions match {
      case Nil => this
      case pos :: positionsLeft =>
        val c = getCell(pos)
        val newPositions = c.kind match {
          case Empty(0) =>
            val surrounding = Position.surroundings(pos, width, height).filter(p => {
              val c = getCell(p)
              c.hidden && c.kind != Mine
            })
            surrounding.toList ::: positionsLeft
          case _ => positionsLeft
        }

        val newBoard = updateCell(pos, _.copy(hidden = false))
        newBoard.showCells(newPositions)
    }
  }

  private def spawnMines(protectedPos: Position): Board = {
    // Spawn the mines
    val minePositions = Position.randoms(width, height).distinct.filter(_ != protectedPos).take(mines)
    val minedCell = Cell(hidden = true, marked = false, kind = Mine)
    val minedBoard = minePositions.foldLeft(this)((board, pos) => board.updateCell(pos, _ => minedCell))

    // Store surrounding mine counts in empty cells
    val initBoard = minePositions.flatMap(Position.surroundings(_, width, height)).foldLeft(minedBoard)((board, pos) => {
      board.updateCell(pos, c => c.kind match {
        case Empty(x) => c.copy(kind = Empty(x + 1))
        case _ => c
      })
    })

    initBoard.setMinesSpawned()
  }
}
