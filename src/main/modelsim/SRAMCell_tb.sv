module SRAMCell_tb;

// Library - VLSILab, Cell - SRAMcell_Harrison, View - schematic
// LAST TIME SAVED: Feb 22 10:52:28 2016
// NETLIST TIME: Feb 25 16:45:21 2016
//`timescale 1ns / 1ns 


reg[0:0] io_write = 0;
reg[0:0] io_writeN = 1;
reg[0:0] io_read = 0;
reg[0:0] io_readN = 1;
reg[0:0] io_bitWriteVal = 1'bz;
wire[0:0] io_bitRead;
wire[0:0] io_progBit;
wire VDD;
wire GND;
wire io_bitWrite;
//reg clk = 0;
//reg reset = 1;

//always #1ns clk = ~clk;

assign VDD = '1;
assign GND = '0;
assign io_bitWrite = io_bitWriteVal;

//SRAMcell_Harrison sram_dut3(io_progBit, GND, VDD, io_bitRead, io_bitWrite, io_read, io_readN, io_write, io_writeN, clk, reset);
SRAMcell_Harrison sram_dut3(.*);
initial begin
  //#2ns reset = '0;  
  #5ns io_bitWriteVal = '1;
  #5ns io_write = '1; io_writeN = '0;
  #5ns io_write = '0; io_writeN = '1;
  //#5ns io_bitWriteVal = 'z;
  #5ns io_read = '1; io_readN = '0;
  #5ns io_read = '0; io_readN = '1;
  #5ns io_bitWriteVal = '0;
  #5ns io_write = '1; io_writeN = '0;
  #5ns io_write = '0; io_writeN = '1;
  //#5ns io_bitWriteVal = 'z;
  #5ns io_read = '1; io_readN = '0;
  #5ns io_read = '0; io_readN = '1;
  #5ns io_bitWriteVal = '1;
  #5ns io_write = '1; io_writeN = '0;
  #5ns io_write = '0; io_writeN = '1;
  //#5ns io_bitWriteVal = 'z;
  #5ns io_read = '1; io_readN = '0;
  #5ns io_read = '0; io_readN = '1;

 
 end

endmodule;
