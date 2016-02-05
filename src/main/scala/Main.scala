import java.io._
import fpga.blocks._
import fpga.Util._
import fpga.Bitgen

object Main {
  def main(args: Array[String]) {
    
    val DebugBlocks = true
    val DescribeRouting = true 
    
    val synthesisPath = args(0)
    val synthesisFolder = new File(synthesisPath)
    val synthesisExtensions = List(".blif",".net",".place",".route")
    val synthesisFiles = getSynthesisFiles(synthesisFolder,synthesisExtensions)

    var bitstream = new Bitgen(synthesisFiles)

    bitstream.assembleFPGA
    bitstream.placeFPGA
    bitstream.configureFPGA
    /* 
    println("My switch block data is \n"+ bitstream.fpga(11)(6).asInstanceOf[ConnectionBlock].toString)
    println("Routing Connections:\n" ++ bitstream.route.routing.mkString("\n"))
    println("Example Block programming bits:\n" ++ bitstream.fpga(2)(12).asInstanceOf[CLB].getBits)
    */

    if(DebugBlocks){
      println("**********FPGA Blocks**********")
      bitstream.prettyPrint("Detailed")
      println("*************blif**************")
      println(bitstream.blif.toString)
      println("*************place**************")
      println(bitstream.place.placement.mkString("\n"))
      println("*************net**************")
      println(bitstream.netlist.toString)
    }

    if(DescribeRouting){
      val routingWriter = new PrintWriter(new File(synthesisPath ++ "Connectivity_v2.txt")) 
      routingWriter.write(bitstream.describeRoutingBlocks)
      routingWriter.close
    }

    val bitstreamWriter = new PrintWriter(new File(synthesisPath ++ "bitstream.bin")) 
    bitstreamWriter.write(bitstream.generateBitstream)
    bitstreamWriter.close
  }
}
