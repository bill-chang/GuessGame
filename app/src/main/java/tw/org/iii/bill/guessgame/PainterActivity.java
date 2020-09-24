package tw.org.iii.bill.guessgame;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PainterActivity extends AppCompatActivity {
    private MyView myView;
    private FirebaseDatabase database;
    private DatabaseReference myRef, myRef1;
    private Timer timer = new Timer();
    private TextView tv;
    private ArrayList<String> abc123 = new ArrayList<>();
    private ArrayList<String> abc456 = new ArrayList<>();
    private ArrayList<String> evSecond = new ArrayList<>();
    private ArrayList<String> abc789 = new ArrayList<>();
    private ArrayList<String> abc000 = new ArrayList<>();
    private ArrayList<String> abc777 = new ArrayList<>();
    private ArrayList<String> lineLast = new ArrayList<>();
    private ArrayList<String> recycleLine = new ArrayList<>();
    private DataSnapshot dataSnapshot;
    private Object topline,run2;
    private ArrayList<ArrayList< HashMap<String,Float>>> turn=new ArrayList<>();
//    private ArrayList<ArrayList< HashMap<String,Float>>> test123=new ArrayList<>();
    private ListView chatContainer;
    ProgressBar progressBar;
    MyCountDownTimer myCountDownTimer;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painter);
        myView = findViewById(R.id.painter);
        new MyPainterTime();
        database = FirebaseDatabase.getInstance();
        myRef1 = database.getReference();
        tv = findViewById(R.id.answer);
        //倒數計時條 adapter
        Timer topicTime = new Timer();
        topicTime.schedule(new MyTask(), 1 * 1000, 1 * 1000);
        chatContainer=findViewById(R.id.chatContainer);
        myRef1.addListenerForSingleValueEvent(singleValue);
        myRef1.addValueEventListener(eventListener);

        countDownTimer();

    }

    ValueEventListener singleValue = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            PainterActivity.this.dataSnapshot = dataSnapshot;
            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                abc123.add(childSnapshot.getKey());
                for (DataSnapshot member : childSnapshot.getChildren()) {
                    abc456.add(member.getKey());
                }
            }
            callTopic();


        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    ValueEventListener eventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                abc777.add(childSnapshot.getKey());
                for (DataSnapshot member : childSnapshot.getChildren()) {
                    evSecond.add(member.getKey());
                    for (DataSnapshot fbLine : member.getChildren()) {
                        abc789.add(fbLine.getKey());
//                        Log.v("bill", "aaaa:::" + abc789.size());
                        for (DataSnapshot fbPoint : fbLine.getChildren()) {
                            abc000.add(member.getKey());
//                            Log.v("bill", "bbb:::" + abc000.size());
                        }
                    }
                }
            }
            searchData();
            initPLv();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void initPLv(){
        String ffp=abc777.get(abc777.size()-1);
        DatabaseReference chatRoom=myRef1.child(ffp).child("chatRoom");
        ListAdapter adapter=new FirebaseListAdapter<Chat>(this,Chat.class,R.layout.chat_layout,chatRoom) {
            @Override
            protected void populateView(View v, Chat chat, int position) {
               ((TextView)v.findViewById(R.id.message_time)).setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",chat.getTime()));
                ((TextView)v.findViewById(R.id.message_user)).setText(chat.getName());
                ((TextView)v.findViewById(R.id.message_text)).setText(String.valueOf(chat.getChatMessage()));

            }
        };
        chatContainer.setAdapter(adapter);
    }




    public void onClear(View view) {
        String fp=abc123.get(abc123.size()-1);
        DatabaseReference clearAll=myRef1.child(fp).child("paintTool").child("line");
        clearAll.removeValue();
        myView.clear();
    }

    public void redo(View view) {
        String fp=abc123.get(abc123.size()-1);
        myRef1.child(fp).child("paintTool").child("line").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    recycleLine.add(postSnapshot.getKey());
                }
                String fp=abc123.get(abc123.size()-1);
                String wp= recycleLine.get(recycleLine.size()-1);
                int i=Integer.parseInt(wp);
                String change=""+(i+1);

                Log.v("bill","name:"+change);
                DatabaseReference noll= myRef1.child(fp).child("paintTool").child("line").child(change);
                noll.setValue(run2);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myView.redo();
    }



    public void undo(View view) {
        String fp=abc123.get(abc123.size()-1);
        DatabaseReference snap=myRef1.child(fp).child("paintTool").child("line");
        snap.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    lineLast.add(postSnapshot.getKey());
                }
                String fp=abc123.get(abc123.size()-1);
                String sp=lineLast.get(lineLast.size()-1);
                run2=dataSnapshot.child(sp).getValue();
//                test123.add((ArrayList)run2);
                Log.v("bill","run2:"+run2);
                DatabaseReference linePt=myRef1.child(fp).child("paintTool").child("line").child(sp);

                linePt.removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myView.undo();
    }

    public void chColor(View view) {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose color")
                .initialColor(myView.getColor())
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        //toast("onColorSelected: 0x" + Integer.toHexString(selectedColor));
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        //changeBackgroundColor(selectedColor);
                        myView.setColor(selectedColor);
                    }
                })
                .setNegativeButton("cancel", null)
                .build()
                .show();
    }

    private class MyPainterTime extends AsyncTask<Integer, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(Integer... integers) {
            return null;
        }
    }

    private class MyTask extends TimerTask {
        int i = 0;

        @Override
        public void run() {

            timer.schedule(new MyTask(), 1 * 1000, 1 * 1000);
            if (i == 40) {
                finish();
            }
        }
    }

//    @Override
//    public void finish() {
//        if (timer != null) {
//            timer.cancel();
//            timer.purge();
//            timer = null;
//        }
//
//        super.finish();
//    }

    public void callTopic() {
        String fp = abc123.get(abc123.size() - 1);
        String sp=abc456.get(abc456.size()-2);
        ArrayList<String> sd=(ArrayList<String>) dataSnapshot.child(fp).child(sp).getValue();
        final int random=(int)(Math.random()*4);

        String topicText=sd.get(random);
        tv.setText(topicText);
    }
    public void searchData(){
        String fp = abc777.get(abc777.size() - 1);
        topline =dataSnapshot.child(fp).child("paintTool").child("line").getValue();
        turn.add((ArrayList)topline);
        Intent intent=new Intent(this,GameActivity.class);
        intent.putExtra("paintData",turn);

//        Log.v("bill","turn:"+turn.size());
    }

    public void countDownTimer() {
        progressBar = findViewById(R.id.painter_progressbar);

        progressBar.setProgress(600);
        myCountDownTimer = new MyCountDownTimer(60000, 500);
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
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    @Override
    protected void onDestroy() {
        myRef1.removeEventListener(eventListener);
        super.onDestroy();
    }
}

