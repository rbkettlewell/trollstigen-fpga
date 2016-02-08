package FPGASimulation

import Chisel._

class TopLevel extends Module{
  val io = new Bundle{
    val reset_n = UInt(INPUT, 1)
    val gclk = UInt(INPUT,1)
    val tp0 = UInt(INPUT,1)
    val gpi   = UInt(INPUT, 16)
    val gpo   = UInt(OUTPUT, 16)
    val hipi  = UInt(INPUT, 8)
    val hipo  = UInt(OUTPUT,8)
  }

  val fpgaUtils = new FPGAUtils() 
  val fpga = fpgaUtils.assembleFPGA()
  fpgaUtils.connectBlocksMutate(fpga)

  // Default Assignments
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
}

class TopLevelTest(c: TopLevel) extends Tester(c) {
  poke(c.io.gpi, int(UInt("b000")))
  step(1)
  step(1)
  step(1)
  step(1)
  step(1)
  expect(c.io.gpo, 0)
  poke(c.io.gpi, int(UInt("b001")))
  step(1)
  step(1)
  step(1)
  step(1)
  step(1)
  expect(c.io.gpo, 8)
  poke(c.io.gpi, int(UInt("b010")))
  step(1)
  step(1)
  step(1)
  step(1)
  step(1)
  expect(c.io.gpo, 8)
  poke(c.io.gpi, int(UInt("b011")))
  step(1)
  step(1)
  step(1)
  step(1)
  step(1)
  expect(c.io.gpo, 0)
  poke(c.io.gpi, int(UInt("b100")))
  step(1)
  step(1)
  step(1)
  step(1)
  step(1)
  expect(c.io.gpo, 8)
  poke(c.io.gpi, int(UInt("b101")))
  step(1)
  step(1)
  step(1)
  step(1)
  step(1)
  expect(c.io.gpo, 0)
  poke(c.io.gpi, int(UInt("b110")))
  step(1)
  step(1)
  step(1)
  step(1)
  step(1)
  expect(c.io.gpo, 0)
  poke(c.io.gpi, int(UInt("b111")))
  step(1)
  step(1)
  step(1)
  step(1)
  step(1)
  expect(c.io.gpo, 8)
}

object TopLevel {
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new TopLevel())) {
      c => new TopLevelTest(c) }
  }
}
