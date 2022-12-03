import java.nio.file.{Files, Path}

object day2 {
  def modulo(a: Int, b: Int): Int = {
    (a % b + b) % b
  }

  def part1(input: String): Int = {
    input.linesIterator.map { line =>
      // 0 - rock, 1 - paper, 2 - scissors
      val opponentShape = line(0) - 'A'
      val yourShape = line(2) - 'X'

      val pointsForShape = yourShape + 1
      val pointsForOutcome = (yourShape, opponentShape) match {
        case (2, 1) | (1, 0) | (0, 2)        => 6
        case _ if yourShape == opponentShape => 3
        case _                               => 0
      }
      pointsForShape + pointsForOutcome
    }.sum
  }

  def part2(input: String): Int = {
    input.linesIterator.map { line =>
      val opponentShape = line(0) - 'A' // 0 - rock, 1 - paper, 2 - scissors
      val outcome = line(2)
      val yourShape = outcome match {
        case 'X' => modulo(opponentShape - 1, 3) // lose
        case 'Y' => opponentShape // draw
        case 'Z' => modulo(opponentShape + 1, 3) // win
      }
      val pointsForShape = yourShape + 1
      val pointsForOutcome = outcome match {
        case 'X' => 0
        case 'Y' => 3
        case 'Z' => 6
      }
      pointsForShape + pointsForOutcome
    }.sum
  }

  def main(args: Array[String]): Unit = {
    val input = Files.readString(Path.of("input.txt"))
    println(part1(input))
    println(part2(input))
  }
}
