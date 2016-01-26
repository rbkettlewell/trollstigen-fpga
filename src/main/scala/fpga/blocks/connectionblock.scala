import fpga.Types._
import fpga.blocks.AnyBlock

package fpga.blocks{
  class ConnectionBlock(locationXY: (Int,Int), cBlockConnectivity: Connectivity) extends AnyBlock{
    val location = locationXY
    var switches = cBlockConnectivity
  }
}
