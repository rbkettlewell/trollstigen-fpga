package FPGASimulation

import Chisel._

class IOpad extends Module {
  val io = new Bundle {
    val in_outside = UInt(INPUT, 1)
    val in_inside = UInt(INPUT, 1)
    val out_outside = UInt(OUTPUT, 1)
    val out_inside = UInt(OUTPUT, 1)
    val blockBits = UInt(INPUT,72)
  }

  val dff1 = Reg(next = io.in_outside)

  val dff2 = Reg(next = io.in_inside)

  io.out_inside := dff1 & io.blockBits(0)

  io.out_outside := dff2 & io.blockBits(1)


}
class IOpadTests(c: IOpad) extends Tester(c) {
  poke(c.io.blockBits, int(UInt("h0000_0000_0000_0000_0001"))) // select in_outside
  poke(c.io.in_outside, 1)
  step(1)
  expect(c.io.out_inside, 1)
  poke(c.io.in_inside, 1)
  step(1)
  expect(c.io.out_outside, 0)
  poke(c.io.in_outside, 0)
  step(1)
  expect(c.io.out_inside, 0)

  poke(c.io.blockBits, int(UInt("h0000_0000_0000_0000_0002"))) // select in_inside
  poke(c.io.in_outside, 1)
  step(1)
  expect(c.io.out_inside, 0)
  poke(c.io.in_inside, 1)
  step(1)
  expect(c.io.out_outside, 1)
  poke(c.io.in_outside, 1)
  step(1)
  expect(c.io.out_inside, 0)
}
object IOpad{
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new IOpad())) {
      c => new IOpadTests(c) }
  }
}
