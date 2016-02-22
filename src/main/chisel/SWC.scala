package FPGASimulation

import Chisel._

class SWC extends Module {
	val io = new Bundle {
		val N = new FullEdge().flip
		val E = new FullEdge().flip
		val blkBits = UInt(INPUT, 72)
	}
  io.N.p10 := io.E.p1 & io.blkBits(69)
  io.N.p0 := io.E.p3 & io.blkBits(50)
  io.N.p2 := io.E.p5 & io.blkBits(59)
  io.N.p4 := io.E.p7 & io.blkBits(34)
  io.N.p6 := io.E.p9 & io.blkBits(52)
  io.N.p8 := io.E.p11 & io.blkBits(45)

  io.E.p2  := io.N.p1 & io.blkBits(23)
  io.E.p4  := io.N.p3 & io.blkBits(38)
  io.E.p6  := io.N.p5 & io.blkBits(46)
  io.E.p8  := io.N.p7 & io.blkBits(63)
  io.E.p10  := io.N.p9 & io.blkBits(70)
  io.E.p0 := io.N.p11 & io.blkBits(14)

}

class SWCTest(c: SWC) extends Tester(c) {
	poke(c.io.E.p1, 1)
	poke(c.io.blkBits,1)
	expect(c.io.N.p10, 1)
	expect(c.io.N.p6, 0)
	poke(c.io.N.p1, 1)
	poke(c.io.blkBits,64)
	expect(c.io.E.p2, 1)
	expect(c.io.E.p4, 0)
}

object SWC{
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new SWC())) {
      c => new SWCTest(c) }
  }
}
