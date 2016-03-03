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
      var switchFound = false
      try{
        for(i<- 0 until switches.length){
          val startPath = switches(i)
          if(startPath._1 == fromSegment){
            for(j <- 0 until startPath._2.length){
              val endSegment = startPath._2(j)
              if(endSegment == toSegment){
                switches(i)._2(j) = (endSegment._1, endSegment._2, true, endSegment._4)
                switchFound = true
              }
            }
          }
        }
        if(!switchFound)
          throw new IllegalStateException("Invalid " ++ blockEnumeration.toString ++ " connection at : " ++
            location.toString ++ " " ++ fromSegment.toString ++" -> " ++ toSegment.toString)
      }catch{
        case e: Exception => println("Exception caught: " + e);
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
      // Update SRAM bit order for switch blocks
      remapBits()
    }

    // Description: remapBits purpose is to reorder the block programming bits for all of the
    // switch type blocks in order to improve routability in layout.
    def remapBits(){

      var newBlockBits = Array.fill(BlockSize){"0"}
      var newMapping : List[Int] = List()

      // Index values obtained from Layout-Optimized Connectivity
      blockEnumeration match{
        case PWSB => {
          newMapping = List(-1, -1, -1, 27, 33, 22, 0, -1,
                            -1, -1, 25, 14, -1, 35, 24, 2,
                            -1, -1, 12, -1, 18, -1, -1, 26,
                            -1, -1, -1, 29, 31, 20, 4, -1,
                            -1, -1, 17, 16, -1, -1, 28, 6,
                            -1, -1, -1,  5,  7, 21, 30,-1,
                            -1, -1, 13, -1, 19, -1, -1, 8,
                            -1, -1,  1, 15, -1, 11, 10,32,
                            -1, -1, -1,  3,  9, 23, 34,-1)
        }
        case PSSB => {
          newMapping = List(12, -1, -1, -1, -1, -1, -1, 0,
                            -1, 24, -1, -1, -1, -1, 25, -1,
                            26, 14, -1, -1, -1, -1,  2, 27,
                            16, -1, -1, -1, -1, -1, -1,  4,
                            28, -1, 17, -1,  7, -1, 29, -1,
                            18, 30,  3, -1, -1, 21, 31,  6,
                            -1, 20, 13,  5, 19, 11,  8, -1,
                            32, -1, -1, 15,  9, -1, -1, 33,
                            22, 34,  1, -1, -1, 23, 35, 10)
        }
        case PESB => {
          newMapping = List(-1,  0, 12, 27, 33, -1, -1, -1,
                             2, 24, 25, -1, 20, 35, -1, -1,
                            26, -1, -1, 16, -1, 22, -1, -1,
                            -1,  4, 14, 29, 31, -1, -1, -1,
                            28,  6, -1, -1, 19, 18, -1, -1,
                            -1, 30, 15,  5,  7, -1, -1, -1,
                             8, -1, -1, 17, -1, 23, -1, -1,
                            32, 10,  1, -1, 21, 11, -1, -1,
                            -1, 34, 13,  3,  9, -1, -1, -1)
        }
        case PNSB => {
          newMapping = List(25,  0, 12, -1, -1, 34,  1, 13,
                             2, -1, -1, 26, 20, -1, -1,  3,
                            -1, 27, 24, 16, 30, 22, 15, -1,
                            29,  4, 14, -1, -1, 32,  5, 17,
                            -1,  6, -1, 28, -1, 18, -1,  7,
                            31, -1, -1, -1, -1, -1, -1, 19,
                             8, 33, -1, -1, -1, -1, 21,  9,
                            -1, 10, -1, -1, -1, -1, 11, -1,
                            35, -1, -1, -1, -1, -1, -1, 23)
        }
        case NWC => {
          newMapping = List(-1, -1, -1, -1, -1, 11,  4, -1,
                            -1, -1, -1,  9, -1, -1, -1,  3,
                            -1, -1, 10, -1,  7, -1, -1, -1,
                            -1, -1, -1, -1, -1,  6,  2, -1,
                            -1, -1, -1,  8, -1, -1, -1,  1,
                            -1, -1, -1, -1, -1, -1, -1, -1,
                            -1, -1, -1, -1, -1, -1, -1,  0,
                            -1, -1, -1, -1, -1, -1,  5, -1,
                            -1, -1, -1, -1, -1, -1, -1, -1)
        }
        case SWC => {
          newMapping = List(-1, -1, -1, -1, -1, -1, -1, -1,
                            -1, -1, -1, -1, -1, -1, 11, -1,
                            -1, -1, -1, -1, -1, -1, -1,  6,
                            -1, -1, -1, -1, -1, -1, -1, -1,
                            -1, -1,  3, -1, -1, -1,  7, -1,
                            -1, -1, -1, -1, -1,  5,  8, -1,
                            -1, -1,  1, -1,  4, -1, -1, -1,
                            -1, -1, -1,  2, -1, -1, -1,  9,
                            -1, -1, -1, -1, -1,  0, 10, -1)
        }
        case SEC => {
          newMapping = List(-1, -1, -1, -1, -1, -1, -1, -1,
                            -1,  6, -1, -1, -1, -1, -1, -1,
                            11, -1, -1, -1, -1, -1, -1, -1,
                            -1, -1, -1, -1, -1, -1, -1, -1,
                            10, -1, -1, -1,  3, -1, -1, -1,
                            -1,  9,  5, -1, -1, -1, -1, -1,
                            -1, -1, -1,  4, -1,  1, -1, -1,
                             8, -1, -1, -1,  2, -1, -1, -1,
                            -1,  7,  0, -1, -1, -1, -1, -1)
        }
        case NEC => {
          newMapping = List(-1,  5,  7, -1, -1, -1, -1, -1,
                             0, -1, -1, -1, 11, -1, -1, -1,
                            -1, -1, -1,  9, -1,  6, -1, -1,
                            -1,  1,  8, -1, -1, -1, -1, -1,
                            -1,  2, -1, -1, -1, 10, -1, -1,
                            -1, -1, -1, -1, -1, -1, -1, -1,
                             3, -1, -1, -1, -1, -1, -1, -1,
                            -1,  4, -1, -1, -1, -1, -1, -1,
                            -1, -1, -1, -1, -1, -1, -1, -1)
        }
        case ISB => {
          newMapping = List(37, 15, 21, 57, 66, 51, 13, 19,
                             0, 56, 54, 45, 33, 69, 70, 10,
                            71, 40, 48, 27, 39, 18, 22, 55,
                            43,  3, 24, 60, 63, 36,  7, 25,
                            68,  6, 47, 42, 29, 30, 58,  4,
                            46, 65, 35,  8, 11, 53, 61, 28,
                             9, 49, 41, 32, 50, 23, 31,  1,
                            62, 12,  2, 44, 26, 17, 16, 64,
                            52, 59, 20,  5, 14, 38, 67, 34)
        }
        case _ => newMapping = (0 until 72).toList
      }
      //println("old mapping\n")
      //println(blockBits.mkString)
      // blockBit remapping loop
      (0 until 72).foreach{i =>
        val newIndex = newMapping(i)
        if(newIndex != -1){
          newBlockBits(i) = blockBits(newIndex)
        }
      }
      // Update blockBits with new mapping
      blockBits = newBlockBits
      //println("new mapping\n")
      //println(blockBits.mkString)
    }

    def getBits(): String ={
      var programmingBits = ""
      for(i <- 0 until BlockSize/8){
        programmingBits = programmingBits ++ "\n" ++ blockBits.slice(i*8,i*8+8).mkString("")
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
