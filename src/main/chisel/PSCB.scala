package FPGASimulation

import Chisel._

class PSCB extends Module {
	val io = new Bundle {
		val N = new CLBSouth().flip
		val S = new IOEdge().flip
		val E = new FullEdge().flip
		val W = new FullEdge()
		val blkBits = UInt(INPUT, 72)
	}

  /*
  From: (S,2,false,Track), To: (N,2,false,Pin)
  From: (S,3,false,Track), To: (N,2,false,Pin)
  From: (S,8,false,Track), To: (N,2,false,Pin)
  From: (S,9,false,Track), To: (N,2,false,Pin)
  From: (S,1,false,Pad), To: (N,0,false,Track),(N,1,false,Track),(N,2,false,Track),
                            (N,3,false,Track),(N,4,false,Track),(N,5,false,Track)
  From: (N,6,false,Pin), To: (S,6,false,Track),(S,7,false,Track),
                            (S,8,false,Track),(S,9,false,Track)
  From: (N,0,false,Track), To: (S,0,false,Pad)
  From: (N,1,false,Track), To: (S,0,false,Pad)
  From: (N,4,false,Track), To: (S,0,false,Pad)
  From: (N,5,false,Track), To: (S,0,false,Pad)
  From: (N,8,false,Track), To: (S,0,false,Pad)
  From: (N,9,false,Track), To: (S,0,false,Pad)
  */

	io.E <> io.W
	io.N.p2 := (io.W.p2 & io.blkBits(0)) | (io.E.p3 & io.blkBits(1)) |
						 (io.W.p8 & io.blkBits(2)) | (io.E.p9 & io.blkBits(3)) |
						 (io.S.p1 & io.blkBits(0) & io.blkBits(6)) |
						 (io.S.p1 & io.blkBits(1) & io.blkBits(7))
  io.E.p0 := (io.S.p1 & io.blkBits(4)) | io.W.p0
  io.W.p1 := (io.S.p1 & io.blkBits(5)) | io.E.p1
  io.E.p2 := (io.S.p1 & io.blkBits(6)) | io.W.p2
  io.W.p3 := (io.S.p1 & io.blkBits(7)) | io.E.p3
  io.E.p4 := (io.S.p1 & io.blkBits(8)) | io.W.p4
  io.W.p5 := (io.S.p1 & io.blkBits(9)) | io.E.p5
  io.E.p6 := (io.N.p6 & io.blkBits(10)) | io.W.p6
  io.W.p7 := (io.N.p6 & io.blkBits(11)) | io.E.p7
  io.E.p8 := (io.N.p6 & io.blkBits(12)) | io.W.p8
  io.W.p9 := (io.N.p6 & io.blkBits(13)) | io.E.p9
	io.S.p0 := (io.W.p0 & io.blkBits(14)) | (io.E.p1 & io.blkBits(15)) |
						 (io.W.p4 & io.blkBits(16)) | (io.E.p5 & io.blkBits(17)) |
             (io.W.p8 & io.blkBits(18)) | (io.E.p9 & io.blkBits(19)) |
						 (io.N.p6 & io.blkBits(12) & io.blkBits(18)) |
						 (io.N.p6 & io.blkBits(13) & io.blkBits(19))
}

class PSCBTest(c: PSCB) extends Tester(c) {
  poke(c.io.S.p1, 1)
  poke(c.io.N.p6, 1)
  poke(c.io.W.p2, 1)
  poke(c.io.E.p3, 0)
  poke(c.io.W.p8, 0)
  poke(c.io.E.p9, 0)
	poke(c.io.blkBits, int(UInt("h0000_0000_0000_0000_3FFF")))
	expect(c.io.N.p2, 1)
  expect(c.io.E.p0, 1)
  expect(c.io.W.p1, 1)
  expect(c.io.E.p2, 1)
  expect(c.io.W.p3, 1)
  expect(c.io.E.p4, 1)
  expect(c.io.W.p5, 1)
  expect(c.io.E.p6, 1)
  expect(c.io.W.p7, 1)
  expect(c.io.E.p8, 1)
  expect(c.io.W.p9, 1)
  expect(c.io.S.p0, 0)
}

object PSCB {
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new PSCB())) {
      c => new PSCBTest(c) }
  }
}
