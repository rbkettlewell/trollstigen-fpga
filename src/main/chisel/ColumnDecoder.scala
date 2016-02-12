package FPGASimulation

import Chisel._

class ColumnDecoder extends Module {
  val io = new Bundle {
    val WE = UInt (INPUT, 1)
    val RE = UInt (INPUT, 1)
    val bRead  = UInt(OUTPUT,19)
    val bWrite = UInt(OUTPUT,19)
    val byteRead  = Vec.fill(19){UInt(INPUT,8)}
    val byteWrite = Vec.fill(19){UInt(OUTPUT,8)}
    val cAddr    = UInt(INPUT,5)
    val cDataIn  = UInt(INPUT,8)
    val cDataOut = UInt(OUTPUT,8)
  }

  val colSelect = UInt()
  io.bRead := UInt(0)
  io.bWrite := UInt(0)

  when (io.cAddr === UInt(0)){
    colSelect := UInt("b000_0000_0000_0000_0001")
  }.elsewhen (io.cAddr === UInt(1)){
    colSelect := UInt("b000_0000_0000_0000_0010")
  }.elsewhen (io.cAddr === UInt(2)){
    colSelect := UInt("b000_0000_0000_0000_0100")
  }.elsewhen (io.cAddr === UInt(3)){
    colSelect := UInt("b000_0000_0000_0000_1000")
  }.elsewhen (io.cAddr === UInt(4)){
    colSelect := UInt("b000_0000_0000_0001_0000")
  }.elsewhen (io.cAddr === UInt(5)){
    colSelect := UInt("b000_0000_0000_0010_0000")
  }.elsewhen (io.cAddr === UInt(6)){
    colSelect := UInt("b000_0000_0000_0100_0000")
  }.elsewhen (io.cAddr === UInt(7)){
    colSelect := UInt("b000_0000_0000_1000_0000")
  }.elsewhen (io.cAddr === UInt(8)){
    colSelect := UInt("b000_0000_0001_0000_0000")
  }.elsewhen (io.cAddr === UInt(9)){
    colSelect := UInt("b000_0000_0010_0000_0000")
  }.elsewhen (io.cAddr === UInt(10)){
    colSelect := UInt("b000_0000_0100_0000_0000")
  }.elsewhen (io.cAddr === UInt(11)){
    colSelect := UInt("b000_0000_1000_0000_0000")
  }.elsewhen (io.cAddr === UInt(12)){
    colSelect := UInt("b000_0001_0000_0000_0000")
  }.elsewhen (io.cAddr === UInt(13)){
    colSelect := UInt("b000_0010_0000_0000_0000")
  }.elsewhen (io.cAddr === UInt(14)){
    colSelect := UInt("b000_0100_0000_0000_0000")
  }.elsewhen (io.cAddr === UInt(15)){
    colSelect := UInt("b000_1000_0000_0000_0000")
  }.elsewhen (io.cAddr === UInt(16)){
    colSelect := UInt("b001_0000_0000_0000_0000")
  }.elsewhen (io.cAddr === UInt(17)){
    colSelect := UInt("b010_0000_0000_0000_0000")
  }.elsewhen (io.cAddr === UInt(18)){
    colSelect := UInt("b100_0000_0000_0000_0000")
  }.otherwise{
    colSelect := UInt("b000_0000_0000_0000_0000")
  }

  //select column to read, but fold all the time
  when (io.RE === UInt(1)) {
    io.bRead := colSelect
  }
  io.cDataOut := io.byteRead.foldLeft(UInt(0))(_|_)

  //select column to write, and write the data from cDataIn
  io.byteWrite := Vec.fill(19){UInt(0)}
  when (io.WE === UInt(1)) {
    io.bWrite := colSelect
    io.byteWrite(io.cAddr) := io.cDataIn
  }
}
class ColumnDecoderTests(c: ColumnDecoder) extends Tester(c) {
  poke(c.io.cAddr, 0)
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
  expect(c.io.cDataOut, 1)
}

object ColumnDecoder{
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new ColumnDecoder())) {
      c => new ColumnDecoderTests(c) }
  }
}
