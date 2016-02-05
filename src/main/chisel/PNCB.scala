package FPGASimulation

import Chisel._

class PNCB extends Module {
	val io = new Bundle {
		val N = new IOEdge().flip
		val S = new CLBNorth().flip
		val E = new FullEdge().flip
		val W = new FullEdge()
		val blkBits = UInt(INPUT, 72)
	}

  /*
  From: (S,2,false,Track), To: (N,0,false,Pad)
  From: (S,3,false,Track), To: (N,0,false,Pad)
  From: (S,6,false,Track), To: (N,0,false,Pad)
  From: (S,7,false,Track), To: (N,0,false,Pad)
  From: (S,10,false,Track), To: (N,0,false,Pad)
  From: (S,11,false,Track), To: (N,0,false,Pad)
  From: (N,1,false,Pad), To: (S,0,false,Track),(S,1,false,Track),(S,2,false,Track),
                            (S,3,false,Track),(S,4,false,Track),(S,5,false,Track)
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
	io.N.p0 := (io.W.p2 & io.blkBits(0)) | (io.E.p3 & io.blkBits(1)) |
						 (io.W.p6 & io.blkBits(2)) | (io.E.p7 & io.blkBits(3)) |
             (io.W.p10 & io.blkBits(4)) | (io.E.p11 & io.blkBits(5))
  io.E.p0 := (io.N.p1 & io.blkBits(6))
  io.W.p1 := (io.N.p1 & io.blkBits(7))
  io.E.p2 := (io.N.p1 & io.blkBits(8))
  io.W.p3 := (io.N.p1 & io.blkBits(9))
  io.E.p4 := (io.N.p1 & io.blkBits(10))
  io.W.p5 := (io.N.p1 & io.blkBits(11))
	io.S.p0 := (io.W.p0 & io.blkBits(12)) | (io.E.p1 & io.blkBits(13)) |
						 (io.W.p6 & io.blkBits(14)) | (io.E.p7 & io.blkBits(15))
  io.S.p4 := (io.W.p4 & io.blkBits(16)) | (io.E.p5 & io.blkBits(17)) |
             (io.W.p10 & io.blkBits(18)) | (io.E.p11 & io.blkBits(19))
}

class PNCBTest(c: PNCB) extends Tester(c) {
  poke(c.io.N.p1, 1)
  poke(c.io.W.p2, 1)
  poke(c.io.E.p3, 0)
  poke(c.io.W.p6, 0)
  poke(c.io.E.p7, 0)
  poke(c.io.W.p10, 0)
  poke(c.io.E.p11, 0)
	poke(c.io.blkBits, int(UInt("h0000_0000_0000_000F_FFFF")))
	expect(c.io.N.p1, 1)
  expect(c.io.E.p0, 1)
  expect(c.io.W.p1, 1)
  expect(c.io.E.p2, 1)
  expect(c.io.W.p3, 1)
  expect(c.io.E.p4, 1)
  expect(c.io.W.p5, 1)
  expect(c.io.S.p0, 0)
  expect(c.io.S.p4, 0)
}

object PNCB {
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new PNCB())) {
      c => new PNCBTest(c) }
  }
}
