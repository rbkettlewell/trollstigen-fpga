import Chisel._

class IHCB extends Module {
	val io = new Bundle {
		val W1 = UInt(OUTPUT)
		val W2 = UInt(INPUT)
		val W3 = UInt(INPUT)
		val W5 = UInt(OUTPUT)
		val W8 = UInt(INPUT)
		val W9 = UInt(INPUT)
		val E0 = UInt(INPUT)
		val E1 = UInt(INPUT)
		val E3 = UInt(OUTPUT)
		val E4 = UInt(INPUT)
		val E5 = UInt(INPUT)
		val E6 = UInt(INPUT)
		val E7 = UInt(INPUT)
		val E10 = UInt(INPUT)
		val E11 = UInt(INPUT)
		val blkBits = (INPUT, 72)
	}

	val inPins = Vec.fill(12) {UInt()}
	inPins(1) := io.W2
	inPins(2) := io.W3
	inPins(3) := io.W8
	inPins(4) := io.W9
	inPins(5) := io.E0
	inPins(6) := io.E1
	inPins(7) := io.E6
	inPins(8) := io.E7
	inPins(9) := io.E4
	inPins(10) := io.E5
	inPins(11) := io.E10
	inPins(12) := io.E11

	val outPins = Vec.fill(3) {UInt()}
	outPins(1) := io.E3
	outPins(2) := io.W1
	outPins(3) := io.W5

	val curBlk := UInt(0)

	for (i <- 0 until 2) {
		when (curBlk := io.blkBits(4*i+1) === UInt(1)) {
			outPins(i+1) := inPins(4*i+1)
		} .elsewhen (curBlk := io.blkBits(4*i+2) === UInt(1)) {
			outPins(i+1) := inPins(4*i+2)
		}	.elsewhen (curBlk := io.blkBits(4*i+3) === UInt(1)) {
			outPins(i+1) := inPins(4*i+3)
		}	.elsewhen (curBlk := io.blkBits(4*i+4) === UInt(1)) {
			outPins(i+1) := inPins(4*i+4)
		}	.otherwise {
			outPins(i+1) := UInt(0)
		}
	}
}

class IHCBTest(c: IHCB) extends Tester(c) {
	poke(c.io.blkBits, UInt(1))
	poke(c.io.W2, 1)
	peek(c.io.blkBits)
	expect(c.io.E3, 1)
}
