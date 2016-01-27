import fpga.BlockEnum._

package fpga.blocks{
  abstract class AnyBlock{
    val location : (Int,Int)
    val blockEnumeration : BlockEnum
  }
}
