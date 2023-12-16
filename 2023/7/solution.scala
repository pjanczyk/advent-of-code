import java.nio.file.Files
import java.nio.file.Path
import scala.math.Ordering.Implicits.seqOrdering

@main def day7(): Unit = {
  val input = Files.readString(Path.of("input.txt")).trim
  println(getTotalWinnings(input, part1HandOrdering))
  println(getTotalWinnings(input, part2HandOrdering))
}

type Hand = String

def getTotalWinnings(input: String, handOrdering: Ordering[Hand]): Int = {
  input.linesIterator.toSeq
    .map { case s"$hand $bid" => (hand, bid.toInt) }
    .sortBy { case (hand, _) => hand }(handOrdering)
    .zipWithIndex
    .map { case ((_, bid), index) => bid * (index + 1) }
    .sum
}

def part1HandOrdering: Ordering[Hand] = Ordering.by { hand =>
  val labelCounts = hand.groupBy(identity).values.map(_.length).toSeq.sorted.reverse
  val cardStrengths = hand.map(Seq('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A').indexOf)
  (labelCounts, cardStrengths)
}

def part2HandOrdering: Ordering[Hand] = Ordering.by { hand =>
  val jokerCount = hand.count(_ == 'J')
  val regularLabelCounts = hand.filter(_ != 'J').groupBy(identity).values.map(_.length).toSeq.sorted.reverse
  val labelCounts = regularLabelCounts.zipAll(Seq(jokerCount), 0, 0).map(_ + _)
  val cardStrengths = hand.map(Seq('J', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A').indexOf)
  (labelCounts, cardStrengths)
}
