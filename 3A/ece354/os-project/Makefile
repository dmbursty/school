# Makefile
# David Grant, 2004

CC=m68k-elf-gcc
CXX=m68k-elf-g++
# BIG FRICKIN' NOTE: if we add -O2 to the flags here, the compilers
#  optimizes the Counter variable into a register.  So when the interrupt
#  occurs, we change it's value, but then restore it on our way out, so the
#  Counter never actually get's incremented.  
CFLAGS=-Wall -m5200 -pipe -nostdlib
LD=m68k-elf-gcc
AS=m68k-elf-as
AR=m68k-elf-ar
ARFLAGS=
OBJCPY=m68k-elf-objcopy
ASM=start.s pcb_hw_save.s
LDFLAGS = -T../mcf5307.ld -Wl,-Map=init.map
SOURCES=init.c pcb.c null_proc.c
OBJECTS=$(SOURCES:.c=.o)
TARGET=init

# Note, GCC builds things in order, it's important to put the
#  ASM first, so that it is located at the beginning of our program.

$(TARGET).s19: $(ASM) $(OBJECTS)
	$(CC) $(CFLAGS) $(LDFLAGS) $^ -o $(TARGET).tsk
	$(OBJCPY) --output-format=srec $(TARGET).tsk $(TARGET).s19

%.o: %.c
	$(CC) $(CFLAGS) $(LDFLAGS) -c $< -o $@

clean:
	rm -f *.s19 *.o *.tsk *.map

