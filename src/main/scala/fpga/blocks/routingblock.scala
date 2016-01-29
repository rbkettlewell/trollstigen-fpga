import fpga.Types._
import fpga.BlockEnum._
import fpga.blocks.AnyBlock

package fpga.blocks{
  class RoutingBlock(locationXY: (Int,Int), pbEnum : BlockEnum, switchConnectivity: Connectivity) extends AnyBlock{
    val location = locationXY
    val blockEnumeration = pbEnum
    var switches = switchConnectivity
    val BlockSize = 72
    var blockBits = Array.fill(BlockSize){"0"}

    def setSwitch(fromSegment : Segment , toSegment : Segment){
      for(i<- 0 until switches.length){
        val startPath = switches(i)
        if(startPath._1 == fromSegment){
          for(j <- 0 until startPath._2.length){
            val endSegment = startPath._2(j)
            if(endSegment == toSegment){
              switches(i)._2(j) = (endSegment._1, endSegment._2, true)
            }
          }
        }
      }
    }

    def setBits(){
      var bitIndex = 0
      switches.foreach{path => 
        path._2.foreach{segment =>
          if(segment._3){
            blockBits(bitIndex) = "1"
          }
          bitIndex += 1
        }
      }
    }

    def getBits(): String ={
      var programmingBits = ""
      for(i <- 0 until BlockSize/8){
        programmingBits = programmingBits ++ "\n" ++ blockBits.slice(i*8,i*8+7).mkString("")
      }
      programmingBits
    }

    override def toString(): String = {
      val pbStart = "Block: " ++ blockEnumeration.toString ++ "\nLocation:" ++ location.toString ++ "\n"
      val allPaths = switches.map(p => ("From: " ++ p._1.toString ++ ", To: " ++ p._2.mkString(",") ++"\n"))
      pbStart ++ allPaths.mkString("") 
    }
  }
}
