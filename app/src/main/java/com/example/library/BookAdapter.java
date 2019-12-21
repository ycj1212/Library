package com.example.library;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class BookAdapter extends BaseAdapter {
    static {
        System.loadLibrary("native-lib");
    }
    private Context mContext;
    private ArrayList<Bookdb> data;

    TextView title;
    TextView author;
    TextView publisher;
    TextView year;
    TextView status;
    Button add, modify, delete;

    ListView listView;

    String id = new UserActivity().id;
    boolean flag;
    int pos;

    public BookAdapter(Context context, ArrayList<Bookdb> bookdb, ListView listView) {
        mContext = context;
        data = bookdb;
        this.listView = listView;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item_admin, parent, false);
        }

        title = (TextView)convertView.findViewById(R.id.booklist_title_admin);
        author = (TextView)convertView.findViewById(R.id.booklist_author_admin);
        publisher = (TextView)convertView.findViewById(R.id.booklist_publisher_admin);
        year = (TextView)convertView.findViewById(R.id.booklist_year_admin);
        status = (TextView)convertView.findViewById(R.id.loan_status_admin);

        add = (Button)convertView.findViewById(R.id.add);
        modify = (Button)convertView.findViewById(R.id.modify);
        delete = (Button)convertView.findViewById(R.id.delete);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });

        title.setText(data.get(position).getBookName());
        author.setText(data.get(position).getAuthor());
        publisher.setText(data.get(position).getPublisher());
        year.setText(data.get(position).getYear());
        if(data.get(position).getLoaner().equals("")) {
            status.setText("대출가능");
            status.setTextColor(Color.BLUE);
        }
        else {
            status.setText("대출중");
            status.setTextColor(Color.RED);
        }

        return convertView;
    }

    void show()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("이 책을 삭제하시겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        flag = true;
                        Toast.makeText(mContext,"삭제하였습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        flag = false;
                        Toast.makeText(mContext,"취소하였습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }
}
