package FPGASimulation

import Chisel._

class BlkEdges extends Bundle {
  val N = new PortEdge().flip()
  val S = new PortEdge()
  val E = new PortEdge().flip()
  val W = new PortEdge()
}
