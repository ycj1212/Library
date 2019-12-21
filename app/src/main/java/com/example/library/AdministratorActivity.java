package com.example.library;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class AdministratorActivity extends AppCompatActivity {
    static {
        System.loadLibrary("native-lib");
    }

    BookFragment bookFragment = new BookFragment();
    UserFragment userFragment = new UserFragment();

    BottomNavigationView bottomNavigationView;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;

    ArrayList<Bookdb> bookdb = bookdb();
    ArrayList<Userdb> userdb = userdb();

    String id;
    int idx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        idx = intent.getIntExtra("idx", -1);

        login_admin(id, idx, userdb.get(idx).getName());

        final View include_book = findViewById(R.id.include_book);
        final View include_user = findViewById(R.id.include_user);

        ListView listView = (ListView)findViewById(R.id.listview_book);
        ListView listView2 = (ListView)findViewById(R.id.listview_user);

        final BookAdapter bookAdapter = new BookAdapter(this, bookdb, listView);
        final UserAdapter userAdapter = new UserAdapter(this, userdb);

        listView.setAdapter(bookAdapter);
        listView2.setAdapter(userAdapter);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.include_book, bookFragment).commitAllowingStateLoss();

        bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation_admin);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch(menuItem.getItemId()) {
                    case R.id.action_book:
                        include_book.setVisibility(View.VISIBLE);
                        include_user.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.action_user:
                        include_book.setVisibility(View.INVISIBLE);
                        include_user.setVisibility(View.VISIBLE);
                        break;
                    case R.id.action_logout2:
                        //로그아웃
                        show();
                }
                return true;
            }
        });
    }

    void show()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("로그아웃 하실건가요?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"로그아웃하였습니다.",Toast.LENGTH_LONG).show();
                        logout_admin();
                        finish();
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"취소하였습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }

    public native void login_admin(String id, int idx, String name);
    public native void logout_admin();
    public native ArrayList<Bookdb> bookdb();
    public native ArrayList<Userdb> userdb();
}
