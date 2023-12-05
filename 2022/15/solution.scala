import java.nio.file.{Files, Path}

object day15 {
  def main(args: Array[String]): Unit = {
    val input = Files.readString(Path.of("input.txt")).trim
    println(part1(input))
    println(part2(input))
  }

  def part1(input: String): Int = {
    val sensors = parseInput(input)
    val rowY = 2000000

    val positionsWithSensorCoverage = sensors.flatMap { sensor =>
      val sensorToRowDistance = Math.abs(sensor.pos.y - rowY)
      if (sensorToRowDistance <= sensor.closestBeaconDistance) {
        val a = sensor.closestBeaconDistance - sensorToRowDistance
        (sensor.pos.x - a) to (sensor.pos.x + a)
      } else {
        Seq.empty
      }
    }.toSet

    val beaconsInRow =
      sensors.map(_.closestBeaconPos).filter(_.y == rowY).map(_.x).toSet

    (positionsWithSensorCoverage -- beaconsInRow).size
  }

  def part2(input: String): BigInt = {
    val sensors = parseInput(input)

    def searchRecursively(area: Rectangle): Option[Pos] = {
      val isFullyCoveredByOneSensor = sensors.exists(sensor =>
        area.corners.forall(sensor.isWithinClosesBeaconDistance)
      )
      if (isFullyCoveredByOneSensor) {
        // Distress beacon cannot be in this area
        None
      } else {
        area.split() match {
          case Some((subarea1, subarea2)) =>
            searchRecursively(subarea1)
              .orElse(searchRecursively(subarea2))
          case None =>
            // Cannot split it further and it isn't covered by any sensor - it's the distress beacon
            Some(area.corners.head)
        }
      }
    }

    val searchArea = Rectangle(x1 = 0, y1 = 0, x2 = 4000000, y2 = 4000000)
    val distressBeacon = searchRecursively(searchArea).get
    BigInt(distressBeacon.x) * 4000000 + distressBeacon.y
  }

  private def parseInput(input: String): Seq[Sensor] = {
    val report =
      """Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)""".r
    input.linesIterator.map { case report(sensorX, sensorY, beaconX, beaconY) =>
      Sensor(
        pos = Pos(sensorX.toInt, sensorY.toInt),
        closestBeaconPos = Pos(beaconX.toInt, beaconY.toInt)
      )
    }.toSeq
  }

  private case class Pos(x: Int, y: Int)

  private case class Rectangle(x1: Int, y1: Int, x2: Int, y2: Int) {
    val corners: Seq[Pos] =
      Seq(Pos(x1, y1), Pos(x2, y1), Pos(x1, y2), Pos(x2, y2))

    private def width: Int = x2 - x1 + 1
    private def height: Int = y2 - y1 + 1

    def split(): Option[(Rectangle, Rectangle)] =
      if (width >= height)
        splitHorizontally()
      else
        splitVertically()

    private def splitHorizontally(): Option[(Rectangle, Rectangle)] =
      Option.when(width >= 2) {
        val xMiddle = (x1 + x2) / 2
        (this.copy(x2 = xMiddle), this.copy(x1 = xMiddle + 1))
      }

    private def splitVertically(): Option[(Rectangle, Rectangle)] =
      Option.when(height >= 2) {
        val yMiddle = (y1 + y2) / 2
        (this.copy(y2 = yMiddle), this.copy(y1 = yMiddle + 1))
      }
  }

  private case class Sensor(pos: Pos, closestBeaconPos: Pos) {
    val closestBeaconDistance: Int = manhattanDistance(pos, closestBeaconPos)

    def isWithinClosesBeaconDistance(p: Pos): Boolean =
      manhattanDistance(pos, p) <= closestBeaconDistance
  }

  private def manhattanDistance(p1: Pos, p2: Pos): Int =
    Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y)
}
