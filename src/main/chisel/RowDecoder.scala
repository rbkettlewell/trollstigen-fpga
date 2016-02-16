package FPGASimulation

import Chisel._

class RowDecoder extends Module {
  private val rows = 19

  val io = new Bundle {
    val rAddr = UInt (INPUT, 6)
    val bAddr = UInt (INPUT, 4)
    val gRows = Vec.fill(rows){UInt(OUTPUT,9)}
  }

  val decoderOutput = Vec.fill(rows){Module(new BlockDecoder()).io}

  (0 until rows).foreach{i=> 
    decoderOutput(i).bAddr := io.bAddr
    io.gRows(i):= decoderOutput(i).bRow
  }
    
  (0 until rows).foreach{i=>
    when (UInt(i) === io.rAddr){
      decoderOutput(i).bAddr := io.bAddr
    }.otherwise{
      decoderOutput(i).bAddr := UInt(10)
    }
  }
}
class RowDecoderTests(c: RowDecoder) extends Tester(c) {
  private val rows = 19
  poke(c.io.rAddr, 0)
  (0 until rows).foreach{r=>
    poke(c.io.rAddr, r)
    (0 until 9).foreach{i=>
      poke(c.io.bAddr, i)
      if (r == 0)
        expect(c.io.gRows(r), Math.pow(2,i).toInt)
      else{
        expect(c.io.gRows(r-1), 0)
        expect(c.io.gRows(r), Math.pow(2,i).toInt)
      }
      step(1)
    }
  }
}
object RowDecoder{
  def main(args: Array[String]): Unit = {
    val tutArgs = args.slice(1, args.length)
    chiselMainTest(tutArgs, () => Module(new RowDecoder())) {
      c => new RowDecoderTests(c) }
  }
}
