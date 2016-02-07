package FPGASimulation

import Chisel._

class Empty extends Module {
	val io = new Bundle {
		val blkBits = UInt(INPUT, 72)
	}
}
