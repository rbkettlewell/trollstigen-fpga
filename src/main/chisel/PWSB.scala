package FPGASimulation

import Chisel._

class PWSB extends Module {
	val io = new Bundle {
		val N = new FullEdge().flip
		val S = new FullEdge()
		val E = new FullEdge().flip
		val blkBits = UInt(INPUT, 72)
	}

  /*
  From: (S,0,false,Track), To: (E,0,false,Track),(N,0,false,Track)
  From: (S,2,false,Track), To: (E,2,false,Track),(N,2,false,Track)
  From: (S,4,false,Track), To: (E,4,false,Track),(N,4,false,Track)
  From: (S,6,false,Track), To: (E,6,false,Track),(N,6,false,Track)
  From: (S,8,false,Track), To: (E,8,false,Track),(N,8,false,Track)
  From: (S,10,false,Track), To: (E,10,false,Track),(N,10,false,Track)
  From: (E,1,false,Track), To: (S,1,false,Track),(N,0,false,Track)
  From: (E,3,false,Track), To: (S,3,false,Track),(N,2,false,Track)
  From: (E,5,false,Track), To: (S,5,false,Track),(N,4,false,Track)
  From: (E,7,false,Track), To: (S,7,false,Track),(N,6,false,Track)
  From: (E,9,false,Track), To: (S,9,false,Track),(N,8,false,Track)
  From: (E,11,false,Track), To: (S,11,false,Track),(N,10,false,Track)
  From: (N,1,false,Track), To: (E,0,false,Track),(S,1,false,Track)
  From: (N,3,false,Track), To: (E,2,false,Track),(S,3,false,Track)
  From: (N,5,false,Track), To: (E,4,false,Track),(S,5,false,Track)
  From: (N,7,false,Track), To: (E,6,false,Track),(S,7,false,Track)
  From: (N,9,false,Track), To: (E,8,false,Track),(S,9,false,Track)
  From: (N,11,false,Track), To: (E,10,false,Track),(S,11,false,Track)
	*/

	io.E.p0 := (io.S.p0 & io.blkBits(6)) | (io.N.p1 & io.blkBits(14))
	io.N.p0 := (io.S.p0 & io.blkBits(58)) | (io.E.p1 & io.blkBits(50))
	io.E.p2 := (io.S.p2 & io.blkBits(15)) | (io.N.p3 & io.blkBits(23))
	io.N.p2 := (io.S.p2 & io.blkBits(67)) | (io.E.p3 & io.blkBits(59))
	io.E.p4 := (io.S.p4 & io.blkBits(30)) | (io.N.p5 & io.blkBits(38))
	io.N.p4 := (io.S.p4 & io.blkBits(43)) | (io.E.p5 & io.blkBits(34))
	io.E.p6 := (io.S.p6 & io.blkBits(39)) | (io.N.p7 & io.blkBits(46))
	io.N.p6 := (io.S.p6 & io.blkBits(44)) | (io.E.p7 & io.blkBits(52))
	io.E.p8 := (io.S.p8 & io.blkBits(55)) | (io.N.p9 & io.blkBits(63))
	io.N.p8 := (io.S.p8 & io.blkBits(68)) | (io.E.p9 & io.blkBits(45))
	io.E.p10 := (io.S.p10 & io.blkBits(62)) | (io.N.p11 & io.blkBits(70))
	io.N.p10 := (io.S.p10 & io.blkBits(61)) | (io.E.p11 & io.blkBits(69))
	io.S.p1 := (io.E.p1 & io.blkBits(18)) | (io.N.p1 & io.blkBits(10))
	io.S.p3 := (io.E.p3 & io.blkBits(11)) | (io.N.p3 & io.blkBits(3))
	io.S.p5 := (io.E.p5 & io.blkBits(35)) | (io.N.p5 & io.blkBits(27))
	io.S.p7 := (io.E.p7 & io.blkBits(20)) | (io.N.p7 & io.blkBits(28))
	io.S.p9 := (io.E.p9 & io.blkBits(29)) | (io.N.p9 & io.blkBits(4))
	io.S.p11 := (io.E.p11 & io.blkBits(5)) | (io.N.p11 & io.blkBits(13))
}

class PWSBTest(c: PWSB) extends Tester(c) {
  poke(c.io.S.p0, 1)
  poke(c.io.S.p2, 1)
  poke(c.io.S.p4, 1)
  poke(c.io.S.p6, 1)
  poke(c.io.S.p8, 1)
  poke(c.io.S.p10, 1)
	poke(c.io.E.p1, 1)
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
}

object PWSB {
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new PWSB())) {
      c => new PWSBTest(c) }
  }
}
