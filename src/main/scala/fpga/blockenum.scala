package fpga{
  object BlockEnum extends Enumeration{
    type BlockEnum = Value
    /* Fpga Block Enum mapping:
      ("Northeast Corner")                : NEC
      ("Southeast Corner")                : SEC
      ("Southwest Corner")                : SWC
      ("Northwest Corner")                : NWC

      ("Perimeter North Connection Block"): PNCB
      ("Perimeter East Connection Block") : PECB
      ("Perimeter South Connection Block"): PSCB
      ("Perimeter West Connection Block") : PWCB

      ("Perimeter North Switch Block")    : PNSB
      ("Perimeter East Switch Block")     : PESB
      ("Perimeter South Switch Block")    : PSSB
      ("Perimeter West Switch Block")     : PWSB

      ("Internal Vertical Connection Block")    : IVCB
      ("Internal Horizontal Connection Block")  : IHCB

      ("Internal Switch Block")           : ISB
      ("Configurable Logic Block")        : CLB
      ("Input Ouput Logic Block")         : IOB */

     val NEC, SEC, SWC, NWC,
      PNCB, PECB, PSCB, PWCB,
      PNSB, PESB, PSSB, PWSB,
      IVCB, IHCB,
      ISB, CLB, IOB, Empty = Value
  }
}
