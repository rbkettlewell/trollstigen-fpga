package FPGASimulation

import Chisel._

class WordSelector extends Module {
  val io = new Bundle {
    val bRow = UInt(INPUT,1)
    val bRead = UInt(INPUT,1)
    val bWrite = UInt(INPUT,1)
    val readWord   = UInt(OUTPUT,1)
    val readWordN  = UInt(OUTPUT,1)
    val writeWord  = UInt(OUTPUT,1)
    val writeWordN = UInt(OUTPUT,1)
  }

  io.readWordN := ~(io.bRow & io.bRead)
  io.readWord := (io.bRow & io.bRead)
  io.writeWordN := ~(io.bRow & io.bWrite)
  io.writeWord := (io.bRow & io.bWrite)
}
class WordSelectorTests(c: WordSelector) extends Tester(c) {
  // Test case where no read or write
  expect(c.io.readWord, 0)
  expect(c.io.readWordN, 1)
  poke(c.io.bRow, 1)
  poke(c.io.bRead, 1)
  expect(c.io.readWord, 1)
  expect(c.io.readWordN, 0)
  
  expect(c.io.writeWord, 0)
  expect(c.io.writeWordN, 1)
  poke(c.io.bRow, 1)
  poke(c.io.bWrite, 1)
  expect(c.io.writeWord, 1)
  expect(c.io.writeWordN, 0)
}
object WordSelector{
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new WordSelector())) {
      c => new WordSelectorTests(c) }
  }
}
