// Test enable circuitry

module  xor3(clock,
                a_in,
                b_in,
                c_in,
                z_out
                );

input   clock;
input   a_in;
input b_in;
input c_in;
reg temp;
output z_out;

always @(posedge clock)
begin
  temp <= a_in ^ b_in ^ c_in;
end

assign z_out = temp;

endmodule

