// Test enable circuitry

module  xor3(gclk, resetn,
	     hip7, hip6, hip5, hip4, hip3, hip2, hip1, hip0,
	     gpio15, gpio14, gpio13, gpio12, gpio11, gpio10,
	     gpio9, gpio8, gpio7, gpio6, gpio5, gpio4, gpio3,
	     gpio2, gpio1, gpio0);

input	gclk;
input	resetn;
input   hip7;
input 	hip6;
input 	hip5;
input 	hip4;
input	hip3;
output	hip2;
output	hip1;
output	hip0;

input 	gpio15;
input 	gpio14;
input 	gpio13;
input 	gpio12;
input 	gpio11;
input 	gpio10;
input 	gpio9;
input 	gpio8;
output 	gpio7;
output 	gpio6;
output 	gpio5;
output 	gpio4;
output 	gpio3;
output 	gpio2;
output 	gpio1;
output 	gpio0;

reg temp;
reg temp1;

always @(posedge gclk)
begin
  temp <= gpio15 ^ gpio14 ^ gpio13;
  temp1 <= resetn & gpio15;
end

assign gpio0 = temp;

assign gpio7 = gpio12;
assign gpio6 = gpio11;
assign gpio5 = gpio10;
assign gpio4 = gpio9;
assign gpio3 = gpio8;
assign gpio2 = hip7;
assign gpio1 = hip6;
assign hip2 = hip5;
assign hip1 = hip4;
assign hip0 = hip3;

endmodule

