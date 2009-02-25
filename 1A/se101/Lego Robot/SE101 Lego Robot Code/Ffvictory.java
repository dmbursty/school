/* Generated by Brick's Music Studio 1.5 */
/* Developed by Guido Truffelli www.aga.it/~guy */
/* Special thanks to Brian Bagnall */

/* Original MIDI file C:\Documents and Settings\Max\Desktop\Ffvictory.mid */
/* Transposed by 0 semitones; Playback speed 100% */

import josx.platform.rcx.Sound;

/* Trumpet */

class MusicPlayer {
  private static final short [] note = {
    0,10, 78,25, 0,2, 98,25, 117,27, 78,27, 98,27, 117,27, 156,25, 196,27, 
    233,27, 311,27, 392,25, 0,2, 466,25, 622,54, 622,52, 0,2, 622,52, 622,160, 
    494,160, 554,158, 0,2, 622,106, 554,52, 622,481, 233,160, 208,158, 233,160, 208,79, 
    277,160, 277,81, 262,158, 277,81, 262,160, 262,79, 233,160, 208,160, 196,158, 208,81, 
    175,719, 233,160, 208,160, 233,160, 208,79, 277,160, 277,79, 262,160, 277,81, 262,158, 
    262,81, 233,160, 208,158, 233,160, 277,81, 311,719, 233,160, 208,160, 233,160, 208,79, 
    277,160, 277,79, 262,160, 277,79, 0,2, 262,158, 262,81, 233,158, 208,160, 196,160, 
    208,79, 0,2, 175,719, 233,160, 208,160, 233,158, 208,81, 277,160, 277,79, 262,160, 
    277,79, 262,160, 262,79, 233,160, 208,160, 233,160, 277,79, 311,721, 233,160, 208,158, 
    233,160, 208,81, 277,158, 277,81, 262,160, 277,79, 262,160, 262,79, 233,160, 208,160, 
    196,160, 208,79, 175,721, 233,158, 0,2, 208,158, 233,160, 208,79, 277,160, 277,81, 
    262,158, 0,2, 277,79, 262,160, 262,79, 233,160, 208,160, 233,158, 0,2, 277,79, 
    233,535, };

  public static void play() {
    for(int i=0;i<note.length; i+=2) {
      final short w = note[i+1];
      Sound.playTone(note[i], w);
      try { Thread.sleep(w*10); } catch (InterruptedException e) {}
    }
  }
  public static void main(String [] args) { MusicPlayer.play(); }
}
