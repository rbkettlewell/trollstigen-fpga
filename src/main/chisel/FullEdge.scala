package FPGASimulation

import Chisel._

class FullEdge extends Bundle {
  val p0 = UInt(INPUT, 1)
  val p1 = UInt(OUTPUT, 1)
  val p2 = UInt(INPUT, 1)
  val p3 = UInt(OUTPUT, 1)
  val p4 = UInt(INPUT, 1)
  val p5 = UInt(OUTPUT, 1)
  val p6 = UInt(INPUT, 1)
  val p7 = UInt(OUTPUT, 1)
  val p8 = UInt(INPUT, 1)
  val p9 = UInt(OUTPUT, 1)
  val p10 = UInt(INPUT, 1)
  val p11 = UInt(OUTPUT, 1)
}
