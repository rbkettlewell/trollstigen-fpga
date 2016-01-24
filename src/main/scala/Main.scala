import fpga.Types._
import fpga.blocks._

object Main {
  def main(args: Array[String]) {
    val w = new SwitchBlock((0,0),List())
    println("My switch block position is "+ w.location)
  }
}
