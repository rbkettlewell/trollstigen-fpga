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
        case "Northwest Corner" =>{
          val toPathES = ("S",(t : Track ) => (21 - 2*t)%12)
          val northwestCornerES = defineConnectivity(("E", oddChannels), List(toPathES))
          val toPathSE = ("E",(t : Track ) => (20 - 2*t)%12)
          val northwestCornerSE = defineConnectivity(("S", evenChannels), List(toPathSE))
          val blockConnectivity = northwestCornerES ++ northwestCornerSE 
          blockConnectivity
        }
        case "Northeast Corner" =>{
          val toPathSW = ("W",(t : Track ) => (3 + 2*t)%12)
          val northeastCornerSW = defineConnectivity(("S", evenChannels), List(toPathSW))
          val toPathWS = ("S",(t : Track ) => (11 + 2*t)%12)
          val northeastCornerWS = defineConnectivity(("W", evenChannels), List(toPathWS))
          val blockConnectivity = northeastCornerSW ++ northeastCornerWS
          blockConnectivity
        }
        case "Southeast Corner" =>{
          val toPathNW = ("W",(t : Track ) => (13 - 2*t)%12)
          val southeastCornerNW = defineConnectivity(("N", oddChannels), List(toPathNW))
          val toPathWN = ("N",(t : Track ) => (12 - 2*t)%12)
          val southeastCornerWN = defineConnectivity(("W", evenChannels), List(toPathWN))
          val blockConnectivity = southeastCornerNW ++ southeastCornerWN
          blockConnectivity
        }
        case "Southwest Corner" =>{
          val toPathNE = ("E",(t : Track ) => (2 + 2*t)%12)
          val southwestCornerNE = defineConnectivity(("N", oddChannels), List(toPathNE))
          val toPathEN = ("N",(t : Track ) => (10 + 2*t)%12)
          val southwestCornerEN = defineConnectivity(("E", oddChannels), List(toPathEN))
          val blockConnectivity = southwestCornerNE ++ southwestCornerEN
          blockConnectivity
        }
        case "North Switch Block" =>{
          val toPathES = ("S",oddChannels)
          val toPathEW = ("W",oddChannels)
          val switchBlockEast = defineConnectivity(("E", oddChannels), List(toPathES, toPathEW))
          val toPathSW = ("W",oddChannels)
          val toPathSE = ("E",evenChannels)
          val switchBlockSouth = defineConnectivity(("S", evenChannels), List(toPathSW, toPathSE))
          val toPathWE = ("E",evenChannels)
          val toPathWS = ("S",oddChannels)
          val switchBlockWest = defineConnectivity(("W", evenChannels), List(toPathWE, toPathWS))
          val blockConnectivity = switchBlockEast ++ switchBlockSouth ++ switchBlockWest
          blockConnectivity
        }
        case "East Switch Block" =>{
          val toPathNS = ("S",oddChannels)
          val toPathNW = ("W",oddChannels)
          val switchBlockNorth = defineConnectivity(("N", oddChannels), List(toPathNS, toPathNW))
          val toPathSW = ("W",oddChannels)
          val toPathSN = ("N",evenChannels)
          val switchBlockSouth = defineConnectivity(("S", evenChannels), List(toPathSW, toPathSN))
          val toPathWN = ("N",evenChannels)
          val toPathWS = ("S",oddChannels)
          val switchBlockWest = defineConnectivity(("W", evenChannels), List(toPathWN, toPathWS))
          val blockConnectivity = switchBlockNorth ++ switchBlockSouth ++ switchBlockWest
          blockConnectivity
        }
        case "South Switch Block" =>{
          val toPathNE = ("E",evenChannels)
          val toPathNW = ("W",oddChannels)
          val switchBlockNorth = defineConnectivity(("N", oddChannels), List(toPathNE, toPathNW))
          val toPathEW = ("W",oddChannels)
          val toPathEN = ("N",evenChannels)
          val switchBlockEast = defineConnectivity(("E", oddChannels), List(toPathEW, toPathEN))
          val toPathWN = ("N",evenChannels)
          val toPathWE = ("E",evenChannels)
          val switchBlockWest = defineConnectivity(("W", evenChannels), List(toPathWN, toPathWE))
          val blockConnectivity = switchBlockNorth ++ switchBlockEast ++ switchBlockWest
          blockConnectivity
        }
        case "West Switch Block" =>{
          val toPathNE = ("E",evenChannels)
          val toPathNS = ("S",oddChannels)
          val switchBlockNorth = defineConnectivity(("N", oddChannels), List(toPathNE, toPathNS))
          val toPathES = ("S",oddChannels)
          val toPathEN = ("N",evenChannels)
          val switchBlockEast = defineConnectivity(("E", oddChannels), List(toPathES, toPathEN))
          val toPathSN = ("N", evenChannels)
          val toPathSE = ("E", evenChannels)
          val switchBlockSouth = defineConnectivity(("S", evenChannels), List(toPathSN, toPathSE))
          val blockConnectivity = switchBlockNorth ++ switchBlockEast ++ switchBlockSouth
          blockConnectivity
        }
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
        case "North Perimeter Connection Block" =>{
          val northInput  = (("N",0),List(("S",2),("S",3),("S",6),("S",7),("S",10),("S",11)))
          val northOutput = (("N",1),List(("S",0),("S",1),("S",2),("S",3),("N",4),("N",5)))
          val southInputZero  = (("S",0),List(("N",0),("N",1),("N",4),("N",5),("N",8),("N",9)))
          val southInputFour  = (("S",4),List(("N",0),("N",1),("N",2),("N",3),("N",4),("N",5)))
          val blockConnectivity = List(northInput, northOutput, southInputZero, southInputFour)
          blockConnectivity
        }
        case "South Perimeter Connection Block" =>{
          val southInput  = (("S",0),List(("N",0),("N",1),("N",4),("N",5),("N",8),("N",9)))
          val southOutput = (("S",1),List(("N",0),("N",1),("N",2),("N",3),("N",4),("N",5)))
          val northInput  = (("N",2),List(("S",2),("S",3),("S",8),("S",9)))
          val northOutput = (("N",6),List(("S",6),("S",7),("S",8),("S",9)))
          val blockConnectivity = List(northInput, northOutput, southInput, southOutput)
          blockConnectivity
        }
        case "East Perimeter Connection Block" =>{
          val southInput  = (("S",0),List(("N",0),("N",1),("N",4),("N",5),("N",8),("N",9)))
          val southOutput = (("S",1),List(("N",0),("N",1),("N",2),("N",3),("N",4),("N",5)))
          val northInput  = (("N",2),List(("S",2),("S",3),("S",8),("S",9)))
          val northOutput = (("N",6),List(("S",6),("S",7),("S",8),("S",9)))
          val blockConnectivity = List(northInput, northOutput, southInput, southOutput)
          blockConnectivity
        }
        case "West Perimeter Connection Block" =>{
          val southInput  = (("S",0),List(("N",0),("N",1),("N",4),("N",5),("N",8),("N",9)))
          val southOutput = (("S",1),List(("N",0),("N",1),("N",2),("N",3),("N",4),("N",5)))
          val northInput  = (("N",2),List(("S",2),("S",3),("S",8),("S",9)))
          val northOutput = (("N",6),List(("S",6),("S",7),("S",8),("S",9)))
          val blockConnectivity = List(northInput, northOutput, southInput, southOutput)
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

