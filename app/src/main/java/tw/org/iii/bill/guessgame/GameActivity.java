package tw.org.iii.bill.guessgame;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class GameActivity extends AppCompatActivity implements View.OnClickListener  {
    private MyView2 myView2;
    private Boolean isNewRoom = true;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ListView game_user;
    private EditText chat_answer_et, chat_right_et;
    private ListView left_lv, right_lv;
    private ArrayList<String> aaa = new ArrayList<>();
    private ArrayList<String> bbb = new ArrayList<>();
    private ArrayList<String> evOne = new ArrayList<>();
    private ArrayList<String> evTwo = new ArrayList<>();
    private DataSnapshot dataSnapshot;
    private AudioManager audioManager;
    private ToggleButton buttonMute;
    private Intent serviceIntent;
    private String etName,fp;
    private Date date;
    private ArrayAdapter adapter;
    private ArrayList draw;

    ProgressBar progressBar;
    MyCountDownTimer myCountDownTimer;

    //設一個adapter 填滿 作答區
    //作答


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        soundMute();
        countDownTimer();
        myView2=findViewById(R.id.view2);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        myRef.addValueEventListener(eventListener);
        myRef.addListenerForSingleValueEvent(singleListener);

//        if(isNewRoom==false) {
//            Intent intent = new Intent(this, PainterActivity.class);
//            startActivity(intent);
//        }else{
//            Intent intent = new Intent(this, PainterActivity.class);
//            startActivity(intent);
//        }

        game_user = findViewById(R.id.game_user);
        chat_right_et=findViewById(R.id.chat_right_et);
        chat_answer_et = findViewById(R.id.chat_answer_et);
        right_lv=findViewById(R.id.right_lv);

        Intent intent=getIntent();
        etName=intent.getStringExtra("name");
//        if()
//        draw=intent.getIntegerArrayListExtra("PaintData");



//        chat_answer_et.setText(etName);

        Thread gameUser = new Thread(){
            @Override
            public void run() {

            }
        };
    }


    ValueEventListener singleListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            GameActivity.this.dataSnapshot = dataSnapshot;
            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                aaa.add(childSnapshot.getKey());
                for (DataSnapshot member : childSnapshot.getChildren()) {
                    bbb.add(member.getKey());
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    ValueEventListener eventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                evOne.add(childSnapshot.getKey());
                for (DataSnapshot member : childSnapshot.getChildren()) {
                    evTwo.add(member.getKey());
                }
            }
            initLv();
            initChatMessage();

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void initLv() {

        fp=evOne.get(evOne.size()-1);
        DatabaseReference userInfo=myRef.child(fp).child("users");
        ListAdapter adapter=new FirebaseListAdapter<User>(this,User.class,R.layout.users_layout,userInfo) {
            @Override
            protected void populateView(View v, User user, int position) {
//                ((ImageView)v.findViewById(R.id.users_iv)).setAutofillHints(user.getImg());
                ((TextView)v.findViewById(R.id.user_name)).setText(user.getName());
                ((TextView)v.findViewById(R.id.users_pointed)).setText(String.valueOf(user.getPoint()));
            }
        };
        game_user.setAdapter(adapter);

    }

    public void initChatMessage(){
        String roomName=evOne.get(evOne.size()-1);
        DatabaseReference chatMessage=myRef.child(roomName).child("chatRoom");
        ListAdapter adapter=new FirebaseListAdapter<Chat>(this,Chat.class,R.layout.chat_layout,chatMessage) {
            @Override
            protected void populateView(View v, Chat chat, int position) {
                ((TextView)v.findViewById(R.id.message_time)).setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",chat.getTime()));
                ((TextView)v.findViewById(R.id.message_user)).setText(chat.getName());
                ((TextView)v.findViewById(R.id.message_text)).setText(String.valueOf(chat.getChatMessage()));
            }
        };
        right_lv.setAdapter(adapter);
    }



    private void soundMute() {
        buttonMute = findViewById(R.id.game_soundMute);
        buttonMute.setChecked(false);

        buttonMute.setOnClickListener((View.OnClickListener) this);

        serviceIntent = new Intent(getApplicationContext(), MusicService.class);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        buttonMute.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, isChecked);

            }
        });
    }

    public void countDownTimer() {
        progressBar = findViewById(R.id.game_progressbar);

        progressBar.setProgress(600);
        myCountDownTimer = new MyCountDownTimer(60000, 50);
        myCountDownTimer.start();
    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int progress = (int) (millisUntilFinished / 100);
            progressBar.setProgress(progress);
        }

        @Override
        public void onFinish() {
            progressBar.setProgress(0);
            myCountDownTimer.cancel();
            startActivity(new Intent(GameActivity.this,PainterActivity.class));
        }
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    public void doInfo(View view) {

//        DataSnapshot userId=dataSnapshot.child(fp).child("users").child(etName);
//        Object abc=userId.getValue();

        final AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this, R.style.dialog_style1);
        builder
                .setTitle("房間資訊")
                .setMessage("使用者名稱:"+etName+"\n"+"使用者分數:"+0)
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        builder.setCancelable(false);
        builder.show();
    }

    public void doReport(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this, R.style.dialog_style1);
        builder
                .setTitle("檢舉")
                .setMessage("是否要回報此題目設計不良?")
                .setPositiveButton("確定送出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        builder.setCancelable(false);
        builder.show();
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.music_mute:
                break;
        }
    }

    public void rightSendMessage(View view) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                fp=aaa.get(aaa.size()-1);
//                String chatMessage=chat_right_et.getText().toString();
//                DatabaseReference chatMessageRefer=myRef.child(fp).child("chatRoom").push();
//                chatMessageRefer.setValue(new Chat(etName,chatMessage));
//
//
//                chat_right_et.setText("");
//            }
//        }).start();
        fp=aaa.get(aaa.size()-1);
        String chatMessage=chat_right_et.getText().toString();
        DatabaseReference chatMessageRefer=myRef.child(fp).child("chatRoom").push();
        chatMessageRefer.setValue(new Chat(etName,chatMessage));


        chat_right_et.setText("");

    }

    public void checkAnswer(View view) {
//        找到PaintActivity的callTopic()的物件,並取得值
//        Log.v("bill", "aaa2=" + aaa.size());
        SimpleDateFormat awSimpleDateFormat=new SimpleDateFormat("hh:mm:ss");
        String topic = "";
        String awTime=awSimpleDateFormat.format(date);
        String answer = chat_answer_et.getText().toString();
        date=new Date();
        if (answer == topic.getBytes().toString()) {
            String awMessage=awTime+"  "+etName+":"+chat_right_et.getText().toString().replace(answer,"****");

        }else{
            String wgMessage=awTime+"  "+etName+":"+chat_right_et.getText().toString();
        }
    }

    public void doLeave(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this, R.style.dialog_style1);
        builder
                .setMessage("確定要退出此遊戲房間?")
                .setPositiveButton("確定退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setClass(GameActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        builder.setCancelable(false);
        builder.show();

    }

    @Override
    protected void onDestroy() {
        myRef.removeEventListener(eventListener);
        super.onDestroy();
    }
}
