package tw.org.iii.bill.guessgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LeaveGameDialog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_game_dialog);
    }

    public void doLeaveGame(View view) {
        Button btn = findViewById(R.id.leaveGame_Yes);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LeaveGameDialog.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
