import fpga.blocks.AnyBlock

package fpga.blocks{
  class IOBlock(locationXY: (Int,Int)) extends AnyBlock{
    val location = locationXY
    var padEnable   = false
    var inputToFPGA = false
    def configure(netlistNode: scala.xml.Node){
    }
  }
}
