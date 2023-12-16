import java.nio.file.Files
import java.nio.file.Path

@main def day9(): Unit = {
  val input = Files.readString(Path.of("input.txt")).trim
  println(part1(input))
  println(part2(input))
}

def part1(input: String): Int = {
  parseSequences(input).map(extrapolateNext).sum
}

def part2(input: String): Int = {
  parseSequences(input).map(extrapolatePrevious).sum
}

def parseSequences(input: String): Seq[Seq[Int]] = {
  input.linesIterator.toSeq
    .map { line => line.split(' ').map(_.toInt).toSeq }
}

def extrapolateNext(input: Seq[Int]): Int = {
  Iterator
    .iterate(input) { sequence => sequence.sliding(2).map { case Seq(a, b) => b - a }.toSeq }
    .takeWhile { sequence => sequence.exists(_ != 0) }
    .toSeq
    .reverse
    .foldLeft(0) { case (acc, sequence) => sequence.last + acc }
}

def extrapolatePrevious(input: Seq[Int]): Int = {
  Iterator
    .iterate(input) { sequence => sequence.sliding(2).map { case Seq(a, b) => b - a }.toSeq }
    .takeWhile { sequence => sequence.exists(_ != 0) }
    .toSeq
    .reverse
    .foldLeft(0) { case (acc, sequence) => sequence.head - acc }
}
