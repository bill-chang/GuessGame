package tw.org.iii.bill.guessgame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class MyView2 extends View {

    private Paint paint;
    private Thread thread;
    private ArrayList<ArrayList<HashMap<String,Float>>> lines;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private  ArrayList<String> abc123 =new ArrayList<>();
    private  ArrayList<String> abc456 = new ArrayList<>();
    private  ArrayList<ArrayList<Object>> abc789 = new ArrayList<>();
//    private  ArrayList<String> abc000 = new ArrayList<>();
    private DataSnapshot dataSnapshot;
    //畫圖檔



    public MyView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setBackgroundResource(R.drawable.background_white);
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();

        lines=new ArrayList<>();
        paint=new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(20);
        myRef.addValueEventListener(eventListener) ;

    }
//
//    ValueEventListener singleListener=new ValueEventListener() {
//        @Override
//        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//            MyView2.this.dataSnapshot=dataSnapshot;
//            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
//                abc123.add(childSnapshot.getKey());
//                for (DataSnapshot member : childSnapshot.getChildren()) {
//                    abc456.add(member.getKey());
//                }
//            }
//        }

//        @Override
//        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//        }
//    };

    ValueEventListener eventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            MyView2.this.dataSnapshot=dataSnapshot;
            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                abc123.add(childSnapshot.getKey());
                for (DataSnapshot member : childSnapshot.getChildren()) {
                    abc456.add(member.getKey());
                }
            }
//            if (dataSnapshot.child(abc123.get(abc123.size()-1)).child("paintTool").exists()) {
//                draw();
//            }

        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

//    public Thread getThread() {
//        return thread;
//    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for(ArrayList<HashMap<String,Float>> line:lines) {
            for (int i = 1; i < line.size(); i++) {
                HashMap<String, Float> p0 = line.get(i - 1);
                HashMap<String, Float> p1 = line.get(i);
                canvas.drawLine(p0.get("x"), p0.get("y"), p1.get("x"), p1.get("y"), paint);
            }
        }


    }


//    private void draw(){
//        String fp=abc123.get(abc123.size()-1);
//        DataSnapshot line=dataSnapshot.child(fp).child("paintTool");
//        Object obLine=line.getValue();
//        abc789.add((ArrayList)obLine);
//        for (ArrayList<ArrayList<Object>> object:abc789){
//            for(int i=2;i<object.size();i++){
//                Canvas canvas=new Canvas();
//                HashMap<String, Float> p0 =object.get(i - 1);
//                HashMap<String, Float> p1 = object.get(i);
//                canvas.drawLine(p0.get("x"), p0.get("y"), p1.get("x"), p1.get("y"), paint);
//            }
//        }
//    }
}

