// Library - VLSILab, Cell - TransmissionGate, View - schematic
// LAST TIME SAVED: Feb 25 14:02:43 2016
// NETLIST TIME: Feb 25 16:45:21 2016
//`timescale 1ns / 1ns 

module TransmissionGate_tb();

// Library - VLSILab, Cell - SRAMcell_Harrison, View - schematic
// LAST TIME SAVED: Feb 22 10:52:28 2016
// NETLIST TIME: Feb 25 16:45:21 2016
//`timescale 1ns / 1ns 

wire  in;
wire  out;

logic SRAMin;

wire VDD;
wire GND;

logic inVal = 1'bz;
logic outVal = 1'bz;

assign VDD = '1;
assign GND = '0;

assign in = inVal;
assign out = outVal;

TransmissionGate transgate_dut(.*);

initial begin
   // Enable Transmission gate
  SRAMin = '0;
  #2ns inVal = 'z; 
  outVal = '1; 
  #2ns inVal = 'z; 
  outVal = '0; 
  #2ns inVal = 'z; 
  outVal = 'z; 
  #2ns inVal = 'z; 
  outVal = 'z; 
  #2ns inVal = 'x; 
  outVal = '1; 
  #2ns inVal = 'x; 
  outVal = '0; 
  // Diasable Transmission gate
  SRAMin = '1;
  #2ns inVal = 'z; 
  outVal = '1; 
  #2ns inVal = 'z; 
  outVal = '0; 
  #2ns inVal = 'z; 
  outVal = 'z; 
  #2ns inVal = 'z; 
  outVal = 'z; 
  #2ns inVal = 'x; 
  outVal = '1; 
  #2ns inVal = 'x; 
  outVal = '0; 
   #2ns inVal = 'x; 
  outVal = 'z; 
   #10ns inVal = 'x; 
  outVal = 'z; 
    // Z Transmission gate
  SRAMin = 'x;
  #2ns inVal = 'z; 
  outVal = '1; 
  #2ns inVal = 'z; 
  outVal = '0; 
  #2ns inVal = 'z; 
  outVal = 'z; 
  #2ns inVal = 'z; 
  outVal = 'z; 
  #2ns inVal = 'x; 
  outVal = '1; 
  #2ns inVal = 'x; 
  outVal = '0; 
  #2ns inVal = 'x; 
  outVal = 'z; 
   // X Transmission gate
  SRAMin = 'z;
  #2ns inVal = 'z; 
  outVal = '1; 
  #2ns inVal = 'z; 
  outVal = '0; 
  #2ns inVal = 'z; 
  outVal = 'z; 
  #2ns inVal = 'z; 
  outVal = 'z; 
  #2ns inVal = 'x; 
  outVal = '1; 
  #2ns inVal = 'x; 
  outVal = '0; 
  #2ns inVal = 'x; 
  outVal = 'z; 
  #2ns inVal = 'x; 
  outVal = 'z; 
 end

endmodule;