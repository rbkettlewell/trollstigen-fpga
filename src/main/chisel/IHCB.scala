import Chisel._

class IHCB extends Module {
	val io = new Bundle {
		val W2 = UInt(INPUT, 1)
		val W3 = UInt(INPUT, 1)
		val W8 = UInt(INPUT, 1)
		val W9 = UInt(INPUT, 1)
		val E0 = UInt(INPUT, 1)
		val E1 = UInt(INPUT, 1)
		val E6 = UInt(INPUT, 1)
		val E7 = UInt(INPUT, 1)
		val E4 = UInt(INPUT, 1)
		val E5 = UInt(INPUT, 1)
		val E10 = UInt(INPUT, 1)
		val E11 = UInt(INPUT, 1)
		val E3 = UInt(OUTPUT, 1)
		val W1 = UInt(OUTPUT, 1)
		val W5 = UInt(OUTPUT, 1)
		val blkBits = UInt(INPUT, 72)
	}

	val inPins = Vec.fill(12) {UInt(width = 1)}
	inPins(0) := io.W2
	inPins(1) := io.W3
	inPins(2) := io.W8
	inPins(3) := io.W9
	inPins(4) := io.E0
	inPins(5) := io.E1
	inPins(6) := io.E6
	inPins(7) := io.E7
	inPins(8) := io.E4
	inPins(9) := io.E5
	inPins(10) := io.E10
	inPins(11) := io.E11

	val outPins = Vec.fill(3) {UInt(width = 1)}
	io.E3 := outPins(0)
	io.W1 := outPins(1)
	io.W5 := outPins(2)

	for (i <- 0 until 3) {
		when (io.blkBits(4*i) === UInt(1)) {
			outPins(i) := inPins(4*i)
		} .elsewhen (io.blkBits(4*i+1) === UInt(1)) {
			outPins(i) := inPins(4*i+1)
		}	.elsewhen (io.blkBits(4*i+2) === UInt(1)) {
			outPins(i) := inPins(4*i+2)
		}	.elsewhen (io.blkBits(4*i+3) === UInt(1)) {
			outPins(i) := inPins(4*i+3)
		}	.otherwise {
			outPins(i) := UInt(0)
		}
	}
}

class IHCBTest(c: IHCB) extends Tester(c) {
	poke(c.io.blkBits, 1)
	poke(c.io.W2, 1)
	poke(c.io.W3, 1)
	peek(c.io.blkBits)
	expect(c.io.E3, 1)
	expect(c.io.W1, 0)

}

object IHCB {
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new IHCB())) {
      c => new IHCBTest(c) }
  }
}
