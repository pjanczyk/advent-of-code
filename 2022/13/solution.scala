import org.json.JSONArray

import java.nio.file.{Files, Path}
import scala.jdk.CollectionConverters._
import scala.math.Ordering.Implicits.seqOrdering

object day13 {
  def main(args: Array[String]): Unit = {
    val input = Files.readString(Path.of("input.txt")).trim
    println(part1(input))
    println(part2(input))
  }

  def part1(input: String): Int = {
    input
      .split("\n\n")
      .toSeq
      .map(_.linesIterator.map(Data.parse).toSeq)
      .zipWithIndex
      .filter { case (Seq(packet1, packet2), _) => packet1 <= packet2 }
      .map { case (_, index) => index + 1 }
      .sum
  }

  def part2(input: String): Int = {
    val inputPackets = input.linesIterator
      .filter(_.nonEmpty)
      .map(Data.parse)
      .toSeq
    val dividerPackets = Seq("[[2]]", "[[6]]").map(Data.parse)
    val allPacketsSorted = (inputPackets ++ dividerPackets).sorted
    dividerPackets.map(allPacketsSorted.indexOf).map(_ + 1).product
  }

  private abstract sealed class Data extends Ordered[Data] {
    override def compare(that: Data): Int = {
      (this, that) match {
        case (a: IntegerData, b: IntegerData) => a.value.compare(b.value)
        case (a, b)                           => a.asList.elements.compare(b.asList.elements)
      }
    }

    private def asList: ListData = {
      this match {
        case integer: IntegerData => ListData(Seq(integer))
        case list: ListData       => list
      }
    }
  }
  private object Data {
    def parse(str: String): Data =
      fromJson(new JSONArray(str))

    private def fromJson(value: Any): Data =
      value match {
        case array: JSONArray => ListData(array.asScala.map(fromJson).toSeq)
        case integer: Int     => IntegerData(integer)
      }
  }

  private case class IntegerData(value: Int) extends Data
  private case class ListData(elements: Seq[Data]) extends Data
}
