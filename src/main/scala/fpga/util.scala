import java.io.File
import fpga.Types._
import scala.collection.mutable.MutableList

package fpga{
  object Util{
    def getSynthesisFiles(dir: File, extensions: List[String]): List[String] = {
      var synthesisFiles = MutableList.fill(4){""}
      val files = dir.listFiles.filter(_.isFile).toList.filter { file =>
          extensions.exists(file.getName.endsWith(_))
      }
      try{
        if (files.length != 4)
            throw new IllegalStateException("Missing or duplicated synthesis files detected")
        else{
          for (filename <- files.map(_.toString)){
            if (filename.contains(".blif"))
              synthesisFiles(0) = filename
            else if (filename.contains(".net"))
              synthesisFiles(1) = filename
            else if (filename.contains(".place"))
              synthesisFiles(2) = filename
            else if (filename.contains(".route"))
              synthesisFiles(3) = filename
            else
              throw new IllegalStateException("Incorrect synthesis file extension")
          }
        }
      }catch{
        case e: Exception => println("Exception caught: " + e);
      }
        synthesisFiles.toList
    }
  }
}
