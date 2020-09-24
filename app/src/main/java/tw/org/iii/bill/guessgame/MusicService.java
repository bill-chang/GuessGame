package tw.org.iii.bill.guessgame;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MusicService extends Service {
    private MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate(){
        //Toast.makeText(this, "Service create", Toast.LENGTH_SHORT).show();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sand_castle);
        mediaPlayer.setLooping(true);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        //Toast.makeText(this, "Service start", Toast.LENGTH_SHORT).show();
        mediaPlayer.start();
    }

    @Override
    public void onDestroy() {
        //Toast.makeText(this, "Service stop", Toast.LENGTH_SHORT).show();
        mediaPlayer.stop();

    }
}
