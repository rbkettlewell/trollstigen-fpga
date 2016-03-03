`timescale 1ns/1ps
module pfet (G, B, S, D);

input G, B;
inout S, D;

wire G_cleaned;

assign #0.020 G_cleaned = (G === 1'b0) ? 1'b0 : 1'b1; 

tranif0(S,D,G_cleaned);

endmodule;
