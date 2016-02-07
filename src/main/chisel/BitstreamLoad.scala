package FPGASimulation

import scala.io.Source
import Chisel._

class BitstreamLoad(bitFile : String){
  private val source = Source.fromFile(bitFile)
  private val bitstream = try source.mkString.toArray finally source.close()

  private val xTiles = 8
  private val yTiles = 16
  private val cols = xTiles*2 + 3
  private val rows = yTiles*2 + 3
  private val BlockSize = 72
  private val Debug = true

  
  def getBlkBits(locationXY : (Int,Int)): UInt ={
    val row = locationXY._1
    val col = locationXY._2
    val readOffset = BlockSize*(cols*row + col)

    if(Debug && (locationXY == (0,14)))
      println(bitstream.slice(readOffset, readOffset + BlockSize).reverse.mkString)
    Cat(bitstream.slice(readOffset, readOffset + BlockSize).reverse.map(bit => UInt(bit.toString.toInt)))
  }
}
