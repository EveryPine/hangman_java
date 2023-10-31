package com.example.hangman_java;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {
    Intent intent;
    private logic gameLogic;
    private ArrayList<Integer> items;
    private Context context;

    GridViewAdapter(logic l){
        this.gameLogic = l;
        this.items = l.getData();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        context = parent.getContext();
        int item = items.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_button, parent, false);
        }
        Button gameButton = convertView.findViewById(R.id.game_button);
        gameButton.setText(String.valueOf(item));

        gameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gameLogic.check(item)){
                    gameButton.setVisibility(View.INVISIBLE);
                    intent = new Intent("update_score");
                    intent.putExtra("new_score", gameLogic.getScore()); // 여기서 newScore는 업데이트된 점수입니다.
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
                if(gameLogic.checkGameEnd()){
                    gameLogic.initGame();
                    items = gameLogic.getData();
                    for(int i = 0; i < parent.getChildCount(); i++){
                        if (parent.getChildAt(i) instanceof LinearLayout) {
                            LinearLayout gameButtonLayout = (LinearLayout) parent.getChildAt(i);
                            View invisibledGameButton = gameButtonLayout.getChildAt(0);

                            if (invisibledGameButton instanceof Button) {
                                Button button = (Button) invisibledGameButton;
                                button.setVisibility(View.VISIBLE);
                                notifyDataSetChanged();
                            }
                        }
                    }

                }
            }
        });
        return convertView;
    }
}
