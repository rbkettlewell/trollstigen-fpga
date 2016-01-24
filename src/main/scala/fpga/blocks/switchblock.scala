import fpga.Types._

package fpga.blocks{
  class SwitchBlock(locationXY: (Int,Int), switchConnectivity: Connectivity){
    val location = locationXY
    var switches = switchConnectivity
  }
}
