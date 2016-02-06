package FPGASimulation

import Chisel._

class NWC extends Module {
	val io = new Bundle {
		val S = new FullEdge()
		val E = new FullEdge().flip
		val blkBits = UInt(INPUT, 72)
	}
  io.E.p8  := io.S.p0 & io.blkBits(0)
  io.E.p6  := io.S.p2 & io.blkBits(1)
  io.E.p4  := io.S.p4 & io.blkBits(2)
  io.E.p2  := io.S.p6 & io.blkBits(3)
  io.E.p0  := io.S.p8 & io.blkBits(4)
  io.E.p10 := io.S.p10 & io.blkBits(5)

  io.S.p9  := io.E.p1 & io.blkBits(6)
  io.S.p7  := io.E.p3 & io.blkBits(7)
  io.S.p5  := io.E.p5 & io.blkBits(8)
  io.S.p3  := io.E.p7 & io.blkBits(9)
  io.S.p1  := io.E.p9 & io.blkBits(10)
  io.S.p11 := io.E.p11 & io.blkBits(11)
  
}

class NWCTest(c: NWC) extends Tester(c) {
	poke(c.io.S.p0, 1)
	poke(c.io.blkBits,1)
	expect(c.io.E.p8, 1)
	expect(c.io.E.p6, 0)
	poke(c.io.E.p1, 1)
	poke(c.io.blkBits,64)
	expect(c.io.S.p9, 1)
	expect(c.io.S.p1, 0)
}

object NWC{
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new NWC())) {
      c => new NWCTest(c) }
  }
}
