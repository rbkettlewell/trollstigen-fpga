import fpga.Types._
import fpga.blocks._

package fpga{
  class Bitgen{
    private val chanWidth = 12
    private val xTiles = 8
    private val yTiles = 16
    private val rows = (xTiles + 1)*2
    private val cols = (yTiles + 1)*2

    var fpga = Array.ofDim(rows, cols) : FPGABlocks

    // Description: mapPaths creates a range based on the channel width and then applies the PathDefinition function to the
    // elements of the range. The returned track numbers are bundled into tuples containing their respective direction
    // along with a default switch state being off.
    private def mapPaths(path : PathDefinition): List[Segment] = {
      val channel = List.range(0, chanWidth/2) 
      val segments = channel.map(i => (path._1, path._2(i), false))
      segments
    }

    // Description: defineConnectivity takes a channel width as an input, as well as the PathDefinition defining the valid
    // start or end points from an entry location (N,E,S,W) of the specific FPGA block. This method applies mapPaths 
    // multiple times and then zips the returned data into a key value pair for improved indexing.
    // By convention, the first element in the Path tuple is the start point of the segment, whereas the second element of 
    // the tuple contains the endpoint segments.
    def defineConnectivity(fromPath : PathDefinition, toPaths : List[PathDefinition]) : Connectivity = {
      val fromPaths = mapPaths(fromPath)
      val toPathConections = toPaths.map(toPath => mapPaths(toPath))
      val pathConnectivity = List.range(0,chanWidth/2).map(i => (fromPaths(i), toPathConections.map(toPath => toPath(i))))
      pathConnectivity
    }

    // Description: getBlockConnectivity takes a string descriptor of a specific fpga block and then builds a
    // connectivity list for that specific fpga primitive. This method helps break up the complexity of the
    // assembleFPGA method.
    def getBlockConnectivity(blockType : String): Connectivity = {
      val oddChannels  = (t : Track) => 1 + 2*t
      val evenChannels = (t : Track) => 2*t

      blockType match{
        case "Internal Switch Block" =>{
          val toPathNE = ("E",(t : Track ) => (2 + 2*t)%12)
          val toPathNS = ("S",oddChannels)
          val toPathNW = ("W",(t : Track ) => (13 - 2*t)%12)
          val switchBlockNorth = defineConnectivity(("N", oddChannels), List(toPathNE, toPathNS, toPathNW))
          val toPathES = ("S",(t : Track ) => (21 - 2*t)%12)
          val toPathEW = ("W",oddChannels)
          val toPathEN = ("N",(t : Track ) => (10 + 2*t)%12)
          val switchBlockEast = defineConnectivity(("E", oddChannels), List(toPathES, toPathEW, toPathEN))
          val toPathSW = ("W",(t : Track ) => (3 + 2*t)%12)
          val toPathSN = ("N", evenChannels)
          val toPathSE = ("E",(t : Track ) => (20 - 2*t)%12)
          val switchBlockSouth = defineConnectivity(("S", evenChannels), List(toPathSW, toPathSN, toPathSE))
          val toPathWN = ("N",(t : Track ) => (12 - 2*t)%12)
          val toPathWE = ("E",evenChannels)
          val toPathWS = ("S",(t : Track ) => (11 + 2*t)%12)
          val switchBlockWest = defineConnectivity(("W", evenChannels), List(toPathWN, toPathWE, toPathWS))
          val blockConnectivity = switchBlockNorth ++ switchBlockEast ++ switchBlockSouth ++ switchBlockWest
          blockConnectivity
        }
        case _ => List() // TODO change this to an exception.
      }
    }

    def assembleFPGA(){
      val internalSwitchConnectivity = getBlockConnectivity("Internal Switch Block")

      // Define the primitive fpga block types (i.e. SwitchBlock, CLB etc.) based on the X,Y position 
      // within the SRAM block hierarchy. All switches begin in the off state when the block objects are
      // instantiated.
      for (row <- 0 until rows){
        for (col <- 0 until cols){
          var locationXY = (row, col)
          locationXY match{
            case (0,0) => new EmptyBlock(locationXY)
            case (0,c) if c < cols - 1 => fpga(row)(col) = new SwitchBlock(locationXY, internalSwitchConnectivity)
            case _ => new EmptyBlock(locationXY) 
          }
        }
      }
    }
  }
}

