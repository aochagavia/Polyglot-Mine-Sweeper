package aochagavia

object Util {
  def updateMap[A](map: Map[Position, A], pos: Position, update: A => A): Map[Position, A] =
    map + (pos -> update(map(pos)))
}
