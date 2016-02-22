package FPGASimulation

import Chisel._

class NWC extends Module {
	val io = new Bundle {
		val S = new FullEdge()
		val E = new FullEdge().flip
		val blkBits = UInt(INPUT, 72)
	}
  io.E.p8  := io.S.p0 & io.blkBits(55)
  io.E.p6  := io.S.p2 & io.blkBits(39)
  io.E.p4  := io.S.p4 & io.blkBits(30)
  io.E.p2  := io.S.p6 & io.blkBits(15)
  io.E.p0  := io.S.p8 & io.blkBits(6)
  io.E.p10 := io.S.p10 & io.blkBits(62)

  io.S.p9  := io.E.p1 & io.blkBits(29)
  io.S.p7  := io.E.p3 & io.blkBits(20)
  io.S.p5  := io.E.p5 & io.blkBits(35)
  io.S.p3  := io.E.p7 & io.blkBits(11)
  io.S.p1  := io.E.p9 & io.blkBits(18)
  io.S.p11 := io.E.p11 & io.blkBits(5)

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
