import scala.io.Source
import fpga.Types._

package fpga.parsers{
  class RouteParser(routeFile: String){

    private val StartLine = 4 
    var routing = parse(routeFile)
  
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
