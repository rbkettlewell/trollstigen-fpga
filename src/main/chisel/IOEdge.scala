package FPGASimulation
import Chisel._

class IOEdge extends Bundle {
  val p0 = UInt(INPUT, 1)
  val p1 = UInt(OUTPUT, 1)
}
