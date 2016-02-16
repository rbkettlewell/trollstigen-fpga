module BlockDecoder(
    input [3:0] io_bAddr,
    output[8:0] io_bRow
);

  wire[8:0] T0;
  wire[8:0] T1;
  wire[8:0] T2;
  wire[8:0] T3;
  wire[8:0] T4;
  wire[8:0] T5;
  wire[8:0] T6;
  wire[8:0] T7;
  wire[8:0] T8;
  wire T9;
  wire T10;
  wire T11;
  wire T12;
  wire T13;
  wire T14;
  wire T15;
  wire T16;
  wire T17;
  wire T18;
  wire T19;
  wire T20;
  wire T21;
  wire T22;
  wire T23;
  wire T24;
  wire T25;
  wire T26;
  wire T27;
  wire T28;
  wire T29;
  wire T30;
  wire T31;
  wire T32;
  wire T33;
  wire T34;
  wire T35;
  wire T36;
  wire T37;
  wire T38;
  wire T39;
  wire T40;


  assign io_bRow = T0;
  assign T0 = T37 ? 9'h100 : T1;
  assign T1 = T33 ? 9'h80 : T2;
  assign T2 = T29 ? 9'h40 : T3;
  assign T3 = T25 ? 9'h20 : T4;
  assign T4 = T21 ? 9'h10 : T5;
  assign T5 = T17 ? 9'h8 : T6;
  assign T6 = T13 ? 9'h4 : T7;
  assign T7 = T10 ? 9'h2 : T8;
  assign T8 = T9 ? 9'h1 : 9'h0;
  assign T9 = io_bAddr == 4'h0;
  assign T10 = T12 & T11;
  assign T11 = io_bAddr == 4'h1;
  assign T12 = T9 ^ 1'h1;
  assign T13 = T15 & T14;
  assign T14 = io_bAddr == 4'h2;
  assign T15 = T16 ^ 1'h1;
  assign T16 = T9 | T11;
  assign T17 = T19 & T18;
  assign T18 = io_bAddr == 4'h3;
  assign T19 = T20 ^ 1'h1;
  assign T20 = T16 | T14;
  assign T21 = T23 & T22;
  assign T22 = io_bAddr == 4'h4;
  assign T23 = T24 ^ 1'h1;
  assign T24 = T20 | T18;
  assign T25 = T27 & T26;
  assign T26 = io_bAddr == 4'h5;
  assign T27 = T28 ^ 1'h1;
  assign T28 = T24 | T22;
  assign T29 = T31 & T30;
  assign T30 = io_bAddr == 4'h6;
  assign T31 = T32 ^ 1'h1;
  assign T32 = T28 | T26;
  assign T33 = T35 & T34;
  assign T34 = io_bAddr == 4'h7;
  assign T35 = T36 ^ 1'h1;
  assign T36 = T32 | T30;
  assign T37 = T39 & T38;
  assign T38 = io_bAddr == 4'h8;
  assign T39 = T40 ^ 1'h1;
  assign T40 = T36 | T34;
endmodule

