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

  def connectBlocksMutate(fpga:Array[Array[Module]]){
    for (r <- 0 until rows) {
      for (c <- 0 until cols) {
        if(r == rows - 2 && c == cols - 2){ //NEC
          fpga(r)(c).asInstanceOf[NEC].io.W <> fpga(r)(c-1).asInstanceOf[PNCB].io.E
          fpga(r)(c).asInstanceOf[NEC].io.S <> fpga(r-1)(c).asInstanceOf[PECB].io.N
        /*}else if (r == 1 && c == cols - 2){ //SEC
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
          fpga(r)(c).asInstanceOf[IVCB].io.S <> fpga(r-1)(c).asInstanceOf[CLB].io.N*/
        }else if (!isOdd(r) && isOdd(c) && (1 < r && r < rows - 2) && (1 < c && c < cols - 2)){ //IHCB
          fpga(r)(c).asInstanceOf[IHCB].io.W <> fpga(r)(c-1).asInstanceOf[CLB].io.E
          fpga(r)(c).asInstanceOf[IHCB].io.E <> fpga(r)(c+1).asInstanceOf[CLB].io.W
        // }else if (isOdd(r) && isOdd(c) && (1 < c && c < cols - 2) && (1 < r && r < rows - 2)){ //ISB
        //   fpga(r)(c).asInstanceOf[ISB].io.N <> fpga(r+1)(c).asInstanceOf[IHCB].io.S
        //   fpga(r)(c).asInstanceOf[ISB].io.S <> fpga(r-1)(c).asInstanceOf[IHCB].io.N
        //   fpga(r)(c).asInstanceOf[ISB].io.E <> fpga(r)(c+1).asInstanceOf[IVCB].io.W
        //   fpga(r)(c).asInstanceOf[ISB].io.W <> fpga(r)(c-1).asInstanceOf[IVCB].io.E
        }else if (!isOdd(r) && !isOdd(c) && (1 < c && c < cols - 2) && (1 < r && r < rows - 2)){ //CLB
        }else if ((r == 0 || r == rows - 1) && (0 < c && c < cols - 1) && !isOdd(c)){ //T/B IOpads
        }else if ((c == 0 || c == cols - 1) && (0 < r && r < rows - 1) && !isOdd(r)){ //L/R IOpads
        }else {}
      }
    }
  }
}
