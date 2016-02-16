package FPGASimulation

import Chisel._

class PESB extends Module {
	val io = new Bundle {
		val W = new FullEdge()
		val S = new FullEdge()
		val N = new FullEdge().flip
		val blkBits = UInt(INPUT, 72)
	}

  /*
	From: (S,0,false,Track), To: (W,1,false,Track),(N,0,false,Track)
From: (S,2,false,Track), To: (W,3,false,Track),(N,2,false,Track)
From: (S,4,false,Track), To: (W,5,false,Track),(N,4,false,Track)
From: (S,6,false,Track), To: (W,7,false,Track),(N,6,false,Track)
From: (S,8,false,Track), To: (W,9,false,Track),(N,8,false,Track)
From: (S,10,false,Track), To: (W,11,false,Track),(N,10,false,Track)
From: (W,0,false,Track), To: (S,1,false,Track),(N,0,false,Track)
From: (W,2,false,Track), To: (S,3,false,Track),(N,2,false,Track)
From: (W,4,false,Track), To: (S,5,false,Track),(N,4,false,Track)
From: (W,6,false,Track), To: (S,7,false,Track),(N,6,false,Track)
From: (W,8,false,Track), To: (S,9,false,Track),(N,8,false,Track)
From: (W,10,false,Track), To: (S,11,false,Track),(N,10,false,Track)
From: (N,1,false,Track), To: (W,1,false,Track),(S,1,false,Track)
From: (N,3,false,Track), To: (W,3,false,Track),(S,3,false,Track)
From: (N,5,false,Track), To: (W,5,false,Track),(S,5,false,Track)
From: (N,7,false,Track), To: (W,7,false,Track),(S,7,false,Track)
From: (N,9,false,Track), To: (W,9,false,Track),(S,9,false,Track)
From: (N,11,false,Track), To: (W,11,false,Track),(S,11,false,Track)
	*/
	io.W.p1 := (io.S.p0 & io.blkBits(0)) | (io.N.p1 & io.blkBits(24))
	io.N.p0 := (io.S.p0 & io.blkBits(1)) | (io.W.p0 & io.blkBits(13))
	io.W.p3 := (io.S.p2 & io.blkBits(2)) | (io.N.p3 & io.blkBits(26))
	io.N.p2 := (io.S.p2 & io.blkBits(3)) | (io.W.p2 & io.blkBits(15))
	io.W.p5 := (io.S.p4 & io.blkBits(4)) | (io.N.p5 & io.blkBits(28))
	io.N.p4 := (io.S.p4 & io.blkBits(5)) | (io.W.p4 & io.blkBits(17))
	io.W.p7 := (io.S.p6 & io.blkBits(6)) | (io.N.p7 & io.blkBits(30))
	io.N.p6 := (io.S.p6 & io.blkBits(7)) | (io.W.p6 & io.blkBits(19))
	io.W.p9 := (io.S.p8 & io.blkBits(8)) | (io.N.p9 & io.blkBits(32))
	io.N.p8 := (io.S.p8 & io.blkBits(9)) | (io.W.p8 & io.blkBits(21))
	io.W.p11 := (io.S.p10 & io.blkBits(10)) | (io.N.p11 & io.blkBits(34))
	io.N.p10 := (io.S.p10 & io.blkBits(11)) | (io.W.p10 & io.blkBits(23))
	io.S.p1 := (io.W.p0 & io.blkBits(12)) | (io.N.p1 & io.blkBits(25))
	io.S.p3 := (io.W.p2 & io.blkBits(14)) | (io.N.p3 & io.blkBits(27))
	io.S.p5 := (io.W.p4 & io.blkBits(16)) | (io.N.p5 & io.blkBits(29))
	io.S.p7 := (io.W.p6 & io.blkBits(18)) | (io.N.p7 & io.blkBits(31))
	io.S.p9 := (io.W.p8 & io.blkBits(20)) | (io.N.p9 & io.blkBits(33))
	io.S.p11 := (io.W.p10 & io.blkBits(22)) | (io.N.p11 & io.blkBits(35))






}
//the test case isn't correct
class PESBTest(c: PESB) extends Tester(c) {
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
	expect(c.io.S.p11, 0)
	*/
}

object PESB {
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new PESB())) {
      c => new PESBTest(c) }
  }
}
