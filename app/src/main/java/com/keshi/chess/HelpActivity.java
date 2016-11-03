package com.keshi.chess;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class HelpActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        // Keep screen ON.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // Get Button of HelpActivity.
        Button button_back=(Button)findViewById(R.id.help_back);
        // Set listener of controls.
        button_back.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();// Close this activity.
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Open music.
        Intent intent =new Intent("com.keshi.MUSIC");
        intent.putExtra("count",1);
        sendBroadcast(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Pause music.
        Intent intent =new Intent("com.keshi.MUSIC");
        intent.putExtra("count",-1);
        sendBroadcast(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}
