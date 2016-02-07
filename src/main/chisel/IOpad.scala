package FPGASimulation

import Chisel._

class IOpad extends Module {
  val io = new Bundle {
    val outside = new IOEdge()
    val inside = new IOEdge()
    val blkBits = UInt(INPUT,72)
  }

  val dff1 = Reg(next = io.outside.p0)

  val dff2 = Reg(next = io.inside.p0)

  io.inside.p1 := dff1 & io.blkBits(0)

  io.outside.p1 := dff2 & io.blkBits(1)


}
class IOpadTests(c: IOpad) extends Tester(c) {
  poke(c.io.blkBits, int(UInt("h0000_0000_0000_0000_0001"))) // select in_outside
  poke(c.io.outside.p0, 1)
  step(1)
  expect(c.io.inside.p1, 1)
  poke(c.io.inside.p0, 1)
  step(1)
  expect(c.io.outside.p1, 0)
  poke(c.io.outside.p0, 0)
  step(1)
  expect(c.io.inside.p1, 0)

  poke(c.io.blkBits, int(UInt("h0000_0000_0000_0000_0002"))) // select in_inside
  poke(c.io.outside.p0, 1)
  step(1)
  expect(c.io.inside.p1, 0)
  poke(c.io.inside.p0, 1)
  step(1)
  expect(c.io.outside.p1, 1)
  poke(c.io.outside.p0, 1)
  step(1)
  expect(c.io.inside.p1, 0)
}
object IOpad{
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new IOpad())) {
      c => new IOpadTests(c) }
  }
}
