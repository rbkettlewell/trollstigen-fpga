// Library - VLSILab, Cell - 280nmNAND, View - schematic
// LAST TIME SAVED: Feb 23 15:49:23 2016
// NETLIST TIME: Feb 25 16:45:21 2016
//`timescale 1ns / 1ns 

module VLSILab_280nmNAND_schematic ( output_, GND, VDD, A, B );

output  output_;

//wire dirty_out;

inout  GND, VDD;

input  A, B;

//assign output_ = (dirty_out !== 1'bx) ? dirty_out : 1'bz;
//assign output_ = dirty_out;
specify 
    //specparam CDS_LIBNAME  = "VLSILab";
    //specparam CDS_CELLNAME = "280nmNAND";
    //specparam CDS_VIEWNAME = "schematic";
endspecify

nfet  T2 ( .G(A), .B(GND), .S(net22), .D(output_));
nfet  T1 ( .G(B), .B(GND), .S(GND), .D(net22));
pfet  T3 ( .G(B), .B(VDD), .S(VDD), .D(output_));
pfet  T0 ( .G(A), .B(VDD), .S(VDD), .D(output_));

endmodule
