package FPGASimulation

import Chisel._

class ISB extends Module {
	val io = new Bundle {
		val N = new FullEdge().flip
		val S = new FullEdge()
		val E = new FullEdge().flip
		val W = new FullEdge()
		val blkBits = UInt(INPUT, 72)
	}

  /*
  From: (S,0,false,Track), To: (W,3,false,Track),(E,8,false,Track),(N,0,false,Track)
  From: (S,2,false,Track), To: (W,5,false,Track),(E,6,false,Track),(N,2,false,Track)
  From: (S,4,false,Track), To: (W,7,false,Track),(E,4,false,Track),(N,4,false,Track)
  From: (S,6,false,Track), To: (W,9,false,Track),(E,2,false,Track),(N,6,false,Track)
  From: (S,8,false,Track), To: (W,11,false,Track),(E,0,false,Track),(N,8,false,Track)
  From: (S,10,false,Track), To: (W,1,false,Track),(E,10,false,Track),(N,10,false,Track)
  From: (W,0,false,Track), To: (S,11,false,Track),(E,0,false,Track),(N,0,false,Track)
  From: (W,2,false,Track), To: (S,1,false,Track),(E,2,false,Track),(N,10,false,Track)
  From: (W,4,false,Track), To: (S,3,false,Track),(E,4,false,Track),(N,8,false,Track)
  From: (W,6,false,Track), To: (S,5,false,Track),(E,6,false,Track),(N,6,false,Track)
  From: (W,8,false,Track), To: (S,7,false,Track),(E,8,false,Track),(N,4,false,Track)
  From: (W,10,false,Track), To: (S,9,false,Track),(E,10,false,Track),(N,2,false,Track)
  From: (E,1,false,Track), To: (S,9,false,Track),(W,1,false,Track),(N,10,false,Track)
  From: (E,3,false,Track), To: (S,7,false,Track),(W,3,false,Track),(N,0,false,Track)
  From: (E,5,false,Track), To: (S,5,false,Track),(W,5,false,Track),(N,2,false,Track)
  From: (E,7,false,Track), To: (S,3,false,Track),(W,7,false,Track),(N,4,false,Track)
  From: (E,9,false,Track), To: (S,1,false,Track),(W,9,false,Track),(N,6,false,Track)
  From: (E,11,false,Track), To: (S,11,false,Track),(W,11,false,Track),(N,8,false,Track)
  From: (N,1,false,Track), To: (S,1,false,Track),(E,2,false,Track),(W,1,false,Track)
  From: (N,3,false,Track), To: (S,3,false,Track),(E,4,false,Track),(W,11,false,Track)
  From: (N,5,false,Track), To: (S,5,false,Track),(E,6,false,Track),(W,9,false,Track)
  From: (N,7,false,Track), To: (S,7,false,Track),(E,8,false,Track),(W,7,false,Track)
  From: (N,9,false,Track), To: (S,9,false,Track),(E,10,false,Track),(W,5,false,Track)
  From: (N,11,false,Track), To: (S,11,false,Track),(E,0,false,Track),(W,3,false,Track)

	See Dropbox: /development/Bitgen/Arch_8/ILB_map.xlsx for mapping below
	*/

