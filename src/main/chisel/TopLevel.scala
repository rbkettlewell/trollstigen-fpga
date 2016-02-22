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
  // Connect West GPIO to external world
  (0 until 6).foreach{i=> 
    val iopadWest = fpga((3+i)*2)(0).asInstanceOf[IOpad].io
      io.gpo(i) := iopadWest.outside.p0
      iopadWest.outside.p1 := io.gpi(i)
  }
  // Connect North GPIO to external world
  (0 until 8).foreach{i=> 
    val iopadNorth= fpga(9*2)((i+1)*2).asInstanceOf[IOpad].io
      io.gpo(6+i) := iopadNorth.outside.p0
      iopadNorth.outside.p1 := io.gpi(6+i)
  }
  // Connect East GPIO to external world
  (0 until 2).foreach{i=> 
    val iopadEast= fpga((7+i)*2)(9*2).asInstanceOf[IOpad].io
      io.gpo(14+i) := iopadEast.outside.p0
      iopadEast.outside.p1 := io.gpi(14+i)
  }
}

class TopLevelTest(c: TopLevel) extends Tester(c) {


  val testXOR3 = true 
  val testAND3 = false 
  val testMajority = false
  val testTwoBitAdder = false 
  val testEvenOdd = false 

  if(testEvenOdd){
    val stepInternal = 6
    poke(c.io.reset_n, 1)
    poke(c.io.gpi, int(UInt("b0")))
    step(stepInternal)
    expect(c.io.gpo, 2)
    poke(c.io.gpi, int(UInt("b1")))
    step(1)
    poke(c.io.gpi, int(UInt("b0")))
    step(4)
    expect(c.io.gpo, 0)
    poke(c.io.gpi, int(UInt("b1")))
    step(1)
    poke(c.io.gpi, int(UInt("b0")))
    step(stepInternal)
    expect(c.io.gpo, 2)
    //Reset test case which should prevent toggle
    poke(c.io.reset_n, 0)
    poke(c.io.gpi, int(UInt("b1")))
    step(1)
    poke(c.io.gpi, int(UInt("b0")))
    step(4)
    expect(c.io.gpo, 2)
    step(stepInternal)
  }
  if(testTwoBitAdder){
    val stepInternal = 6 
    poke(c.io.gpi, int(UInt("b000")))
    step(stepInternal)
    expect(c.io.gpo, 0)
    poke(c.io.gpi, int(UInt("b001")))
    step(stepInternal)
    expect(c.io.gpo, 8)
    poke(c.io.gpi, int(UInt("b010")))
    step(stepInternal)
    expect(c.io.gpo, 8)
    poke(c.io.gpi, int(UInt("b011")))
    step(stepInternal)
    expect(c.io.gpo, 16)
    step(stepInternal)
    peek(c.io.gpo)
  }
  if(testAND3){
    val stepInternal = 5 
    poke(c.io.gpi, int(UInt("b000")))
    step(stepInternal)
    expect(c.io.gpo, 0)
    poke(c.io.gpi, int(UInt("b001")))
    step(stepInternal)
    expect(c.io.gpo, 0)
    poke(c.io.gpi, int(UInt("b010")))
    step(stepInternal)
    expect(c.io.gpo, 0)
    poke(c.io.gpi, int(UInt("b011")))
    step(stepInternal)
    expect(c.io.gpo, 0)
    poke(c.io.gpi, int(UInt("b100")))
    step(stepInternal)
    expect(c.io.gpo, 0)
    poke(c.io.gpi, int(UInt("b101")))
    step(stepInternal)
    expect(c.io.gpo, 0)
    poke(c.io.gpi, int(UInt("b110")))
    step(stepInternal)
    expect(c.io.gpo, 0)
    poke(c.io.gpi, int(UInt("b111")))
    step(stepInternal)
    expect(c.io.gpo, 8)
  }
  if(testXOR3){
    val stepInternal = 10 
    poke(c.io.gpi, int(UInt("b000")))
    step(stepInternal)
    expect(c.io.gpo, 0)
    poke(c.io.gpi, int(UInt("b001")))
    step(stepInternal)
    expect(c.io.gpo, 8)
    poke(c.io.gpi, int(UInt("b010")))
    step(stepInternal)
    expect(c.io.gpo, 8)
    poke(c.io.gpi, int(UInt("b011")))
    step(stepInternal)
    expect(c.io.gpo, 0)
    poke(c.io.gpi, int(UInt("b100")))
    step(stepInternal)
    expect(c.io.gpo, 8)
    poke(c.io.gpi, int(UInt("b101")))
    step(stepInternal)
    expect(c.io.gpo, 0)
    poke(c.io.gpi, int(UInt("b110")))
    step(stepInternal)
    expect(c.io.gpo, 0)
    poke(c.io.gpi, int(UInt("b111")))
    step(stepInternal)
    expect(c.io.gpo, 8)
  }

  if(testMajority){
    val stepInternal = 8 
    poke(c.io.reset_n, 1)
    poke(c.io.gpi, int(UInt("b00000000")))
    step(stepInternal)
    //expect(c.io.gpo, 0)
    peek(c.io.gpo)
    poke(c.io.gpi, int(UInt("b10101001")))
    step(stepInternal)
    peek(c.io.gpo)
    // expect(c.io.gpo, 0)
    poke(c.io.gpi, int(UInt("b11101110")))
    step(stepInternal)
    peek(c.io.gpo)
    // expect(c.io.gpo, 256)
    poke(c.io.gpi, int(UInt("b00000001")))
    step(stepInternal)
    // expect(c.io.gpo, 0)
    peek(c.io.gpo)
    step(stepInternal)
  }
}

object TopLevel {
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new TopLevel())) {
      c => new TopLevelTest(c) }
  }
}
