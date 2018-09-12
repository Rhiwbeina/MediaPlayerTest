package com.example.davidladd.mediaplayertest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;


import java.util.ArrayList;

import java.util.HashSet;
import java.util.List;

import java.util.Random;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class davesSpeechComposer {
    final String TAG = "Dave";
    private SharedPreferences mPreferences;
    //private String[] intros;
    Set<String> intros = new HashSet<String>();
    Set<String> outros = new HashSet<String>();
    List<String> introsList = new ArrayList<String>();
    List<String> outrosList = new ArrayList<String>();
    Bundle previousSongBundle;

    public davesSpeechComposer(Context context) {

        mPreferences = context.getSharedPreferences("sentences", MODE_PRIVATE);

        if (mPreferences.contains("intros")){
            intros = mPreferences.getStringSet("intros", new HashSet<String>());
            outros = mPreferences.getStringSet("outros", new HashSet<String>());
            Log.d(TAG, "davesSpeechComposer: got pref data");
        }
        else
        {
            Log.d(TAG, "davesSpeechComposer: creating pref data ");
            SharedPreferences.Editor shpedit = mPreferences.edit();
            // putStringSet: editing a string will not be saved, must change number of strings or delete the file
            intros.add("Next up on the show, [artist], with the smash hit [title].");
            intros.add("Now. From the [year] album, [album]. [artist], with [title] ");
            intros.add("What were you doing back in [year]? Here's [artist]");
            intros.add("[title], need I say more?");

            outros.add("Good to hear [artist] again.");
            outros.add("Wow, that takes me back. [year] to be precise.");
            outros.add("[title]. For sure.");

            shpedit.putInt("sentanceCount", intros.size());
            shpedit.putStringSet("intros", intros);
            shpedit.putStringSet("outros", outros);
            shpedit.apply();
            shpedit.commit();

            intros = mPreferences.getStringSet("intros", new HashSet<String>());
            outros = mPreferences.getStringSet("outros", new HashSet<String>());
            Log.d(TAG, "davesSpeechComposer: got pref data");
        }


        // loop over sentances and save to an array of strings
        for (String asentence: intros) {
            Log.d(TAG, "davesSpeechComposer: foreach adding from prefs " + asentence);
            introsList.add(asentence);
        }
        for (String asentence: outros) {
            Log.d(TAG, "davesSpeechComposer: foreach adding from prefs " + asentence);
            outrosList.add(asentence);
        }

        previousSongBundle = new Bundle();
        Log.d(TAG, "davesSpeechComposer: new instance done" );
    }

    public String getSentence(Bundle songBundle){
        String fullText = "";
        if (!previousSongBundle.isEmpty()){
            Log.d(TAG, "getSentence: previous bund = NOT EMPTY " + previousSongBundle.toString());
            fullText = " " + getPartSentence(previousSongBundle, outrosList);
        }
        fullText = fullText + " " + getPartSentence(songBundle, introsList);

        previousSongBundle = songBundle;
        return fullText;
    }

    public String getPartSentence(Bundle songBundle, List<String> arrayList){
        Log.d(TAG, "getSentence: previous bundle is " + previousSongBundle.toString());

        ArrayList<String> temps = new ArrayList() ;
        for (int ii = 0; ii< arrayList.size(); ii++ ) {
            if (isItSafe(songBundle, arrayList.get(ii))){
                temps.add(arrayList.get(ii));
            }
        }

        Log.d(TAG, "getSentence: so number of suitable intros = " + temps.size());
        Random r = new Random();
        int ranint = (r.nextInt(temps.size()));
        String introText = temps.get(ranint);
        if (introText.contains("[title]")){
            introText = introText.replace("[title]", songBundle.getCharSequence("title"));
        }
        if (introText.contains("[artist")){
            introText = introText.replace("[artist]", songBundle.getCharSequence("artist"));
        }
        if (introText.contains("[album]")){
            introText = introText.replace("[album]", songBundle.getCharSequence("album"));
        }
        if(introText.contains("[year]")){
            introText = introText.replace("[year]", songBundle.getCharSequence("year"));
        }

        Log.d(TAG, "getSentence: index " + ranint + " = " + temps.get(ranint));
        Log.d(TAG, "getSentence: " + introText);
        //previousSongBundle = songBundle;
        return introText;
    }

    public boolean isItSafe(Bundle songBundle, String sentence){
        boolean itIsSafe = true;
        if (songBundle.getString("year") == null){
            if (sentence.contains("[year]")){
                itIsSafe = false;
            }
        }
        if (songBundle.getString("album") == null){
            if (sentence.contains("[album]")){
                itIsSafe = false;
            }
        }
        if (songBundle.getString("artist") == null || songBundle.getString("artist") == "<unknown>"){
            if (sentence.contains("[artist]")){
                itIsSafe = false;
            }
        }
        return itIsSafe;
    }
}
