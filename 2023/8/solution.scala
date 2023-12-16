import java.nio.file.Files
import java.nio.file.Path
import scala.annotation.tailrec
import scala.math.Ordering.Implicits.seqOrdering

@main def day8(): Unit = {
  val input = Files.readString(Path.of("input.txt")).trim
  println(part1(input))
  println(part2(input))
}

def part1(input: String): Long = {
  val (turns, nodes) = parseInput(input)
  countSteps(turns, nodes, startNode = "AAA", endNode = _ == "ZZZ")
}

def part2(input: String): Long = {
  val (turns, nodes) = parseInput(input)
  val startNodes = nodes.keys.filter(_.endsWith("A")).toSeq
  startNodes
    .map(startNode => countSteps(turns, nodes, startNode, endNode = _.endsWith("Z")))
    .reduce(leastCommonMultiple)
}

type Turns = Seq[Char]
type Nodes = Map[String, (String, String)]

def parseInput(input: String): (Turns, Nodes) = {
  val turns = input.linesIterator.next()
  val nodes = input.linesIterator.drop(2)
    .map { case s"${from} = (${left}, ${right})" => from -> (left, right) }
    .toMap
  (turns, nodes)
}

def countSteps(turns: Turns, nodes: Nodes, startNode: String, endNode: String => Boolean): Long = {
  val repeatedTurns = Iterator.continually(turns).flatten
  repeatedTurns
    .scanLeft(startNode) { (currentNode, turn) =>
      val (leftNode, rightNode) = nodes(currentNode)
      turn match {
        case 'L' => leftNode
        case 'R' => rightNode
      }
    }
    .takeWhile(node => !endNode(node))
    .size
    .toLong
}

def leastCommonMultiple(a: Long, b: Long): Long = {
  a * b / greatestCommonDivisor(a, b)
}

@tailrec
def greatestCommonDivisor(a: Long, b: Long): Long = {
  if b == 0 then a else greatestCommonDivisor(b, a % b)
}