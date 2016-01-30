import scala.io.Source
import fpga.Types._
import scala.math

package fpga.parsers{
  class RouteParser(routeFile: String){

    private val StartLine = 4 
    var routing = parse(routeFile)

    def normalizeScalar(scalar : Int): Int = {
      if(scalar < 0)
        Math.floor(scalar/2.0).toInt
      else
        Math.ceil(scalar/2.0).toInt
    }

    def getRoute(start: RouteInfo, end: RouteInfo): Route ={
      val startGridXY = updateCoordinates(start._1,start._2)
      val endGridXY = updateCoordinates(end._1,end._2)
      var currentGridXY = (0,0)

      val vectorXY = (normalizeScalar(endGridXY._1 - startGridXY._1), normalizeScalar(endGridXY._2 - startGridXY._2))
      val compassVector = vectorToCompass(vectorXY,start._2)
      
      // Adds normalized vector offset to start point
      if(start._2 == "CHANX" && (end._2 == "CHANX" || end._2 == "CHANY"))
        currentGridXY = (startGridXY._1 + vectorXY._1, startGridXY._2)
      else if (start._2 == "CHANY" && (end._2 == "CHANX" || end._2 == "CHANY"))
        currentGridXY = (startGridXY._1, startGridXY._2 + vectorXY._2)
      else if (start._2 == "IPIN" || start._2 == "OPIN")
        currentGridXY = (startGridXY._1 + vectorXY._1, startGridXY._2 + vectorXY._2)
      else 
        currentGridXY = startGridXY

      (currentGridXY,(compassVector._1,start._3,false),(compassVector._2,end._3,false))
    }

    def updateCoordinates(location : LocationXY, connection : ConnectionType): LocationXY = {
      val x = location._1
      val y = location._2
      var newXY = (0,0)
      
      if(connection == "CHANX")
        newXY = (2*x,2*y+1)
      else if(connection == "CHANY")
        newXY = (2*x+1,2*y)
      else if(connection == "IPIN" || connection == "OPIN")
        newXY = (2*x,2*y)
      else
        println("Error: Invalid Connection Type attempted: " ++ connection)
      newXY
    }

    def vectorToCompass(vecXY : (Int,Int), startConnection : ConnectionType) : (Direction,Direction) ={
      val xComponent = vecXY._1
      val yComponent = vecXY._2

      if(xComponent == 0 && yComponent > 0)
        ("S","N")
      else if(xComponent == 0 && yComponent < 0)
        ("N","S")
      else if(xComponent > 0 && yComponent == 0)
        ("W","E")
      else if(xComponent < 0 && yComponent == 0)
        ("E","W")
      else if(startConnection == "CHANY"){
        if(xComponent > 0 && yComponent > 0)
          ("S","E")
        else if(xComponent > 0 && yComponent < 0)
          ("N","E")
        else if(xComponent < 0 && yComponent > 0)
          ("S","W")
        else if(xComponent < 0 && yComponent < 0)
          ("N","W")
        else
          ("Invalid","Invalid")
      }
      else if(xComponent > 0 && yComponent > 0)
        ("W","N")
      else if(xComponent > 0 && yComponent < 0)
        ("W","S")
      else if(xComponent < 0 && yComponent > 0)
        ("E","N")
      else if(xComponent < 0 && yComponent < 0)
        ("E","S")
      else
        ("Invalid","Invalid")
    }

    def parse(filename : String): Routes = {
      val lines = Source.fromFile(filename).getLines().drop(StartLine - 1)
      var routes : Routes = Array()
      for(line <- lines){
        if(line.contains("Net")){
          println(line)
        }
      }
      routes
    }
  }
}
