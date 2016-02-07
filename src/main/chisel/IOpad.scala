package FPGASimulation

import Chisel._

class IOpad extends Module {
  val io = new Bundle {
    val outside = new IOEdge().flip
    val inside = new IOEdge()
    val blkBits = UInt(INPUT,72)
  }

  val dffInside  = Reg(next = io.inside.p0)
  val dffOutside = Reg(next = io.outside.p1)

  io.outside.p0:= dffInside & io.blkBits(0)
  io.inside.p1 := dffOutside & io.blkBits(1)
}

class IOpadTests(c: IOpad) extends Tester(c) {
  poke(c.io.blkBits, int(UInt("h0000_0000_0000_0000_0002"))) // External -> FPGA 
  poke(c.io.outside.p1, 1)
  step(1)
  expect(c.io.inside.p1, 1)
  poke(c.io.inside.p0, 1)
  step(1)
  expect(c.io.outside.p0, 0)
  poke(c.io.blkBits, int(UInt("h0000_0000_0000_0000_0001"))) // FPGA -> External 
  poke(c.io.outside.p1, 1)
  step(1)
  expect(c.io.inside.p1, 0)
  poke(c.io.inside.p0, 1)
  step(1)
  expect(c.io.inside.p0, 1)
}

object IOpad{
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new IOpad())) {
      c => new IOpadTests(c) }
  }
}
