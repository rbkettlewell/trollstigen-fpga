package FPGASimulation

import Chisel._

class TopLevel extends Module{
  val io = new Bundle{
    val ipad0 = UInt(INPUT, 1)
    val opad1 = UInt(OUTPUT, 1)
    val sram0 = UInt(INPUT, 72)
    val sram1 = UInt(INPUT, 72)
  }

  //make all the blocks traversing from bottom left, going right then up
  //note: IOpads also numbered the same way (e.g. pad @ (0.2) is index 8 in IOpads Vector)
  val IOpads = Vec.fill(48) {Module(new IOpad()).io}
  val SWswitch = Module(new SWC())
  val PSCBs = Vec.fill(8)   {Module(new PSCB()).io}
  val PSSBs = Vec.fill(7)   {Module(new PSSB()).io}
  val SEswitch = Module(new SEC())
  val PWCBs = Vec.fill(16)  {Module(new PWCB()).io}
  val CLBs  = Vec.fill(128) {Module(new CLB()).io}
  val IHCBs = Vec.fill(112) {Module(new IHCB()).io}
  val PECBs = Vec.fill(16)  {Module(new PECB()).io}
  val PWSBs = Vec.fill(15)  {Module(new PWSB()).io}
  val IVCBs = Vec.fill(120) {Module(new IVCB()).io}
  val ISBs  = Vec.fill(105) {Module(new ISB()).io}
  val PESBs = Vec.fill(15)  {Module(new PESB()).io}
  val NWswitch = Module(new NWC())
  val PNCBs = Vec.fill(16)  {Module(new PNCB()).io}
  val PNSBs = Vec.fill(15)  {Module(new PNSB()).io}
  val NEswitch = Module(new NEC())

  //indexes of blocks will be in form (i,j), where we traverse right
  //then up the FPGA to access the Vecs.

  //loop through to connect CLBs to IHCBs, except last col. of CLBs
  //note indexes i,j do not correspond exactly to position in FPGA.
  for (j <- 0 until 16) {
    for (i <-0 until 7) {
      CLBs(i+8*j).E <> IHCBs(i+7*j).W
      IHCBs(i+7*j).E <> CLBs(i+8*j+1).W
    }
    //connect PWCBs, PECBs, and left/right IOpads to above
    IOpads(2*j+8).inside <> PWCBs(j).W
    PWCBs(j).E <> CLBs(8*j).W
    CLBs(8*j+7).E <> PECBs(j).W
    PECBs(j).E <> IOpads(2*j+9).inside
  }

  //loop through to connect ISBs to IVCBs
  for (j <- 0 until 15) {
    for (i <- 0 until 7) {
      IVCBs(i+8*j).E <> ISBs(i+7*j).W
      ISBs(i+7*j).E <> IVCBs(i+8*j+1).W
    }
    //connect PWSBs, PESBs
    PWSBs(j).E <> IVCBs(8*j).W
    IVCBs(8*j+7).E <> PESBs(j).W
  }

  //connect bottom row of PSCBs <> PSSBs <> IOpads and top row of PNCBs <> PNSBs <> IOpads
  for (i <- 0 until 8) {
    IOpads(i) <> PSCBs(i).S
    IOpads(i+40) <> PNCBs(i).N
  }
  for (i <- 0 until 7) {
    PSCBs(i).E <> PSSBs(i).W
    PSSBs(i).E <> PSCBs(i+1).W
  }
  //finally connect corner switch blocks
  SWswitch.io.E <> PSCBs(0).W
  SWswitch.io.N <> PWCBs(0).S
  SEswitch.io.W <> PSCBs(7).E
  SEswitch.io.N <> PECBs(0).S
  NWswitch.io.S <> PWCBs(15).N
  NWswitch.io.E <> PNCBs(0).W
  NEswitch.io.S <> PECBs(15).N
  NEswitch.io.W <> PNCBs(7).E

  //below code works with tester
  val IHCB1 = Module(new IHCB())
  val CLB1 = Module(new CLB())
  CLB1.io.E <> IHCB1.io.W
  io.sram0 <> IHCB1.io.blkBits
  io.sram1 <> CLB1.io.blkBits
  IHCB1.io.S.p0 := io.ipad0
  io.opad1 := CLB1.io.S.p6
}

class TopLevelTest(c: TopLevel) extends Tester(c) {
  poke(c.io.sram0, int(UInt("h0000_0000_0000_0000_0010")))
  poke(c.io.sram1, int(UInt("h0003_0000_0000_0000_0006")))
  poke(c.io.ipad0, 0)
  expect(c.io.opad1, 0)
  step(1)
  poke(c.io.ipad0, 1)
  expect(c.io.opad1, 1)
}

object TopLevel {
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new TopLevel())) {
      c => new TopLevelTest(c) }
  }
}
