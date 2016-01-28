import java.io.File
import fpga.Types._

package fpga{
  object Util{
    def getListOfFiles(dir: File, extensions: List[String]): List[String] = {
      val files = dir.listFiles.filter(_.isFile).toList.filter { file =>
          extensions.exists(file.getName.endsWith(_))
      }
      files.map(_.toString) 
    }
  }
}
