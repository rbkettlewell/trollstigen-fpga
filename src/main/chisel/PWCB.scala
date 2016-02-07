package FPGASimulation

import Chisel._

class PWCB extends Module {
	val io = new Bundle {
		val N = new FullEdge().flip
		val S = new FullEdge()
		val E = new CLBWest().flip
		val W = new IOEdge().flip
		val blkBits = UInt(INPUT, 72)
	}

	/*
  From: (W,2,false,Track), To: (E,3,false,Pin)
  From: (W,3,false,Track), To: (E,3,false,Pin)
  From: (W,8,false,Track), To: (E,3,false,Pin)
  From: (W,9,false,Track), To: (E,3,false,Pin)
  From: (W,1,false,Pad), To: (E,0,false,Track),(E,1,false,Track),(E,2,false,Track),
                             (E,3,false,Track),(E,4,false,Track),(E,5,false,Track)
  From: (E,0,false,Track), To: (W,0,false,Pad)
  From: (E,1,false,Track), To: (W,0,false,Pad)
  From: (E,4,false,Track), To: (W,0,false,Pad)
  From: (E,5,false,Track), To: (W,0,false,Pad)
  From: (E,8,false,Track), To: (W,0,false,Pad)
  From: (E,9,false,Track), To: (W,0,false,Pad)
	*/

	io.N <> io.S
	io.E.p3 := (io.S.p2 & io.blkBits(0)) | (io.N.p3 & io.blkBits(1)) |
						 (io.S.p8 & io.blkBits(2)) | (io.N.p9 & io.blkBits(3)) |
						 (io.W.p1 & io.blkBits(0) & io.blkBits(6)) |
						 (io.W.p1 & io.blkBits(1) & io.blkBits(7))
  io.N.p0 := (io.W.p1 & io.blkBits(4))
  io.S.p1 := (io.W.p1 & io.blkBits(5))
  io.N.p2 := (io.W.p1 & io.blkBits(6))
  io.S.p3 := (io.W.p1 & io.blkBits(7))
  io.N.p4 := (io.W.p1 & io.blkBits(8))
  io.S.p5 := (io.W.p1 & io.blkBits(9))
	io.W.p0 := (io.S.p0 & io.blkBits(10)) | (io.N.p1 & io.blkBits(11)) |
						 (io.S.p4 & io.blkBits(12)) | (io.N.p5 & io.blkBits(13)) |
             (io.S.p8 & io.blkBits(14)) | (io.N.p9 & io.blkBits(15))
}

class PWCBTest(c: PWCB) extends Tester(c) {
  poke(c.io.W.p1, 1)
  poke(c.io.S.p2, 1)
  poke(c.io.N.p3, 0)
  poke(c.io.S.p8, 0)
  poke(c.io.N.p9, 0)
	poke(c.io.blkBits, int(UInt("h0000_0000_0000_0000_03FF")))
	expect(c.io.E.p3, 1)
  expect(c.io.N.p0, 1)
  expect(c.io.S.p1, 1)
  expect(c.io.N.p2, 1)
  expect(c.io.S.p3, 1)
  expect(c.io.N.p4, 1)
  expect(c.io.S.p5, 1)
  expect(c.io.W.p0, 0)
}

object PWCB {
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new PWCB())) {
      c => new PWCBTest(c) }
  }
}
