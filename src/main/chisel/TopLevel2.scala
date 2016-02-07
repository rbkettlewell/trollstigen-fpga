package FPGASimulation

import Chisel._

class TopLevel2 extends Module{
  val io = new Bundle{
    val ipad0 = UInt(INPUT, 1)
    val opad1 = UInt(OUTPUT, 1)
    val sram0 = UInt(INPUT, 72)
    val sram1 = UInt(INPUT, 72)
  }

  type LocationXY  = (Int,Int)

  private val chanWidth = 12
  private val xTiles = 8
  private val yTiles = 16
  private val cols = xTiles*2 + 3
  private val rows = yTiles*2 + 3

  var fpga = assembleFPGA()

  def assembleFPGA(): Array[Array[Module]]={
    val fpga = Range(0, rows).toArray.map{row=>
      Range(0, cols).toArray.map(col=> getSpecializedBlock((row, col)))
    }
    fpga
  }

  def isOdd(num : Int): Boolean = {
      val oddTrue = num%2 == 1
      oddTrue
    }

  def getSpecializedBlock(locationXY : (Int,Int)): Module = {
    val row = locationXY._1
    val col = locationXY._2

    if(row == rows - 2 && col == cols - 2){
      Module(new NEC())
    }else if (row == 1 && col == cols - 2){
      Module(new SEC())
    }else if (row == 1 && col == 1){
      Module(new SWC())
    }else if (row == rows - 2 && col == 1){
      Module(new NWC())
    }else if (row == rows - 2 && (col > 0 && col < cols - 2) && !isOdd(col)){
      Module(new PNCB())
    }else if ((0 < row && row < rows - 2) && col == cols - 2 && !isOdd(row)){
      Module(new PECB())
    }else if (row == 1 && (col > 0 && col < cols - 1) && !isOdd(col)){
      Module(new PSCB())
    }else if ((0 < row && row < rows - 1) && col == 1 && !isOdd(row)){
      Module(new PWCB())
    }else if((row == rows - 2) && (col > 0 && col < cols - 2) && isOdd(col)){
      Module(new PNSB())
    }else if ((0 < row && row < rows - 2) && col == cols - 2 && isOdd(row)){
      Module(new PESB())
    }else if (row == 1 && (col > 0 && col < cols - 2) && isOdd(col)){
      Module(new PSSB())
    }else if ((0 < row && row < rows - 2) && col == 1 && isOdd(row)){
      Module(new PWSB())
    }else if (isOdd(row) && !isOdd(col) && (1 < row && row < rows - 2) && (1 < col && col < cols - 2)){
      Module(new IVCB())
    }else if (!isOdd(row) && isOdd(col) && (1 < row && row < rows - 2) && (1 < col && col < cols - 2)){
      Module(new IHCB())
    }else if (isOdd(row) && isOdd(col) && (1 < col && col < cols - 2) && (1 < row && row < rows - 2)){
      Module(new ISB())
    }else if (!isOdd(row) && !isOdd(col) && (1 < col && col < cols - 2) && (1 < row && row < rows - 2)){
      Module(new CLB())
    }else if((row == 0 || row == rows - 1) && (0 < col && col < cols - 1) && !isOdd(col)){
      Module(new IOpad())
    }else if((col == 0 || col == cols - 1) && (0 < row && row < rows - 1) && !isOdd(row)){
      Module(new IOpad())
    }else{
      Module(new Empty())
    }
  }

