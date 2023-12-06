import java.nio.file.Files
import java.nio.file.Path
import scala.util.chaining.*

@main def day6(): Unit = {
  val input = Files.readString(Path.of("input.txt")).trim
  println(part1(input))
  println(part2(input))
}

def part1(input: String): Long = {
  input.linesIterator
    .map(line => line.split(':').last.split(' ').flatMap(_.toIntOption))
    .toSeq
    .transpose
    .map { case Seq(raceTime, recordDistance) => countWaysToBeatRecord(raceTime, recordDistance) }
    .product
}

def part2(input: String): Long = {
  input.linesIterator
    .map(line => line.split(':').last.filter(_.isDigit).toLong)
    .toSeq
    .pipe { case Seq(raceTime, recordDistance) => countWaysToBeatRecord(raceTime, recordDistance) }
}

def countWaysToBeatRecord(raceTime: Long, recordDistance: Long): Int = {
  (0L to raceTime).count { holdTime => (raceTime - holdTime) * holdTime > recordDistance }
}
