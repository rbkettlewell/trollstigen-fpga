package FPGASimulation

import Chisel._

class PECB extends Module {
	val io = new Bundle {
		val N = new FullEdge().flip
		val S = new FullEdge()
		val E = new IOEdge().flip
		val W = new CLBEast().flip
		val blkBits = UInt(INPUT, 72)
	}

	/*
  From: (W,2,false,Track), To: (E,0,false,Pad)
  From: (W,3,false,Track), To: (E,0,false,Pad)
  From: (W,6,false,Track), To: (E,0,false,Pad)
  From: (W,7,false,Track), To: (E,0,false,Pad)
  From: (W,10,false,Track), To: (E,0,false,Pad)
  From: (W,11,false,Track), To: (E,0,false,Pad)
  From: (E,1,false,Pad), To: (W,0,false,Track),(W,1,false,Track),(W,2,false,Track),
                            (W,3,false,Track),(W,4,false,Track),(W,5,false,Track)
  From: (E,0,false,Track), To: (W,1,false,Pin)
  From: (E,1,false,Track), To: (W,1,false,Pin)
  From: (E,6,false,Track), To: (W,1,false,Pin)
  From: (E,7,false,Track), To: (W,1,false,Pin)
  From: (E,4,false,Track), To: (W,5,false,Pin)
  From: (E,5,false,Track), To: (W,5,false,Pin)
  From: (E,10,false,Track), To: (W,5,false,Pin)
  From: (E,11,false,Track), To: (W,5,false,Pin)
	*/

	io.N <> io.S
	io.E.p0 := (io.S.p2 & io.blkBits(0)) | (io.N.p3 & io.blkBits(1)) |
						 (io.S.p6 & io.blkBits(2)) | (io.N.p7 & io.blkBits(3)) |
             (io.S.p8 & io.blkBits(4)) | (io.N.p9 & io.blkBits(5))
  io.N.p0 := (io.E.p1 & io.blkBits(6))
  io.S.p1 := (io.E.p1 & io.blkBits(7))
  io.N.p2 := (io.E.p1 & io.blkBits(8))
  io.S.p3 := (io.E.p1 & io.blkBits(9))
  io.N.p4 := (io.E.p1 & io.blkBits(10))
  io.S.p5 := (io.E.p1 & io.blkBits(11))
	io.W.p1 := (io.S.p0 & io.blkBits(12)) | (io.N.p1 & io.blkBits(13)) |
						 (io.S.p6 & io.blkBits(14)) | (io.N.p7 & io.blkBits(15))
  io.W.p5 := (io.S.p4 & io.blkBits(16)) | (io.N.p5 & io.blkBits(17)) |
             (io.S.p10 & io.blkBits(18)) | (io.N.p11 & io.blkBits(19))
}

class PECBTest(c: PECB) extends Tester(c) {
  poke(c.io.E.p1, 1)
  poke(c.io.S.p2, 1)
  poke(c.io.N.p3, 0)
  poke(c.io.S.p6, 0)
  poke(c.io.N.p7, 0)
  poke(c.io.S.p8, 0)
  poke(c.io.N.p9, 0)
	poke(c.io.blkBits, int(UInt("h0000_0000_0000_000F_FFFF")))
	expect(c.io.E.p0, 1)
  expect(c.io.N.p0, 1)
  expect(c.io.S.p1, 1)
  expect(c.io.N.p2, 1)
  expect(c.io.S.p3, 1)
  expect(c.io.N.p4, 1)
  expect(c.io.S.p5, 1)
  expect(c.io.W.p1, 0)
  expect(c.io.W.p5, 0)
}

object PECB {
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new PECB())) {
      c => new PECBTest(c) }
  }
}
