import fpga.BlockEnum._

package fpga.blocks{
  class EmptyBlock(locationXY: (Int,Int), pbEnum : BlockEnum) extends AnyBlock{
    val location = locationXY
    val blockEnumeration = pbEnum
    val name = "Empty Block"
    val BlockSize = 72
    var blockBits = Array.fill(BlockSize){"0"}

    def getBits(): String ={
      var programmingBits = ""
      for(i <- 0 until BlockSize/8){
        programmingBits = programmingBits ++ "\n" ++ blockBits.slice(i*8,i*8+8).mkString("")
      }
      programmingBits
    }
  }
}
