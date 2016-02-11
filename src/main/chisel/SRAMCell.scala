package FPGASimulation

import Chisel._

class SRAMCell extends Module {
  val io = new Bundle {
    val write   = UInt(INPUT,1)
    val writeN = UInt(INPUT,1)
    val read    = UInt(INPUT,1)
    val readN  = UInt(INPUT,1)
    val bitWrite = UInt(INPUT,1)
    val bitRead  = UInt(OUTPUT,1)
  }

  val bitState = Reg(init = UInt(0))
  when (io.write === UInt(1) && io.writeN === UInt(0)){
    bitState := io.bitWrite
  }.elsewhen(io.read === UInt(1) && io.readN === UInt(0)){
    io.bitRead := bitState
  }.otherwise{
    io.bitRead := UInt(0)
  }

}
class SRAMCellTests(c: SRAMCell) extends Tester(c) {
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
  step(1)
  // Test case where no read or write
  poke(c.io.read, 0)
  poke(c.io.readN, 1)
  step(1)
  expect(c.io.bitRead, 0)
}
object SRAMCell{
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new SRAMCell())) {
      c => new SRAMCellTests(c) }
  }
}
