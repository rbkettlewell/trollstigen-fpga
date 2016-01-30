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
    bitstream.configureFPGA
    bitstream.prettyPrint("Detailed")
    
    println("My switch block data is \n"+ bitstream.fpga(1)(14).asInstanceOf[ConnectionBlock].toString)
    println("Block programming bits:\n" ++ bitstream.fpga(1)(14).asInstanceOf[ConnectionBlock].getBits)
    println("Routing Connections:\n" ++ bitstream.route.routing.mkString("\n"))
  }
}
