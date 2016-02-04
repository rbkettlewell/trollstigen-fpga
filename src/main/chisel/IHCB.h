#ifndef __IHCB__
#define __IHCB__

#include "emulator.h"

class IHCB_t : public mod_t {
 private:
  val_t __rand_seed;
  void __srand(val_t seed) { __rand_seed = seed; }
  val_t __rand_val() { return ::__rand_val(&__rand_seed); }
 public:
  dat_t<1> IHCB__io_E0;
  dat_t<1> IHCB__io_E1;
  dat_t<1> IHCB__io_E6;
  dat_t<1> IHCB__io_E7;
  dat_t<1> IHCB__io_W1;
  dat_t<1> IHCB__io_E4;
  dat_t<1> IHCB__io_E5;
  dat_t<1> IHCB__io_E10;
  dat_t<1> IHCB__io_E11;
  dat_t<1> IHCB__io_W5;
  dat_t<1> IHCB__io_W2;
  dat_t<1> IHCB__io_W3;
  dat_t<1> IHCB__io_W8;
  dat_t<1> IHCB__io_W9;
  dat_t<1> IHCB__io_E3;
  dat_t<72> IHCB__io_blkBits;
  clk_t clk;

  void init ( val_t rand_init = 0 );
  void clock_lo ( dat_t<1> reset, bool assert_fire=true );
  void clock_hi ( dat_t<1> reset );
  int clock ( dat_t<1> reset );
  void print ( FILE* f );
  void print ( std::ostream& s );
  void dump ( FILE* f, int t );
  void dump_init ( FILE* f );

};

#include "emul_api.h"
class IHCB_api_t : public emul_api_t {
 public:
  IHCB_api_t(mod_t* m) : emul_api_t(m) { }
  void init_sim_data();
};

#endif
