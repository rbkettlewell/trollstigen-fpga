import Chisel._

package SimTroll{

  class CLB extends Module {
    val io = new Bundle {
      val sel = UInt(INPUT, 6)
      val blockBits = UInt(INPUT,72)
      val out = UInt(OUTPUT,1)
    }

    val muxSel = io.blockBits(70) === UInt(1)

    val dffResetVal = io.blockBits(71)
    val dff = Reg(init=dffResetVal)

    val lutEnabledSel = io.sel & Cat((64 to 69).map(i => UInt(io.blockBits(i))).reverse)
    val lut  = MuxLookup(lutEnabledSel, UInt(0), (0 until 64).map(i=> UInt(i) -> io.blockBits(i)))

    dff := lut
    io.out := Mux(muxSel,dff,lut)
  }
  class CLBTests(c: CLB) extends Tester(c) {
    poke(c.io.blockBits, int(UInt("h0003_0000_0000_0000_0006")))
    poke(c.io.sel, int(UInt("b01")))
    step(1)
    expect(c.io.out, 1)
    poke(c.io.sel, int(UInt("b10")))
    step(1)
    expect(c.io.out, 1)
    poke(c.io.sel, 8)
    step(1)
    expect(c.io.out, 0)
  }
  object CLB{
    def main(args: Array[String]): Unit = {
      val tutArgs = args.slice(1, args.length)
      chiselMainTest(tutArgs, () => Module(new CLB())) {
        c => new CLBTests(c) }
    }
  }
}
