package com.example.davidladd.mediaplayertest;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import java.util.Dictionary;
import java.util.HashMap;

import static android.speech.tts.TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID;

public class DavesTTS extends TextToSpeech{
    TextToSpeech mtts;
    String TAG = "Dave";
    Bundle songBundle;

    public DavesTTS(Context context, OnInitListener listener) {
        super(context, listener);

        this.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                Log.d(TAG, "TSS startin");
            }

            @Override
            public void onDone(String utteranceId) {
                Log.d(TAG, "TTS done");
                MainActivity.doneSpeaking(utteranceId, songBundle);
            }

            @Override
            public void onError(String utteranceId) {
            }
        });
    }

    public void setUpListener(){
    }

    public void sayHello(){
        this.speak("Hello there all you pop pickers.", TextToSpeech.QUEUE_FLUSH, null);
    }

    public void sayIt(String text, Bundle songBundle){
        this.songBundle = songBundle;
        HashMap<String, String> hashtts = new HashMap<>();
        hashtts.put(KEY_PARAM_UTTERANCE_ID, "1");

        Log.d(TAG, "sayIt: " + text);
        this.speak(text, TextToSpeech.QUEUE_FLUSH, hashtts);
    }

}
