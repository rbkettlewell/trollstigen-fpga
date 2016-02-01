import fpga.blocks.AnyBlock
import scala.collection.mutable.MutableList

package fpga{
  object Types{
    type Direction = String
    type PinTrack = Int
    type Switch = Boolean
    type ConnectClass = String
    type Segment = (Direction, PinTrack , Switch, ConnectClass)
    type Path = (Segment,Array[Segment])
    type PathDefinition = (Direction, PinTrack => PinTrack, ConnectClass)
    type Connectivity = Array[Path]
    type ConnectionType = String
    type ConnectNumber = Int
    type FPGABlocks = Array[Array[AnyBlock]]
    type BlockName = String
    type LocationXY  = (Int,Int)
    type Subblock  = Int
    type PlaceInfo = (BlockName, LocationXY, Subblock)
    type Placement = List[PlaceInfo]
    type RouteInfo = (LocationXY, ConnectionType, ConnectNumber, ConnectClass)
    type BlifModel = String
    type Covering = Array[String]
    type BlifInputs = Array[BlockName]
    type BlifOutput = BlockName
    type ResetVal = String
    type BlifInfo = (BlifModel, BlifInputs , BlifOutput, Covering, ResetVal)
    type Blifs = Array[BlifInfo]
    type Route = (LocationXY, Segment, Segment)
    type Routes = Array[Route]
  }
}
