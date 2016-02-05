package FPGASimulation

import Chisel._

class IVCB extends Module {
	val io = new Bundle {
		val N = new CLBSouth().flip
		val S = new CLBNorth().flip
		val E = new FullEdge().flip
		val W = new FullEdge()
		val blkBits = UInt(INPUT, 72)
	}

  /*
  From: (S,2,false,Track), To: (N,2,false,Pin)
  From: (S,3,false,Track), To: (N,2,false,Pin)
  From: (S,8,false,Track), To: (N,2,false,Pin)
  From: (S,9,false,Track), To: (N,2,false,Pin)
  From: (N,6,false,Pin), To: (S,0,false,Track),(S,1,false,Track),
                             (S,2,false,Track),(S,3,false,Track)
  From: (N,0,false,Track), To: (S,0,false,Pin)
  From: (N,1,false,Track), To: (S,0,false,Pin)
  From: (N,6,false,Track), To: (S,0,false,Pin)
  From: (N,7,false,Track), To: (S,0,false,Pin)
  From: (N,4,false,Track), To: (S,4,false,Pin)
  From: (N,5,false,Track), To: (S,4,false,Pin)
  From: (N,10,false,Track), To: (S,4,false,Pin)
  From: (N,11,false,Track), To: (S,4,false,Pin)
  */

	io.E <> io.W
	io.N.p2 := (io.W.p2 & io.blkBits(0)) | (io.E.p3 & io.blkBits(1)) |
						 (io.W.p8 & io.blkBits(2)) | (io.E.p9 & io.blkBits(3))
  io.E.p0 := (io.N.p6 & io.blkBits(4))
  io.W.p1 := (io.N.p6 & io.blkBits(5))
  io.E.p2 := (io.N.p6 & io.blkBits(6))
  io.W.p3 := (io.N.p6 & io.blkBits(7))
	io.S.p0 := (io.E.p0 & io.blkBits(8)) | (io.E.p1 & io.blkBits(9)) |
						 (io.E.p6 & io.blkBits(10)) | (io.E.p7 & io.blkBits(11))
	io.S.p4 := (io.W.p4 & io.blkBits(12)) | (io.E.p5 & io.blkBits(13)) |
					 	 (io.W.p10 & io.blkBits(14)) | (io.E.p11 & io.blkBits(15))
}

class IVCBTest(c: IVCB) extends Tester(c) {
	poke(c.io.N.p6, 1)
  poke(c.io.W.p2, 1)
  poke(c.io.E.p3, 0)
  poke(c.io.W.p8, 0)
  poke(c.io.E.p9, 0)
	poke(c.io.blkBits, int(UInt("h0000_0000_0000_0000_00FF")))
	expect(c.io.N.p2, 1)
  expect(c.io.E.p0, 1)
  expect(c.io.W.p1, 1)
  expect(c.io.E.p2, 1)
  expect(c.io.W.p3, 1)
  expect(c.io.S.p0, 0)
  expect(c.io.S.p4, 0)
}

object IVCB {
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new IVCB())) {
      c => new IVCBTest(c) }
  }
}
