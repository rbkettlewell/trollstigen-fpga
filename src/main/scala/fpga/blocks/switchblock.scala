import fpga.Types._
import fpga.BlockEnum._
import fpga.blocks.AnyBlock

package fpga.blocks{
  class SwitchBlock(locationXY: (Int,Int), pbEnum : BlockEnum, switchConnectivity: Connectivity) extends AnyBlock{
    val location = locationXY
    val blockEnumeration = pbEnum
    var switches = switchConnectivity

    def setSwitch(fromSegment : Segment , toSegment : Segment){

    }
    override def toString(): String = {
      val pbStart = "Block: " ++ blockEnumeration.toString ++ "\n"
      val allPaths = switches.map(p => ("From: " ++ p._1.toString ++ ", To: " ++ p._2.mkString(",") ++"\n"))
      pbStart ++ allPaths.mkString("") 
    }
  }
}
