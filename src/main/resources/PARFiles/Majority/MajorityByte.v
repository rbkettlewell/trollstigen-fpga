module Majority(gclk, resetn,
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
input	hip2;
input	hip1;
input	hip0;

input 	gpio15;
input 	gpio14;
input 	gpio13;
input 	gpio12;
input 	gpio11;
input 	gpio10;
input 	gpio9;
output  gpio8;
input 	gpio7;
input 	gpio6;
input 	gpio5;
input 	gpio4;
input   gpio3;
input 	gpio2;
input 	gpio1;
input 	gpio0;

reg majorityOnes;

always @(posedge gclk)
begin

  majorityOnes <= (gpio7 + gpio6 + gpio5 +  gpio4 + gpio3 + gpio2 + gpio1 + gpio0) > 4;
end

assign gpio8 = majorityOnes;

endmodule

