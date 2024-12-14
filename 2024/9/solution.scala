package day9

import java.nio.file.Files
import java.nio.file.Path
import scala.collection.mutable

@main def main(): Unit = {
  val input = Files.readString(Path.of("input.txt")).trim
  println(part1(input))
  println(part2(input))
}

def part1(input: String): Long = {
  type FileId = Int

  def parseInput(input: String): List[Option[FileId]] = {
    input.toList
      .map(_.toString.toInt)
      .zipWithIndex
      .flatMap { (size, index) =>
        if (index % 2 == 0) {
          val fileId = index / 2
          Iterator.fill(size)(Some(fileId))
        } else {
          Iterator.fill(size)(None)
        }
      }
  }

  def compact(disk: List[Option[FileId]]): List[FileId] = {
    val remaining = disk.to(mutable.ArrayDeque)
    val result = mutable.Buffer[FileId]()
    while (remaining.nonEmpty) {
      if (remaining.last.isEmpty) {
        remaining.removeLast()
      } else {
        remaining.removeHead() match {
          case Some(fileId) => result.append(fileId)
          case None => remaining.removeLast().foreach(result.append)
        }
      }
    }
    result.toList
  }

  def checksum(disk: List[FileId]): Long = {
    disk.zipWithIndex
      .map { (fileId, blockIndex) => fileId.toLong * blockIndex.toLong }
      .sum
  }

  val disk = parseInput(input)
  val compactedDisk = compact(disk)
  checksum(compactedDisk)
}

def part2(input: String): Long = {
  case class File(id: Int, blocks: Range)
  case class Free(blocks: Range)

  def parseInput(input: String): List[File | Free] = {
    var i = 0
    input.toList
      .map(_.toString.toInt)
      .zipWithIndex
      .map {
        case (size, index) if index % 2 == 0 =>
          val file = File(
            id = index / 2,
            blocks = i until (i + size)
          )
          i += size
          file
        case (size, index) =>
          val free = Free(i until (i + size))
          i += size
          free
      }
  }

  def compact(disk: List[File | Free]): List[File] = {
    val frees = disk.collect { case free: Free => free }.toArray
    val files = disk.collect { case file: File => file }
    files.reverse.map { file =>
      frees.indexWhere(free => free.blocks.start < file.blocks.start && free.blocks.size >= file.blocks.size) match {
        case -1 => file
        case freeIndex =>
          val free = frees(freeIndex)
          frees(freeIndex) = Free(free.blocks.drop(file.blocks.size))
          file.copy(blocks = free.blocks.take(file.blocks.size))
      }
    }
  }

  def checksum(files: List[File]): Long =
    files.map { file => file.blocks.sum.toLong * file.id.toLong }.sum

  val disk = parseInput(input)
  val compactedDisk = compact(disk)
  checksum(compactedDisk)
}
