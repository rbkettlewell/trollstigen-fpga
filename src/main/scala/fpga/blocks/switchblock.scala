import fpga.Types._
import fpga.blocks.AnyBlock

package fpga.blocks{
  class SwitchBlock(locationXY: (Int,Int), switchConnectivity: Connectivity) extends AnyBlock{
    val location = locationXY
    var switches = switchConnectivity
  }
}
