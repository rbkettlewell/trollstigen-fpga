package FPGASimulation

import Chisel._

class IVCB extends Module {
	val io = new BlkEdges()
	val blkBits = UInt(INPUT, 72)
	io.N <> io.S
}

class IVCBTest(c: IVCB) extends Tester(c) {
	poke(c.io.S.p0, 1)
	expect(c.io.N.p0, 1)
	expect(c.io.S.p1, 0)
}

object IVCB {
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new IVCB())) {
      c => new IVCBTest(c) }
  }
}
