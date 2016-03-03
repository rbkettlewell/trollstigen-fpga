//`timescale 1ns / 1ns 

module TestNand280;

  logic a,b,y;
  wire VDD = 1'b1;
  wire GND = 1'b0;

  VLSILab_280nmNAND_schematic g1 (.output_(y), .GND(GND), .VDD(VDD), .A(a), .B(b));

initial 
  begin
  a = 1'b0;
  b = 1'b0;
  #10ns a = '1;
  #10ns b = '0;
  #10ns a = '0;
  #10ns b = '1;
  #10ns a = '1;
  #10ns b = '1;
  #10ns a = 'z;
  #10ns b = '0;
  #10ns a = '0;
  #10ns b = 'x;
  #10ns a = 'z;
  #10ns b = 'z;
  end

endmodule  