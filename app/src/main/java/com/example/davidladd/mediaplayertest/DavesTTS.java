package com.example.davidladd.mediaplayertest;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import java.util.HashMap;

import static android.speech.tts.TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID;

public class DavesTTS extends TextToSpeech{
    //TextToSpeech mtts;
    String TAG = "Dave";
    Bundle songBundle;
    //final MainActivity mainActivity;

    public DavesTTS(Context context, OnInitListener listener, final MainActivity mainActivity) {
        super(context, listener);
        //this.mainActivity = mainActivity;

        this.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                Log.d(TAG, "TSS startin");
            }

            @Override
            public void onDone(final String utteranceId) {
                Log.d(TAG, "TTS done");
                //mainActivity.doneSpeaking(utteranceId, songBundle);
                Runnable rr = new Runnable()
                {
                    public void run()
                    {
                        Log.d(TAG, "runnable: post calling donespeaking");
                        mainActivity.doneSpeaking(utteranceId, songBundle);
                        //handler.postDelayed(this, 1000);
                    }
                };
                mainActivity.mainHandler.postDelayed(rr, 10);

            }

            @Override
            public void onError(String utteranceId) {
            }
        });
    }

    public void sayIt(String text, Bundle songBundle){
        this.songBundle = songBundle;
        HashMap<String, String> hashtts = new HashMap<>();
        hashtts.put(KEY_PARAM_UTTERANCE_ID, "1");

        Log.d(TAG, "sayIt: " + text);
        //this.speak("             ." + text, TextToSpeech.QUEUE_FLUSH, hashtts);
        // Have to add spaces and period to allow delay after music dip
        this.setSpeechRate(1.2f);
        this.speak("             ." + text, TextToSpeech.QUEUE_ADD, hashtts);
    }

}
