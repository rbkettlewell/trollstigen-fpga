import java.io.File 
import fpga.blocks._
import fpga.Util._
import fpga.Bitgen

object Main {
  def main(args: Array[String]) {
    
    //val synthesisFolder = new File("/home/bkettlew/Projects/trollstigen-fpga/src/main/resources/PARFiles/")
    val synthesisFolder = new File(args(0))
    val synthesisExtensions = List(".blif",".net",".place",".route")
    val synthesisFiles = getSynthesisFiles(synthesisFolder,synthesisExtensions)
    val bitstream = new Bitgen(synthesisFiles)

    bitstream.assembleFPGA
    bitstream.prettyPrint("Detailed")
    println(bitstream.place.getPlacement)

    println("My switch block data is \n"+ bitstream.fpga(1)(1).asInstanceOf[SwitchBlock].toString)
  }
}
