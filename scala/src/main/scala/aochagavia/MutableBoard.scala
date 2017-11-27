package aochagavia

object MutableBoard {
  def create(width: Int, height: Int): MutableBoard = {
    val emptyCell = Cell(hidden = true, marked = false, kind = Empty(0))
    val cells = Array.fill(width * height)(emptyCell)
    new MutableBoard(cells, width, mines = 10)
  }
}

class MutableBoard(private var cellArr: Array[Cell],
                   val width: Int,
                   val mines: Int) extends Board {
  require(mines < width * height)

  protected var minesSpawned: Boolean = false
  protected def cells: Iterable[Cell] = cellArr
  def height: Int = cellArr.length / width

  def getCell(pos: Position): Cell = {
    assert(Position.isValid(pos, this.width, this.height))
    this.cellArr(Position.toIndex(pos, this.width))
  }

  protected def updateCell(pos: Position, update: Cell => Cell): MutableBoard = {
    assert(Position.isValid(pos, width, height))
    val i = Position.toIndex(pos, width)
    cellArr(i) = update(cellArr(i))
    this
  }

  protected def setMinesSpawned(): MutableBoard = {
    minesSpawned = true
    this
  }
}
