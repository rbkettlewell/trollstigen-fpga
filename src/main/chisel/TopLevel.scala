package FPGASimulation

import Chisel._

class TopLevel extends Module{
  val io = new Bundle{
    val ipad0 = UInt(INPUT, 1)
    val opad1 = UInt(OUTPUT, 1)
    val sram0 = UInt(INPUT, 72)
    val sram1 = UInt(INPUT, 72)
  }
  val IHCB1 = Module(new IHCB())
  val CLB1 = Module(new CLB())
  CLB1.io.E <> IHCB1.io.W
  io.sram0 <> IHCB1.io.blkBits
  io.sram1 <> CLB1.io.blkBits
  IHCB1.io.S.p0 := io.ipad0
  io.opad1 := CLB1.io.S.p6
}

class TopLevelTest(c: TopLevel) extends Tester(c) {
  poke(c.io.sram0, int(UInt("h0000_0000_0000_0000_0010")))
  poke(c.io.sram1, int(UInt("h0003_0000_0000_0000_0006")))
  poke(c.io.ipad0, 0)
  expect(c.io.opad1, 0)
  step(1)
  poke(c.io.ipad0, 1)
  expect(c.io.opad1, 1)
}

object TopLevel {
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new TopLevel())) {
      c => new TopLevelTest(c) }
  }
}
