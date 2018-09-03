package com.example.davidladd.mediaplayertest;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

public class DavesMediaPlayer extends MediaPlayer {
    String TAG = "Dave";
    //final Handler handler = new Handler();
    public volatile boolean timerEnable;
    private volatile DavesMediaPlayer instanceDMP;

    public DavesMediaPlayer() {

        this.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.d(TAG, "onPrepared: so start ");
                DavesMediaPlayer.super.start();
                startMyTimer();
                }
        });

        instanceDMP = this;
    }

    public void playSong(String pathToSong){

        try{
            Log.d(TAG, "playSong: "  + pathToSong);
            this.setDataSource(pathToSong);
            this.prepareAsync();
        }
        catch (Exception eee){
            Log.d(TAG, "playSong: err  " + eee);
        }
    }

    public void startMyTimer(){
        timerEnable = true;
        final Handler handler = new Handler();
        final Runnable r = new Runnable()
        {
            public void run()
            {
                Log.d(TAG, "run: ing a handler thighy");
                if(timerEnable == true){
                    Log.d(TAG, "pos " + instanceDMP.getCurrentPosition() );
                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.postDelayed(r, 1000);
    }

    public void stopSong(){
        timerEnable = false;
        this.release();
    }


}
