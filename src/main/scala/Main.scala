import fpga.Bitgen
import fpga.blocks._

object Main {
  def main(args: Array[String]) {
    val bitstream = new Bitgen
    bitstream.assembleFPGA
    bitstream.prettyPrint("Detailed")

    // println("My switch block data is "+ bitstream.fpga(1)(1).asInstanceOf[SwitchBlock].switches)
  }
}
