package FPGASimulation

import Chisel._

class TopLevel extends Module{
  val io = new Bundle{
    val reset_n = UInt(INPUT, 1)
    val gclk = UInt(INPUT,1)
    val gpi   = UInt(INPUT, 16)
    val gpo   = UInt(OUTPUT, 16)
    val hipi  = UInt(INPUT, 8)
    val hipo  = UInt(OUTPUT,8)
  }

  val fpgaUtils = new FPGAUtils() 
  var fpga = fpgaUtils.assembleFPGA()

  //below code works with tester
  val IHCB1 = fpga(2)(13).asInstanceOf[IHCB].io
  //val CLB1 = Module(new CLB())
  val CLB1 = fpga(2)(12).asInstanceOf[CLB].io

  io.gpo := UInt(0)
  CLB1.E <> IHCB1.W
  IHCB1.S.p0 := io.gpi(0)
  io.gpo(1) := CLB1.S.p6
}

class TopLevelTest(c: TopLevel) extends Tester(c) {
  //poke(c.io.globalBlkBits(10)(11), int(UInt("h00_0000_0000_0000_0010")))
  //poke(c.io.globalBlkBits(10)(10), int(UInt("h03_0000_0000_0000_0006")))
  poke(c.io.gpi, 0)
  expect(c.io.gpo, 0)
  step(1)
  poke(c.io.gpi, 1)
  expect(c.io.gpo, 2)
  step(1)
}

object TopLevel {
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new TopLevel())) {
      c => new TopLevelTest(c) }
  }
}
