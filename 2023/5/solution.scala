import java.nio.file.Files
import java.nio.file.Path

@main def day5(): Unit = {
  val input = Files.readString(Path.of("input.txt")).trim
  println(part1(input))
  println(part2(input))
}


def part1(input: String): Long = {
  val (Seq(seedSegment), mappingSegments) = input.split("\n\n").toSeq.splitAt(1)
  val seeds = parseSeedSegment(seedSegment)
  val mappings = mappingSegments.map(parseMappingSegment)

  val locations = seeds.map { seed =>
    mappings.foldLeft(seed) { (value, mapping) => mapping.apply(value) }
  }

  locations.min
}


def part2(input: String): Long = {
  val (Seq(seedSegment), mappingSegments) = input.split("\n\n").toSeq.splitAt(1)
  val seedRanges = parseSeedRangeSegment(seedSegment)
  val mappings = mappingSegments.map(parseMappingSegment)

  val locationRanges = mappings.foldLeft(seedRanges) { (ranges, mapping) =>
    ranges.flatMap(mapping.apply)
  }

  locationRanges.map(_.start).min
}

case class CompositeMapping(mappings: Seq[RangeMapping]) {
  def apply(value: Long): Long =
    mappings.flatMap(_.apply(value))
      .headOption
      .getOrElse(value)

  def apply(range: Range): Seq[Range] =
    continuousMappings.flatMap(_.apply(range))

  private def continuousMappings: Seq[RangeMapping] = {
    val coveredRanges = mappings.map(_.source).sortBy(_.start)
    val notCoveredRanges =
      (Range(Long.MinValue, Long.MinValue) +: coveredRanges :+ Range(Long.MaxValue, Long.MaxValue))
        .sliding(2)
        .map { case Seq(range1, range2) => Range(range1.end, range2.start) }
        .toSeq

    mappings ++ notCoveredRanges.map(RangeMapping.identity)
  }
}

case class RangeMapping(source: Range, destination: Range) {
  private val offset = destination.start - source.start

  def apply(value: Long): Option[Long] =
    Option.when(value >= source.start && value < source.end) {
      value + offset
    }

  def apply(range: Range): Option[Range] = {
    val intersectionStart = math.max(range.start, source.start)
    val intersectionEnd = math.min(range.end, source.end)
    Option.when(intersectionStart < intersectionEnd) {
      Range(intersectionStart + offset, intersectionEnd + offset)
    }
  }
}

object RangeMapping {
  def identity(range: Range): RangeMapping = RangeMapping(range, range)
}

case class Range(start: Long, end: Long)

def parseSeedSegment(segment: String): Seq[Long] = {
  segment.stripPrefix("seeds: ").split(' ').map(_.toLong).toSeq
}

def parseSeedRangeSegment(segment: String): Seq[Range] = {
  segment.stripPrefix("seeds: ").split(' ').map(_.toLong)
    .grouped(2)
    .map { case Array(start, length) => Range(start, start + length) }
    .toSeq
}

def parseMappingSegment(segment: String): CompositeMapping = {
  CompositeMapping(
    segment.linesIterator
      .drop(1)
      .map(_.split(' ').map(_.toLong))
      .map { case Array(destinationStart, sourceStart, length) =>
        RangeMapping(
          source = Range(sourceStart, sourceStart + length),
          destination = Range(destinationStart, destinationStart + length)
        )
      }
      .toSeq
  )
}
