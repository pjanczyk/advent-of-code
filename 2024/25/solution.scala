package day25

import java.nio.file.Files
import java.nio.file.Path

@main def main(): Unit = {
  val input = Files.readString(Path.of("input.txt")).trim
  println(part1(input))
}

def part1(input: String): Long = {
  val schematics = parseInput(input)
  (for {
    key <- schematics.collect { case key: Key => key }
    lock <- schematics.collect { case lock: Lock => lock }
    if (key.pins zip lock.pins).forall { (keyPin, lockPin) => keyPin <= lockPin }
  } yield ()).size
}

sealed trait Schematic

case class Key(pins: List[Int]) extends Schematic

case class Lock(pins: List[Int]) extends Schematic

def parseInput(input: String): List[Schematic] = {
  input.split("\n\n")
    .map { schematic =>
      if (schematic.startsWith("#"))
        Lock(schematic.linesIterator.toList.transpose.map(_.count(_ == '.')))
      else
        Key(schematic.linesIterator.toList.transpose.map(_.count(_ == '#')))
    }
    .toList
}
