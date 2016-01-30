module Combinational(
    io_x,
    io_y,
    io_z
);

  input [15:0] io_x;
  input [15:0] io_y;
  output [15:0] io_z;

  wire [15:0] io_x, io_y, io_z;

  assign io_z = io_x + io_y;
endmodule

