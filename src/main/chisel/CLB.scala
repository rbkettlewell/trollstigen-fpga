package FPGASimulation

import Chisel._

class CLB extends Module {
  val io = new Bundle {
    val N = new CLBNorth()
    val S = new CLBSouth()
    val E = new CLBEast()
    val W = new CLBWest()
    val blkBits = UInt(INPUT,72)
  }

  val sel = Cat(io.E.p5, io.N.p4, io.W.p3, io.S.p2, io.E.p1, io.N.p0)
  val muxSel = io.blkBits(70) === UInt(1)

  val dffResetVal = io.blkBits(71)
  val dff = Reg(init=dffResetVal)

  val lutEnabledSel = sel & Cat((64 to 69).map(i => ~UInt(io.blkBits(i))).reverse)
  val lut  = MuxLookup(lutEnabledSel, UInt(0), (0 until 64).map(i=> UInt(i) -> io.blkBits(i)))

  dff := lut
  io.S.p6 := Mux(muxSel,dff,lut)
}
class CLBTests(c: CLB) extends Tester(c) {
  poke(c.io.blkBits, int(UInt("h0003_0000_0000_0000_0006")))
  def setLutInput(setSel : Array[Int]){
    poke(c.io.E.p5, setSel(5))
    poke(c.io.N.p4, setSel(4))
    poke(c.io.W.p3, setSel(3))
    poke(c.io.S.p2, setSel(2))
    poke(c.io.E.p1, setSel(1))
    poke(c.io.N.p0, setSel(0))
  }
  setLutInput(Array(0,0,0,0,0,1).reverse)
  //poke(sel, int(UInt("b01")))
  step(1)
  expect(c.io.S.p6, 1)
  setLutInput(Array(0,0,0,0,1,0).reverse)
  //poke(sel, int(UInt("b10")))
  step(1)
  expect(c.io.S.p6, 1)
  setLutInput(Array(0,0,0,0,1,1).reverse)
  //poke(sel, 8)
  step(1)
  expect(c.io.S.p6, 0)
}
object CLB{
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new CLB())) {
      c => new CLBTests(c) }
  }
}
