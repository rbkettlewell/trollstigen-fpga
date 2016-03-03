// Library - VLSILab, Cell - SRAMcell_Harrison, View - schematic
// LAST TIME SAVED: Feb 22 10:52:28 2016
// NETLIST TIME: Feb 25 16:45:21 2016
//`timescale 1ns / 1ns 

module SRAMcell_Harrison (io_progBit, GND, VDD, io_bitRead, io_bitWrite, io_read,
     io_readN, io_write, io_writeN);

output  io_progBit;

inout  GND, VDD, io_bitRead, io_bitWrite;

input  io_read, io_readN, io_write, io_writeN;


nfet  T3 ( .G(io_writeN), .B(GND), .S(net035), .D(bit_w));
nfet  T8 ( .G(bit_w), .B(GND), .S(GND), .D(io_progBit));
nfet  T2 ( .G(io_progBit), .B(GND), .S(GND), .D(net035));
nfet  T10 ( .G(io_progBit), .B(GND), .S(GND), .D(net044));
nfet  T11 ( .G(io_read), .B(GND), .S(net044), .D(io_bitRead));
nfet  T0 ( .G(io_write), .B(GND), .S(io_bitWrite), .D(bit_w));
pfet  T5 ( .G(io_progBit), .B(VDD), .S(VDD), .D(net033));
pfet  T4 ( .G(io_write), .B(VDD), .S(net033), .D(bit_w));
pfet  T9 ( .G(bit_w), .B(VDD), .S(VDD), .D(io_progBit));
pfet  T13 ( .G(io_progBit), .B(VDD), .S(VDD), .D(net045));
pfet  T12 ( .G(io_readN), .B(VDD), .S(net045), .D(io_bitRead));
pfet  T1 ( .G(io_writeN), .B(VDD), .S(io_bitWrite), .D(bit_w));

endmodule
