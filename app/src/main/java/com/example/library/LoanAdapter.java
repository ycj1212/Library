package com.example.library;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class LoanAdapter extends BaseAdapter {
    static {
        System.loadLibrary("native-lib");
    }

    private Context mContext;
    private ArrayList<Bookdb> data;

    String id;
    boolean flag;

    public LoanAdapter(Context context, ArrayList<Bookdb> bookdb, String id) {
        mContext = context;
        data = bookdb;
        this.id = id;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item, parent, false);
        }

        TextView title = (TextView)convertView.findViewById(R.id.booklist_title);
        TextView author = (TextView)convertView.findViewById(R.id.booklist_author);
        TextView publisher = (TextView)convertView.findViewById(R.id.booklist_publisher);
        TextView year = (TextView)convertView.findViewById(R.id.booklist_year);
        TextView status = (TextView)convertView.findViewById(R.id.loan_status);
        Button loan = (Button)convertView.findViewById(R.id.loan);

        loan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
                if(flag == true) {
                    returnBook(position);
                }
            }
        });

        if(data.get(position).getLoaner().equals(id)) {
            title.setVisibility(View.VISIBLE);
            author.setVisibility(View.VISIBLE);
            publisher.setVisibility(View.VISIBLE);
            year.setVisibility(View.VISIBLE);
            status.setVisibility(View.VISIBLE);
            loan.setVisibility(View.VISIBLE);
            title.setText(data.get(position).getBookName());
            author.setText(data.get(position).getAuthor());
            publisher.setText(data.get(position).getPublisher());
            year.setText(data.get(position).getYear());
            status.setText("대출중 (" + data.get(position).getReturnDate() + "까지 반납 예정)");
            status.setTextColor(Color.RED);
            loan.setText("반납");
            loan.setClickable(true);
        }
        else {
            title.setVisibility(View.GONE);
            author.setVisibility(View.GONE);
            publisher.setVisibility(View.GONE);
            year.setVisibility(View.GONE);
            status.setVisibility(View.GONE);
            loan.setVisibility(View.GONE);
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

    public native void returnBook(int idx);
}
