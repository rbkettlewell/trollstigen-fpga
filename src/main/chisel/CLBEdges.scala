package FPGASimulation

import Chisel._

class CLBEast extends Bundle {
  val p1 = UInt(INPUT, 1)
  val p5 = UInt(INPUT, 1)
}

class CLBWest extends Bundle {
  val p3 = UInt(INPUT, 1)
}

class CLBNorth extends Bundle {
  val p0 = UInt(INPUT, 1)
  val p4 = UInt(INPUT, 1)
}

class CLBSouth extends Bundle {
  val p2 = UInt(INPUT, 1)
  val p6 = UInt(OUTPUT, 1)
}

class IOEdge extends Bundle {
  val p0 = UInt(INPUT, 1)
  val p1 = UInt(OUTPUT, 1)
}
