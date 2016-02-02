import fpga.BlockEnum._

package fpga.blocks{
  class CLB(locationXY: (Int,Int), pbEnum : BlockEnum) extends AnyBlock{
    val location = locationXY
    var name = "Unknown CLB"
    val blockEnumeration = pbEnum
    var inputsEnable = Array.fill[Boolean](6)(false)
    var outputEnable = false
    var clockEnable  = false
    var muxSelect    = false       // true will select DFF, false defaults to LUT6 output
    var dFFResetEnable  = false
    var dFFResetValue   = false
    var lutSRAMBits     = Array.fill[Boolean](64)(false)

  }
}
