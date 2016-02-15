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
  private val yTiles = 8 
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
      columnDecoder.io.byteRead(j) := SRAMArray(i)(j).bitRead
      SRAMArray(i)(j).bitWrite := columnDecoder.io.byteWrite(j)
    }
  }

  val casReg = Reg(UInt(0, 5))
  val rasReg = Reg(UInt(0, 6))
  val basReg = Reg(UInt(0, 4))

  io.cDataExtOut := UInt(0)
  columnDecoder.io.WE := ~io.WEN
  columnDecoder.io.RE := ~io.REN
  columnDecoder.io.cDataIn := UInt(0)

  when (io.OEN === UInt(1)) {
    when (io.CASN === UInt(0)) {
      casReg := io.cDataExtIn(4,0)
    } .elsewhen (io.RASN === UInt(0)) {
      rasReg := io.cDataExtIn(5,0)
    } .elsewhen (io.BASN === UInt(0)) {
      basReg := io.cDataExtIn(3,0)
    }
    when (io.WEN === UInt(0)) {
      columnDecoder.io.cDataIn := io.cDataExtIn
    }
  } .elsewhen (io.REN === UInt(0)) {
      io.cDataExtOut := columnDecoder.io.cDataOut
  } .otherwise {
  }

  columnDecoder.io.cAddr := casReg
  rowDecoder.io.rAddr := rasReg
  rowDecoder.io.bAddr := basReg
}

class SRAMTopLevelTests(c: SRAMTopLevel) extends Tester(c) {
  poke(c.io.BASN, 1)
  poke(c.io.RASN, 1)
  poke(c.io.CASN, 1)
  poke(c.io.OEN, 1)
  poke(c.io.WEN, 1)
  poke(c.io.REN, 1)
  step(1)
  poke(c.io.cDataExtIn, 2)
  step(1)
  step(1)
  poke(c.io.BASN, 0)
  step(1)
  step(1)
  poke(c.io.BASN, 1)
  step(1)
  step(1)
  poke(c.io.cDataExtIn, 1)
  step(1)
  step(1)
  poke(c.io.RASN, 0)
  step(1)
  step(1)
  poke(c.io.RASN, 1)
  step(1)
  step(1)
  poke(c.io.cDataExtIn, 1)
  step(1)
  step(1)
  poke(c.io.CASN, 0)
  step(1)
  step(1)
  poke(c.io.CASN, 1)
  step(1)
  step(1)
  poke(c.io.cDataExtIn, 32)
  step(1)
  step(1)
  poke(c.io.WEN, 0)
  step(4)
  poke(c.io.WEN, 1)
  step(1)
  step(1)
  poke(c.io.cDataExtIn, 2)
  step(1)
  step(1)
  poke(c.io.BASN, 0)
  step(1)
  step(1)
  poke(c.io.BASN, 1)
  step(1)
  step(1)
  poke(c.io.cDataExtIn, 1)
  step(1)
  step(1)
  poke(c.io.RASN, 0)
  step(1)
  step(1)
  poke(c.io.RASN, 1)
  step(1)
  step(1)
  poke(c.io.cDataExtIn, 1)
  step(1)
  step(1)
  poke(c.io.CASN, 0)
  step(1)
  step(1)
  poke(c.io.CASN, 1)
  step(1)
  step(1)
  poke(c.io.REN, 0)
  step(4)
  poke(c.io.OEN, 0)
  step(1)
  step(1)
  expect(c.io.cDataExtOut, 32)
  poke(c.io.REN, 1)
  step(1)
  step(1)
  poke(c.io.OEN, 1)
}

object SRAMTopLevel{
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new SRAMTopLevel())) {
      c => new SRAMTopLevelTests(c) }
  }
}
