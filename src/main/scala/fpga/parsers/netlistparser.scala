import scala.xml._
import fpga.Types._

package fpga.parsers{
  class NetlistParser(netlistFile: String){
    val rawNetlist = parse(netlistFile)
    
    override def toString(): String ={
      var netlistString = ""
      val allNets = rawNetlist.map (b=> b._1 ++ " " ++  b._2 ++ b._3.mkString(", ") ++ ", " ++ b._4.toString)
      netlistString ++ allNets.mkString("\n")
    }

    def parse(filename : String): RawNetlist = {
      var rawBlocks : RawNetlist = Array()
      val loadBlocks= xml.XML.loadFile(netlistFile) \ "block"

      loadBlocks.foreach{ b =>
        val blockType = b \ "@mode" text
        val name = b \ "@name" text
        val inputs = (b \ "inputs" text) split " " 
        val sequential = (b \ "outputs" text) contains "dff"
        rawBlocks = rawBlocks ++ Array((name, blockType, inputs,sequential))
      }
      rawBlocks
    }
  }
}
