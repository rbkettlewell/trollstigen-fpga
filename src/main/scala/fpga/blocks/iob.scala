import fpga.BlockEnum._

package fpga.blocks{
  class IOB(locationXY: (Int,Int), pbEnum : BlockEnum) extends AnyBlock{
    val location = locationXY
    var name = "Unknown IOB"
    val blockEnumeration = pbEnum
    var outEnable = "0"
    var inEnable = "0"
    val BlockSize = 72
    var blockBits = Array.fill(BlockSize){"0"}

    def setBits(){
      blockBits(0) = outEnable
      blockBits(1) = inEnable
    }

    def getBits(): String ={
      var programmingBits = ""
      for(i <- 0 until BlockSize/8){
        programmingBits = programmingBits ++ "\n" ++ blockBits.slice(i*8,i*8+8).mkString("")
      }
      programmingBits
    }
    
  }
}
