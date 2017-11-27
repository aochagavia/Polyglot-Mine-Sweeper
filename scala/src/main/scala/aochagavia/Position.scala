package aochagavia

import scala.util.Random

object Position {
  def fromIndex(i: Int, width: Int): Position = Position(i % width, i / width)
  def toIndex(pos: Position, width: Int): Int = pos.x + pos.y * width
  def isValid(pos: Position, width: Int, height: Int): Boolean =
    0 <= pos.x && pos.x < width && 0 <= pos.y && pos.y < height

  // An infinite stream of random positions
  def randoms(width: Int, height: Int): Stream[Position] =
    Stream.continually(Random.nextInt(width * height)).map(Position.fromIndex(_, width))

  // A stream of cells surrounding the given center
  def surroundings(center: Position, width: Int, height: Int): Stream[Position] = {
    val shiftUpLeft = new Position(-1, -1)
    (0 until 9).toStream
      .map(i => Position.fromIndex(i, 3) + shiftUpLeft + center)
      .filter(_ != center) // Skip current position
      .filter(isValid(_, width, height))
  }
}

case class Position(x: Int, y: Int) {
  def +(that: Position): Position = Position(x + that.x, y + that.y)
}
