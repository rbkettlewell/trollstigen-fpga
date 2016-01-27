import fpga.BlockEnum._
import fpga.blocks.AnyBlock

package fpga.blocks{
  class EmptyBlock(locationXY: (Int,Int), pbEnum : BlockEnum) extends AnyBlock{
    val location = locationXY
    val blockEnumeration = pbEnum
  }
}
