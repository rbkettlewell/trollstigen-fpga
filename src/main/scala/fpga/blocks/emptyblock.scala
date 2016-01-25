import fpga.blocks.AnyBlock

package fpga.blocks{
  class EmptyBlock(locationXY: (Int,Int)) extends AnyBlock{
    val location = locationXY
  }
}
