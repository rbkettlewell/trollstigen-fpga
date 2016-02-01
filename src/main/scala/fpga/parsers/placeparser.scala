import scala.io.Source
import fpga.Types._

package fpga.parsers{
  class PlaceParser(placeFile : String){

    private val StartLine = 6
    var placement = parse(placeFile)
  
    def parse(filename : String): Placement = {
      val lines = Source.fromFile(placeFile).getLines().drop(StartLine - 1)
      var place : Placement = List()
      for (line <- lines){
        val placementInfo = line.split("\t").filter(_!="") 
        val name      = placementInfo(0) 
        val location  = (placementInfo(1).toInt,placementInfo(2).toInt)
        val subblock  = placementInfo(3).toInt
        place = place ++ List((name, location, subblock))
      }
      place
    }
  }
}
