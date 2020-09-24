package tw.org.iii.bill.guessgame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class MyView extends View  {

    private Paint paint;
    private Thread thread;
    private ArrayList<ArrayList<HashMap<String,Float>>> lines,recycle;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private  ArrayList<String> abc123 = new ArrayList<>();
    private  ArrayList<String> abc456 = new ArrayList<>();

    private int color = Color.BLUE;
    private DataSnapshot dataSnapshot;
//    private   ArrayList<HashMap<String,Float>> existLine;

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setBackgroundResource(R.drawable.background_white);
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();

        lines=new ArrayList<>();
        paint=new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(20);
        recycle=new ArrayList<>();
        myRef.addListenerForSingleValueEvent(singleListener);

    }

    ValueEventListener singleListener=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            MyView.this.dataSnapshot=dataSnapshot;
            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                abc123.add(childSnapshot.getKey());
                for (DataSnapshot member : childSnapshot.getChildren()) {
                    abc456.add(member.getKey());
                }
            }
//            if (dataSnapshot.child(abc123.get(abc123.size()-1)).child("paintTool").exists()) {
//                existLine=(ArrayList<HashMap<String, Float>>)(dataSnapshot.child(abc123.get(abc123.size() - 1)).child("paintTool").getValue());
//            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    public Thread getThread() {
        return thread;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(ArrayList<HashMap<String,Float>> line:lines) {
            HashMap<String,Float> color = line.get(0);

            paint.setColor(color.get("color").intValue());
            for (int i = 2; i < line.size(); i++) {
                HashMap<String, Float> p0 = line.get(i - 1);
                HashMap<String, Float> p1 = line.get(i);
                canvas.drawLine(p0.get("x"), p0.get("y"), p1.get("x"), p1.get("y"), paint);
            }
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        String fp = abc123.get(abc123.size() - 1);
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            recycle.clear();
            ArrayList<HashMap<String,Float>> line=new ArrayList<>();

            HashMap<String,Float> setting = new HashMap<>();
            setting.put("color", (float)color);
            line.add(setting);

            lines.add(line);
        }


        float ex=event.getX(),ey=event.getY();
        HashMap<String,Float> point=new HashMap<>();
        point.put("x",ex);
        point.put("y",ey);

        lines.get(lines.size()-1).add(point);

        DatabaseReference fbLine=myRef.child(fp).child("paintTool").child("line");
        fbLine.setValue(lines);

        invalidate();
        return true;
    }

    public void setColor(int newColor){
        color = newColor;
        invalidate();
    }

    public int getColor(){
        return color;
    }

    public void clear(){


        lines.clear();
        invalidate();
    }
    public void undo(){
        if (lines.size()>0) {
            recycle.add(lines.remove(lines.size() - 1));
            invalidate();
        }

    }
    public void redo(){
        if (recycle.size()>0) {
            lines.add(recycle.remove(recycle.size() - 1));
            invalidate();
        }
    }
}
