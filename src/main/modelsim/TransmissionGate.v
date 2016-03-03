// Library - VLSILab, Cell - TransmissionGate, View - schematic
// LAST TIME SAVED: Feb 25 14:02:43 2016
// NETLIST TIME: Feb 25 16:45:21 2016
//`timescale 1ns / 1ns 

module TransmissionGate ( GND, VDD, in, out, SRAMin );

inout  GND, VDD, in, out;

input  SRAMin;


specify 
    //specparam CDS_LIBNAME  = "VLSILab";
    //specparam CDS_CELLNAME = "TransmissionGate";
    //specparam CDS_VIEWNAME = "schematic";
endspecify

pfet  T3 ( .G(SRAMin), .B(VDD), .S(VDD), .D(SRAM_bar));
pfet  T0 ( .G(SRAMin), .B(VDD), .S(out), .D(in));
nfet  T2 ( .G(SRAMin), .B(GND), .S(GND), .D(SRAM_bar));
nfet  T1 ( .G(SRAM_bar), .B(GND), .S(out), .D(in));

endmodule
