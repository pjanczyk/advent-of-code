import java.nio.file.Files
import java.nio.file.Path
import scala.jdk.StreamConverters.*
import scala.util.matching.Regex

@main def day3(): Unit = {
  val schematic = Files.readString(Path.of("input.txt")).linesIterator.toIndexedSeq
  println(part1(schematic))
  println(part2(schematic))
}

def part1(schematic: IndexedSeq[String]): Int = {
  val numbers = findNumbers(schematic)

  val partNumbers = numbers.filter { number =>
    findAdjacentSymbols(number, schematic)
      .exists { case (_, symbol) => !symbol.isDigit && symbol != '.' }
  }

  partNumbers.map(_.value).sum
}

def part2(schematic: IndexedSeq[String]): Int = {
  val numbers = findNumbers(schematic)

  val asterisksToNumbers = numbers
    .flatMap { number =>
      findAdjacentSymbols(number, schematic)
        .collect { case (asterisk, '*') => asterisk -> number }
    }
    .groupMap(_._1)(_._2)

  val gearRatios = asterisksToNumbers
    .collect { case (_, Seq(number1, number2)) => number1.value * number2.value }

  gearRatios.sum
}

case class Number(value: Int, y: Int, xMin: Int, xMax: Int)

def findNumbers(schematic: IndexedSeq[String]): Seq[Number] = {
  schematic
    .zipWithIndex
    .flatMap { case (line, y) =>
      Regex("\\d+")
        .findAllMatchIn(line)
        .map { match_ =>
          Number(match_.toString().toInt, y = y, xMin = match_.start, xMax = match_.end - 1)
        }
    }
}

def findAdjacentSymbols(number: Number, schematic: IndexedSeq[String]): Seq[((Int, Int), Char)] = {
  for {
    y <- (number.y - 1) to (number.y + 1) if schematic.indices.contains(y)
    x <- (number.xMin - 1) to (number.xMax + 1) if schematic(y).indices.contains(x)
    symbol = schematic(y)(x)
  } yield ((y, x), symbol)
}