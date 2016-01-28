import fpga.Types._
import fpga.BlockEnum._
import fpga.blocks._
import fpga.parsers._

package fpga{
  class Bitgen(synthesisFiles : List[String]){
    private val chanWidth = 12
    private val xTiles = 8
    private val yTiles = 16
    private val cols = xTiles*2 + 3
    private val rows = yTiles*2 + 3
    private val blifFile  = synthesisFiles(0)
    private val netFile   = synthesisFiles(1)
    private val placeFile = synthesisFiles(3)
    private val routeFile = synthesisFiles(3) //TODO Fix the broken file extension finder, add error detection

    var fpga = Array.ofDim(rows, cols) : FPGABlocks

    var place = new PlaceParser(placeFile)

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


    //Description: getBlockEnum defines the fpga block specialization based on location in the FPGA grid.
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
      }else if (row == rows - 2 && (col > 0 && col < cols - 2) && !isOdd(col)){
        PNCB
      }else if ((0 < row && row < rows - 2) && col == cols - 2 && !isOdd(row)){
        PECB
      }else if (row == 1 && (col > 0 && col < cols - 1) && !isOdd(col)){
        PSCB
      }else if ((0 < row && row < rows - 1) && col == 1 && !isOdd(row)){
        PWCB
      }else if((row == rows - 2) && (col > 0 && col < cols - 2) && isOdd(col)){
        PNSB
      }else if ((0 < row && row < rows - 2) && col == cols - 2 && isOdd(row)){
        PESB
      }else if (row == 1 && (col > 0 && col < cols - 2) && isOdd(col)){
        PSSB
      }else if ((0 < row && row < rows - 2) && col == 1 && isOdd(row)){
        PWSB
      }else if (isOdd(row) && !isOdd(col) && (1 < row && row < rows - 3) && (1 < col && col < cols - 3)){
        IVCB 
      }else if (!isOdd(row) && isOdd(col) && (1 < row && row < rows - 3) && (1 < col && col < cols - 3)){
        IHCB 
      }else if (isOdd(row) && isOdd(col) && (1 < col && col < cols - 3) && (1 < row && row < rows - 3)){
        ISB
      }else if (!isOdd(row) && !isOdd(col) && (1 < col && col < cols - 3) && (1 < row && row < rows - 3)){
        CLB
      }else if((row == 0 || row == rows - 1) && (0 < col && col < cols - 1) && !isOdd(col)){
        IOB
      }else if((col == 0 || col == cols - 1) && (0 < row && row < rows - 1) && !isOdd(row)){
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
        //("Internal Horizontal Connection Block")  : IECB
        case IHCB =>{
          val eastInput  = List(2,3,8,9).map(t => (("W",t,false),List(("E",3,false))))
          val westInputOne  = List(0,1,6,7).map(t => (("E",t,false),List(("W",1,false))))
          val westInputFive = List(4,5,10,11).map(t => (("E",t,false),List(("W",5,false))))
          val blockConnectivity = eastInput ++ westInputOne ++ westInputFive
          blockConnectivity
        }
        //("Internal Vertical Connection Block") : ISCB
        case IVCB =>{
          val northInput  = List(2,3,8,9).map(t => (("S",t,false),List(("N",2,false))))
          val northOutput = List((("N",6,false),List(6,7,8,9).map(t=>("S",t,false))))
          val southInputZero = List(0,1,6,7).map(t => (("N",t,false),List(("S",0,false))))
          val southInputFour = List(4,5,10,11).map(t => (("N",t,false),List(("S",4,false))))
          val blockConnectivity = northInput ++ northOutput ++ southInputZero ++ southInputFour
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

    def prettyPrint(printType : String): Unit = {
      println
      for (row <- rows - 1 to 0 by -1){
        var fpgaRow = ""
        for (col <- 0 until cols){
          val block = fpga(row)(col)

          if (printType == "Basic"){
            block match{
              case b : SwitchBlock     => fpgaRow = fpgaRow ++ " S "
              case b : ConnectionBlock => fpgaRow = fpgaRow ++ " C "
              case b : CLB             => fpgaRow = fpgaRow ++ " L "
              case b : IOB             => fpgaRow = fpgaRow ++ " P "
              case b : EmptyBlock      => fpgaRow = fpgaRow ++ " E "
            }
          }else if (printType == "Detailed"){
              fpgaRow = fpgaRow ++ block.blockEnumeration.toString.take(3) ++ " " 
          }else{
            println("Invalid prettyPrint configuration parameter")
          }
        }
        println(fpgaRow)
      }
      println
    }

    def assembleFPGA(){

      for (row <- 0 until rows){
        for (col <- 0 until cols){

          val locationXY = (row, col)
          val blockEnumeration = getBlockEnum(locationXY)
          val blockConnectivity = getBlockConnectivity(blockEnumeration)
          val switchBlocks = List(NEC,SEC,SWC,NWC,PNSB,PESB,PSSB,PWSB,ISB)
          val connectionBlocks = List(PNCB,PECB,PSCB,PWCB,IVCB,IHCB)

          blockEnumeration match{
            case s if switchBlocks.exists(_==s)     => fpga(row)(col) = new SwitchBlock(locationXY, blockEnumeration, blockConnectivity)
            case c if connectionBlocks.exists(_==c) => fpga(row)(col) = new ConnectionBlock(locationXY, blockEnumeration, blockConnectivity)
            case CLB   => fpga(row)(col) = new CLB(locationXY, blockEnumeration)
            case IOB   => fpga(row)(col) = new IOB(locationXY, blockEnumeration)
            case Empty => fpga(row)(col) = new EmptyBlock(locationXY, blockEnumeration)
          }
        }
      }
    }
  }
}

