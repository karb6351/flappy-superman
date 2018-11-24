package com.example.david.testproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class RecordListAdapter extends BaseAdapter {
    private ArrayList<User> users;
    private Context context;
    private static LayoutInflater inflater = null;


    public RecordListAdapter(Context context, ArrayList<User> users){

        this.users = users;
        inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi = view;
        if (vi == null){
            vi = inflater.inflate(R.layout.rank_item, null);
        }
        TextView rankNameText = vi.findViewById(R.id.rank_name_text);
        TextView rankScoreText = vi.findViewById(R.id.rank_score_text);
        TextView rankText = vi.findViewById(R.id.rank_text);
        rankNameText.setText(users.get(i).name);
        rankScoreText.setText(users.get(i).score + "");
        rankText.setText(users.indexOf(users.get(i)) + 1 + "");
        return vi;
    }
}
