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
    private val placeFile = synthesisFiles(2)
    private val routeFile = synthesisFiles(3)

    var fpga = Array.ofDim(rows, cols) : FPGABlocks

    var place = new PlaceParser(placeFile)
    
    var route = new RouteParser(routeFile)

    val blif = new BlifParser(blifFile)

    def isOdd(num : Int): Boolean = {
      val oddTrue = num%2 == 1
      oddTrue
    }

    // Description: mapPaths creates a range based on the channel width and then applies the PathDefinition function to the
    // elements of the range. The returned track numbers are bundled into tuples containing their respective direction
    // along with a default switch state being off.
    private def mapPaths(path : PathDefinition): Array[Segment] = {
      val channel = Array.range(0, chanWidth/2) 
      val segments = channel.map(i => (path._1, path._2(i), false, path._3))
      segments
    }

    // Description: defineConnectivity takes a channel width as an input, as well as the PathDefinition defining the valid
    // start or end points from an entry location (N,E,S,W) of the specific FPGA block. This method applies mapPaths 
    // multiple times and then zips the returned data into a key value pair for improved indexing.
    // By convention, the first element in the Path tuple is the start point of the segment, whereas the second element of 
    // the tuple contains the endpoint segments.
    def defineConnectivity(fromPath : PathDefinition, toPaths : Array[PathDefinition]) : Connectivity = {
      val fromPaths = mapPaths(fromPath)
      val toPathConections = toPaths.map(toPath => mapPaths(toPath))
      val pathConnectivity = Array.range(0,chanWidth/2).map(i => (fromPaths(i), toPathConections.map(toPath => toPath(i))))
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
      }else if (isOdd(row) && !isOdd(col) && (1 < row && row < rows - 2) && (1 < col && col < cols - 2)){
        IVCB 
      }else if (!isOdd(row) && isOdd(col) && (1 < row && row < rows - 2) && (1 < col && col < cols - 2)){
        IHCB 
      }else if (isOdd(row) && isOdd(col) && (1 < col && col < cols - 2) && (1 < row && row < rows - 2)){
        ISB
      }else if (!isOdd(row) && !isOdd(col) && (1 < col && col < cols - 2) && (1 < row && row < rows - 2)){
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
      val oddChannels  = (t : PinTrack) => 1 + 2*t
      val evenChannels = (t : PinTrack) => 2*t

      block match{
        //("Northeast Corner"): NEC
        case NEC =>{
          val toPathSW = ("W",(t : PinTrack ) => (3 + 2*t)%12 , "Track")
          val northeastCornerSW = defineConnectivity(("S", evenChannels,"Track"), Array(toPathSW))
          val toPathWS = ("S",(t : PinTrack ) => (11 + 2*t)%12, "Track")
          val northeastCornerWS = defineConnectivity(("W", evenChannels, "Track"), Array(toPathWS))
          val blockConnectivity = northeastCornerSW ++ northeastCornerWS
          blockConnectivity
        }
        //("Southeast Corner"): SEC
        case SEC =>{
          val toPathNW = ("W",(t : PinTrack ) => (13 - 2*t)%12, "Track")
          val southeastCornerNW = defineConnectivity(("N", oddChannels, "Track"), Array(toPathNW))
          val toPathWN = ("N",(t : PinTrack ) => (12 - 2*t)%12, "Track")
          val southeastCornerWN = defineConnectivity(("W", evenChannels, "Track"), Array(toPathWN))
          val blockConnectivity = southeastCornerNW ++ southeastCornerWN
          blockConnectivity
        }
        //("Southwest Corner"): SWC
        case SWC =>{
          val toPathNE = ("E",(t : PinTrack ) => (2 + 2*t)%12, "Track")
          val southwestCornerNE = defineConnectivity(("N", oddChannels, "Track"), Array(toPathNE))
          val toPathEN = ("N",(t : PinTrack ) => (10 + 2*t)%12, "Track")
          val southwestCornerEN = defineConnectivity(("E", oddChannels, "Track"), Array(toPathEN))
          val blockConnectivity = southwestCornerNE ++ southwestCornerEN
          blockConnectivity
        }
        //("Northwest Corner"): NWC
        case NWC =>{
          val toPathES = ("S",(t : PinTrack ) => (21 - 2*t)%12, "Track")
          val northwestCornerES = defineConnectivity(("E", oddChannels, "Track"), Array(toPathES))
          val toPathSE = ("E",(t : PinTrack ) => (20 - 2*t)%12, "Track")
          val northwestCornerSE = defineConnectivity(("S", evenChannels, "Track"), Array(toPathSE))
          val blockConnectivity = northwestCornerES ++ northwestCornerSE 
          blockConnectivity
        }
        //("Perimeter North Connection Block"): PNCB
        case PNCB =>{
          val northInput  = Array(2,3,6,7,10,11).map(t => (("S",t,false, "Track"),Array(("N",0,false,"Pad"))))
          val northOutput = Array((("N",1,false,"Pad"),Array(0,1,2,3,4,5).map(t=>("S",t,false, "Track"))))
          val southInputZero = Array(0,1,6,7).map(t => (("N",t,false, "Track"),Array(("S",0,false,"Pin"))))
          val southInputFour = Array(4,5,10,11).map(t => (("N",t,false, "Track"),Array(("S",4,false,"Pin"))))
          val blockConnectivity = northInput ++ northOutput ++ southInputZero ++ southInputFour
          blockConnectivity
        }
        //("Perimeter East Connection Block") : PECB
        case PECB =>{
          val eastOutput = Array((("E",1,false,"Pad"),Array(0,1,2,3,4,5).map(t=>("W",t,false, "Track"))))
          val eastInput = Array(2,3,6,7,10,11).map(t => (("W",t,false, "Track"),Array(("E",0,false,"Pad"))))
          val westInputOne  = Array(0,1,6,7).map(t => (("E",t,false, "Track"),Array(("W",1,false,"Pin"))))
          val westInputFive = Array(4,5,10,11).map(t => (("E",t,false, "Track"),Array(("W",5,false,"Pin"))))
          val blockConnectivity = eastInput ++ eastOutput ++ westInputOne ++ westInputFive
          blockConnectivity
        }
        //("Perimeter South Connection Block"): PSCB
        case PSCB =>{
          val northInput  = Array(2,3,8,9).map(t => (("S",t,false, "Track"),Array(("N",2,false,"Pin"))))
          val northOutput = Array((("N",6,false,"Pin"),Array(6,7,8,9).map(t=>("S",t,false, "Track"))))
          val southInput  = Array(0,1,4,5,8,9).map(t => (("N",t,false, "Track"),Array(("S",0,false,"Pad"))))
          val southOutput = Array((("S",1,false,"Pad"),Array(0,1,2,3,4,5).map(t=>("N",t,false, "Track"))))
          val blockConnectivity = northInput ++ northOutput ++ southInput ++ southOutput
          blockConnectivity
        }
        //("Perimeter West Connection Block") : PWCB
        case PWCB  =>{
          val eastInput  = Array(2,3,8,9).map(t => (("W",t,false, "Track"),Array(("E",3,false,"Pin"))))
          val westInput  = Array(0,1,4,5,8,9).map(t => (("E",t,false, "Track"),Array(("W",0,false,"Pad"))))
          val westOutput = Array((("W",1,false,"Pad"),Array(0,1,2,3,4,5).map(t=>("E",t,false,"Track"))))
          val blockConnectivity = eastInput ++ westInput ++ westOutput
          blockConnectivity
        }
        //("Perimeter North Switch Block")    : PNSB
        case PNSB =>{
          val toPathES = ("S",oddChannels, "Track")
          val toPathEW = ("W",oddChannels, "Track")
          val switchBlockEast = defineConnectivity(("E", oddChannels, "Track"), Array(toPathES, toPathEW))
          val toPathSW = ("W",oddChannels, "Track")
          val toPathSE = ("E",evenChannels, "Track")
          val switchBlockSouth = defineConnectivity(("S", evenChannels, "Track"), Array(toPathSW, toPathSE))
          val toPathWE = ("E",evenChannels, "Track")
          val toPathWS = ("S",oddChannels, "Track")
          val switchBlockWest = defineConnectivity(("W", evenChannels, "Track"), Array(toPathWE, toPathWS))
          val blockConnectivity = switchBlockEast ++ switchBlockSouth ++ switchBlockWest
          blockConnectivity
        }
        //("Perimeter East Switch Block")     : PESB
        case PESB =>{
          val toPathNS = ("S",oddChannels, "Track")
          val toPathNW = ("W",oddChannels, "Track")
          val switchBlockNorth = defineConnectivity(("N", oddChannels, "Track"), Array(toPathNS, toPathNW))
          val toPathSW = ("W",oddChannels, "Track")
          val toPathSN = ("N",evenChannels, "Track")
          val switchBlockSouth = defineConnectivity(("S", evenChannels, "Track"), Array(toPathSW, toPathSN))
          val toPathWN = ("N",evenChannels, "Track")
          val toPathWS = ("S",oddChannels, "Track")
          val switchBlockWest = defineConnectivity(("W", evenChannels, "Track"), Array(toPathWN, toPathWS))
          val blockConnectivity = switchBlockNorth ++ switchBlockSouth ++ switchBlockWest
          blockConnectivity
        }
        //("Perimeter South Switch Block")    : PSSB
        case PSSB =>{
          val toPathNE = ("E",evenChannels, "Track")
          val toPathNW = ("W",oddChannels, "Track")
          val switchBlockNorth = defineConnectivity(("N", oddChannels, "Track"), Array(toPathNE, toPathNW))
          val toPathEW = ("W",oddChannels, "Track")
          val toPathEN = ("N",evenChannels, "Track")
          val switchBlockEast = defineConnectivity(("E", oddChannels, "Track"), Array(toPathEW, toPathEN))
          val toPathWN = ("N",evenChannels, "Track")
          val toPathWE = ("E",evenChannels, "Track")
          val switchBlockWest = defineConnectivity(("W", evenChannels, "Track"), Array(toPathWN, toPathWE))
          val blockConnectivity = switchBlockNorth ++ switchBlockEast ++ switchBlockWest
          blockConnectivity
        }
        //("Perimeter West Switch Block")     : PWSB
        case PWSB =>{
          val toPathNE = ("E",evenChannels, "Track")
          val toPathNS = ("S",oddChannels, "Track")
          val switchBlockNorth = defineConnectivity(("N", oddChannels, "Track"), Array(toPathNE, toPathNS))
          val toPathES = ("S",oddChannels, "Track")
          val toPathEN = ("N",evenChannels, "Track")
          val switchBlockEast = defineConnectivity(("E", oddChannels, "Track"), Array(toPathES, toPathEN))
          val toPathSN = ("N", evenChannels, "Track")
          val toPathSE = ("E", evenChannels, "Track")
          val switchBlockSouth = defineConnectivity(("S", evenChannels, "Track"), Array(toPathSN, toPathSE))
          val blockConnectivity = switchBlockNorth ++ switchBlockEast ++ switchBlockSouth
          blockConnectivity
        }
        //("Internal Horizontal Connection Block")  : IHCB 
        case IHCB =>{
          val eastInput  = Array(2,3,8,9).map(t => (("W",t,false, "Track"),Array(("E",3,false, "Pin"))))
          val westInputOne  = Array(0,1,6,7).map(t => (("E",t,false, "Track"),Array(("W",1,false, "Pin"))))
          val westInputFive = Array(4,5,10,11).map(t => (("E",t,false, "Track"),Array(("W",5,false, "Pin"))))
          val blockConnectivity = eastInput ++ westInputOne ++ westInputFive
          blockConnectivity
        }
        //("Internal Vertical Connection Block") : IVCB 
        case IVCB =>{
          val northInput  = Array(2,3,8,9).map(t => (("S",t,false, "Track"),Array(("N",2,false, "Pin"))))
          val northOutput = Array((("N",6,false, "Pin"),Array(0,1,2,3).map(t=>("S",t,false, "Track"))))
          val southInputZero = Array(0,1,6,7).map(t => (("N",t,false, "Track"),Array(("S",0,false, "Pin"))))
          val southInputFour = Array(4,5,10,11).map(t => (("N",t,false, "Track"),Array(("S",4,false, "Pin"))))
          val blockConnectivity = northInput ++ northOutput ++ southInputZero ++ southInputFour
          blockConnectivity
        }
        //("Internal Switch Block")           : ISB
        case ISB =>{
          val toPathNE = ("E",(t : PinTrack ) => (2 + 2*t)%12, "Track")
          val toPathNS = ("S",oddChannels, "Track")
          val toPathNW = ("W",(t : PinTrack ) => (13 - 2*t)%12, "Track")
          val switchBlockNorth = defineConnectivity(("N", oddChannels, "Track"), Array(toPathNE, toPathNS, toPathNW))
          val toPathES = ("S",(t : PinTrack ) => (21 - 2*t)%12, "Track")
          val toPathEW = ("W",oddChannels, "Track")
          val toPathEN = ("N",(t : PinTrack ) => (10 + 2*t)%12, "Track")
          val switchBlockEast = defineConnectivity(("E", oddChannels, "Track"), Array(toPathES, toPathEW, toPathEN))
          val toPathSW = ("W",(t : PinTrack ) => (3 + 2*t)%12, "Track")
          val toPathSN = ("N", evenChannels, "Track")
          val toPathSE = ("E",(t : PinTrack ) => (20 - 2*t)%12, "Track")
          val switchBlockSouth = defineConnectivity(("S", evenChannels, "Track"), Array(toPathSW, toPathSN, toPathSE))
          val toPathWN = ("N",(t : PinTrack ) => (12 - 2*t)%12, "Track")
          val toPathWE = ("E",evenChannels, "Track")
          val toPathWS = ("S",(t : PinTrack ) => (11 + 2*t)%12, "Track")
          val switchBlockWest = defineConnectivity(("W", evenChannels, "Track"), Array(toPathWN, toPathWE, toPathWS))
          val blockConnectivity = switchBlockNorth ++ switchBlockEast ++ switchBlockSouth ++ switchBlockWest
          blockConnectivity
        }
        case _ => Array() // CLB and IOB return empty connectivity lists
      }
    }

    // Description: This method sequentially reads all the routing node connections and sets the specific block
    // switches accordingly.
    def configureRouting(){
      //Set all the transmission gate values
      route.routing.foreach{r =>
        // Note: The vpr coordinate notation is (X,Y) where we define X => column and Y => row
        val col = r._1._1
        val row = r._1._2
        var block = fpga(row)(col)
          block match{
            case b : SwitchBlock     => fpga(row)(col).asInstanceOf[SwitchBlock].setSwitch(r._2,r._3)
            case b : ConnectionBlock => fpga(row)(col).asInstanceOf[ConnectionBlock].setSwitch(r._2,r._3)
          }
      }
    }

    def configureFPGA(){

      configureRouting()

      for (row <- 0 until rows){
        for (col <- 0 until cols){
          val block = fpga(row)(col)
          block match{
            case b : SwitchBlock     => fpga(row)(col).asInstanceOf[SwitchBlock].setBits
            case b : ConnectionBlock => fpga(row)(col).asInstanceOf[ConnectionBlock].setBits
            case b : CLB             =>
            case b : IOB             =>
            case b : EmptyBlock      =>
          }
        }
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

