import Chisel._

class IVCB extends Module {
	val io = new Bundle {
    val S2 = UInt(INPUT, 1)
		val S3 = UInt(INPUT, 1)
		val S8 = UInt(INPUT, 1)
		val S9 = UInt(INPUT, 1)
		val N6 = UInt(INPUT, 1)
		val N0 = UInt(INPUT, 1)
		val N1 = UInt(INPUT, 1)
		val N6 = UInt(INPUT, 1)
		val N7 = UInt(INPUT, 1)
		val N4 = UInt(INPUT, 1)
		val N5 = UInt(INPUT, 1)
		val N10 = UInt(INPUT, 1)
    val N11 = UInt(INPUT, 1)
		val N2 = UInt(OUTPUT, 1)
		val S0 = UInt(OUTPUT, 1)
    val S1 = UInt(OUTPUT, 1)
    val S2 = UInt(OUTPUT, 1)
    val S3 = UInt(OUTPUT, 1)
    val S4 = UInt(OUTPUT, 1)
		val blkBits = UInt(INPUT, 72)
	}

	val inPins = Vec.fill(13) {UInt(width = 1)}
	inPins(0) := io.S2
	inPins(1) := io.S3
	inPins(2) := io.S8
	inPins(3) := io.S9
  inPins(4) := io.N6
	inPins(5) := io.N0
	inPins(6) := io.N1
	inPins(7) := io.N6
	inPins(8) := io.N7
	inPins(9) := io.N4
	inPins(10) := io.N5
	inPins(11) := io.N10
	inPins(12) := io.N11

	val outPins = Vec.fill(5) {UInt(width = 1)}
	io.S0 := outPins(0)
	io.S1 := outPins(1)
	io.S2 := outPins(2)
  io.S3 := outPins(3)
  io.S4 := outPins(4)

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

class IVCBTest(c: IVCB) extends Tester(c) {
	poke(c.io.blkBits, 1)
	poke(c.io.W2, 1)
	poke(c.io.W3, 1)
	peek(c.io.blkBits)
	expect(c.io.E3, 1)
	expect(c.io.W1, 0)

}

object IVCB {
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new IVCB())) {
      c => new IVCBTest(c) }
  }
}
