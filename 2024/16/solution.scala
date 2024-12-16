package day16

import java.nio.file.Files
import java.nio.file.Path
import scala.collection.mutable

@main def main(): Unit = {
  val input = Files.readString(Path.of("input.txt")).trim
  println(part1(input))
}

def part1(input: String): (Int, Int) = {
  val area = parseInput(input)
  dijkstra(area)
}

case class Node(pos: Position, dir: Direction)

def nodes(area: Area): Iterable[Node] = {
  for {
    pos <- area.positionOf('S') ++ area.positionOf('.') ++ area.positionOf('E')
    dir <- Direction.upDownLeftRight
  } yield Node(pos, dir)
}

def neighbours(node: Node): List[(Node, Int)] = {
  List(
    Node(node.pos + node.dir, node.dir) -> 1,
    Node(node.pos, node.dir.rotateLeft) -> 1000,
    Node(node.pos, node.dir.rotateRight) -> 1000
  )
}

def dijkstra(area: Area): (Int, Int) = {
  val source = Node(area.positionOf('S').head, Direction.right)
  val target = area.positionOf('E').head

  val prev = mutable.Map[Node, List[Node]]()
  val dist = mutable.Map[Node, Int]()
    .addOne(source -> 0)
    .withDefaultValue(Int.MaxValue / 2)
  val queue = mutable.Set[Node]()
    .addAll(nodes(area))

  while (queue.nonEmpty) {
    println(queue.size)

    val node = queue.minBy(dist(_))
    queue.remove(node)

    neighbours(node)
      .filter { (neighbour, cost) => queue.contains(neighbour) }
      .foreach { (neighbour, cost) =>
        val alt = dist(node) + cost
        if (alt < dist(neighbour)) {
          dist(neighbour) = alt
          prev(neighbour) = List(node)
        } else if (alt == dist(neighbour)) {
          prev(neighbour) = node :: prev(neighbour)
        }
      }
  }

  val (bestTarget, minScore) = dist.filter(_._1.pos == target).minBy(_._2)

  def pathToSource(node: Node): Set[Position] =
    Set(node.pos) ++ prev.getOrElse(node, Nil).flatMap(pathToSource)

  val positionOnBestPath = pathToSource(bestTarget).size

  (minScore, positionOnBestPath)
}

def parseInput(input: String): Area = {
  Area(input.linesIterator.toVector.map(_.toVector))
}

case class Area(matrix: Vector[Vector[Char]]) {
  def at(pos: Position): Char =
    matrix(pos.y)(pos.x)

  def positionOf(c: Char): Seq[Position] =
    for {
      y <- matrix.indices
      x <- matrix.head.indices
      if matrix(y)(x) == c
    } yield Position(x, y)
}

case class Position(x: Int, y: Int) {
  def +(dir: Direction): Position = Position(x + dir.x, y + dir.y)
}

case class Direction(x: Int, y: Int) {
  def rotateLeft: Direction = Direction(y, -x)
  def rotateRight: Direction = Direction(-y, x)
}

object Direction {
  val right: Direction = Direction(1, 0)
  val upDownLeftRight: List[Direction] =
    List(Direction(0, 1), Direction(0, -1), Direction(-1, 0), Direction(1, 0))
}
