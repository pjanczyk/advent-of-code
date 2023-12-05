import java.nio.file.Files
import java.nio.file.Path
import scala.collection.mutable
import scala.jdk.StreamConverters.*
import scala.util.matching.Regex

@main def day4(): Unit = {
  val input = Files.readString(Path.of("input.txt")).trim
  println(part1(input))
  println(part2(input))
}

def part1(input: String): Int = {
  parseCards(input)
    .collect { case Card(_, matches) if matches > 0 => math.pow(2, matches - 1).toInt }
    .sum
}

def part2(input: String): Int = {
  val cards = parseCards(input)
    .map(card => card.id -> card)
    .toMap

  def processCard(card: Card): Int = {
    val cardsBelow = (1 to card.matches).map(i => cards(card.id + i))
    1 + cardsBelow.map(processCard).sum
  }

  cards.values.map(processCard).sum
}

case class Card(id: Int, matches: Int)

def parseCards(input: String): Seq[Card] = {
  input.linesIterator
    .map { case s"Card ${idStr}: ${winningNumbersStr} | ${myNumbersStr}" =>
      val id = idStr.trim.toInt
      val winningNumbers = winningNumbersStr.trim.split("\\s+").map(_.toInt).toSet
      val myNumbers = myNumbersStr.trim.split("\\s+").map(_.toInt).toSet
      val matches = myNumbers.intersect(winningNumbers).size
      Card(id, matches)
    }
    .toSeq
}
