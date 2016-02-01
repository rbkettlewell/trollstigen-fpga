import scala.io.Source
import fpga.Types._

package fpga.parsers{
  class BlifParser(blifFile: String){
    val blifs = parse(blifFile)

    override def toString(): String ={
      var blifString = ""
      val allBlifs = blifs.map (b=> b._1 ++ " " ++  b._2.mkString(", ") ++ ", " + b._3 ++ "\n\t" ++ b._4.mkString("\n\t") ++ ", " ++ b._5)
      blifString ++ allBlifs.mkString("\n")
    }

    def parse(filename : String): Blifs = {
      val StartOffset = 4 
      val lines = Source.fromFile(filename).getLines.toArray
      var blif : Blifs = Array()
      val lineCount = lines.length
      var lineNumber = StartOffset
    
      while (lineNumber < lineCount){
        val line = lines(lineNumber)
        val blifRawInfo = line.split(" ") 
        val modelName = blifRawInfo(0)
        // TODO verify that stripping out the vcc, gnd constants do not effect the result.:w
        if(modelName == ".names" && blifRawInfo.length > 2){
          val ioNames  = blifRawInfo.tail
          val inputs = ioNames.init
          val output = ioNames.last
          var cover : Array[String] = Array()
          lineNumber += 1
          // Keep reading cover until blank line is found
          while (lines(lineNumber) != ""){
            cover = cover ++ Array(lines(lineNumber))
            lineNumber += 1
          }
          blif = blif ++ Array((".names",inputs,output,cover,""))
        }else if (modelName == ".latch"){
          val clockEdge = blifRawInfo(3)
          val resetDefault = blifRawInfo(5)
          if(clockEdge != "re")
            println("Error, non rising edge latch is being requested")
          blif = blif ++ Array((".latch",Array(blifRawInfo(1)),blifRawInfo(1),Array(""),resetDefault))
          lineNumber += 1
        }
        else
          lineNumber += 1
      }
      blif 
    }
  }
}
