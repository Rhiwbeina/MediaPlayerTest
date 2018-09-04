package com.example.davidladd.mediaplayertest;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class DavesMediaPlayer extends MediaPlayer {
    String TAG = "Dave";
    //final Handler handler = new Handler();
    public volatile boolean timerEnable;
    //private volatile DavesMediaPlayer instanceDMP;

    public DavesMediaPlayer() {

        this.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.d(TAG, "onPrepared: so start ");
                DavesMediaPlayer.super.start();
                Log.d(TAG, "onPrepared: duration is " + DavesMediaPlayer.super.getDuration());
                //startMyTimer(DavesMediaPlayer.super.getDuration() - 30000);
                startMyTimer(20000);
                }
        });

        //instanceDMP = this;
    }

    public void playSong(Bundle songBundle){
        try{
            final String pathToSong = songBundle.getString("data");
            Log.d(TAG, "playSong: "  + pathToSong);
            this.setDataSource(pathToSong);
            this.prepareAsync();
        }
        catch (Exception eee){
            Log.d(TAG, "playSong: err  " + eee);
        }
    }

    public void startMyTimer(int duration){
        //timerEnable = true;
        final Handler handler = new Handler();
        final Runnable r = new Runnable()
        {
            public void run()
            {
                //handler.postDelayed(this, 1000);
                Log.d(TAG, "runnable: 30 seconds left so choose next song");
                MainActivity.chooseSong();
            }
        };
        handler.postDelayed(r, duration);
    }

    public void stopSong(){
        //timerEnable = false;
        //if (this != null){
            if (this.isPlaying()){
                this.stop();
            }
            this.release();
        //}
        //this.release();
    }
}
