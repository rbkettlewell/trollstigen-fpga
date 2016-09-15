module TopLevel(gclk,
                reset,
	              xclk,         // HIP7
                hex_latch,    // HIP6
                hex_sdo,      // HIP5
                hex_sclk,     // HIP4
                hip_spi_cs,   // HIP3
                hip_sclk,     // HIP2
                hip_miso,     // HIP1
                hip_mosi,     // HIP0
                pmod_reset_1, // GPIO15
                pmod_int_1,   // GPIO14
                pmod_sclk_1,  // GPIO13
                pmod_miso_1,  // GPIO12
                pmod_mosi_1,  // GPIO11
                pmod_ss_1,    // GPIO10
                pmod_reset_0, // GPIO9
                pmod_int_0,   // GPIO8
                pmod_sclk_0,  // GPIO7
                pmod_miso_0,  // GPIO6
                pmod_mosi_0,  // GPIO5
                pmod_ss_0,    // GPIO4
                dio0,         // GPIO3
                dio1,         // GPIO2
                dio2,         // GPIO1
                dio3          // GPIO0
                );

// Unused IO
input xclk;
input hex_latch;
input hex_sdo;
input hex_sclk;
input hip_spi_cs;
input hip_sclk;
input hip_miso;
input hip_mosi;
input pmod_reset_1;
input pmod_int_1;
input pmod_sclk_1;
input pmod_miso_1;
input pmod_mosi_1;
input pmod_ss_1;
input pmod_reset_0;
input pmod_int_0;
input pmod_sclk_0;
input pmod_miso_0;
input pmod_mosi_0;
input pmod_ss_0;

// Used IO
input gclk;
input reset;
output dio3;
output dio2;
output dio1;
output dio0;

wire [3:0] count_reg = 4'd0;

always @(posedge gclk) begin
    count_reg <= count_reg + 1'd1;
end

assign dio3 = count_reg[3];
assign dio2 = count_reg[2];
assign dio1 = count_reg[1];
assign dio0 = count_reg[0];

endmodule
