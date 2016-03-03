`timescale 1ns/1ps
module ISB_tb();

    // Column Decoder declarations
    logic  io_WE;
    logic  io_RE;
    logic[14:0] io_bRead;
    logic[14:0] io_bWrite;
    logic [0:7] io_byteRead_14 = 8'b_zzzz_zzzz;
    logic [0:7] io_byteRead_13 = 8'b_zzzz_zzzz;
    logic [0:7] io_byteRead_12 = 8'b_zzzz_zzzz;
    logic [0:7] io_byteRead_11 = 8'b_zzzz_zzzz;
    logic [0:7] io_byteRead_10 = 8'b_zzzz_zzzz;
    logic [0:7] io_byteRead_9 = 8'b_zzzz_zzzz;
    logic [0:7] io_byteRead_8 = 8'b_zzzz_zzzz;
    logic [0:7] io_byteRead_7 = 8'b_zzzz_zzzz;
    logic [0:7] io_byteRead_6 = 8'b_zzzz_zzzz;
    logic [0:7] io_byteRead_5 = 8'b_zzzz_zzzz;
    logic [0:7] io_byteRead_4 = 8'b_zzzz_zzzz;
    logic [0:7] io_byteRead_3 = 8'b_zzzz_zzzz;
    logic [0:7] io_byteRead_2 = 8'b_zzzz_zzzz;
    logic [0:7] io_byteRead_1 = 8'b_zzzz_zzzz;
    logic [0:7] io_byteRead_0;
    logic[0:7] io_byteWrite_14;
    logic[0:7] io_byteWrite_13;
    logic[0:7] io_byteWrite_12;
    logic[0:7] io_byteWrite_11;
    logic[0:7] io_byteWrite_10;
    logic[0:7] io_byteWrite_9;
    logic[0:7] io_byteWrite_8;
    logic[0:7] io_byteWrite_7;
    logic[0:7] io_byteWrite_6;
    logic[0:7] io_byteWrite_5;
    logic[0:7] io_byteWrite_4;
    logic[0:7] io_byteWrite_3;
    logic[0:7] io_byteWrite_2;
    logic[0:7] io_byteWrite_1;
    logic[0:7] io_byteWrite_0;
    logic [3:0] io_cAddr;
    logic [7:0] io_cDataIn;
    logic[7:0] io_cDataOut;

   // Row Decoder declarations
    logic [5:0] io_rAddr;
    logic [3:0] io_bAddr;
    logic[8:0] io_gRows_18;
    logic[8:0] io_gRows_17;
    logic[8:0] io_gRows_16;
    logic[8:0] io_gRows_15;
    logic[8:0] io_gRows_14;
    logic[8:0] io_gRows_13;
    logic[8:0] io_gRows_12;
    logic[8:0] io_gRows_11;
    logic[8:0] io_gRows_10;
    logic[8:0] io_gRows_9;
    logic[8:0] io_gRows_8;
    logic[8:0] io_gRows_7;
    logic[8:0] io_gRows_6;
    logic[8:0] io_gRows_5;
    logic[8:0] io_gRows_4;
    logic[8:0] io_gRows_3;
    logic[8:0] io_gRows_2;
    logic[8:0] io_gRows_1;
    logic[8:0] io_gRows_0; // Actual gRow connection, all the rest are disconnected
		
	

    // ISB declarations;
    logic  E0, E2, E4, E6, E8, E10, N0, N2, N4, N6, N8, N10, S1, S3, S5,
     S7, S9, S11, W1, W3, W5, W7, W9, W11;

    wire  GND, VDD;

    logic  ColRead, ColWrite, E1, E3, E5, E7, E9, E11, N1, N3, N5, N7, N9,
     N11, S0, S2, S4, S6, S8, S10, W0, W2, W4, W6, W8, W10;

    logic [0:8]  Row;
    logic [0:7]  bitWrite;
    wire [0:7]  bitRead;
		
		//Switch block output net enumerations 
		typedef enum {enum_N0,enum_N2,enum_N4,enum_N6,enum_N8,enum_N10,
									enum_E0,enum_E2,enum_E4,enum_E6,enum_E8,enum_E10,
									enum_S1,enum_S3,enum_S5,enum_S7,enum_S9,enum_S11,
									enum_W1,enum_W3,enum_W5,enum_W7,enum_W9,enum_W11} SBOutputs;
									
		//Switch block input net enumerations 
		typedef enum {enum_N1,enum_N3,enum_N5,enum_N7,enum_N9,enum_N11,
									enum_E1,enum_E3,enum_E5,enum_E7,enum_E9,enum_E11,
									enum_S0,enum_S2,enum_S4,enum_S6,enum_S8,enum_S10,
									enum_W0,enum_W2,enum_W4,enum_W6,enum_W8,enum_W10} SBInputs;
		
		logic[71:0] blkBits; // Programming bits for the Macrocell
		
		logic toFromCheck = 1'bz;
		logic faultDetected = 1'b0;
		
		SBInputs fromArray [71:0];
		SBOutputs toArray [71:0];
		
		logic [23:0] SBOutputArray;
		logic [23:0] SBInputArray;
		
		//Switch block output LUT for verification
		assign SBOutputArray [0	] = N0;
		assign SBOutputArray [1	] = N2;
		assign SBOutputArray [2	] = N4;
		assign SBOutputArray [3	] = N6;
		assign SBOutputArray [4	] = N8;
		assign SBOutputArray [5	] = N10;
		
		assign SBOutputArray [6	] = E0;
		assign SBOutputArray [7	] = E2;
		assign SBOutputArray [8	] = E4;
		assign SBOutputArray [9	] = E6;
		assign SBOutputArray [10] = E8;
		assign SBOutputArray [11] = E10;
		
		assign SBOutputArray [12] = S1;
		assign SBOutputArray [13] = S3;
		assign SBOutputArray [14] = S5;
		assign SBOutputArray [15] = S7;
		assign SBOutputArray [16] = S9;
		assign SBOutputArray [17] = S11;
		
		assign SBOutputArray [18] = W1;
		assign SBOutputArray [19] = W3;
		assign SBOutputArray [20] = W5;
		assign SBOutputArray [21] = W7;
		assign SBOutputArray [22] = W9;
		assign SBOutputArray [23] = W11;
		
		//Switch block input Map for verification
		assign N1 = SBInputArray [0	];
		assign N3 = SBInputArray [1	];
		assign N5 = SBInputArray [2	];
		assign N7 = SBInputArray [3	];
		assign N9 = SBInputArray [4	];
		assign N11 = SBInputArray [5];

		assign E1 = SBInputArray [6	];
		assign E3 = SBInputArray [7	];
		assign E5 = SBInputArray [8	];
		assign E7 = SBInputArray [9	];
		assign E9 = SBInputArray [10];
		assign E11 = SBInputArray [11];

		assign S0 = SBInputArray [12];
		assign S2 = SBInputArray [13];
		assign S4 = SBInputArray [14];
		assign S6 = SBInputArray [15];
		assign S8 = SBInputArray [16];
		assign S10 = SBInputArray [17];

		assign W0 = SBInputArray [18];
		assign W2 = SBInputArray [19];
		assign W4 = SBInputArray [20];
		assign W6 = SBInputArray [21];
		assign W8 = SBInputArray [22];
		assign W10 = SBInputArray [23];
		
		// ISB from-to connectivity mapping
		assign fromArray[	0	] = 	enum_E1			; assign toArray[	0	] = 	enum_W1	;
		assign fromArray[	1	] = 	enum_S10		; assign toArray[	1	] = 	enum_W1	;
		assign fromArray[	2	] = 	enum_W2			; assign toArray[	2	] = 	enum_S1	;
		assign fromArray[	3	] = 	enum_N3			; assign toArray[	3	] = 	enum_S3	;
		assign fromArray[	4	] = 	enum_N9			; assign toArray[	4	] = 	enum_S9	;
		assign fromArray[	5	] = 	enum_E11		; assign toArray[	5	] = 	enum_S11	;
		assign fromArray[	6	] = 	enum_S8	  	; assign toArray[	6	] = 	enum_E0	;
		assign fromArray[	7	] = 	enum_W0			; assign toArray[	7	] = 	enum_E0	;
		assign fromArray[	8	] = 	enum_S0			; assign toArray[	8	] = 	enum_W3	;
		assign fromArray[	9	] = 	enum_N1			; assign toArray[	9	] = 	enum_W1	;
		assign fromArray[	10	] = 	enum_N1		; assign toArray[	10	] = 	enum_S1	;
		assign fromArray[	11	] = 	enum_E7		; assign toArray[	11	] = 	enum_S3	;
		assign fromArray[	12	] = 	enum_W10	; assign toArray[	12	] = 	enum_S9	;
		assign fromArray[	13	] = 	enum_N11	; assign toArray[	13	] = 	enum_S11	;
		assign fromArray[	14	] = 	enum_N11	; assign toArray[	14	] = 	enum_E0	;
		assign fromArray[	15	] = 	enum_S6	  ; assign toArray[	15	] = 	enum_E2	;
		assign fromArray[	16	] = 	enum_N11	; assign toArray[	16	] = 	enum_W3	;
		assign fromArray[	17	] = 	enum_E3		; assign toArray[	17	] = 	enum_W3	;
		assign fromArray[	18	] = 	enum_E9		; assign toArray[	18	] = 	enum_S1	;
		assign fromArray[	19	] = 	enum_W6		; assign toArray[	19	] = 	enum_S5	;
		assign fromArray[	20	] = 	enum_E3		; assign toArray[	20	] = 	enum_S7	;
		assign fromArray[	21	] = 	enum_W0		; assign toArray[	21	] = 	enum_S11	;
		assign fromArray[	22	] = 	enum_W2		; assign toArray[	22	] = 	enum_E2	;
		assign fromArray[	23	] = 	enum_N1		; assign toArray[	23	] = 	enum_E2	;
		assign fromArray[	24	] = 	enum_E5		; assign toArray[	24	] = 	enum_W5	;
		assign fromArray[	25	] = 	enum_S2		; assign toArray[	25	] = 	enum_W5	;
		assign fromArray[	26	] = 	enum_W4		; assign toArray[	26	] = 	enum_S3	;
		assign fromArray[	27	] = 	enum_N5		; assign toArray[	27	] = 	enum_S5	;
		assign fromArray[	28	] = 	enum_N7		; assign toArray[	28	] = 	enum_S7	;
		assign fromArray[	29	] = 	enum_E1		; assign toArray[	29	] = 	enum_S9	;
		assign fromArray[	30	] = 	enum_S4		; assign toArray[	30	] = 	enum_E4	;
		assign fromArray[	31	] = 	enum_W4		; assign toArray[	31	] = 	enum_E4	;
		assign fromArray[	32	] = 	enum_N9		; assign toArray[	32	] = 	enum_W5	;
		assign fromArray[	33	] = 	enum_S4		; assign toArray[	33	] = 	enum_W7	;
		assign fromArray[	34	] = 	enum_E7		; assign toArray[	34	] = 	enum_N4	;
		assign fromArray[	35	] = 	enum_E5		; assign toArray[	35	] = 	enum_S5	;
		assign fromArray[	36	] = 	enum_W6		; assign toArray[	36	] = 	enum_N6	;
		assign fromArray[	37	] = 	enum_W8		; assign toArray[	37	] = 	enum_S7	;
		assign fromArray[	38	] = 	enum_N3		; assign toArray[	38	] = 	enum_E4	;
		assign fromArray[	39	] = 	enum_S2		; assign toArray[	39	] = 	enum_E6	;
		assign fromArray[	40	] = 	enum_E7		; assign toArray[	40	] = 	enum_W7	;
		assign fromArray[	41	] = 	enum_N7		; assign toArray[	41	] = 	enum_W7	;
		assign fromArray[	42	] = 	enum_W10	; assign toArray[	42	] = 	enum_N2	;
		assign fromArray[	43	] = 	enum_S4	  ; assign toArray[	43	] = 	enum_N4	;
		assign fromArray[	44	] = 	enum_S6	  ; assign toArray[	44	] = 	enum_N6	;
		assign fromArray[	45	] = 	enum_E11	; assign toArray[	45	] = 	enum_N8	;
		assign fromArray[	46	] = 	enum_N5		; assign toArray[	46	] = 	enum_E6	;
		assign fromArray[	47	] = 	enum_W6		; assign toArray[	47	] = 	enum_E6	;
		assign fromArray[	48	] = 	enum_S6		; assign toArray[	48	] = 	enum_W9	;
		assign fromArray[	49	] = 	enum_E9		; assign toArray[	49	] = 	enum_W9	;
		assign fromArray[	50	] = 	enum_E3		; assign toArray[	50	] = 	enum_N0	;
		assign fromArray[	51	] = 	enum_W8		; assign toArray[	51	] = 	enum_N4	;
		assign fromArray[	52	] = 	enum_E9		; assign toArray[	52	] = 	enum_N6	;
		assign fromArray[	53	] = 	enum_W2		; assign toArray[	53	] = 	enum_N10	;
		assign fromArray[	54	] = 	enum_W8		; assign toArray[	54	] = 	enum_E8	;
		assign fromArray[	55	] = 	enum_S0		; assign toArray[	55	] = 	enum_E8	;
		assign fromArray[	56	] = 	enum_N5		; assign toArray[	56	] = 	enum_W9	;
		assign fromArray[	57	] = 	enum_S8		; assign toArray[	57	] = 	enum_W11	;
		assign fromArray[	58	] = 	enum_S0		; assign toArray[	58	] = 	enum_N0	;
		assign fromArray[	59	] = 	enum_E5		; assign toArray[	59	] = 	enum_N2	;
		assign fromArray[	60	] = 	enum_W4		; assign toArray[	60	] = 	enum_N8	;
		assign fromArray[	61	] = 	enum_S10	; assign toArray[	61	] = 	enum_N10	;
		assign fromArray[	62	] = 	enum_S10	; assign toArray[	62	] = 	enum_E10	;
		assign fromArray[	63	] = 	enum_N7	  ; assign toArray[	63	] = 	enum_E8	;
		assign fromArray[	64	] = 	enum_E11	; assign toArray[	64	] = 	enum_W11	;
		assign fromArray[	65	] = 	enum_N3		; assign toArray[	65	] = 	enum_W11	;
		assign fromArray[	66	] = 	enum_W0		; assign toArray[	66	] = 	enum_N0	;
		assign fromArray[	67	] = 	enum_S2		; assign toArray[	67	] = 	enum_N2	;
		assign fromArray[	68	] = 	enum_S8		; assign toArray[	68	] = 	enum_N8	;
		assign fromArray[	69	] = 	enum_E1		; assign toArray[	69	] = 	enum_N10	;
		assign fromArray[	70	] = 	enum_N9		; assign toArray[	70	] = 	enum_E10	;
		assign fromArray[	71	] = 	enum_W10	; assign toArray[	71	] = 	enum_E10	;

		
    // Endianess is fixed between memory little endian to macrocell bigendian words.
    assign Row = {<<{io_gRows_0}}; // right to left streaming
    assign  io_byteRead_0 = bitRead; 
    assign bitWrite = io_byteWrite_0; 

    // Macrocell at 0,0 read write signals
    assign ColRead = io_bRead[0];
    assign ColWrite = io_bWrite[0];

    // Power and ground connections;
    assign VDD = '1;
    assign GND = '0;

    // Dut instantiations
    ColumnDecoder colDecodeDut(.*);
    RowDecoder rowDecoderDut(.*);
    ISB isbDut(.*);

    integer i, localRow;

    initial begin

			// Initialize variables of interest
			io_cDataIn[7:0] = '0;
			io_WE = '1;
			io_rAddr = '0;
			io_cAddr = '0;
			io_WE = '0;
			io_RE = '0;
			SBInputArray[23:0] = 'z;
			
			for(i = 0; i < 72;i++) begin
				// Clear old programming block bits
				blkBits[71:0] = '0;
				// Set new programming bit TODO!!! Next iteration of Switch block will need to invert these programming bits
				#10ns blkBits[i] = 1'b1;
				
				// Program blkBits, currently there is no SRAM readback, since the ISB proper 
				// connectivity will provide some information on this.
				for(localRow = 0; localRow < 9 ; localRow++) begin
          #10ns io_bAddr[3:0] = localRow;
       //   $display("Index i: %d, bAddr %d", i, localRow);
          #10ns io_cDataIn[7:0] = blkBits[localRow*8 +: 8];
       //   $display("Wrote CData %b",io_cDataIn);
          #10ns io_WE = '1;
       //   $display("Enable WE");
          #10ns io_WE = '0;
       //   $display("Disable WE");
        end
			
        // Clear old inputs
        SBInputArray[23:0] = 'z;
      //  $display("Cleared old inputs");
        // Set new input high
        #10ns SBInputArray[fromArray[i]] = '1;
      //  $display("Set new inputs");
				#10ns;
        // Check segment connection
        #10ns toFromCheck = SBInputArray[fromArray[i]] === SBOutputArray[toArray[i]];
        $display("Test %d toFrom is %b",i, toFromCheck);
				SBInputArray[23:0] = 'z;
      end
			
			//TODO check all other outputs to make sure they are not equal to the input value
    end
endmodule
