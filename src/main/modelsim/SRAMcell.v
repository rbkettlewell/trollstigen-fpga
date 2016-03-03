// Library - VLSILab, Cell - SRAMcell_Harrison, View - schematic
// LAST TIME SAVED: Feb 22 10:52:28 2016
// NETLIST TIME: Feb 25 16:45:21 2016
//`timescale 1ns / 1ns 

module SRAMcell_behavorial (output io_progBit, inout GND, inout VDD, 
		output io_bitRead, 
		input  io_bitWrite, 
		input  io_read,
		input  io_readN,
		input  io_write,
		input  io_writeN,
		input clk, 
		input reset
);

  reg  bitState;
  wire T10;
  wire T0;
  wire T1;
  wire T2;
  wire T3;
  wire T4;
  wire T5;
  wire T6;
  wire T7;
  wire T8;
  wire T9;


  assign io_progBit = ~bitState; // Invert the programming bit to match the layout
  assign T10 = reset ? 1'h0 : T0;
  assign T0 = T1 ? io_bitWrite : bitState;
  assign T1 = T3 & T2;
  assign T2 = io_writeN == 1'h0;
  assign T3 = io_write == 1'h1;
  assign io_bitRead = T4;
  assign T4 = T5 ? bitState : 1'bz;
  assign T5 = T9 & T6;
  assign T6 = T8 & T7;
  assign T7 = io_readN == 1'h0;
  assign T8 = io_read == 1'h1;
  assign T9 = T1 ^ 1'h1;

  always @(posedge clk) begin
    if(reset) begin
      bitState <= 1'h0;
    end else if(T1) begin
      bitState <= io_bitWrite;
    end
  end
endmodule
