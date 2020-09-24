package tw.org.iii.bill.guessgame;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //啟動執行序
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(4000);
                    startActivity(new Intent().setClass(WelcomeActivity.this,MainActivity.class));
                    finish();
                }catch (Exception e){
                    e.toString();
                }
            }
        }).start();


    }
}
