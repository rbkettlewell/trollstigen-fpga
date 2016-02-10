package FPGASimulation

import Chisel._

class BlockDecoder extends Module {
  val io = new Bundle {
    val bAddr = UInt (INPUT, 4)
    val bRow = UInt(OUTPUT,9)
  }

  when (io.bAddr === UInt(0)){
    io.bRow := UInt("b0_0000_0001")
  }.elsewhen (io.bAddr === UInt(1)){
    io.bRow := UInt("b0_0000_0010")
  }.elsewhen (io.bAddr === UInt(2)){
    io.bRow := UInt("b0_0000_0100")
  }.elsewhen (io.bAddr === UInt(3)){
    io.bRow := UInt("b0_0000_1000")
  }.elsewhen (io.bAddr === UInt(4)){
    io.bRow := UInt("b0_0001_0000")
  }.elsewhen (io.bAddr === UInt(5)){
    io.bRow := UInt("b0_0010_0000")
  }.elsewhen (io.bAddr === UInt(6)){
    io.bRow := UInt("b0_0100_0000")
  }.elsewhen (io.bAddr === UInt(7)){
    io.bRow := UInt("b0_1000_0000")
  }.elsewhen (io.bAddr === UInt(8)){
    io.bRow := UInt("b1_0000_0000")
  }.otherwise{ 
    io.bRow := UInt("b0_0000_0000")
  }

}
class BlockDecoderTests(c: BlockDecoder) extends Tester(c) {
  (0 until 9).foreach{i=>
    poke(c.io.bAddr, i)
    step(1)
    expect(c.io.bRow, Math.pow(2,i).toInt)
  }
}
object BlockDecoder{
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new BlockDecoder())) {
      c => new BlockDecoderTests(c) }
  }
}