module RowDecoder(
    input [5:0] io_rAddr,
    input [3:0] io_bAddr,
    output[8:0] io_gRows_18,
    output[8:0] io_gRows_17,
    output[8:0] io_gRows_16,
    output[8:0] io_gRows_15,
    output[8:0] io_gRows_14,
    output[8:0] io_gRows_13,
    output[8:0] io_gRows_12,
    output[8:0] io_gRows_11,
    output[8:0] io_gRows_10,
    output[8:0] io_gRows_9,
    output[8:0] io_gRows_8,
    output[8:0] io_gRows_7,
    output[8:0] io_gRows_6,
    output[8:0] io_gRows_5,
    output[8:0] io_gRows_4,
    output[8:0] io_gRows_3,
    output[8:0] io_gRows_2,
    output[8:0] io_gRows_1,
    output[8:0] io_gRows_0
);

  wire[3:0] T0;
  wire[3:0] T1;
  wire T2;
  wire T3;
  wire[3:0] T4;
  wire[3:0] T5;
  wire T6;
  wire T7;
  wire[3:0] T8;
  wire[3:0] T9;
  wire T10;
  wire T11;
  wire[3:0] T12;
  wire[3:0] T13;
  wire T14;
  wire T15;
  wire[3:0] T16;
  wire[3:0] T17;
  wire T18;
  wire T19;
  wire[3:0] T20;
  wire[3:0] T21;
  wire T22;
  wire T23;
  wire[3:0] T24;
  wire[3:0] T25;
  wire T26;
  wire T27;
  wire[3:0] T28;
  wire[3:0] T29;
  wire T30;
  wire T31;
  wire[3:0] T32;
  wire[3:0] T33;
  wire T34;
  wire T35;
  wire[3:0] T36;
  wire[3:0] T37;
  wire T38;
  wire T39;
  wire[3:0] T40;
  wire[3:0] T41;
  wire T42;
  wire T43;
  wire[3:0] T44;
  wire[3:0] T45;
  wire T46;
  wire T47;
  wire[3:0] T48;
  wire[3:0] T49;
  wire T50;
  wire T51;
  wire[3:0] T52;
  wire[3:0] T53;
  wire T54;
  wire T55;
  wire[3:0] T56;
  wire[3:0] T57;
  wire T58;
  wire T59;
  wire[3:0] T60;
  wire[3:0] T61;
  wire T62;
  wire T63;
  wire[3:0] T64;
  wire[3:0] T65;
  wire T66;
  wire T67;
  wire[3:0] T68;
  wire[3:0] T69;
  wire T70;
  wire T71;
  wire[3:0] T72;
  wire[3:0] T73;
  wire T74;
  wire T75;
  wire[8:0] BlockDecoder_io_bRow;
  wire[8:0] BlockDecoder_1_io_bRow;
  wire[8:0] BlockDecoder_2_io_bRow;
  wire[8:0] BlockDecoder_3_io_bRow;
  wire[8:0] BlockDecoder_4_io_bRow;
  wire[8:0] BlockDecoder_5_io_bRow;
  wire[8:0] BlockDecoder_6_io_bRow;
  wire[8:0] BlockDecoder_7_io_bRow;
  wire[8:0] BlockDecoder_8_io_bRow;
  wire[8:0] BlockDecoder_9_io_bRow;
  wire[8:0] BlockDecoder_10_io_bRow;
  wire[8:0] BlockDecoder_11_io_bRow;
  wire[8:0] BlockDecoder_12_io_bRow;
  wire[8:0] BlockDecoder_13_io_bRow;
  wire[8:0] BlockDecoder_14_io_bRow;
  wire[8:0] BlockDecoder_15_io_bRow;
  wire[8:0] BlockDecoder_16_io_bRow;
  wire[8:0] BlockDecoder_17_io_bRow;
  wire[8:0] BlockDecoder_18_io_bRow;


  assign T0 = T3 ? 4'ha : T1;
  assign T1 = T2 ? io_bAddr : io_bAddr;
  assign T2 = 6'h12 == io_rAddr;
  assign T3 = T2 ^ 1'h1;
  assign T4 = T7 ? 4'ha : T5;
  assign T5 = T6 ? io_bAddr : io_bAddr;
  assign T6 = 6'h11 == io_rAddr;
  assign T7 = T6 ^ 1'h1;
  assign T8 = T11 ? 4'ha : T9;
  assign T9 = T10 ? io_bAddr : io_bAddr;
  assign T10 = 6'h10 == io_rAddr;
  assign T11 = T10 ^ 1'h1;
  assign T12 = T15 ? 4'ha : T13;
  assign T13 = T14 ? io_bAddr : io_bAddr;
  assign T14 = 6'hf == io_rAddr;
  assign T15 = T14 ^ 1'h1;
  assign T16 = T19 ? 4'ha : T17;
  assign T17 = T18 ? io_bAddr : io_bAddr;
  assign T18 = 6'he == io_rAddr;
  assign T19 = T18 ^ 1'h1;
  assign T20 = T23 ? 4'ha : T21;
  assign T21 = T22 ? io_bAddr : io_bAddr;
  assign T22 = 6'hd == io_rAddr;
  assign T23 = T22 ^ 1'h1;
  assign T24 = T27 ? 4'ha : T25;
  assign T25 = T26 ? io_bAddr : io_bAddr;
  assign T26 = 6'hc == io_rAddr;
  assign T27 = T26 ^ 1'h1;
  assign T28 = T31 ? 4'ha : T29;
  assign T29 = T30 ? io_bAddr : io_bAddr;
  assign T30 = 6'hb == io_rAddr;
  assign T31 = T30 ^ 1'h1;
  assign T32 = T35 ? 4'ha : T33;
  assign T33 = T34 ? io_bAddr : io_bAddr;
  assign T34 = 6'ha == io_rAddr;
  assign T35 = T34 ^ 1'h1;
  assign T36 = T39 ? 4'ha : T37;
  assign T37 = T38 ? io_bAddr : io_bAddr;
  assign T38 = 6'h9 == io_rAddr;
  assign T39 = T38 ^ 1'h1;
  assign T40 = T43 ? 4'ha : T41;
  assign T41 = T42 ? io_bAddr : io_bAddr;
  assign T42 = 6'h8 == io_rAddr;
  assign T43 = T42 ^ 1'h1;
  assign T44 = T47 ? 4'ha : T45;
  assign T45 = T46 ? io_bAddr : io_bAddr;
  assign T46 = 6'h7 == io_rAddr;
  assign T47 = T46 ^ 1'h1;
  assign T48 = T51 ? 4'ha : T49;
  assign T49 = T50 ? io_bAddr : io_bAddr;
  assign T50 = 6'h6 == io_rAddr;
  assign T51 = T50 ^ 1'h1;
  assign T52 = T55 ? 4'ha : T53;
  assign T53 = T54 ? io_bAddr : io_bAddr;
  assign T54 = 6'h5 == io_rAddr;
  assign T55 = T54 ^ 1'h1;
  assign T56 = T59 ? 4'ha : T57;
  assign T57 = T58 ? io_bAddr : io_bAddr;
  assign T58 = 6'h4 == io_rAddr;
  assign T59 = T58 ^ 1'h1;
  assign T60 = T63 ? 4'ha : T61;
  assign T61 = T62 ? io_bAddr : io_bAddr;
  assign T62 = 6'h3 == io_rAddr;
  assign T63 = T62 ^ 1'h1;
  assign T64 = T67 ? 4'ha : T65;
  assign T65 = T66 ? io_bAddr : io_bAddr;
  assign T66 = 6'h2 == io_rAddr;
  assign T67 = T66 ^ 1'h1;
  assign T68 = T71 ? 4'ha : T69;
  assign T69 = T70 ? io_bAddr : io_bAddr;
  assign T70 = 6'h1 == io_rAddr;
  assign T71 = T70 ^ 1'h1;
  assign T72 = T75 ? 4'ha : T73;
  assign T73 = T74 ? io_bAddr : io_bAddr;
  assign T74 = 6'h0 == io_rAddr;
  assign T75 = T74 ^ 1'h1;
  assign io_gRows_0 = BlockDecoder_io_bRow;
  assign io_gRows_1 = BlockDecoder_1_io_bRow;
  assign io_gRows_2 = BlockDecoder_2_io_bRow;
  assign io_gRows_3 = BlockDecoder_3_io_bRow;
  assign io_gRows_4 = BlockDecoder_4_io_bRow;
  assign io_gRows_5 = BlockDecoder_5_io_bRow;
  assign io_gRows_6 = BlockDecoder_6_io_bRow;
  assign io_gRows_7 = BlockDecoder_7_io_bRow;
  assign io_gRows_8 = BlockDecoder_8_io_bRow;
  assign io_gRows_9 = BlockDecoder_9_io_bRow;
  assign io_gRows_10 = BlockDecoder_10_io_bRow;
  assign io_gRows_11 = BlockDecoder_11_io_bRow;
  assign io_gRows_12 = BlockDecoder_12_io_bRow;
  assign io_gRows_13 = BlockDecoder_13_io_bRow;
  assign io_gRows_14 = BlockDecoder_14_io_bRow;
  assign io_gRows_15 = BlockDecoder_15_io_bRow;
  assign io_gRows_16 = BlockDecoder_16_io_bRow;
  assign io_gRows_17 = BlockDecoder_17_io_bRow;
  assign io_gRows_18 = BlockDecoder_18_io_bRow;
  BlockDecoder BlockDecoder(
       .io_bAddr( T72 ),
       .io_bRow( BlockDecoder_io_bRow )
  );
  BlockDecoder BlockDecoder_1(
       .io_bAddr( T68 ),
       .io_bRow( BlockDecoder_1_io_bRow )
  );
  BlockDecoder BlockDecoder_2(
       .io_bAddr( T64 ),
       .io_bRow( BlockDecoder_2_io_bRow )
  );
  BlockDecoder BlockDecoder_3(
       .io_bAddr( T60 ),
       .io_bRow( BlockDecoder_3_io_bRow )
  );
  BlockDecoder BlockDecoder_4(
       .io_bAddr( T56 ),
       .io_bRow( BlockDecoder_4_io_bRow )
  );
  BlockDecoder BlockDecoder_5(
       .io_bAddr( T52 ),
       .io_bRow( BlockDecoder_5_io_bRow )
  );
  BlockDecoder BlockDecoder_6(
       .io_bAddr( T48 ),
       .io_bRow( BlockDecoder_6_io_bRow )
  );
  BlockDecoder BlockDecoder_7(
       .io_bAddr( T44 ),
       .io_bRow( BlockDecoder_7_io_bRow )
  );
  BlockDecoder BlockDecoder_8(
       .io_bAddr( T40 ),
       .io_bRow( BlockDecoder_8_io_bRow )
  );
  BlockDecoder BlockDecoder_9(
       .io_bAddr( T36 ),
       .io_bRow( BlockDecoder_9_io_bRow )
  );
  BlockDecoder BlockDecoder_10(
       .io_bAddr( T32 ),
       .io_bRow( BlockDecoder_10_io_bRow )
  );
  BlockDecoder BlockDecoder_11(
       .io_bAddr( T28 ),
       .io_bRow( BlockDecoder_11_io_bRow )
  );
  BlockDecoder BlockDecoder_12(
       .io_bAddr( T24 ),
       .io_bRow( BlockDecoder_12_io_bRow )
  );
  BlockDecoder BlockDecoder_13(
       .io_bAddr( T20 ),
       .io_bRow( BlockDecoder_13_io_bRow )
  );
  BlockDecoder BlockDecoder_14(
       .io_bAddr( T16 ),
       .io_bRow( BlockDecoder_14_io_bRow )
  );
  BlockDecoder BlockDecoder_15(
       .io_bAddr( T12 ),
       .io_bRow( BlockDecoder_15_io_bRow )
  );
  BlockDecoder BlockDecoder_16(
       .io_bAddr( T8 ),
       .io_bRow( BlockDecoder_16_io_bRow )
  );
  BlockDecoder BlockDecoder_17(
       .io_bAddr( T4 ),
       .io_bRow( BlockDecoder_17_io_bRow )
  );
  BlockDecoder BlockDecoder_18(
       .io_bAddr( T0 ),
       .io_bRow( BlockDecoder_18_io_bRow )
  );
endmodule

