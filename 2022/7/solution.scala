import java.nio.file.{Files, Path}
import scala.collection.mutable
import scala.util.matching.Regex

object day7 {
  def main(args: Array[String]): Unit = {
    val input = Files.readString(Path.of("input.txt")).trim
    println(part1(input))
    println(part2(input))
  }

  def part1(input: String): Int = {
    calculateDirectorySizes(input).values.filter(_ <= 100000).sum
  }

  def part2(input: String): Int = {
    val directorySizes = calculateDirectorySizes(input)
    val maxAllowedUsedSpace = 70000000 - 30000000
    val currentUsedSpace = directorySizes("/")
    val minSpaceToFree = currentUsedSpace - maxAllowedUsedSpace
    directorySizes.values.filter(_ >= minSpaceToFree).min
  }

  private def calculateDirectorySizes(input: String): Map[String, Int] = {
    val cdCommand = """\$ cd (\S+)""".r
    val lsFileEntry = """(\d+) (\S+)""".r

    val currentDirectory = mutable.Stack[String]()
    val directorySizes = mutable.Map[String, Int]().withDefaultValue(0)

    input.linesIterator.foreach {
      case cdCommand(dirname) =>
        dirname match {
          case "/"  => currentDirectory.clear()
          case ".." => currentDirectory.pop()
          case _    => currentDirectory.push(dirname)
        }
      case lsFileEntry(size, _) =>
        currentDirectory.tails
          .map(_.reverseIterator.mkString(start = "/", sep = "/", end = ""))
          .foreach { path =>
            directorySizes(path) += size.toInt
          }
      case _ =>
    }

    directorySizes.toMap
  }
}
