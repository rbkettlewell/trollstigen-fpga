import scala.io.Source

val bitstream = scala.io.Source.fromFile("bitstream.bin").mkString

(0 until 3249).foreach( i => println(bitstream.slice(i*8,i*8+8)))
