#include "IHCB.h"

void IHCB_t::init ( val_t rand_init ) {
  this->__srand(rand_init);
  clk.len = 1;
  clk.cnt = clk.len;
  clk.values[0] = 0;
}


int IHCB_t::clock ( dat_t<1> reset ) {
  uint32_t min = ((uint32_t)1<<31)-1;
  if (clk.cnt < min) min = clk.cnt;
  clk.cnt-=min;
  if (clk.cnt == 0) clock_lo( reset );
  if (clk.cnt == 0) clock_hi( reset );
  if (clk.cnt == 0) clk.cnt = clk.len;
  return min;
}


void IHCB_t::print ( FILE* f ) {
}
void IHCB_t::print ( std::ostream& s ) {
}


void IHCB_t::dump_init ( FILE* f ) {
}


void IHCB_t::dump ( FILE* f, int t ) {
}




void IHCB_t::clock_lo ( dat_t<1> reset, bool assert_fire ) {
  val_t T0;
  T0 = (IHCB__io_blkBits.values[0] >> 4) & 1;
  val_t T1;
  T1 = T0 == 0x1L;
  val_t T2;
  { T2 = TERNARY(T1, IHCB__io_E0.values[0], 0x0L);}
  val_t T3;
  T3 = (IHCB__io_blkBits.values[0] >> 5) & 1;
  val_t T4;
  T4 = T3 == 0x1L;
  val_t T5;
  { T5 = T1 ^ 0x1L;}
  val_t T6;
  { T6 = T5 & T4;}
  val_t T7;
  { T7 = TERNARY_1(T6, IHCB__io_E1.values[0], T2);}
  val_t T8;
  T8 = (IHCB__io_blkBits.values[0] >> 6) & 1;
  val_t T9;
  T9 = T8 == 0x1L;
  val_t T10;
  { T10 = T1 | T4;}
  val_t T11;
  { T11 = T10 ^ 0x1L;}
  val_t T12;
  { T12 = T11 & T9;}
  val_t T13;
  { T13 = TERNARY_1(T12, IHCB__io_E6.values[0], T7);}
  val_t T14;
  T14 = (IHCB__io_blkBits.values[0] >> 7) & 1;
  val_t T15;
  T15 = T14 == 0x1L;
  val_t T16;
  { T16 = T10 | T9;}
  val_t T17;
  { T17 = T16 ^ 0x1L;}
  val_t T18;
  { T18 = T17 & T15;}
  val_t T19;
  { T19 = TERNARY_1(T18, IHCB__io_E7.values[0], T13);}
  { IHCB__io_W1.values[0] = T19;}
  val_t T20;
  T20 = (IHCB__io_blkBits.values[0] >> 8) & 1;
  val_t T21;
  T21 = T20 == 0x1L;
  val_t T22;
  { T22 = TERNARY(T21, IHCB__io_E4.values[0], 0x0L);}
  val_t T23;
  T23 = (IHCB__io_blkBits.values[0] >> 9) & 1;
  val_t T24;
  T24 = T23 == 0x1L;
  val_t T25;
  { T25 = T21 ^ 0x1L;}
  val_t T26;
  { T26 = T25 & T24;}
  val_t T27;
  { T27 = TERNARY_1(T26, IHCB__io_E5.values[0], T22);}
  val_t T28;
  T28 = (IHCB__io_blkBits.values[0] >> 10) & 1;
  val_t T29;
  T29 = T28 == 0x1L;
  val_t T30;
  { T30 = T21 | T24;}
  val_t T31;
  { T31 = T30 ^ 0x1L;}
  val_t T32;
  { T32 = T31 & T29;}
  val_t T33;
  { T33 = TERNARY_1(T32, IHCB__io_E10.values[0], T27);}
  val_t T34;
  T34 = (IHCB__io_blkBits.values[0] >> 11) & 1;
  val_t T35;
  T35 = T34 == 0x1L;
  val_t T36;
  { T36 = T30 | T29;}
  val_t T37;
  { T37 = T36 ^ 0x1L;}
  val_t T38;
  { T38 = T37 & T35;}
  val_t T39;
  { T39 = TERNARY_1(T38, IHCB__io_E11.values[0], T33);}
  { IHCB__io_W5.values[0] = T39;}
  val_t T40;
  T40 = (IHCB__io_blkBits.values[0] >> 0) & 1;
  val_t T41;
  T41 = T40 == 0x1L;
  val_t T42;
  { T42 = TERNARY(T41, IHCB__io_W2.values[0], 0x0L);}
  val_t T43;
  T43 = (IHCB__io_blkBits.values[0] >> 1) & 1;
  val_t T44;
  T44 = T43 == 0x1L;
  val_t T45;
  { T45 = T41 ^ 0x1L;}
  val_t T46;
  { T46 = T45 & T44;}
  val_t T47;
  { T47 = TERNARY_1(T46, IHCB__io_W3.values[0], T42);}
  val_t T48;
  T48 = (IHCB__io_blkBits.values[0] >> 2) & 1;
  val_t T49;
  T49 = T48 == 0x1L;
  val_t T50;
  { T50 = T41 | T44;}
  val_t T51;
  { T51 = T50 ^ 0x1L;}
  val_t T52;
  { T52 = T51 & T49;}
  val_t T53;
  { T53 = TERNARY_1(T52, IHCB__io_W8.values[0], T47);}
  val_t T54;
  T54 = (IHCB__io_blkBits.values[0] >> 3) & 1;
  val_t T55;
  T55 = T54 == 0x1L;
  val_t T56;
  { T56 = T50 | T49;}
  val_t T57;
  { T57 = T56 ^ 0x1L;}
  val_t T58;
  { T58 = T57 & T55;}
  val_t T59;
  { T59 = TERNARY_1(T58, IHCB__io_W9.values[0], T53);}
  { IHCB__io_E3.values[0] = T59;}
}


