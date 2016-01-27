import fpga.blocks.AnyBlock
import fpga.BlockEnum._

package fpga.blocks{
  class EmptyBlock(locationXY: (Int,Int), pbEnum : BlockEnum) extends AnyBlock{
    val location = locationXY
    val blockEnumeration = pbEnum
  }
}
