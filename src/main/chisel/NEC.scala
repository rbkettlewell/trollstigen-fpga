package FPGASimulation

import Chisel._

class NEC extends Module {
	val io = new Bundle {
		val S = new FullEdge()
		val W = new FullEdge()
		val blkBits = UInt(INPUT, 72)
	}
  io.W.p3  := io.S.p0 & io.blkBits(8)
  io.W.p5  := io.S.p2 & io.blkBits(25)
  io.W.p7  := io.S.p4 & io.blkBits(33)
  io.W.p9  := io.S.p6 & io.blkBits(48)
  io.W.p11 := io.S.p8 & io.blkBits(57)
  io.W.p1  := io.S.p10 & io.blkBits(1)

  io.S.p11  := io.W.p0 & io.blkBits(21)
  io.S.p1  := io.W.p2 & io.blkBits(2)
  io.S.p3  := io.W.p4 & io.blkBits(26)
  io.S.p5  := io.W.p6 & io.blkBits(19)
  io.S.p7  := io.W.p8 & io.blkBits(37)
  io.S.p9  := io.W.p10 & io.blkBits(12)

}

class NECTest(c: NEC) extends Tester(c) {
	poke(c.io.S.p0, 1)
	poke(c.io.blkBits,1)
	expect(c.io.W.p3, 1)
	expect(c.io.W.p5, 0)
	poke(c.io.W.p0, 1)
	poke(c.io.blkBits,64)
	expect(c.io.S.p11, 1)
	expect(c.io.S.p1, 0)
}

object NEC{
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new NEC())) {
      c => new NECTest(c) }
  }
}
