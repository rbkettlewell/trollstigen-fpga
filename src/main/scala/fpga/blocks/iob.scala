import fpga.BlockEnum._
import fpga.blocks.AnyBlock

package fpga.blocks{
  class IOB(locationXY: (Int,Int), pbEnum : BlockEnum) extends AnyBlock{
    val location = locationXY
    val blockEnumeration = pbEnum
    var padEnable   = false
    var inputToFPGA = false
    def configure(netlistNode: scala.xml.Node){
    }
  }
}
