package FPGASimulation

import Chisel._

class PSSB extends Module {
	val io = new Bundle {
		val N = new FullEdge().flip
		val W = new FullEdge()
		val E = new FullEdge().flip
		val blkBits = UInt(INPUT, 72)
	}

  /*
	From: (W,0,false,Track), To: (E,0,false,Track),(N,0,false,Track)
	From: (W,2,false,Track), To: (E,2,false,Track),(N,2,false,Track)
	From: (W,4,false,Track), To: (E,4,false,Track),(N,4,false,Track)
	From: (W,6,false,Track), To: (E,6,false,Track),(N,6,false,Track)
	From: (W,8,false,Track), To: (E,8,false,Track),(N,8,false,Track)
	From: (W,10,false,Track), To: (E,10,false,Track),(N,10,false,Track)
	From: (E,1,false,Track), To: (W,1,false,Track),(N,0,false,Track)
	From: (E,3,false,Track), To: (W,3,false,Track),(N,2,false,Track)
	From: (E,5,false,Track), To: (W,5,false,Track),(N,4,false,Track)
	From: (E,7,false,Track), To: (W,7,false,Track),(N,6,false,Track)
	From: (E,9,false,Track), To: (W,9,false,Track),(N,8,false,Track)
	From: (E,11,false,Track), To: (W,11,false,Track),(N,10,false,Track)
	From: (N,1,false,Track), To: (W,1,false,Track),(E,0,false,Track)
	From: (N,3,false,Track), To: (W,3,false,Track),(E,2,false,Track)
	From: (N,5,false,Track), To: (W,5,false,Track),(E,4,false,Track)
	From: (N,7,false,Track), To: (W,7,false,Track),(E,6,false,Track)
	From: (N,9,false,Track), To: (W,9,false,Track),(E,8,false,Track)
	From: (N,11,false,Track), To: (W,11,false,Track),(E,10,false,Track)
	*/

	io.E.p0 := (io.W.p0 & io.blkBits(7)) | (io.N.p1 & io.blkBits(14))
	io.N.p0 := (io.W.p0 & io.blkBits(66)) | (io.E.p1 & io.blkBits(50))
	io.E.p2 := (io.W.p2 & io.blkBits(22)) | (io.N.p3 & io.blkBits(23))
	io.N.p2 := (io.W.p2 & io.blkBits(42)) | (io.E.p3 & io.blkBits(59))
	io.E.p4 := (io.W.p4 & io.blkBits(31)) | (io.N.p5 & io.blkBits(38))
	io.N.p4 := (io.W.p4 & io.blkBits(51)) | (io.E.p5 & io.blkBits(34))
	io.E.p6 := (io.W.p6 & io.blkBits(47)) | (io.N.p7 & io.blkBits(46))
	io.N.p6 := (io.W.p6 & io.blkBits(36)) | (io.E.p7 & io.blkBits(52))
	io.E.p8 := (io.W.p8 & io.blkBits(54)) | (io.N.p9 & io.blkBits(63))
	io.N.p8 := (io.W.p8 & io.blkBits(60)) | (io.E.p9 & io.blkBits(45))
	io.E.p10 := (io.W.p10 & io.blkBits(71)) | (io.N.p11 & io.blkBits(70))
	io.N.p10 := (io.W.p10 & io.blkBits(53)) | (io.E.p11 & io.blkBits(69))
	io.W.p1 := (io.E.p1 & io.blkBits(0)) | (io.N.p1 & io.blkBits(9))
	io.W.p3 := (io.E.p3 & io.blkBits(17)) | (io.N.p3 & io.blkBits(16))
	io.W.p5 := (io.E.p5 & io.blkBits(24)) | (io.N.p5 & io.blkBits(32))
	io.W.p7 := (io.E.p7 & io.blkBits(40)) | (io.N.p7 & io.blkBits(41))
	io.W.p9 := (io.E.p9 & io.blkBits(49)) | (io.N.p9 & io.blkBits(56))
	io.W.p11 := (io.E.p11 & io.blkBits(64)) | (io.N.p11 & io.blkBits(65))
}
// TEST CASE NOT CORRECT
class PSSBTest(c: PSSB) extends Tester(c) {
/*  poke(c.io.S.p0, 1)
  poke(c.io.S.p2, 1)
  poke(c.io.S.p4, 1)
  poke(c.io.S.p6, 1)
  poke(c.io.S.p8, 1)
  poke(c.io.S.p10, 1)
	poke(c.io.blkBits, int(UInt("h0000_0000_0000_0000_1FFF")))
	expect(c.io.E.p0, 1)
  expect(c.io.N.p0, 1)
  expect(c.io.E.p2, 1)
  expect(c.io.N.p2, 1)
  expect(c.io.E.p4, 1)
  expect(c.io.N.p4, 1)
  expect(c.io.E.p6, 1)
  expect(c.io.N.p6, 1)
  expect(c.io.E.p8, 1)
  expect(c.io.N.p8, 1)
  expect(c.io.E.p10, 1)
  expect(c.io.N.p10, 1)
	expect(c.io.S.p1, 1)
	expect(c.io.S.p3, 0)
	expect(c.io.S.p5, 0)
	expect(c.io.S.p7, 0)
	expect(c.io.S.p9, 0)
	expect(c.io.S.p11, 0) */
}

object PSSB {
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new PSSB())) {
      c => new PSSBTest(c) }
  }
}
