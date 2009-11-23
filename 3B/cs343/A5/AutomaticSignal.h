#define AUTOMATIC_SIGNAL    \
  uCondition _AS_condition; \
  struct _AS_Wait {         \
    int i;                  \
    _AS_Wait() : i(0) {}    \
  } _AS_wait;


#define WAITUNTIL(pred, before, after) \
  if (!(pred)) {                       \
    before;                            \
    while (!(pred)) {                  \
      _AS_wait.i++;                    \
      _AS_condition.wait();            \
      _AS_wait.i--;                    \
    }                                  \
    after;                             \
  }


#define RETURN(expr...)                               \
  for (int _AS_i = 0; _AS_i < _AS_wait.i; _AS_i++) {  \
    _AS_condition.signal();                           \
  }                                                   \
  return expr;
