import java.nio.file.{Files, Path}
import scala.annotation.tailrec
import scala.sys.error

object day14 {
  def main(args: Array[String]): Unit = {
    val input = Files.readString(Path.of("input.txt")).trim
    println(part1(input))
    println(part2(input))
  }

  def part1(input: String): Int = {
    val initialWorld = parseWorld(input)
    val finalWorld = simulateInfiniteSandFall(initialWorld)
    printWorld(finalWorld)
    finalWorld.values.count(_ == 'o')
  }

  def part2(input: String): Int = {
    val initialWorld = addFloor(parseWorld(input))
    val finalWorld = simulateInfiniteSandFall(initialWorld)
    printWorld(finalWorld)
    finalWorld.values.count(_ == 'o')
  }

  private def parseWorld(input: String): World = {
    input.linesIterator
      .flatMap(line =>
        line
          .split(" -> ")
          .toSeq
          .map { point =>
            val Array(x, y) = point.split(',')
            (x.toInt, y.toInt)
          }
          .sliding(2)
          .flatMap { case Seq((x1, y1), (x2, y2)) =>
            for (
              x <- math.min(x1, x2) to math.max(x1, x2);
              y <- math.min(y1, y2) to math.max(y1, y2)
            ) yield (x, y)
          }
      )
      .map(_ -> '#')
      .toMap
  }

  type World = Map[(Int, Int), Char]

  private def addFloor(world: World): World = {
    val y = world.keys.map(_._2).max + 2
    val xs = (500 - y) to (500 + y)
    val floor = xs.map(x => (x, y) -> '#').toMap
    world ++ floor
  }

  @tailrec private def simulateInfiniteSandFall(world: World): World = {
    simulateSandFall(world) match {
      case None           => world
      case Some(newWorld) => simulateInfiniteSandFall(newWorld)
    }
  }

  private def simulateSandFall(world: World): Option[World] = {
    val worldMaxY = world.keys.map(_._2).max
    var x = 500
    var y = 0
    while (true) {
      if (world.contains((x, y))) {
        return None
      } else if (y > worldMaxY) {
        return None
      } else if (!world.contains((x, y + 1))) {
        y += 1
      } else if (!world.contains((x - 1, y + 1))) {
        x -= 1
        y += 1
      } else if (!world.contains((x + 1, y + 1))) {
        x += 1
        y += 1
      } else {
        return Some(world.updated((x, y), 'o'))
      }
    }
    error("unreachable")
  }

  private def printWorld(world: World): Unit = {
    val minX = world.keys.map(_._1).min
    val maxX = world.keys.map(_._1).max
    val minY = world.keys.map(_._2).min
    val maxY = world.keys.map(_._2).max

    (minY to maxY).foreach { y =>
      (minX to maxX).foreach { x =>
        print(world.getOrElse((x, y), '.'))
      }
      println("")
    }
  }
}
