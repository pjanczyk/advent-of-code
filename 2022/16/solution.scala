import java.nio.file.{Files, Path}
import scala.collection.mutable

object day16 {
  def main(args: Array[String]): Unit = {
    val input = Files.readString(Path.of("input.txt")).trim
    println(part1(input))
    println(part2(input))
  }

  def part1(input: String): Int = {
    val valves = parseInput(input)
    val distances = calculateDistances(valves)
    val flowRates = valves.map(valve => valve.name -> valve.flowRate).toMap
    val valvesWithFlowRate = valves.filter(_.flowRate > 0).map(_.name)

    def recursion(
        minutesLeft: Int,
        pressure: Int,
        currentValve: String,
        openValves: Set[String]
    ): Int = {
      println(s"$minutesLeft $pressure $currentValve $openValves")
      if (minutesLeft == 0) {
        pressure
      } else {
        valvesWithFlowRate
          .filterNot(openValves.contains)
          .filter(nextValve => distances(currentValve, nextValve) < minutesLeft)
          .map { nextValve =>
            val distance = distances(currentValve, nextValve)
            val flowRate = flowRates(nextValve)
            recursion(
              minutesLeft = minutesLeft - distance - 1,
              pressure = pressure + flowRate * (minutesLeft - distance - 1),
              currentValve = nextValve,
              openValves = openValves + nextValve
            )
          }
          .appended(pressure)
          .max
      }
    }

    recursion(
      minutesLeft = 30,
      pressure = 0,
      currentValve = "AA",
      openValves = Set.empty
    )
  }

  def part2(input: String): Int = {
    val valves = parseInput(input)
    val distances = calculateDistances(valves)
    val flowRates = valves.map(valve => valve.name -> valve.flowRate).toMap
    val valvesWithFlowRate = valves.filter(_.flowRate > 0).map(_.name)

    sealed class Turn
    object Turn {
      case object Me extends Turn
      case object Elephant extends Turn
    }

    case class State(
        minutesLeft: Int,
        currentValve: String,
        openValves: Set[String],
        turn: Turn
    )

    lazy val recursion: State => Int =
      memoize { case State(minutesLeft, currentValve, openValves, turn) =>
        println(s"$minutesLeft $currentValve ${openValves.mkString(",")} $turn")
        if (minutesLeft == 0) {
          turn match {
            case Turn.Me =>
              recursion(
                State(
                  minutesLeft = 26,
                  currentValve = "AA",
                  openValves = openValves,
                  turn = Turn.Elephant
                )
              )
            case Turn.Elephant =>
              0
          }
        } else {
          valvesWithFlowRate
            .filterNot(openValves.contains)
            .filter(nextValve =>
              distances(currentValve, nextValve) < minutesLeft
            )
            .map { nextValve =>
              val distance = distances(currentValve, nextValve)
              val flowRate = flowRates(nextValve)
              val newMinutesLeft = minutesLeft - distance - 1
              val pressureIncrease = flowRate * newMinutesLeft
              recursion(
                State(
                  minutesLeft = newMinutesLeft,
                  currentValve = nextValve,
                  openValves = openValves + nextValve,
                  turn = turn
                )
              ) + pressureIncrease
            }
            .appended(
              recursion(
                State(
                  minutesLeft = 0,
                  currentValve = currentValve,
                  openValves = openValves,
                  turn = turn
                )
              )
            )
            .max
        }
      }

    recursion(
      State(
        minutesLeft = 26,
        currentValve = "AA",
        openValves = Set.empty,
        turn = Turn.Me
      )
    )
  }

  private def parseInput(input: String): Seq[Valve] = {
    val valveRegex =
      """Valve (\w+) has flow rate=(\d+); tunnels? leads? to valves? (.+)""".r
    input.linesIterator.map { case valveRegex(name, flowRate, neighbours) =>
      Valve(
        name,
        flowRate.toInt,
        neighbours.split(", ").toSeq
      )
    }.toSeq
  }

  private case class Valve(name: String, flowRate: Int, neighbours: Seq[String])

  private def calculateDistances(
      valves: Seq[Valve]
  ): Map[(String, String), Int] = {
    // Floyd–Warshall algorithm
    val distances = mutable
      .Map[(String, String), Int]()
      .withDefaultValue(Int.MaxValue / 4)
    for (valve <- valves) {
      distances((valve.name, valve.name)) = 0
      for (neighbour <- valve.neighbours)
        distances((valve.name, neighbour)) = 1
    }
    for (
      k <- valves.map(_.name);
      i <- valves.map(_.name);
      j <- valves.map(_.name)
    ) {
      if (distances((i, j)) > distances((i, k)) + distances((k, j))) {
        distances((i, j)) = distances((i, k)) + distances((k, j))
      }
    }
    distances.toMap
  }

  private def memoize[Arg, Result](f: Arg => Result): Arg => Result = {
    val cache = new mutable.HashMap[Arg, Result]()
    (arg: Arg) => cache.getOrElseUpdate(arg, f(arg))
  }
}
