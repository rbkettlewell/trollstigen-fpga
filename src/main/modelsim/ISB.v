// Library - hl2670, Cell - ISB, View - schematic
// LAST TIME SAVED: Mar  2 20:48:10 2016
// NETLIST TIME: Mar  3 00:17:35 2016
`timescale 1ns / 1ns 

module ISB ( E0, E2, E4, E6, E8, E10, N0, N2, N4, N6, N8, N10, S1, S3,
     S5, S7, S9, S11, W1, W3, W5, W7, W9, W11, GND, VDD, bitRead,
     ColRead, ColWrite, E1, E3, E5, E7, E9, E11, N1, N3, N5, N7, N9,
     N11, Row, S0, S2, S4, S6, S8, S10, W0, W2, W4, W6, W8, W10,
     bitWrite );

output  E0, E2, E4, E6, E8, E10, N0, N2, N4, N6, N8, N10, S1, S3, S5,
     S7, S9, S11, W1, W3, W5, W7, W9, W11;

inout  GND, VDD;

input  ColRead, ColWrite, E1, E3, E5, E7, E9, E11, N1, N3, N5, N7, N9,
     N11, S0, S2, S4, S6, S8, S10, W0, W2, W4, W6, W8, W10;

inout [0:7]  bitRead;

input [0:8]  Row;
input [0:7]  bitWrite;

VLSILab_280nmNAND_schematic I179 ( _read8, GND, VDD, Row[8], ColRead);
VLSILab_280nmNAND_schematic I177 ( _write8, GND, VDD, Row[8],
     ColWrite);
VLSILab_280nmNAND_schematic I174 ( _write7, GND, VDD, Row[7],
     ColWrite);
VLSILab_280nmNAND_schematic I172 ( _read7, GND, VDD, Row[7], ColRead);
VLSILab_280nmNAND_schematic I171 ( _read6, GND, VDD, Row[6], ColRead);
VLSILab_280nmNAND_schematic I169 ( _write6, GND, VDD, Row[6],
     ColWrite);
VLSILab_280nmNAND_schematic I166 ( _write5, GND, VDD, Row[5],
     ColWrite);
VLSILab_280nmNAND_schematic I164 ( _read5, GND, VDD, Row[5], ColRead);
VLSILab_280nmNAND_schematic I163 ( _read4, GND, VDD, Row[4], ColRead);
VLSILab_280nmNAND_schematic I161 ( _write4, GND, VDD, Row[4],
     ColWrite);
VLSILab_280nmNAND_schematic I158 ( _write3, GND, VDD, Row[3],
     ColWrite);
VLSILab_280nmNAND_schematic I156 ( _read3, GND, VDD, Row[3], ColRead);
VLSILab_280nmNAND_schematic I155 ( _read2, GND, VDD, Row[2], ColRead);
VLSILab_280nmNAND_schematic I153 ( _write2, GND, VDD, Row[2],
     ColWrite);
VLSILab_280nmNAND_schematic I150 ( _write1, GND, VDD, Row[1],
     ColWrite);
VLSILab_280nmNAND_schematic I148 ( _read1, GND, VDD, Row[1], ColRead);
VLSILab_280nmNAND_schematic I145 ( _write0, GND, VDD, Row[0],
     ColWrite);
VLSILab_280nmNAND_schematic I144 ( _read0, GND, VDD, Row[0], ColRead);
SRAMcell_Harrison I71 ( e10_w10, GND, VDD, bitRead[7], bitWrite[7],
     read8, _read8, write8, _write8);
SRAMcell_Harrison I70 ( e10_n9, GND, VDD, bitRead[6], bitWrite[6],
     read8, _read8, write8, _write8);
SRAMcell_Harrison I69 ( n10_e1, GND, VDD, bitRead[5], bitWrite[5],
     read8, _read8, write8, _write8);
SRAMcell_Harrison I68 ( n8_s8, GND, VDD, bitRead[4], bitWrite[4],
     read8, _read8, write8, _write8);
SRAMcell_Harrison I67 ( n2_s2, GND, VDD, bitRead[3], bitWrite[3],
     read8, _read8, write8, _write8);
SRAMcell_Harrison I66 ( n0_w0, GND, VDD, bitRead[2], bitWrite[2],
     read8, _read8, write8, _write8);
SRAMcell_Harrison I65 ( w11_n3, GND, VDD, bitRead[1], bitWrite[1],
     read8, _read8, write8, _write8);
SRAMcell_Harrison I64 ( w11_e11, GND, VDD, bitRead[0], bitWrite[0],
     read8, _read8, write8, _write8);
SRAMcell_Harrison I60 ( n8_w4, GND, VDD, bitRead[4], bitWrite[4],
     read7, _read7, write7, _write7);
SRAMcell_Harrison I61 ( n10_s10, GND, VDD, bitRead[5], bitWrite[5],
     read7, _read7, write7, _write7);
SRAMcell_Harrison I62 ( e10_s10, GND, VDD, bitRead[6], bitWrite[6],
     read7, _read7, write7, _write7);
SRAMcell_Harrison I63 ( e8_n7, GND, VDD, bitRead[7], bitWrite[7],
     read7, _read7, write7, _write7);
SRAMcell_Harrison I57 ( w11_s8, GND, VDD, bitRead[1], bitWrite[1],
     read7, _read7, write7, _write7);
SRAMcell_Harrison I58 ( n0_s0, GND, VDD, bitRead[2], bitWrite[2],
     read7, _read7, write7, _write7);
SRAMcell_Harrison I59 ( n2_e5, GND, VDD, bitRead[3], bitWrite[3],
     read7, _read7, write7, _write7);
SRAMcell_Harrison I56 ( w9_n5, GND, VDD, bitRead[0], bitWrite[0],
     read7, _read7, write7, _write7);
SRAMcell_Harrison I55 ( e8_s0, GND, VDD, bitRead[7], bitWrite[7],
     read6, _read6, write6, _write6);
SRAMcell_Harrison I54 ( e8_w8, GND, VDD, bitRead[6], bitWrite[6],
     read6, _read6, write6, _write6);
SRAMcell_Harrison I53 ( n10_w2, GND, VDD, bitRead[5], bitWrite[5],
     read6, _read6, write6, _write6);
SRAMcell_Harrison I52 ( n6_e9, GND, VDD, bitRead[4], bitWrite[4],
     read6, _read6, write6, _write6);
SRAMcell_Harrison I51 ( n4_w8, GND, VDD, bitRead[3], bitWrite[3],
     read6, _read6, write6, _write6);
SRAMcell_Harrison I50 ( n0_e3, GND, VDD, bitRead[2], bitWrite[2],
     read6, _read6, write6, _write6);
SRAMcell_Harrison I49 ( w9_e9, GND, VDD, bitRead[1], bitWrite[1],
     read6, _read6, write6, _write6);
SRAMcell_Harrison I48 ( w9_s6, GND, VDD, bitRead[0], bitWrite[0],
     read6, _read6, write6, _write6);
SRAMcell_Harrison I47 ( e6_w6, GND, VDD, bitRead[7], bitWrite[7],
     read5, _read5, write5, _write5);
SRAMcell_Harrison I46 ( e6_n5, GND, VDD, bitRead[6], bitWrite[6],
     read5, _read5, write5, _write5);
SRAMcell_Harrison I45 ( n8_e11, GND, VDD, bitRead[5], bitWrite[5],
     read5, _read5, write5, _write5);
SRAMcell_Harrison I44 ( n6_s6, GND, VDD, bitRead[4], bitWrite[4],
     read5, _read5, write5, _write5);
SRAMcell_Harrison I43 ( n4_s4, GND, VDD, bitRead[3], bitWrite[3],
     read5, _read5, write5, _write5);
SRAMcell_Harrison I42 ( n2_w10, GND, VDD, bitRead[2], bitWrite[2],
     read5, _read5, write5, _write5);
SRAMcell_Harrison I41 ( w7_n7, GND, VDD, bitRead[1], bitWrite[1],
     read5, _read5, write5, _write5);
SRAMcell_Harrison I40 ( w7_e7, GND, VDD, bitRead[0], bitWrite[0],
     read5, _read5, write5, _write5);
SRAMcell_Harrison I39 ( e6_s2, GND, VDD, bitRead[7], bitWrite[7],
     read4, _read4, write4, _write4);
SRAMcell_Harrison I38 ( e4_n3, GND, VDD, bitRead[6], bitWrite[6],
     read4, _read4, write4, _write4);
SRAMcell_Harrison I37 ( s7_w8, GND, VDD, bitRead[5], bitWrite[5],
     read4, _read4, write4, _write4);
SRAMcell_Harrison I36 ( n6_w6, GND, VDD, bitRead[4], bitWrite[4],
     read4, _read4, write4, _write4);
SRAMcell_Harrison I35 ( s5_e5, GND, VDD, bitRead[3], bitWrite[3],
     read4, _read4, write4, _write4);
SRAMcell_Harrison I34 ( n4_e7, GND, VDD, bitRead[2], bitWrite[2],
     read4, _read4, write4, _write4);
SRAMcell_Harrison I33 ( w7_s4, GND, VDD, bitRead[1], bitWrite[1],
     read4, _read4, write4, _write4);
SRAMcell_Harrison I32 ( w5_n9, GND, VDD, bitRead[0], bitWrite[0],
     read4, _read4, write4, _write4);
SRAMcell_Harrison I31 ( e4_w4, GND, VDD, bitRead[7], bitWrite[7],
     read3, _read3, write3, _write3);
SRAMcell_Harrison I30 ( e4_s4, GND, VDD, bitRead[6], bitWrite[6],
     read3, _read3, write3, _write3);
SRAMcell_Harrison I29 ( s9_e1, GND, VDD, bitRead[5], bitWrite[5],
     read3, _read3, write3, _write3);
SRAMcell_Harrison I28 ( s7_n7, GND, VDD, bitRead[4], bitWrite[4],
     read3, _read3, write3, _write3);
SRAMcell_Harrison I27 ( s5_n5, GND, VDD, bitRead[3], bitWrite[3],
     read3, _read3, write3, _write3);
SRAMcell_Harrison I26 ( s3_w4, GND, VDD, bitRead[2], bitWrite[2],
     read3, _read3, write3, _write3);
SRAMcell_Harrison I25 ( w5_s2, GND, VDD, bitRead[1], bitWrite[1],
     read3, _read3, write3, _write3);
SRAMcell_Harrison I24 ( w5_e5, GND, VDD, bitRead[0], bitWrite[0],
     read3, _read3, write3, _write3);
SRAMcell_Harrison I23 ( e2_n1, GND, VDD, bitRead[7], bitWrite[7],
     read2, _read2, write2, _write2);
SRAMcell_Harrison I22 ( e2_w2, GND, VDD, bitRead[6], bitWrite[6],
     read2, _read2, write2, _write2);
SRAMcell_Harrison I21 ( s11_w0, GND, VDD, bitRead[5], bitWrite[5],
     read2, _read2, write2, _write2);
SRAMcell_Harrison I20 ( s7_e3, GND, VDD, bitRead[4], bitWrite[4],
     read2, _read2, write2, _write2);
SRAMcell_Harrison I19 ( s5_w6, GND, VDD, bitRead[3], bitWrite[3],
     read2, _read2, write2, _write2);
SRAMcell_Harrison I18 ( s1_e9, GND, VDD, bitRead[2], bitWrite[2],
     read2, _read2, write2, _write2);
SRAMcell_Harrison I17 ( w3_e3, GND, VDD, bitRead[1], bitWrite[1],
     read2, _read2, write2, _write2);
SRAMcell_Harrison I15 ( e2_s6, GND, VDD, bitRead[7], bitWrite[7],
     read1, _read1, write1, _write1);
SRAMcell_Harrison I16 ( w3_n11, GND, VDD, bitRead[0], bitWrite[0],
     read2, _read2, write2, _write2);
SRAMcell_Harrison I13 ( s11_n11, GND, VDD, bitRead[5], bitWrite[5],
     read1, _read1, write1, _write1);
SRAMcell_Harrison I14 ( e0_n11, GND, VDD, bitRead[6], bitWrite[6],
     read1, _read1, write1, _write1);
SRAMcell_Harrison I5 ( s11_e11, GND, VDD, bitRead[5], bitWrite[5],
     read0, _read0, write0, _write0);
SRAMcell_Harrison I12 ( s9_w10, GND, VDD, bitRead[4], bitWrite[4],
     read1, _read1, write1, _write1);
SRAMcell_Harrison I10 ( s1_n1, GND, VDD, bitRead[2], bitWrite[2],
     read1, _read1, write1, _write1);
SRAMcell_Harrison I9 ( w1_n1, GND, VDD, bitRead[1], bitWrite[1], read1,
     _read1, write1, _write1);
SRAMcell_Harrison I8 ( w3_s0, GND, VDD, bitRead[0], bitWrite[0], read1,
     _read1, write1, _write1);
SRAMcell_Harrison I7 ( e0_w0, GND, VDD, bitRead[7], bitWrite[7], read0,
     _read0, write0, _write0);
SRAMcell_Harrison I6 ( e0_s8, GND, VDD, bitRead[6], bitWrite[6], read0,
     _read0, write0, _write0);
SRAMcell_Harrison I11 ( s3_e7, GND, VDD, bitRead[3], bitWrite[3],
     read1, _read1, write1, _write1);
SRAMcell_Harrison I4 ( s9_n9, GND, VDD, bitRead[4], bitWrite[4], read0,
     _read0, write0, _write0);
SRAMcell_Harrison I0 ( w1_e1, GND, VDD, bitRead[0], bitWrite[0], read0,
     _read0, write0, _write0);
SRAMcell_Harrison I2 ( s1_w2, GND, VDD, bitRead[2], bitWrite[2], read0,
     _read0, write0, _write0);
SRAMcell_Harrison I3 ( s3_n3, GND, VDD, bitRead[3], bitWrite[3], read0,
     _read0, write0, _write0);
SRAMcell_Harrison I1 ( w1_s10, GND, VDD, bitRead[1], bitWrite[1],
     read0, _read0, write0, _write0);
TransmissionGate I108 ( GND, VDD, E11, S11, s11_e11);
TransmissionGate I72 ( GND, VDD, W0, N0, n0_w0);
TransmissionGate I91 ( GND, VDD, W10, E10, e10_w10);
TransmissionGate I90 ( GND, VDD, N9, E10, e10_n9);
TransmissionGate I92 ( GND, VDD, S10, E10, e10_s10);
TransmissionGate I106 ( GND, VDD, W0, E0, e0_w0);
TransmissionGate I105 ( GND, VDD, N11, E0, e0_n11);
TransmissionGate I107 ( GND, VDD, S8, E0, e0_s8);
TransmissionGate I98 ( GND, VDD, S2, E6, e6_s2);
TransmissionGate I99 ( GND, VDD, N3, E4, e4_n3);
TransmissionGate I104 ( GND, VDD, S6, E2, e2_s6);
TransmissionGate I103 ( GND, VDD, W2, E2, e2_w2);
TransmissionGate I102 ( GND, VDD, N1, E2, e2_n1);
TransmissionGate I101 ( GND, VDD, S4, E4, e4_s4);
TransmissionGate I100 ( GND, VDD, W4, E4, e4_w4);
TransmissionGate I94 ( GND, VDD, W8, E8, e8_w8);
TransmissionGate I95 ( GND, VDD, S0, E8, e8_s0);
TransmissionGate I96 ( GND, VDD, N5, E6, e6_n5);
TransmissionGate I97 ( GND, VDD, W6, E6, e6_w6);
TransmissionGate I93 ( GND, VDD, N7, E8, e8_n7);
TransmissionGate I143 ( GND, VDD, N3, W11, w11_n3);
TransmissionGate I142 ( GND, VDD, E11, W11, w11_e11);
TransmissionGate I141 ( GND, VDD, S8, W11, w11_s8);
TransmissionGate I140 ( GND, VDD, N5, W9, w9_n5);
TransmissionGate I139 ( GND, VDD, E9, W9, w9_e9);
TransmissionGate I138 ( GND, VDD, S6, W9, w9_s6);
TransmissionGate I111 ( GND, VDD, E1, S9, s9_e1);
TransmissionGate I112 ( GND, VDD, N9, S9, s9_n9);
TransmissionGate I113 ( GND, VDD, W10, S9, s9_w10);
TransmissionGate I114 ( GND, GND, E3, S7, s7_e3);
TransmissionGate I118 ( GND, VDD, N5, S5, s5_n5);
TransmissionGate I129 ( GND, VDD, S0, W3, w3_s0);
TransmissionGate I110 ( GND, VDD, W0, S11, s11_w0);
TransmissionGate I128 ( GND, VDD, N1, W1, w1_n1);
TransmissionGate I127 ( GND, VDD, E1, W1, w1_e1);
TransmissionGate I134 ( GND, VDD, N9, W5, w5_n9);
TransmissionGate I130 ( GND, VDD, E3, W3, w3_e3);
TransmissionGate I82 ( GND, VDD, S6, N6, n6_s6);
TransmissionGate I83 ( GND, VDD, E9, N6, n6_e9);
TransmissionGate I85 ( GND, VDD, S8, N8, n8_s8);
TransmissionGate I86 ( GND, VDD, E11, N8, n8_e11);
TransmissionGate I87 ( GND, VDD, W2, N10, n10_w2);
TransmissionGate I89 ( GND, VDD, E1, N10, n10_e1);
TransmissionGate I78 ( GND, VDD, W8, N4, n4_w8);
TransmissionGate I80 ( GND, VDD, E7, N4, n4_e7);
TransmissionGate I75 ( GND, VDD, W10, N2, n2_w10);
TransmissionGate I77 ( GND, VDD, E5, N2, n2_e5);
TransmissionGate I74 ( GND, VDD, E3, N0, n0_e3);
TransmissionGate I123 ( GND, VDD, E9, S1, s1_e9);
TransmissionGate I126 ( GND, VDD, S10, W1, w1_s10);
TransmissionGate I125 ( GND, VDD, W2, S1, s1_w2);
TransmissionGate I124 ( GND, VDD, N1, S1, s1_n1);
TransmissionGate I109 ( GND, VDD, N11, S11, s11_n11);
TransmissionGate I122 ( GND, VDD, W4, S3, s3_w4);
TransmissionGate I119 ( GND, VDD, W6, S5, s5_w6);
TransmissionGate I120 ( GND, VDD, E7, S3, s3_e7);
TransmissionGate I121 ( GND, VDD, N3, S3, s3_n3);
TransmissionGate I116 ( GND, VDD, W8, S7, s7_w8);
TransmissionGate I115 ( GND, VDD, N7, S7, s7_n7);
TransmissionGate I117 ( GND, VDD, E5, S5, s5_e5);
TransmissionGate I133 ( GND, VDD, E5, W5, w5_e5);
TransmissionGate I132 ( GND, VDD, S2, W5, w5_s2);
TransmissionGate I131 ( GND, VDD, N11, W3, w3_n11);
TransmissionGate I135 ( GND, VDD, S4, W7, w7_s4);
TransmissionGate I137 ( GND, VDD, N7, W7, w7_n7);
TransmissionGate I136 ( GND, VDD, E7, W7, w7_e7);
TransmissionGate I84 ( GND, VDD, W4, N8, n8_w4);
TransmissionGate I79 ( GND, VDD, S4, N4, n4_s4);
TransmissionGate I81 ( GND, VDD, W6, N6, n6_w6);
TransmissionGate I88 ( GND, VDD, S10, N10, n10_s10);
TransmissionGate I76 ( GND, VDD, S2, N2, n2_s2);
TransmissionGate I73 ( GND, VDD, S0, N0, n0_s0);
SRAM_inv I178 ( read8, VDD, GND, _read8);
SRAM_inv I176 ( write8, VDD, GND, _write8);
SRAM_inv I175 ( write7, VDD, GND, _write7);
SRAM_inv I173 ( read7, VDD, GND, _read7);
SRAM_inv I170 ( read6, VDD, GND, _read6);
SRAM_inv I168 ( write6, VDD, GND, _write6);
SRAM_inv I167 ( write5, VDD, GND, _write5);
SRAM_inv I165 ( read5, VDD, GND, _read5);
SRAM_inv I162 ( read4, VDD, GND, _read4);
SRAM_inv I160 ( write4, VDD, GND, _write4);
SRAM_inv I159 ( write3, VDD, GND, _write3);
SRAM_inv I157 ( read3, VDD, GND, _read3);
SRAM_inv I154 ( read2, VDD, GND, _read2);
SRAM_inv I152 ( write2, VDD, GND, _write2);
SRAM_inv I151 ( write1, VDD, GND, _write1);
SRAM_inv I149 ( read1, VDD, GND, _read1);
SRAM_inv I147 ( write0, VDD, GND, _write0);
SRAM_inv I146 ( read0, VDD, GND, _read0);

endmodule