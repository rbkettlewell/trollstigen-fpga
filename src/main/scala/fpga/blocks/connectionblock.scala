import fpga.Types._
import fpga.BlockEnum._
import fpga.blocks.AnyBlock

package fpga.blocks{
  class ConnectionBlock(locationXY: (Int,Int), pbEnum : BlockEnum, cBlockConnectivity : Connectivity) extends AnyBlock{
    val location = locationXY
    val blockEnumeration = pbEnum
    var switches = cBlockConnectivity
  }
}
