package FPGASimulation

import Chisel._

class SRAMWord extends Module {
  val io = new Bundle {
    val write   = UInt(INPUT,1)
    val writeN = UInt(INPUT,1)
    val read    = UInt(INPUT,1)
    val readN  = UInt(INPUT,1)
    val bitWrite = UInt(INPUT,8)
    val bitRead  = UInt(OUTPUT,8)
    val progBits = UInt(OUTPUT,8)
  }

  // Default assignments
  io.bitRead  := UInt(0)
  io.progBits := UInt(0)

  (0 until 8).foreach{i=>
    val sCell = Module(new SRAMCell())
    sCell.io.write := io.write
    sCell.io.writeN := io.writeN
    sCell.io.read  := io.read
    sCell.io.readN := io.readN
    sCell.io.bitWrite := io.bitWrite(i)
    io.bitRead(i) := sCell.io.bitRead
    io.progBits(i) := sCell.io.progBit
  }
}
class SRAMWordTests(c: SRAMWord) extends Tester(c) {
  //Test write 1
  poke(c.io.write, 1)
  poke(c.io.writeN, 0)
  step(1)
  poke(c.io.bitWrite, 0)
  step(1)
  poke(c.io.write, 0)
  poke(c.io.writeN, 1)
  step(1)
  poke(c.io.read, 1)
  poke(c.io.readN, 0)
  step(1)
  expect(c.io.bitRead, 0)
  expect(c.io.progBits, 0)
  //Test write zero
  step(1)
  poke(c.io.write, 1)
  poke(c.io.writeN, 0)
  step(1)
  poke(c.io.bitWrite, 1)
  step(1)
  poke(c.io.write, 0)
  poke(c.io.writeN, 1)
  step(1)
  poke(c.io.read, 1)
  poke(c.io.readN, 0)
  step(1)
  expect(c.io.bitRead, 1)
  expect(c.io.progBits, 1)
  step(1)
  // Test case where no read or write
  poke(c.io.read, 0)
  poke(c.io.readN, 1)
  step(1)
  expect(c.io.bitRead, 0)
  expect(c.io.progBits, 1)
}
object SRAMWord{
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new SRAMWord())) {
      c => new SRAMWordTests(c) }
  }
}
