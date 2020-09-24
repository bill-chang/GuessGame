package tw.org.iii.bill.guessgame;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

public class MusicSettingActivity extends AppCompatActivity implements View.OnClickListener{

    private Intent serviceIntent;
    private AudioManager audioManager;
    private ToggleButton buttonMute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_setting);

        init();

    }

    private void init() {

        buttonMute = findViewById(R.id.music_mute);
        buttonMute.setChecked(false);

        buttonMute.setOnClickListener(this);

        serviceIntent = new Intent(getApplicationContext(), MusicService.class);

        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        buttonMute.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, isChecked);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.music_mute:
                break;
        }

    }

}
