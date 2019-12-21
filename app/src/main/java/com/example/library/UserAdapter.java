package com.example.library;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class UserAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Userdb> data;

    String id = new UserActivity().id;

    public UserAdapter(Context context, ArrayList<Userdb> userdb) {
        mContext = context;
        data = userdb;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item_user, parent, false);
        }

        TextView name = (TextView)convertView.findViewById(R.id.user_name);
        TextView id = (TextView)convertView.findViewById(R.id.user_id);
        TextView pw = (TextView)convertView.findViewById(R.id.user_pw);

        Button modify = (Button)convertView.findViewById(R.id.user_modify);
        Button delete = (Button)convertView.findViewById(R.id.user_delete);

        name.setText(data.get(position).getName());
        id.setText(data.get(position).getId());
        pw.setText(data.get(position).getPw());

        return convertView;
    }
}
