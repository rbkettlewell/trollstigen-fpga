import fpga.Types._
import fpga.BlockEnum._
import fpga.blocks._

package fpga{
  class Bitgen{
    private val chanWidth = 12
    private val xTiles = 8
    private val yTiles = 16
    private val rows = xTiles*2 + 3
    private val cols = yTiles*2 + 3

    var fpga = Array.ofDim(rows, cols) : FPGABlocks

    def isOdd(num : Int): Boolean = {
      val oddTrue = num%2 == 1
      oddTrue
    }

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

    def getBlockEnum(locationXY : (Int,Int)): BlockEnum ={
      val row = locationXY._1
      val col = locationXY._2

      if(row == rows - 2 && col == cols - 2){
        NEC
      }else if (row == 1 && col == cols - 2){
        SEC
      }else if (row == 1 && col == 1){
        SWC 
      }else if (row == rows - 2 && col == 1){
        NWC
      }else if((row == 0 || row == rows - 1) && isOdd(col)){
        IOB
      }else{
        Empty
      }
    }

    // Description: getBlockConnectivity takes a block enumeration value and then builds a connectivity list for that
    // specific fpga primitive. This method helps break up the complexity of the assembleFPGA method.
    def getBlockConnectivity(block : BlockEnum): Connectivity = {
      val oddChannels  = (t : Track) => 1 + 2*t
      val evenChannels = (t : Track) => 2*t

      block match{
        //("Northeast Corner"): NEC
        case NEC =>{
          val toPathSW = ("W",(t : Track ) => (3 + 2*t)%12)
          val northeastCornerSW = defineConnectivity(("S", evenChannels), List(toPathSW))
          val toPathWS = ("S",(t : Track ) => (11 + 2*t)%12)
          val northeastCornerWS = defineConnectivity(("W", evenChannels), List(toPathWS))
          val blockConnectivity = northeastCornerSW ++ northeastCornerWS
          blockConnectivity
        }
        //("Southeast Corner"): SEC
        case SEC =>{
          val toPathNW = ("W",(t : Track ) => (13 - 2*t)%12)
          val southeastCornerNW = defineConnectivity(("N", oddChannels), List(toPathNW))
          val toPathWN = ("N",(t : Track ) => (12 - 2*t)%12)
          val southeastCornerWN = defineConnectivity(("W", evenChannels), List(toPathWN))
          val blockConnectivity = southeastCornerNW ++ southeastCornerWN
          blockConnectivity
        }
        //("Southwest Corner"): SWC
        case SWC =>{
          val toPathNE = ("E",(t : Track ) => (2 + 2*t)%12)
          val southwestCornerNE = defineConnectivity(("N", oddChannels), List(toPathNE))
          val toPathEN = ("N",(t : Track ) => (10 + 2*t)%12)
          val southwestCornerEN = defineConnectivity(("E", oddChannels), List(toPathEN))
          val blockConnectivity = southwestCornerNE ++ southwestCornerEN
          blockConnectivity
        }
        //("Northwest Corner"): NWC
        case NWC =>{
          val toPathES = ("S",(t : Track ) => (21 - 2*t)%12)
          val northwestCornerES = defineConnectivity(("E", oddChannels), List(toPathES))
          val toPathSE = ("E",(t : Track ) => (20 - 2*t)%12)
          val northwestCornerSE = defineConnectivity(("S", evenChannels), List(toPathSE))
          val blockConnectivity = northwestCornerES ++ northwestCornerSE 
          blockConnectivity
        }
        //("Perimeter North Connection Block"): PNCB
        case PNCB =>{
          val northInput  = List(2,3,6,7,10,11).map(t => (("S",t,false),List(("N",0,false))))
          val northOutput = List((("N",1,false),List(0,1,2,3,4,5).map(t=>("S",t,false))))
          val southInputZero = List(0,1,6,7).map(t => (("N",t,false),List(("S",0,false))))
          val southInputFour = List(4,5,10,11).map(t => (("N",t,false),List(("S",4,false))))
          val blockConnectivity = northInput ++ northOutput ++ southInputZero ++ southInputFour
          blockConnectivity
        }
        //("Perimeter East Connection Block") : PECB
        case PECB =>{
          val eastOutput = List((("E",1,false),List(0,1,2,3,4,5).map(t=>("W",t,false))))
          val eastInput = List(2,3,6,7,10,11).map(t => (("W",t,false),List(("E",0,false))))
          val westInputOne  = List(0,1,6,7).map(t => (("E",t,false),List(("W",1,false))))
          val westInputFive = List(4,5,10,11).map(t => (("E",t,false),List(("W",5,false))))
          val blockConnectivity = eastInput ++ eastOutput ++ westInputOne ++ westInputFive
          blockConnectivity
        }
        //("Perimeter South Connection Block"): PSCB
        case PSCB =>{
          val northInput  = List(2,3,8,9).map(t => (("S",t,false),List(("N",2,false))))
          val northOutput = List((("N",6,false),List(6,7,8,9).map(t=>("S",t,false))))
          val southInput  = List(0,1,4,5,8,9).map(t => (("N",t,false),List(("S",0,false))))
          val southOutput = List((("S",1,false),List(0,1,2,3,4,5).map(t=>("S",t,false))))
          val blockConnectivity = northInput ++ northOutput ++ southInput ++ southOutput
          blockConnectivity
        }
        //("Perimeter West Connection Block") : PWCB
        case PWCB  =>{
          val eastInput  = List(2,3,8,9).map(t => (("W",t,false),List(("E",3,false))))
          val westInput  = List(0,1,4,5,8,9).map(t => (("E",t,false),List(("W",0,false))))
          val westOutput = List((("W",1,false),List(0,1,2,3,4,5).map(t=>("E",t,false))))
          val blockConnectivity = eastInput ++ westInput ++ westOutput
          blockConnectivity
        }
        //("Perimeter North Switch Block")    : PNSB
        case PNSB =>{
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
        //("Perimeter East Switch Block")     : PESB
        case PESB =>{
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
        //("Perimeter South Switch Block")    : PSSB
        case PSSB =>{
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
        //("Perimeter West Switch Block")     : PWSB
        case PWSB =>{
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
        //("Internal North Connection Block") : INCB
        case INCB =>{
          val northInput  = List(2,3,8,9).map(t => (("S",t,false),List(("N",2,false))))
          val northOutput = List((("N",6,false),List(6,7,8,9).map(t=>("S",t,false))))
          val southInputZero = List(0,1,6,7).map(t => (("N",t,false),List(("S",0,false))))
          val southInputFour = List(4,5,10,11).map(t => (("N",t,false),List(("S",4,false))))
          val blockConnectivity = northInput ++ northOutput ++ southInputZero ++ southInputFour
          blockConnectivity
        }
        //("Internal East Connection Block")  : IECB
        case IECB =>{
          val eastInput  = List(2,3,8,9).map(t => (("W",t,false),List(("E",3,false))))
          val westInputOne  = List(0,1,6,7).map(t => (("E",t,false),List(("W",1,false))))
          val westInputFive = List(4,5,10,11).map(t => (("E",t,false),List(("W",5,false))))
          val blockConnectivity = eastInput ++ westInputOne ++ westInputFive
          blockConnectivity
        }
        //("Internal South Connection Block") : ISCB
        case ISCB =>{
          val northInput  = List(2,3,8,9).map(t => (("S",t,false),List(("N",2,false))))
          val northOutput = List((("N",6,false),List(6,7,8,9).map(t=>("S",t,false))))
          val southInputZero = List(0,1,6,7).map(t => (("N",t,false),List(("S",0,false))))
          val southInputFour = List(4,5,10,11).map(t => (("N",t,false),List(("S",4,false))))
          val blockConnectivity = northInput ++ northOutput ++ southInputZero ++ southInputFour
          blockConnectivity
        }
        //("Internal West Connection Block")  : IWCB
        case IWCB =>{
          val eastInput  = List(2,3,8,9).map(t => (("W",t,false),List(("E",3,false))))
          val westInputOne  = List(0,1,6,7).map(t => (("E",t,false),List(("W",1,false))))
          val westInputFive = List(4,5,10,11).map(t => (("E",t,false),List(("W",5,false))))
          val blockConnectivity = eastInput ++ westInputOne ++ westInputFive
          blockConnectivity
        }
        //("Internal Switch Block")           : ISB
        case ISB =>{
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
        case _ => List() // CLB and IOB return empty connectivity lists
      }
    }

    def assembleFPGA(){
      for (row <- 0 until rows){
        for (col <- 0 until cols){
          val locationXY = (row, col)
          val blockEnumeration = getBlockEnum(locationXY)
          val blockConnectivity = getBlockConnectivity(blockEnumeration)

          blockEnumeration match{
            case SWC  => fpga(row)(col) = new SwitchBlock(locationXY, blockConnectivity)
            case _ => new EmptyBlock(locationXY) 
          }
        }
      }
    }
  }
}

