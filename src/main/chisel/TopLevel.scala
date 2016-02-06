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
    }
  }
  //connect PWCBs, last col of CLBs, and PECBs to above
  for (i <- 0 until 16) {
    PWCBs(i).E <> CLBs(8*i).W
    IHCBs(7*i+6).E <> CLBs(8*i+7).W
    CLBs(8*i+7).E <> PECBs(i).W
  }
  //loop through to connect ISBs to IVCBs
  for (j <- 0 until 15) {
    for (i <- 0 until 7) {
      IVCBs(i+8*j).E <> ISBs(i+7*j).W
    }
  }
  //connect PWSBs, last col of IVCBs, and PESBs to above
  for (i <- 0 until 15) {
    PWSBs(i).E <> IVCBs(8*i).W
    ISBs(7*i+6).E <> IVCBs(8*i+7).W
    IVCBs(8*i+7).E <> PESBs(i).W
  }

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
