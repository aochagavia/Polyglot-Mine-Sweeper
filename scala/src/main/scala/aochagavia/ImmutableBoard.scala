package aochagavia

object ImmutableBoard {
  def create(width: Int, height: Int): ImmutableBoard = {
    val emptyCell = Cell(hidden = true, marked = false, kind = Empty(0))
    val cells = for {
      x <- 0 until width
      y <- 0 until height
    } yield (Position(x, y), emptyCell)

    new ImmutableBoard(Map(cells: _*), width, mines = 10, minesSpawned = false)
  }
}

case class ImmutableBoard(cellMap: Map[Position, Cell],
                          width: Int,
                          mines: Int,
                          minesSpawned: Boolean) extends Board {
  require(mines < width * height)

  def cells: Iterable[Cell] = cellMap.values
  def height: Int = cellMap.size / width
  protected def getCell(pos: Position): Cell = cellMap(pos)

  protected def updateCell(pos: Position, update: Cell => Cell): ImmutableBoard = {
    assert(Position.isValid(pos, width, height))
    copy(cellMap = Util.updateMap(cellMap, pos, update))
  }

  protected def setMinesSpawned(): ImmutableBoard = copy(minesSpawned = true)
}
