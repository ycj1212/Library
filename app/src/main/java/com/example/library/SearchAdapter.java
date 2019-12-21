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

public class SearchAdapter extends BaseAdapter {
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
    Button loan;

    ListView listView;

    String id = new UserActivity().id;
    boolean flag;
    int pos;

    public SearchAdapter(Context context, ArrayList<Bookdb> bookdb, ListView listView) {
        mContext = context;
        data = bookdb;
        this.listView = listView;
    }

    @Override
    public int getCount() {
        try {
            return data.size();
        } catch (Exception e) {
            return 0;
        }
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item, parent, false);
        }

        title = (TextView)convertView.findViewById(R.id.booklist_title);
        author = (TextView)convertView.findViewById(R.id.booklist_author);
        publisher = (TextView)convertView.findViewById(R.id.booklist_publisher);
        year = (TextView)convertView.findViewById(R.id.booklist_year);
        status = (TextView)convertView.findViewById(R.id.loan_status);
        loan = (Button)convertView.findViewById(R.id.loan);

        loan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button)v;
                if(b.getText().equals("대출")) {
                    show();
                    if(flag == true) {
                        loan(position);
                    }
                }
            }
        });

        title.setText(data.get(position).getBookName());
        author.setText(data.get(position).getAuthor());
        publisher.setText(data.get(position).getPublisher());
        year.setText(data.get(position).getYear());
        if(data.get(position).getLoaner().equals("")) {
            status.setText("대출가능");
            status.setTextColor(Color.BLUE);
            loan.setClickable(true);
        }
        else {
            status.setText("대출중");
            status.setTextColor(Color.RED);
            loan.setClickable(false);
        }


        return convertView;
    }

    void show()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("이 책을 대출하시겠습니까?");
        builder.setMessage("대출 기한은 대출한 날로부터 7일 입니다.");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        flag = true;
                        Toast.makeText(mContext,"대출하였습니다.",Toast.LENGTH_LONG).show();
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

    public native void loan(int idx);
}
