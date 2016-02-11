package FPGASimulation

import Chisel._

class SRAMBlock extends Module {
  val io = new Bundle {
    val bRow     = UInt(INPUT,9)
    val bRead    = UInt(INPUT,1)
    val bWrite   = UInt(INPUT,1)
    val bitWrite = UInt(INPUT,8)
    val bitRead  = UInt(OUTPUT,8)
    val blkBits  = UInt(OUTPUT,72)
  }

  //Default Assignment
  io.bitRead := UInt(0)
  io.blkBits := UInt(0)

  val bitReadFanin = Vec.fill(9){UInt(width = 8)}

  (0 until 9).foreach{i=>
    val wSel  = Module(new WordSelector())
    val rWord = Module(new SRAMWord())

    wSel.io.bRow := io.bRow(i)
    wSel.io.bRead := io.bRead
    wSel.io.bWrite := io.bWrite
    
    rWord.io.write  := wSel.io.writeWord
    rWord.io.writeN := wSel.io.writeWordN
    rWord.io.read   := wSel.io.readWord
    rWord.io.readN  := wSel.io.readWordN
    rWord.io.bitWrite := io.bitWrite
    bitReadFanin(i) := rWord.io.bitRead

    // Associate programming subwords into larger block
    (i*8 until i*8+ 8).foreach(j => io.blkBits(j) := rWord.io.progBits(j - i*8))
  }

  // TODO verify that this fold operation builds a proper fanin structure
  io.bitRead := bitReadFanin.foldLeft(UInt(0))(_|_)

}
class SRAMBlockTests(c: SRAMBlock) extends Tester(c) {
  
  //Test write 1
  poke(c.io.bRow, 1)
  poke(c.io.bWrite, 0)
  step(1)
  poke(c.io.bitWrite, 1)
  step(1)
  poke(c.io.bWrite, 0)
  poke(c.io.bRead, 1)
  step(1)
  expect(c.io.bitRead, 0)
  expect(c.io.blkBits, 0)
  //Test write zero
  poke(c.io.bRow, 1)
  poke(c.io.bWrite, 1)
  step(1)
  poke(c.io.bitWrite, 1)
  step(1)
  poke(c.io.bWrite, 0)
  poke(c.io.bRead, 1)
  step(1)
  expect(c.io.bitRead, 1)
  expect(c.io.blkBits, 1)
  //Test write 1
  poke(c.io.bRow, 2)
  poke(c.io.bWrite, 0)
  step(1)
  poke(c.io.bitWrite, 1)
  step(1)
  poke(c.io.bWrite, 0)
  poke(c.io.bRead, 1)
  step(1)
  expect(c.io.bitRead, 0)
  expect(c.io.blkBits, 1)
  //Test write zero
  poke(c.io.bRow, 2)
  poke(c.io.bWrite, 1)
  step(1)
  poke(c.io.bitWrite, 1)
  step(1)
  poke(c.io.bWrite, 0)
  poke(c.io.bRead, 1)
  step(1)
  expect(c.io.bitRead, 1)
  expect(c.io.blkBits, 257)
}
object SRAMBlock{
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new SRAMBlock())) {
      c => new SRAMBlockTests(c) }
  }
}
