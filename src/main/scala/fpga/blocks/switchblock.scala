import fpga.Types._
import fpga.BlockEnum._
import fpga.blocks.AnyBlock

package fpga.blocks{
  class SwitchBlock(locationXY: (Int,Int), pbEnum : BlockEnum, switchConnectivity: Connectivity) extends AnyBlock{
    val location = locationXY
    val blockEnumeration = pbEnum
    var switches = switchConnectivity
  }
}
