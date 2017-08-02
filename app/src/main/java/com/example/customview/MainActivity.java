package com.example.customview;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.customview.VIEW1.StackView;
import com.example.customview.VIEW3.Lock;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    StackAdapter a ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivty_main);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }

//        MyViewGroup2 vg = (MyViewGroup2) findViewById(R.id.vg);
//        View view = LayoutInflater.from(this).inflate(R.layout.abc,null);
////        view.setTranslationY(100);
//        vg.addView(view);
//
        List<Integer> args = new ArrayList<>();
        args.add(1);
        args.add(2);
        args.add(3);
        args.add(4);
        args.add(5);
        args.add(6);
        args.add(7);
        args.add(8);
        StackView stackView = (StackView) findViewById(R.id.stack_view);
        stackView.setShowPagerCount(2);
         a = new StackAdapter(args);
        stackView.setAdapter(a);

//        ImageView im = (ImageView) findViewById(R.id.imageView);
//        im.setTranslationX(100f);
//        im.setPivotX(getWindow().getDecorView().getWidth()/2);
//        im.setScaleX(0.5f);
//        Log.e("MainActivity",getWindow().getDecorView().getWidth()/2+"WIDTH");

        final Lock lock = (Lock) findViewById(R.id.lock);
        lock.setOnFinshInputListener(new Lock.onFinshInputListener() {
            @Override
            public void onFinish(boolean isSecretSetup, boolean isCorrect, List<Integer> results) {
                Log.e("MainActivity", "isUP = " + isSecretSetup + " is Correct = " + isCorrect + "  resultSize = " + results.size() );
                if(!isSecretSetup){
                    lock.setSecret(results);
                }
                
                if(isCorrect){
                    // TODO: 2017/8/2  do sth
                }
            }
        });

    };

        class StackAdapter extends StackView.Adapter<StackAdapter.StackHolder>{

            List<Integer> args ;

            public StackAdapter(List<Integer> args) {
                this.args = args;
            }

            @Override
            public StackHolder onCreateViewHolder(ViewGroup parent , int pos) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.abc,parent,false);
                return new StackHolder(view);
            }

            @Override
            public void onBindViewHolder(StackView.ViewHolder holder, int pos) {
                int c = args.get(pos);
                if(c == 1) {
                    ((StackHolder) holder).card.setBackgroundColor(Color.BLUE);
                }else if(c ==2){
                    ((StackHolder) holder).card.setBackgroundColor(Color.RED);
                }else if (c == 3){
                    ((StackHolder) holder).card.setBackgroundColor(Color.YELLOW);
                }else if (c== 4){
                    ((StackHolder) holder).card.setBackgroundColor(Color.GRAY);
                }else{
                    ((StackHolder) holder).card.setBackgroundColor(Color.BLACK);

                }
                ((StackHolder) holder).im.setText(args.get(pos)+"");

            }


            @Override
            public int getItemtCount() {
                return args.size();
            }

            @Override
            public void onPop(int position,int size) {
                args.remove(position);
                if(size==0){
                    args.add(1);
                    args.add(2);
                    args.add(3);
                    args.add(4);
                    notifyDataSetChanged();
                }

            }

            public class StackHolder extends StackView.ViewHolder{

                TextView im;
                CardView card;

                public StackHolder(View itemView) {
                    super(itemView);
                    im = (TextView) itemView.findViewById(R.id.imageView2);
                    card = (CardView) itemView.findViewById(R.id.card);
                }
            }
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1 ){

        }
    }
}

