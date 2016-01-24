package fpga{
  package object types{
    type Direction = String
    type Track = Int
    type Switch = Boolean
    type Segment = (Direction, Track, Switch)
    type Path = (Segment,List[Segment])
    type PathDefinition = (Direction, Track => Track)
    type Connectivity = List[Path]
  }
}
