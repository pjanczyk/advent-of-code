package day14

import java.nio.file.Files
import java.nio.file.Path
import scala.collection.mutable

@main def main(): Unit = {
  val input = Files.readString(Path.of("input.txt")).trim
  println(part1(input))
  part2(input)
}

def part1(input: String): Int = {
  var robots = parseInput(input)
  val space = Vec(101, 103)
  (1 to 100).foreach { _ =>
    robots = simulateMovement(robots, space)
  }
  val center = space / 2
  val quadrants = List(
    robots.filter(r => r.position.x < center.x && r.position.y < center.y),
    robots.filter(r => r.position.x < center.x && r.position.y > center.y),
    robots.filter(r => r.position.x > center.x && r.position.y < center.y),
    robots.filter(r => r.position.x > center.x && r.position.y > center.y)
  )
  quadrants
    .map { robots => robots.size }
    .product
}

def part2(input: String): Unit = {
  var robots = parseInput(input)
  val space = Vec(101, 103)
  var maxChain = 0
  (1 to 10_000).foreach { second =>
    robots = simulateMovement(robots, space)
    val positions = robots.map(_.position).toSet
    val chain = (for {
      x <- 0 until space.x
      y <- 0 until space.y
    } yield {
      Math.max(
        Iterator.iterate(Vec(x, y))(_ + Vec(1, 0)).takeWhile(positions.contains).size,
        Iterator.iterate(Vec(x, y))(_ + Vec(0, 1)).takeWhile(positions.contains).size
      )
    }).max
    if (chain > maxChain) {
      maxChain = chain
      println("-".repeat(101) + second)
      visualize(robots, space)
    }
  }
}

def simulateMovement(robots: List[Robot], space: Vec): List[Robot] = {
  robots.map { robot =>
    robot.copy(
      position = (robot.position + robot.velocity) mod space
    )
  }
}

def visualize(robots: List[Robot], space: Vec): Unit = {
  (0 until space.y).foreach { y =>
    (0 until space.x).foreach { x =>
      if (robots.exists(_.position == Vec(x, y))) {
        print('X')
      } else {
        print(' ')
      }
    }
    println()
  }
}

case class Robot(position: Vec, velocity: Vec)

case class Vec(x: Int, y: Int) {
  def +(o: Vec): Vec = Vec(x + o.x, y + o.y)
  def *(f: Int): Vec = Vec(x * f, y * f)
  def /(f: Int): Vec = Vec(x / f, y / f)
  def mod(o: Vec): Vec = Vec(Math.floorMod(x, o.x), Math.floorMod(y, o.y))
}

def parseInput(input: String): List[Robot] = {
  input.linesIterator.toList
    .map { case s"p=$px,$py v=$vx,$vy" => Robot(Vec(px.toInt, py.toInt), Vec(vx.toInt, vy.toInt)) }
}
