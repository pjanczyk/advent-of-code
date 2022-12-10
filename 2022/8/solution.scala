import java.nio.file.{Files, Path}

object day8 {
  def main(args: Array[String]): Unit = {
    val input = Files.readString(Path.of("input.txt")).trim
    println(part1(input))
    println(part2(input))
  }

  def part1(input: String): Int = {
    val matrix: IndexedSeq[IndexedSeq[Int]] =
      input.linesIterator.map(_.map(_.toString.toInt).toIndexedSeq).toIndexedSeq
    val ys = matrix.indices
    val xs = matrix.head.indices

    (for (y <- ys; x <- xs) yield {
      val height = matrix(y)(x)
      matrix(y).slice(0, x).forall(_ < height) ||
      matrix(y).slice(x + 1, xs.end).forall(_ < height) ||
      matrix.map(_(x)).slice(0, y).forall(_ < height) ||
      matrix.map(_(x)).slice(y + 1, ys.end).forall(_ < height)
    }).count(identity)
  }

  def part2(input: String): Int = {
    val matrix: IndexedSeq[IndexedSeq[Int]] =
      input.linesIterator.map(_.map(_.toString.toInt).toIndexedSeq).toIndexedSeq
    val ys = matrix.indices
    val xs = matrix.head.indices

    (for (y <- ys; x <- xs) yield {
      val height = matrix(y)(x)
      val l = viewingDistance(height, matrix(y).slice(0, x).reverse)
      val r = viewingDistance(height, matrix(y).slice(x + 1, xs.end))
      val t = viewingDistance(height, matrix.map(_(x)).slice(0, y).reverse)
      val b = viewingDistance(height, matrix.map(_(x)).slice(y + 1, ys.end))
      l * r * t * b
    }).max
  }

  private def viewingDistance(tree: Int, treesInOneDirection: Seq[Int]): Int = {
    val lastVisibleTreeIndex = treesInOneDirection.indexWhere(_ >= tree)
    if (lastVisibleTreeIndex == -1) treesInOneDirection.size
    else lastVisibleTreeIndex + 1
  }
}
