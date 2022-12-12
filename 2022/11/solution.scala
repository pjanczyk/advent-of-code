import java.nio.file.{Files, Path}
import scala.collection.mutable

object day11 {
  def main(args: Array[String]): Unit = {
    val input = Files.readString(Path.of("input.txt")).trim
    println(part1(input))
    println(part2(input))
  }

  def part1(input: String): Int = {
    val monkeys = parseMonkeys(input)
    playGame(monkeys, rounds = 20, reliefDivider = 3)
    monkeys.map(_.inspectedItems).sorted.takeRight(2).product
  }

  def part2(input: String): BigInt = {
    val monkeys = parseMonkeys(input)
    playGame(monkeys, rounds = 10000, reliefDivider = 1)
    monkeys.map(_.inspectedItems).sorted.takeRight(2).map(BigInt(_)).product
  }

  private def playGame(
      monkeys: IndexedSeq[Monkey],
      rounds: Int,
      reliefDivider: Int
  ): Unit = {
    (0 until rounds).foreach { _ =>
      monkeys.foreach { monkey => monkey.playRound(monkeys, reliefDivider) }
    }
  }

  class Monkey(
      private val startingItems: Seq[Int],
      private val operation: BigInt => BigInt,
      private val divisor: Int,
      private val monkeyIfDivisible: Int,
      private val monkeyIfNotDivisible: Int
  ) {
    private val items: mutable.ArrayDeque[BigInt] =
      startingItems.map(BigInt(_)).to(mutable.ArrayDeque)

    private var _inspectedItems: Int = 0

    def inspectedItems: Int = _inspectedItems

    def playRound(monkeys: IndexedSeq[Monkey], reliefDivider: Int): Unit = {
      val commonDenominator = monkeys.map(_.divisor).product
      while (items.nonEmpty) {
        val item =
          (operation(items.removeHead()) / reliefDivider) % commonDenominator
        _inspectedItems += 1
        val targetMonkey =
          if (item % divisor == 0) monkeyIfDivisible else monkeyIfNotDivisible
        monkeys(targetMonkey).items.append(item)
      }
    }
  }

  private def parseMonkeys(input: String): IndexedSeq[Monkey] = {
    val monkey =
      """Monkey .+:
        |  Starting items: (.+)
        |  Operation: new = (.+)
        |  Test: divisible by (.+)
        |    If true: throw to monkey (.+)
        |    If false: throw to monkey (.+)""".stripMargin.r

    val oldTimesN = """old \* (\d+)""".r
    val oldPlusN = """old \+ (\d+)""".r

    input
      .split("\n\n")
      .map { case monkey(startingItems, operation, test, ifTrue, ifFalse) =>
        new Monkey(
          startingItems = startingItems.split(", ").map(_.toInt),
          operation = operation match {
            case "old * old"  => old => old * old
            case oldTimesN(n) => old => old * n.toInt
            case oldPlusN(n)  => old => old + n.toInt
          },
          divisor = test.toInt,
          monkeyIfDivisible = ifTrue.toInt,
          monkeyIfNotDivisible = ifFalse.toInt
        )
      }
      .toIndexedSeq
  }
}
