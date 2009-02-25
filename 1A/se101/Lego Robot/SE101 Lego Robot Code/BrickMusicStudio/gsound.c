/* GSound */

#include <dsound.h>
#include <sys/h8.h>

static inline void gplay_freq(unsigned int freq)
{
  static const unsigned int BaseDivider[6] = { 2, 8, 32, 64, 128, 1024 };
  static const unsigned char MaskDivider[6] = { 0x81, 0x01, 0x82, 0x02, 0x83, 0x03 };

  const unsigned long MainFreq = 16000000L;
  int nBestBase = 0;
  int nBestDivider = 0;
  int nBest = -1;
  int d;
  for (d = 0; d < 6; d++)
  {
    const unsigned long BaseFreq = MainFreq / BaseDivider[d];
    unsigned long nDivider = BaseFreq / (2*freq);
    if (nDivider > 0 && nDivider < 256)
    {
      const unsigned long nResult = BaseFreq / (2*nDivider);
      int nScore = nResult-freq;
      if (nScore < 0) nScore = -nScore;
      if (nBest < 0 || nScore < nBest)
      {
        nBestBase = d;
        nBestDivider = nDivider;
        nBest = nScore;
        if (nBest == 0)
          break;
      }
    }
  }
  if (nBest >= 0)
  {
    unsigned char CKSmask = MaskDivider[nBestBase];
    unsigned char match = nBestDivider;

    if (CKSmask & 0x80)
      STCR |=  0x01;
    else
      STCR &= ~0x01;
    T0_CORA = match;
    T0_CSR  = CSR_TOGGLE_ON_A;
    T0_CR   = CR_CLEAR_ON_A | (CKSmask & 0x3);
  }
}

static int cicles_per_msec = 0;

static void init_gsleep()
{
  int mini = 0;
  int maxi = 4096;
  while (mini < maxi)
  {
    unsigned int i;
    int result;
    const int guess = (mini+maxi) / 2;
    time_t t = sys_time;
    while (sys_time == t);
    t++;
    i = guess;
    while (i--);
    result = sys_time-t;
    if (result < 8)
    {
      cicles_per_msec = guess / 8;
      mini = guess+1;
    }
    else
      maxi = guess-1;
  }
}

static inline void gsleep(int msec)
{
  while (msec--)
  {
    unsigned int i = cicles_per_msec;
    while (i--);
  }
}

void gplay_sample(const int* sample, int sleep)
{
  int i;
  if (cicles_per_msec <= 0)
    init_gsleep();
  for (i = 0; sample[i] >= 0; i++) 
  {
		if (sample[i] > 0)
      gplay_freq(sample[i]);
		else
			T0_CR = 0x00;
    gsleep(sleep);
  }
  T0_CR = 0x00;
}

