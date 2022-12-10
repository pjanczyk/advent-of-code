import java.nio.file.{Files, Path}

object day10 {
  def main(args: Array[String]): Unit = {
    val input = Files.readString(Path.of("input.txt")).trim
    println(part1(input))
    println(part2(input))
  }

  def part1(input: String): Int = {
    val addx = "addx ([0-9-]+)".r
    input.linesIterator
      .flatMap {
        case "noop"        => Seq(0)
        case addx(operand) => Seq(0, operand.toInt)
      }
      .scanLeft(1) { (register, operand) => register + operand }
      .zipWithIndex
      .map { case (register, index) => (register, index + 1) }
      .filter { case (_, cycle) => cycle == 20 || cycle % 40 == 20 }
      .map { case (register, cycle) => register * cycle }
      .sum
  }

  def part2(input: String): String = {
    val addx = "addx ([0-9-]+)".r
    input.linesIterator
      .flatMap {
        case "noop"        => Seq(0)
        case addx(operand) => Seq(0, operand.toInt)
      }
      .scanLeft(1) { (register, operand) => register + operand }
      .zipWithIndex
      .map { case (register, index) => (register, index + 1) }
      .map { case (register, cycle) =>
        val spriteStart = register - 1
        val spriteEnd = register + 1
        val drawnPosition = (cycle - 1) % 40
        if (drawnPosition >= spriteStart && drawnPosition <= spriteEnd)
          "██"
        else
          "  "
      }
      .take(240)
      .grouped(40)
      .map(_.mkString)
      .mkString("\n")
  }
}
