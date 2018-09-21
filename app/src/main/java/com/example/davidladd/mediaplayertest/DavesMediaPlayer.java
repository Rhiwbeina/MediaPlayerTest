package com.example.davidladd.mediaplayertest;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

public class DavesMediaPlayer extends MediaPlayer {
    String TAG = "Dave";
    //private volatile DavesMediaPlayer oldDMP;
    private MainActivity mainActivity;
    Runnable rr;
    boolean prepared;
    //Handler handler;

    public DavesMediaPlayer(final MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        prepared = false;

        this.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.d(TAG, "on music Prepared: duration is " + DavesMediaPlayer.super.getDuration());
                prepared = true;
                //DavesMediaPlayer.super.start();
                //mainActivity.buttgong.setText("gong");
                //startMyTimer(DavesMediaPlayer.super.getDuration() - 15000);
                }
        });
    }

    public void prepSong(Bundle songBundle){
        //this.oldDMP = odmp;
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

    public  void playSong(Bundle songBundle, DavesMediaPlayer odmp){
        DavesMediaPlayer.super.start();
        //mainActivity.buttgong.setText("gong");

        startMyTimer(DavesMediaPlayer.super.getDuration() - 15000);
        //startMyTimer(20000);
        //if(oldDMP != null) oldDMP.reset();
    }

    public void startMyTimer(int duration){
        //final Handler
                //this.mainActivity.mainHandlerhandler = new Handler();
        //final Runnable
                rr = new Runnable()
        {
            public void run()
            {
                Log.d(TAG, "runnable: coming to the end so choose next song");
                mainActivity.chooseSong();
                //handler.postDelayed(this, 1000);
            }
        };
        this.mainActivity.mainHandler.postDelayed(rr, duration);
    }


}
