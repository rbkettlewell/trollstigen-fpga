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
    var bitstream = new Bitgen(synthesisFiles)

    bitstream.assembleFPGA
    bitstream.prettyPrint("Detailed")
    println(bitstream.place.getPlacement)

    println("My switch block data is \n"+ bitstream.fpga(1)(12).asInstanceOf[ConnectionBlock].toString)
    bitstream.fpga(1)(1).asInstanceOf[SwitchBlock].setSwitch(("N",9,false),("E",10,false))
    println("My modified switch block data is \n"+ bitstream.fpga(1)(1).asInstanceOf[SwitchBlock].toString)
    bitstream.fpga(1)(1).asInstanceOf[SwitchBlock].setBits
    println("Block programming bits:\n" ++ bitstream.fpga(1)(1).asInstanceOf[SwitchBlock].getBits)
  }
}