	io.W.p3 := (io.S.p0 & io.blkBits(0)) | (io.E.p3 & io.blkBits(40)) | (io.N.p11 & io.blkBits(71))
	io.E.p8 := (io.S.p0 & io.blkBits(1)) | (io.W.p8 & io.blkBits(31)) | (io.N.p7 & io.blkBits(64))
	io.N.p0 := (io.S.p0 & io.blkBits(2)) | (io.W.p0 & io.blkBits(20)) | (io.E.p3 & io.blkBits(41))
	io.W.p5 := (io.S.p2 & io.blkBits(3)) | (io.E.p5 & io.blkBits(43)) | (io.N.p9 & io.blkBits(68))
	io.E.p6 := (io.S.p2 & io.blkBits(4)) | (io.W.p6 & io.blkBits(28)) | (io.N.p5 & io.blkBits(61))
	io.N.p2 := (io.S.p2 & io.blkBits(5)) | (io.W.p10 & io.blkBits(35)) | (io.E.p5 & io.blkBits(44))
	io.W.p7 := (io.S.p4 & io.blkBits(6)) | (io.E.p7 & io.blkBits(46)) | (io.N.p7 & io.blkBits(65))
	io.E.p4 := (io.S.p4 & io.blkBits(7)) | (io.W.p4 & io.blkBits(25)) | (io.N.p3 & io.blkBits(58))
	io.N.p4 := (io.S.p4 & io.blkBits(8)) | (io.W.p8 & io.blkBits(32)) | (io.E.p7 & io.blkBits(47))
	io.W.p9 := (io.S.p6 & io.blkBits(9)) | (io.E.p9 & io.blkBits(49)) | (io.N.p5 & io.blkBits(62))
	io.E.p2 := (io.S.p6 & io.blkBits(10)) | (io.W.p2 & io.blkBits(22)) | (io.N.p1 & io.blkBits(55))
	io.N.p6 := (io.S.p6 & io.blkBits(11)) | (io.W.p6 & io.blkBits(29)) | (io.E.p9 & io.blkBits(50))
	io.W.p11 := (io.S.p8 & io.blkBits(12)) | (io.E.p11 & io.blkBits(52)) | (io.N.p3 & io.blkBits(59))
	io.E.p0 := (io.S.p8 & io.blkBits(13)) | (io.W.p0 & io.blkBits(19)) | (io.N.p11 & io.blkBits(70))
	io.N.p8 := (io.S.p8 & io.blkBits(14)) | (io.W.p4 & io.blkBits(26)) | (io.E.p11 & io.blkBits(53))
	io.W.p1 := (io.S.p10 & io.blkBits(15)) | (io.E.p1 & io.blkBits(37)) | (io.N.p1 & io.blkBits(56))
	io.E.p10 := (io.S.p10 & io.blkBits(16)) | (io.W.p10 & io.blkBits(34)) | (io.N.p9 & io.blkBits(67))
	io.N.p10 := (io.S.p10 & io.blkBits(17)) | (io.W.p2 & io.blkBits(23)) | (io.E.p1 & io.blkBits(38))
	io.S.p11 := (io.W.p0 & io.blkBits(18)) | (io.E.p11 & io.blkBits(51)) | (io.N.p11 & io.blkBits(69))
	io.S.p1 := (io.W.p2 & io.blkBits(21)) | (io.E.p9 & io.blkBits(48)) | (io.N.p1 & io.blkBits(54))
	io.S.p3 := (io.W.p4 & io.blkBits(24)) | (io.E.p7 & io.blkBits(45)) | (io.N.p3 & io.blkBits(57))
	io.S.p5 := (io.W.p6 & io.blkBits(27)) | (io.E.p5 & io.blkBits(42)) | (io.N.p5 & io.blkBits(60))
	io.S.p7 := (io.W.p8 & io.blkBits(30)) | (io.E.p3 & io.blkBits(39)) | (io.N.p7 & io.blkBits(63))
	io.S.p9 := (io.W.p10 & io.blkBits(33)) | (io.E.p1 & io.blkBits(36)) | (io.N.p9 & io.blkBits(66))
}

class ISBTest(c: ISB) extends Tester(c) {
  poke(c.io.S.p0, 1)
  poke(c.io.S.p2, 1)
  poke(c.io.S.p4, 1)
  poke(c.io.S.p6, 1)
  poke(c.io.S.p8, 1)
  poke(c.io.S.p10, 1)
	poke(c.io.W.p0, 1)
	poke(c.io.blkBits, int(UInt("h0000_0000_0000_0007_FFFF")))
	expect(c.io.W.p3, 1)
  expect(c.io.E.p8, 1)
  expect(c.io.N.p0, 1)
  expect(c.io.W.p5, 1)
  expect(c.io.E.p6, 1)
  expect(c.io.N.p2, 1)
  expect(c.io.W.p7, 1)
  expect(c.io.E.p4, 1)
  expect(c.io.N.p4, 1)
  expect(c.io.W.p9, 1)
  expect(c.io.E.p2, 1)
  expect(c.io.N.p6, 1)
	expect(c.io.W.p11, 1)
	expect(c.io.E.p0, 1)
	expect(c.io.N.p8, 1)
	expect(c.io.W.p1, 1)
	expect(c.io.E.p10, 1)
	expect(c.io.N.p10, 1)
	expect(c.io.S.p11, 1)
	expect(c.io.S.p1, 0)
	expect(c.io.S.p3, 0)
	expect(c.io.S.p5, 0)
	expect(c.io.S.p7, 0)
	expect(c.io.S.p9, 0)
}

object ISB {
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new ISB())) {
      c => new ISBTest(c) }
  }
}
