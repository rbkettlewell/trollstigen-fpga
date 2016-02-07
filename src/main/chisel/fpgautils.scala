package FPGASimulation

import Chisel._

class FPGAUtils{
 
  type LocationXY  = (Int,Int)

  private val xTiles = 8
  private val yTiles = 16
  private val cols = xTiles*2 + 3
  private val rows = yTiles*2 + 3
  private val bitsteamBlocks = new BitstreamLoad("bitstream.bin")

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

    val sramBlkBits = bitsteamBlocks.getBlkBits(locationXY)

    if(row == rows - 2 && col == cols - 2){
      val nec = Module(new NEC())
      nec.io.blkBits := sramBlkBits
      nec
    }else if (row == 1 && col == cols - 2){
      val sec = Module(new SEC())
      sec.io.blkBits := sramBlkBits
      sec
    }else if (row == 1 && col == 1){
      val swc = Module(new SWC())
      swc.io.blkBits := sramBlkBits
      swc
    }else if (row == rows - 2 && col == 1){
      val nwc = Module(new NWC())
      nwc.io.blkBits := sramBlkBits
      nwc
    }else if (row == rows - 2 && (col > 0 && col < cols - 2) && !isOdd(col)){
      val pncb = Module(new PNCB())
      pncb.io.blkBits := sramBlkBits
      pncb
    }else if ((0 < row && row < rows - 2) && col == cols - 2 && !isOdd(row)){
      val pecb = Module(new PECB())
      pecb.io.blkBits := sramBlkBits
      pecb
    }else if (row == 1 && (col > 0 && col < cols - 1) && !isOdd(col)){
      val pscb = Module(new PSCB())
      pscb.io.blkBits := sramBlkBits
      pscb 
    }else if ((0 < row && row < rows - 1) && col == 1 && !isOdd(row)){
      val pwcb = Module(new PWCB())
      pwcb.io.blkBits := sramBlkBits
      pwcb
    }else if((row == rows - 2) && (col > 0 && col < cols - 2) && isOdd(col)){
      val pnsb = Module(new PNSB())
      pnsb.io.blkBits := sramBlkBits
      pnsb
    }else if ((0 < row && row < rows - 2) && col == cols - 2 && isOdd(row)){
      val pesb = Module(new PESB())
      pesb.io.blkBits := sramBlkBits
      pesb
    }else if (row == 1 && (col > 0 && col < cols - 2) && isOdd(col)){
      val pssb = Module(new PSSB())
      pssb.io.blkBits := sramBlkBits
      pssb
    }else if ((0 < row && row < rows - 2) && col == 1 && isOdd(row)){
      val pwsb = Module(new PWSB())
      pwsb.io.blkBits := sramBlkBits
      pwsb
    }else if (isOdd(row) && !isOdd(col) && (1 < row && row < rows - 2) && (1 < col && col < cols - 2)){
      val ivcb = Module(new IVCB())
      ivcb.io.blkBits := sramBlkBits
      ivcb 
    }else if (!isOdd(row) && isOdd(col) && (1 < row && row < rows - 2) && (1 < col && col < cols - 2)){
      val ihcb = Module(new IHCB())
      ihcb.io.blkBits := sramBlkBits
      ihcb
    }else if (isOdd(row) && isOdd(col) && (1 < col && col < cols - 2) && (1 < row && row < rows - 2)){
      val isb = Module(new ISB())
      isb.io.blkBits := sramBlkBits
      isb
    }else if (!isOdd(row) && !isOdd(col) && (1 < col && col < cols - 2) && (1 < row && row < rows - 2)){
      val clb = Module(new CLB())
      clb.io.blkBits := sramBlkBits
      clb
    }else if((row == 0 || row == rows - 1) && (0 < col && col < cols - 1) && !isOdd(col)){
      val iob = Module(new IOpad()) 
      iob.io.blkBits := sramBlkBits
      iob
    }else if((col == 0 || col == cols - 1) && (0 < row && row < rows - 1) && !isOdd(row)){
      val iob = Module(new IOpad())
      iob.io.blkBits := sramBlkBits
      iob
    }else{
      val empty = Module(new Empty())
      empty.io.blkBits := sramBlkBits
      empty
    }
  }

}
