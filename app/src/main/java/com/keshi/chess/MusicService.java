package com.keshi.chess;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MusicService extends Service {
    // MediaPlayer-Music,Sound.
    private MediaPlayer music;
    private MediaPlayer sound;
    private int activity_count;
    public static boolean state_mute;
    public static int volume_music,volume_sound;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        activity_count=0;
        // Create MusicReceiver.
        MusicReceiver serviceReceiver=new MusicReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.keshi.MUSIC");
        registerReceiver(serviceReceiver,filter);
        // Set music play of default.
        SharedPreferences preferences=getSharedPreferences("FusionChess",MODE_PRIVATE);
        state_mute=preferences.getBoolean("MUTE",false);
        volume_music=preferences.getInt("MUSIC",3);
        volume_sound=preferences.getInt("SOUND",3);
        playMusic(!state_mute);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(music.isPlaying())
            music.stop();
        music.release(); music=null;
        sound.release(); sound=null;   // Stop music and sound and release them.
        System.gc();
    }

    private void playMusic(boolean play){
        if(play) {
            if(music==null) {
                music = MediaPlayer.create(this, R.raw.music);
                music.setVolume((float) (volume_music/5.0),(float) (volume_music/5.0));
                music.setLooping(true);
                music.start();  // Play music.
            }
            else if(!music.isPlaying())
                music.start();  // Continue Music.
        }
        else if(music!=null){
            if(state_mute) {
                music.release();    // Release music.
                music = null;
                System.gc();
            }
            else music.pause();   // Pause music.
        }
    }
    private void setMusicVolume(int volume_music){
        if(music!=null){
            music.setVolume((float) (volume_music/5.0),(float) (volume_music/5.0));
        }
    }

    // Play sound.
    private void playSound(int resid){
        if(resid==-1) return;   // Exit when resource id is -1.
        sound=MediaPlayer.create(this,resid);
        sound.setVolume((float) (volume_sound/5.0),(float) (volume_sound/5.0));
        sound.setLooping(false);
        sound.start();
    }

     class MusicReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            // state_mute="mute", volume_music="music", volume_sound="sound",
            // play_sound="playSound".
            activity_count+=intent.getIntExtra("count",0);
            state_mute=intent.getBooleanExtra("mute",state_mute);
            if(activity_count>=0)
                playMusic(!state_mute);  // Play music.
            else
                playMusic(false);    // Pause music.

            int music=intent.getIntExtra("music",volume_music);
            if(music!=volume_music) {
                volume_music=music;     // Change volume of music.
                setMusicVolume(volume_music);
            }

            volume_sound= intent.getIntExtra("sound",volume_sound);
            int play_sound=intent.getIntExtra("playSound",-1);
            if(play_sound!=-1)  // Play sound.
                playSound(play_sound);
        }
    }
}