  for (r <- 0 until rows) {
    for (c <- 0 until cols) {
      if(r == rows - 2 && c == cols - 2){ //NEC
        fpga(r)(c).asInstanceOf[NEC].io.W <> fpga(r)(c-1).asInstanceOf[PNCB].io.E
        fpga(r)(c).asInstanceOf[NEC].io.S <> fpga(r-1)(c).asInstanceOf[PECB].io.N
      }else if (r == 1 && c == cols - 2){ //SEC
        fpga(r)(c).asInstanceOf[SEC].io.N <> fpga(r+1)(c).asInstanceOf[PECB].io.S
        fpga(r)(c).asInstanceOf[SEC].io.W <> fpga(r)(c-1).asInstanceOf[PSCB].io.E
      }else if (r == 1 && c == 1){ //SWC
        fpga(r)(c).asInstanceOf[SWC].io.E <> fpga(r)(c+1).asInstanceOf[PSCB].io.W
        fpga(r)(c).asInstanceOf[SWC].io.N <> fpga(r+1)(c).asInstanceOf[PWCB].io.S
      }else if (r == rows - 2 && c == 1){ //NWC
        fpga(r)(c).asInstanceOf[NWC].io.S <> fpga(r-1)(c).asInstanceOf[PWCB].io.N
        fpga(r)(c).asInstanceOf[NWC].io.E <> fpga(r)(c+1).asInstanceOf[PNCB].io.W
      }else if (r == rows - 2 && (c > 0 && c < cols - 2) && !isOdd(c)){ //PNCB
        fpga(r)(c).asInstanceOf[PNCB].io.N <> fpga(r+1)(c).asInstanceOf[IOpad].io.inside
        fpga(r)(c).asInstanceOf[PNCB].io.S <> fpga(r-1)(c).asInstanceOf[CLB].io.N
      }else if ((0 < r && r < rows - 2) && c == cols - 2 && !isOdd(r)){ //PECB
        fpga(r)(c).asInstanceOf[PECB].io.W <> fpga(r)(c-1).asInstanceOf[CLB].io.E
        fpga(r)(c).asInstanceOf[PECB].io.E <> fpga(r)(c+1).asInstanceOf[IOpad].io.inside
      }else if (r == 1 && (c > 0 && c < cols - 1) && !isOdd(c)){ //PSCB
        fpga(r)(c).asInstanceOf[PSCB].io.N <> fpga(r+1)(c).asInstanceOf[CLB].io.S
        fpga(r)(c).asInstanceOf[PSCB].io.S <> fpga(r-1)(c).asInstanceOf[IOpad].io.inside
      }else if ((0 < r && r < rows - 1) && c == 1 && !isOdd(r)){ //PWCB
        fpga(r)(c).asInstanceOf[PWCB].io.W <> fpga(r)(c-1).asInstanceOf[IOpad].io.inside
        fpga(r)(c).asInstanceOf[PWCB].io.E <> fpga(r)(c+1).asInstanceOf[CLB].io.W
      }else if((r == rows - 2) && (c > 0 && c < cols - 2) && isOdd(c)){ //PNSB
        fpga(r)(c).asInstanceOf[PNSB].io.W <> fpga(r)(c-1).asInstanceOf[PNCB].io.E
        fpga(r)(c).asInstanceOf[PNSB].io.S <> fpga(r-1)(c).asInstanceOf[IHCB].io.N
        fpga(r)(c).asInstanceOf[PNSB].io.E <> fpga(r)(c+1).asInstanceOf[PNCB].io.W
      }else if ((0 < r && r < rows - 2) && c == cols - 2 && isOdd(r)){ //PESB
        fpga(r)(c).asInstanceOf[PESB].io.N <> fpga(r+1)(c).asInstanceOf[PECB].io.S
        fpga(r)(c).asInstanceOf[PESB].io.W <> fpga(r)(c-1).asInstanceOf[IVCB].io.E
        fpga(r)(c).asInstanceOf[PESB].io.S <> fpga(r-1)(c).asInstanceOf[PECB].io.N
      }else if (r == 1 && (c > 0 && c < cols - 2) && isOdd(c)){ //PSSB
        fpga(r)(c).asInstanceOf[PSSB].io.W <> fpga(r)(c-1).asInstanceOf[PSCB].io.E
        fpga(r)(c).asInstanceOf[PSSB].io.N <> fpga(r+1)(c).asInstanceOf[IHCB].io.S
        fpga(r)(c).asInstanceOf[PSSB].io.E <> fpga(r)(c+1).asInstanceOf[PSCB].io.W
      }else if ((0 < r && r < rows - 2) && c == 1 && isOdd(r)){ //PWSB
        fpga(r)(c).asInstanceOf[PWSB].io.N <> fpga(r+1)(c).asInstanceOf[PWCB].io.S
        fpga(r)(c).asInstanceOf[PWSB].io.E <> fpga(r)(c+1).asInstanceOf[IVCB].io.W
        fpga(r)(c).asInstanceOf[PWSB].io.S <> fpga(r-1)(c).asInstanceOf[PWCB].io.N
      }else if (isOdd(r) && !isOdd(c) && (1 < r && r < rows - 2) && (1 < c && c < cols - 2)){ //IVCB
        fpga(r)(c).asInstanceOf[IVCB].io.N <> fpga(r+1)(c).asInstanceOf[CLB].io.S
        fpga(r)(c).asInstanceOf[IVCB].io.S <> fpga(r-1)(c).asInstanceOf[CLB].io.N
      }else if (!isOdd(r) && isOdd(c) && (1 < r && r < rows - 2) && (1 < c && c < cols - 2)){ //IHCB
        fpga(r)(c).asInstanceOf[IHCB].io.W <> fpga(r)(c-1).asInstanceOf[CLB].io.E
        fpga(r)(c).asInstanceOf[IHCB].io.E <> fpga(r)(c+1).asInstanceOf[CLB].io.W
      }else if (isOdd(r) && isOdd(c) && (1 < c && c < cols - 2) && (1 < r && r < rows - 2)){ //ISB
        fpga(r)(c).asInstanceOf[ISB].io.N <> fpga(r+1)(c).asInstanceOf[IHCB].io.S
        fpga(r)(c).asInstanceOf[ISB].io.S <> fpga(r-1)(c).asInstanceOf[IHCB].io.N
        fpga(r)(c).asInstanceOf[ISB].io.E <> fpga(r)(c+1).asInstanceOf[IVCB].io.W
        fpga(r)(c).asInstanceOf[ISB].io.W <> fpga(r)(c-1).asInstanceOf[IVCB].io.E
      }else if (!isOdd(r) && !isOdd(c) && (1 < c && c < cols - 2) && (1 < r && r < rows - 2)){ //CLB
      }else if ((r == 0 || r == rows - 1) && (0 < c && c < cols - 1) && !isOdd(c)){ //T/B IOpads
      }else if ((c == 0 || c == cols - 1) && (0 < r && r < rows - 1) && !isOdd(r)){ //L/R IOpads
      }else {}
    }
  }

/*
//Connectivity of Main Tiles (no peripheral & IO blocks)
for (r <- 0 until yTiles-1) {
  for (c <- 0 until xTiles-1) {
    //CLB w/ IHCB to the right
    fpga(2*r+2)(2*c+2).asInstanceOf[CLB].io.E <> fpga(2*r+2)(2*c+3).asInstanceOf[IHCB].io.W
    //IVCB w/ ISB to the right
    fpga(2*r+3)(2*c+2).asInstanceOf[IVCB].io.E <> fpga(2*r+3)(2*r+3).asInstanceOf[ISB].io.W
    //CLB w/ IVCB above
    fpga(2*r+2)(2*c+2).asInstanceOf[CLB].io.N <> fpga(2*r+3)(2*c+2).asInstanceOf[IVCB].io.S
    //IVCB w/ CLB above
    fpga(2*r+3)(2*c+2).asInstanceOf[IVCB].io.N <> fpga(2*r+4)(2*c+2).asInstanceOf[CLB].io.S
  }
  //connect rightmost column
  //connect to the right
  fpga(2*r+2)(cols-4).asInstanceOf[IHCB].io.E <> fpga(2*r+2)(cols-3).asInstanceOf[CLB].io.W
  fpga(2*r+3)(cols-4).asInstanceOf[ISB].io.E <> fpga(2*r+3)(cols-3).asInstanceOf[IVCB].io.W
  //connect between
  fpga(2*r+2)(cols-3).asInstanceOf[CLB].io.N <> fpga(2*r+3)(cols-3).asInstanceOf[IVCB].io.S
  fpga(2*r+3)(cols-3).asInstanceOf[IVCB].io.N <> fpga(2*r+4)(cols-3).asInstanceOf[CLB].io.S
}

//Connect top & bottom connection blocks & IOpads into Main Tiles
for (c <- 0 until xTiles) {
  fpga(rows)(2*c+2).asInstanceOf[IOpad].io.inside <> fpga(rows-1)(2*c+2).asInstanceOf[PNCB].io.N
  fpga(rows-1)(2*c+2).asInstanceOf[PNCB].io.S <> fpga(rows-2)(2*c+2).asInstanceOf[CLB].io.N
  fpga(0)(2*c+2).asInstanceOf[IOpad].io.inside <> fpga(1)(2*c+2).asInstanceOf[PSCB].io.S
  fpga(1)(2*c+2).asInstanceOf[PSCB].io.N <> fpga(2)(2*c+2).asInstanceOf[CLB].io.S
}
//Left & right connection blocks & IOpads
for (r <- 0 until yTiles) {
  fpga(2*r+2)().asInstanceOf[].io. <> fpga()().asInstanceOf[].io.
  fpga()().asInstanceOf[].io. <> fpga()().asInstanceOf[].io.
  fpga()().asInstanceOf[].io. <> fpga()().asInstanceOf[].io.
  fpga()().asInstanceOf[].io. <> fpga()().asInstanceOf[].io.
}

*/
  //below code works with tester
  val IHCB1 = Module(new IHCB())
  val CLB1 = Module(new CLB())
  //val CLB1 = fpga(2)(2).asInstanceOf[CLB].io
  CLB1.io.E <> IHCB1.io.W
  io.sram0 <> IHCB1.io.blkBits
  io.sram1 <> CLB1.io.blkBits
  IHCB1.io.S.p0 := io.ipad0
  io.opad1 := CLB1.io.S.p6

}

class TopLevel2Test(c: TopLevel2) extends Tester(c) {
  poke(c.io.sram0, int(UInt("h0000_0000_0000_0000_0010")))
  poke(c.io.sram1, int(UInt("h0003_0000_0000_0000_0006")))
  poke(c.io.ipad0, 0)
  expect(c.io.opad1, 0)
  step(1)
  poke(c.io.ipad0, 1)
  expect(c.io.opad1, 1)
}

object TopLevel2 {
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new TopLevel2())) {
      c => new TopLevel2Test(c) }
  }
}
