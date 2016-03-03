`timescale 1ns/1ps
module nfet (G, B, S, D);

input G, B;
inout S, D;

wire G_cleaned;

assign #0.020 G_cleaned = (G === 1'b1) ? 1'b1 : 1'b0; 
  
tranif1(S,D, G_cleaned);

endmodule;
