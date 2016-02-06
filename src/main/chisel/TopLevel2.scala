package FPGASimulation

import Chisel._

class TopLevel extends Module{
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

  //below code works with tester
  val IHCB1 = Module(new IHCB())
  //val CLB1 = Module(new CLB())
  val CLB1 = fpga(2)(2).asInstanceOf[CLB].io
  CLB1.E <> IHCB1.io.W
  io.sram0 <> IHCB1.io.blkBits
  io.sram1 <> CLB1.blkBits
  IHCB1.io.S.p0 := io.ipad0
  io.opad1 := CLB1.S.p6
}

class TopLevelTest(c: TopLevel) extends Tester(c) {
  poke(c.io.sram0, int(UInt("h0000_0000_0000_0000_0010")))
  poke(c.io.sram1, int(UInt("h0003_0000_0000_0000_0006")))
  poke(c.io.ipad0, 0)
  expect(c.io.opad1, 0)
  step(1)
  poke(c.io.ipad0, 1)
  expect(c.io.opad1, 1)
}

object TopLevel {
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new TopLevel())) {
      c => new TopLevelTest(c) }
  }
}
