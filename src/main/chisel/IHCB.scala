package FPGASimulation

import Chisel._

class IHCB extends Module {
	val io = new Bundle {
		val N = new FullEdge().flip
		val S = new FullEdge()
		val E = new CLBWest().flip
		val W = new CLBEast().flip
		val blkBits = UInt(INPUT, 72)
	}
	io.N <> io.S
	io.E.p3 := (io.S.p2 & io.blkBits(0)) | (io.N.p3 & io.blkBits(1)) |
						 (io.S.p8 & io.blkBits(2)) | (io.N.p9 & io.blkBits(3))
	io.W.p1 := (io.S.p0 & io.blkBits(4)) | (io.N.p1 & io.blkBits(5)) |
						 (io.S.p6 & io.blkBits(6)) | (io.N.p7 & io.blkBits(7))
	io.W.p5 := (io.S.p4 & io.blkBits(8)) | (io.N.p5 & io.blkBits(9)) |
					 	 (io.S.p10 & io.blkBits(10)) | (io.N.p11 & io.blkBits(11))
}

class IHCBTest(c: IHCB) extends Tester(c) {
	poke(c.io.S.p2, 1)
	poke(c.io.blkBits, 2)
	expect(c.io.N.p2, 1)
	expect(c.io.E.p3, 0)
}

object IHCB {
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new IHCB())) {
      c => new IHCBTest(c) }
  }
}
