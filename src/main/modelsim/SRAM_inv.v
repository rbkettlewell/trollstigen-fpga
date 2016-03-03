// Library - VLSILab, Cell - SRAM_inv, View - schematic
// LAST TIME SAVED: Feb 22 11:53:07 2016
// NETLIST TIME: Feb 25 16:45:21 2016
//`timescale 1ns / 1ns 

module SRAM_inv ( output_, VDD, GND, input_ );

output  output_;

inout  VDD, GND;

input  input_;


specify 
    //specparam CDS_LIBNAME  = "VLSILab";
    //specparam CDS_CELLNAME = "SRAM_inv";
    //specparam CDS_VIEWNAME = "schematic";
endspecify

pfet  T0 ( .G(input_), .B(VDD), .S(VDD), .D(output_));
nfet  T1 ( .G(input_), .B(GND), .S(GND), .D(output_));

endmodule
