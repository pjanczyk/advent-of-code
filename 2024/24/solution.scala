package day24

import java.nio.file.Files
import java.nio.file.Path

@main def main(): Unit = {
  val input = Files.readString(Path.of("input.txt")).trim
  println(part1(input))
  println(part2(input))
}

def part1(input: String): Long = {
  val circuit = parseInput(input)
  (0 to 45)
    .map { i => eval(circuit.gates, circuit.in, z(i)).toLong << i }
    .sum
}

def part2(input: String): String = {
  val originalGates = parseInput(input).gates

  var gates = originalGates
  var correctGates = Set.empty[Gate]

  (0 to 45).foreach { i =>
    val candidateGates = (gates.toSet -- correctGates).toList
    val possibleSwaps =
      candidateGates.combinations(0) ++
        candidateGates.combinations(2) ++
        candidateGates.combinations(4) ++
        candidateGates.combinations(6) ++
        candidateGates.combinations(8)
    gates = possibleSwaps
      .map { swaps => swapGates(gates, swaps) }
      .find { updatedGates => passesTests(updatedGates, i) }
      .get
    correctGates ++= relatedGates(gates, z(i))
  }

  val swappedGates = gates diff originalGates
  swappedGates.map(_.out).sorted.mkString(",")
}

def x(bit: Int): String = f"x$bit%02d"
def y(bit: Int): String = f"y$bit%02d"
def z(bit: Int): String = f"z$bit%02d"

def swapGates(gates: List[Gate], gatesToSwap: List[Gate]): List[Gate] = {
  gates.diff(gatesToSwap) ++
    gatesToSwap.grouped(2)
      .flatMap { case List(gate1, gate2) =>
        List(
          gate1.copy(out = gate2.out),
          gate2.copy(out = gate1.out)
        )
      }
}

def passesTests(gates: List[Gate], i: Int): Boolean = {
  if (hasCycles(gates)) return false

  var tests: List[(Map[String, Int], Int)] = Nil
  tests ::= Map().withDefaultValue(0) -> 0
  if (i < 45) {
    tests ::= Map(x(i) -> 1).withDefaultValue(0) -> 1
    tests ::= Map(y(i) -> 1).withDefaultValue(0) -> 1
    tests ::= Map(x(i) -> 1, y(i) -> 1).withDefaultValue(0) -> 0
  }
  if (i > 0) {
    tests ::= Map(x(i - 1) -> 1).withDefaultValue(0) -> 0
    tests ::= Map(y(i - 1) -> 1).withDefaultValue(0) -> 0
    tests ::= Map(x(i - 1) -> 1, y(i - 1) -> 1).withDefaultValue(0) -> 1
  }
  if (i > 0 && i < 45) {
    tests ::= Map(x(i - 1) -> 1, y(i - 1) -> 1, x(i) -> 1).withDefaultValue(0) -> 0
    tests ::= Map(x(i - 1) -> 1, y(i - 1) -> 1, y(i) -> 1).withDefaultValue(0) -> 0
  }
  tests.forall { (in, expectedOut) => eval(gates, in, z(i)) == expectedOut }
}

def hasCycles(gates: List[Gate]): Boolean = {
  var visited: Set[Gate] = Set.empty
  var finished: Set[Gate] = Set.empty
  var cycleFound = false

  def dfs(gate: Gate): Unit = {
    if (finished(gate)) return
    if (visited(gate)) {
      cycleFound = true
      return
    }
    visited += gate
    gates.find(_.out == gate.in1).foreach(dfs)
    gates.find(_.out == gate.in2).foreach(dfs)
    finished += gate
  }

  gates.foreach(dfs)
  cycleFound
}

def eval(gates: List[Gate], in: Map[String, Int], wire: String): Int = {
  gates.find(_.out == wire) match {
    case Some(gate) =>
      val in1 = eval(gates, in, gate.in1)
      val in2 = eval(gates, in, gate.in2)
      gate.op match {
        case "AND" => in1 & in2
        case "OR" => in1 | in2
        case "XOR" => in1 ^ in2
      }
    case None => in(wire)
  }
}

def relatedGates(gates: List[Gate], wire: String): Set[Gate] = {
  gates.find(_.out == wire) match {
    case Some(gate) => Set(gate) ++ relatedGates(gates, gate.in1) ++ relatedGates(gates, gate.in2)
    case None => Set.empty
  }
}

case class Gate(in1: String, op: String, in2: String, out: String)

case class Circuit(in: Map[String, Int], gates: List[Gate])

def parseInput(input: String): Circuit = {
  val Array(inStr, gatesStr) = input.split("\n\n")
  val in = inStr.linesIterator.map { case s"$in: $value" => (in, value.toInt) }.toMap
  val gates = gatesStr.linesIterator.map { case s"$in1 $op $in2 -> $out" => Gate(in1, op, in2, out) }.toList
  Circuit(in, gates)
}
