// IMusicInterface.aidl
package me.henry.versatile;

// Declare any non-default types here with import statements
import me.henry.versatile.model.music.MusicInfo;
interface IMusicInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
      void playOrPause();
      void pause();
      void next();
      void previous();
      void getCurrentPosition();
      void playMusic(in MusicInfo music,int index);
      void setPlayMode(in int mode);
      MusicInfo getCurrentMusicInfo();
      boolean getPlayState();
}