void IHCB_t::clock_hi ( dat_t<1> reset ) {
}


void IHCB_api_t::init_sim_data (  ) {
  sim_data.inputs.clear();
  sim_data.outputs.clear();
  sim_data.signals.clear();
  IHCB_t* mod = dynamic_cast<IHCB_t*>(module);
  assert(mod);
  sim_data.inputs.push_back(new dat_api<1>(&mod->IHCB__io_W2));
  sim_data.inputs.push_back(new dat_api<1>(&mod->IHCB__io_W3));
  sim_data.inputs.push_back(new dat_api<1>(&mod->IHCB__io_W8));
  sim_data.inputs.push_back(new dat_api<1>(&mod->IHCB__io_W9));
  sim_data.inputs.push_back(new dat_api<1>(&mod->IHCB__io_E0));
  sim_data.inputs.push_back(new dat_api<1>(&mod->IHCB__io_E1));
  sim_data.inputs.push_back(new dat_api<1>(&mod->IHCB__io_E4));
  sim_data.inputs.push_back(new dat_api<1>(&mod->IHCB__io_E5));
  sim_data.inputs.push_back(new dat_api<1>(&mod->IHCB__io_E6));
  sim_data.inputs.push_back(new dat_api<1>(&mod->IHCB__io_E7));
  sim_data.inputs.push_back(new dat_api<1>(&mod->IHCB__io_E10));
  sim_data.inputs.push_back(new dat_api<1>(&mod->IHCB__io_E11));
  sim_data.inputs.push_back(new dat_api<72>(&mod->IHCB__io_blkBits));
  sim_data.outputs.push_back(new dat_api<1>(&mod->IHCB__io_W1));
  sim_data.outputs.push_back(new dat_api<1>(&mod->IHCB__io_W5));
  sim_data.outputs.push_back(new dat_api<1>(&mod->IHCB__io_E3));
  sim_data.clk_map["clk"] = new clk_api(&mod->clk);
}
