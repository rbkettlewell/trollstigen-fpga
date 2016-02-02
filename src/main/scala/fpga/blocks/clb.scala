import fpga.Types._
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

    def toBinary(i : Int, leftPad : String, wordSize : Int): String = {
      (List.fill(wordSize)(leftPad).mkString ++ "%s").format(i.toBinaryString).takeRight(wordSize)
    }

    def fromBinary(binary : String): Int ={
      var i = 0
      var sum = 0
      binary.foreach{c=>
        sum = sum + Math.pow(2,i).toInt * c.toString.toInt
        i += 1
      }
      sum
    }

    def configureLUT6(cover : Covering): Bits={
      var lut = createLUT6()
      var lutBits = Array.fill(64){"0"}
      var bitPositions : Array[Int] = Array()
      cover.foreach{ c=>
        bitPositions = bitPositions ++ (recursiveCover(lut,0,c).map(fromBinary))  }

      bitPositions.foreach{p => lutBits(p) = "1"}
      lutBits
    }

    def createLUT6(): Array[String]={
      Range(0,64).map(toBinary(_,"0",6).reverse).toArray
    }
    
    def recursiveCover(a:Array[String],i: Int, c:String): Array[String]={
      c match {
        case "" => a 
        case _ => {
          if (c.head == '-')
            recursiveCover(a, i + 1, c.tail)
          else
            recursiveCover(a.filter(_(i) == c.head), i + 1, c.tail)
        }
      }
    }
  }
}
