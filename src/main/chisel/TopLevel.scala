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
  val fpga = fpgaUtils.assembleFPGA()
  fpgaUtils.connectBlocksMutate(fpga)

  //Default Assignments
  io.hipo:= UInt(0)
  io.gpo := UInt(0)

  // Connect global reset_n and clock
  fpga(2)(0).asInstanceOf[IOpad].io.outside.p1 := io.reset_n
  fpga(4)(0).asInstanceOf[IOpad].io.outside.p1 := io.gclk

  // Connect HIP to external world
  (1 to 8).foreach{i=> 
    val iopad = fpga(0)(i*2).asInstanceOf[IOpad].io  
      io.hipo(8-i) := iopad.outside.p0
      iopad.outside.p1 := io.hipi(8-i)
  }
  // Connect GPIO to external world
  (0 until 8).foreach{i=> 
    val iopadWest = fpga((9+i)*2)(0).asInstanceOf[IOpad].io
    val iopadNorth= fpga(17*2)((i+1)*2).asInstanceOf[IOpad].io
      io.gpo(i) := iopadWest.outside.p0
      iopadWest.outside.p1 := io.gpi(i)
      io.gpo(8+i) := iopadNorth.outside.p0
      iopadNorth.outside.p1 := io.gpi(8+i)
  }

  //below code works with tester
  val IHCB1 = fpga(2)(13).asInstanceOf[IHCB].io
  //val CLB1 = Module(new CLB())
  val CLB1 = fpga(2)(12).asInstanceOf[CLB].io
  //CLB1.E <> IHCB1.W
  IHCB1.S.p0 := io.gpi(0)
  fpga(0)(14).asInstanceOf[IOpad].io.inside.p0 := CLB1.S.p6
}

class TopLevelTest(c: TopLevel) extends Tester(c) {
  //poke(c.io.globalBlkBits(10)(11), int(UInt("h00_0000_0000_0000_0010")))
  //poke(c.io.globalBlkBits(10)(10), int(UInt("h03_0000_0000_0000_0006")))
  poke(c.io.gpi, 0)
  step(1)
  expect(c.io.hipo, 0)
  step(1)
  poke(c.io.gpi, 1)
  step(1)
  expect(c.io.hipo, 2)
  peek(c.io.hipo)
  step(1)
  peek(c.io.hipo)
}

object TopLevel {
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new TopLevel())) {
      c => new TopLevelTest(c) }
  }
}
