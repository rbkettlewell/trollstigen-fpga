import fpga.Types._
import fpga.BlockEnum._


package fpga.blocks{
  class CLB(locationXY: (Int,Int), pbEnum : BlockEnum) extends AnyBlock{
    val location = locationXY
    var name = "Unknown CLB"
    val blockEnumeration = pbEnum
    var inputsEnable = Array.fill[String](6)("0")
    var muxSelect    = "0" // "1" will select DFF, false defaults to LUT6 output
    var dFFResetValue   = "0"
    var lutSRAMBits     = Array.fill[String](64)("0")
    val BlockSize = 72
    var blockBits = Array.fill(BlockSize){"0"}

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

    def setInputs(lutInputs : Array[String]){
      inputsEnable = lutInputs.map{in =>
        if (in != "open")
          "0" // Inverting for schematic SRAM output inversion for select lines
        else
          "1"
      }
    }

    def configureLUT6(mintermCover : Boolean, cover : Covering){
      var lut = createLUT6()
      var fillBits = "1"
      var setTerm = "0"
      if (mintermCover){
        fillBits = "0"
        setTerm = "1"
      }
        
      var lutBits = Array.fill(64){fillBits}
      var bitPositions : Array[Int] = Array()
      cover.foreach{ c=>
        bitPositions = bitPositions ++ (recursiveCover(lut,0,c).map(fromBinary))  }

      bitPositions.foreach{p => lutBits(p) = setTerm}
      lutSRAMBits = lutBits
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

    def setBits(){
      blockBits = lutSRAMBits ++ inputsEnable ++ Array(muxSelect, dFFResetValue)
    }

    def getBits(): String ={
      var programmingBits = ""
      for(i <- 0 until BlockSize/8){
        programmingBits = programmingBits ++ "\n" ++ blockBits.slice(i*8,i*8+8).mkString("")
      }
      programmingBits
    }
  }
}
