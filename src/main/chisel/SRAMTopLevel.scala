package FPGASimulation

import Chisel._

class SRAMTopLevel extends Module {
  val io = new Bundle {
    val WEN = UInt (INPUT, 1)
    val REN = UInt (INPUT, 1)
    val CASN = UInt (INPUT, 1)
    val RASN = UInt (INPUT, 1)
    val BASN = UInt (INPUT, 1)
    val OEN = UInt (INPUT, 1)
    val cDataExtIn  = UInt(INPUT,8)
    val cDataExtOut = UInt(OUTPUT,8)
  }

  private val xTiles = 8
  private val yTiles = 16
  private val cols = xTiles*2 + 3
  private val rows = yTiles*2 + 3

  val rowDecoder = Module(new RowDecoder())
  val columnDecoder = Module(new ColumnDecoder())
  val SRAMArray = Vec.fill(rows){Vec.fill(cols){Module(new SRAMBlock()).io}}
  for (i <- 0 until rows) {
    for (j <- 0 until cols) {
      SRAMArray(i)(j).bRow := rowDecoder.io.gRows(i)
      SRAMArray(i)(j).bRead := columnDecoder.io.bRead(j)
      SRAMArray(i)(j).bWrite := columnDecoder.io.bWrite(j)
      SRAMArray(i)(j).bitRead := columnDecoder.io.byteRead(j)
      SRAMArray(i)(j).bitWrite := columnDecoder.io.byteWrite(j)
    }
  }

  val casReg = Reg(UInt(0, 5))
  val rasReg = Reg(UInt(0, 6))
  val basReg = Reg(UInt(0, 4))

  when (io.OEN === UInt(1)) {
    columnDecoder.io.WE := ~io.WEN
    columnDecoder.io.RE := ~io.REN
    when (io.CASN === UInt(0)) {
      casReg := io.cDataExtIn(0,4)
    } .elsewhen (io.RASN === UInt(0)) {
      rasReg := io.cDataExtIn(0,5)
    } .elsewhen (io.BASN === UInt(0)) {
      basReg := io.cDataExtIn(0,3)
    }
    when (io.WEN === UInt(0)) {
      columnDecoder.io.cDataIn := io.cDataExtIn
    }
  } .otherwise {
      when (io.REN === UInt(0)) {
        io.cDataExtOut := columnDecoder.io.cDataOut
      }
  }

  columnDecoder.io.cAddr := casReg
  rowDecoder.io.rAddr := rasReg
  rowDecoder.io.bAddr := basReg
}

class SRAMTopLevelTests(c: SRAMTopLevel) extends Tester(c) {
/*  poke(c.io.cAddr, 0)
  poke(c.io.WE, 1)
  poke(c.io.RE, 0)
  poke(c.io.cDataIn, 1)
  expect(c.io.bWrite, 1)
  expect(c.io.bRead, 0)
  (0 until 19).foreach{i=>
    if (i == 0)
      expect(c.io.byteWrite(i), 1)
    else{
      expect(c.io.byteWrite(i), 0)
    }
  }
  poke(c.io.WE, 0)
  poke(c.io.RE, 1)
  expect(c.io.bWrite, 0)
  expect(c.io.bRead, 1)
  expect(c.io.byteWrite(0), 0)
  (0 until 19).foreach{i=>
    if (i == 0)
      poke(c.io.byteRead(i), 1)
    else {
      poke(c.io.byteRead(i), 0)
    }
  }
  expect(c.io.cDataOut, 1)*/
}

object SRAMTopLevel{
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new SRAMTopLevel())) {
      c => new SRAMTopLevelTests(c) }
  }
}
