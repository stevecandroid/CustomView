package com.example.customview;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    StackAdapter a ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivty_main);

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
        StackView stackView = (StackView) findViewById(R.id.stack_view);
         a = new StackAdapter(args);
        stackView.setAdapter(a);

//        ImageView im = (ImageView) findViewById(R.id.imageView);
//        im.setTranslationX(100f);
//        im.setPivotX(getWindow().getDecorView().getWidth()/2);
//        im.setScaleX(0.5f);
//        Log.e("MainActivity",getWindow().getDecorView().getWidth()/2+"WIDTH");

    };

        class StackAdapter extends StackView.Adapter<StackAdapter.StackHolder>{

            List<Integer> args ;

            public StackAdapter(List<Integer> args) {
                this.args = args;
            }

            @Override
            public StackHolder onCreateViewHolder(ViewGroup parent) {
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
                }
                ((StackHolder) holder).im.setText(pos+"");

            }


            @Override
            public int getItemtCount() {
                return args.size();
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


}

