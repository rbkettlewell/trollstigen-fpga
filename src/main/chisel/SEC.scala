package FPGASimulation

import Chisel._

class SEC extends Module {
	val io = new Bundle {
		val N = new FullEdge().flip
		val W = new FullEdge()
		val blkBits = UInt(INPUT, 72)
	}
  io.N.p0  := io.W.p0 & io.blkBits(0)
  io.N.p10 := io.W.p2 & io.blkBits(1)
  io.N.p8  := io.W.p4 & io.blkBits(2)
  io.N.p6  := io.W.p6 & io.blkBits(3)
  io.N.p4  := io.W.p8 & io.blkBits(4)
  io.N.p2  := io.W.p10 & io.blkBits(5)

  io.W.p1  := io.N.p1 & io.blkBits(6)
  io.W.p11 := io.N.p3 & io.blkBits(7)
  io.W.p9  := io.N.p5 & io.blkBits(8)
  io.W.p7  := io.N.p7 & io.blkBits(9)
  io.W.p5  := io.N.p9 & io.blkBits(10)
  io.W.p3  := io.N.p11 & io.blkBits(11)
  
}

class SECTest(c: SEC) extends Tester(c) {
	poke(c.io.W.p0, 1)
	poke(c.io.blkBits,1)
	expect(c.io.N.p0, 1)
	expect(c.io.N.p4, 0)
	poke(c.io.N.p3, 1)
	poke(c.io.blkBits,128)
	expect(c.io.W.p11, 1)
	expect(c.io.W.p1, 0)
}

object SEC{
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new SEC())) {
      c => new SECTest(c) }
  }
}
