package com.example.davidladd.mediaplayertest;

import android.media.MediaPlayer;
import android.media.VolumeShaper;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class DavesMediaPlayer extends MediaPlayer {
    String TAG = "Dave";
    private volatile DavesMediaPlayer oldDMP;
    private MainActivity mainActivity;
    Runnable rr;
    Handler handler;

    public DavesMediaPlayer(final MainActivity mainActivity) {
        this.mainActivity = mainActivity;

        this.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.d(TAG, "on music Prepared: duration is " + DavesMediaPlayer.super.getDuration());
                DavesMediaPlayer.super.start();
                mainActivity.buttgong.setText("gong");

                startMyTimer(DavesMediaPlayer.super.getDuration() - 15000);
                //startMyTimer(20000);
                if(oldDMP != null) oldDMP.reset();
                }
        });
    }

    public void playSong(Bundle songBundle, DavesMediaPlayer odmp){
        this.oldDMP = odmp;
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
        //final Handler
                handler = new Handler();
        //final Runnable
                rr = new Runnable()
        {
            public void run()
            {
                Log.d(TAG, "runnable: 30 seconds left so choose next song");
                mainActivity.chooseSong();
                //handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(rr, duration);
    }

}
