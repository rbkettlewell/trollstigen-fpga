import fpga.Bitgen
import fpga.blocks._

object Main {
  def main(args: Array[String]) {
    val bitstream = new Bitgen
    bitstream.assembleFPGA()
    println("My switch block data is "+ bitstream.fpga(0)(1).asInstanceOf[SwitchBlock].switches)
  }
}
