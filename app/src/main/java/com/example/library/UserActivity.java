package com.example.library;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

class Userdb {
    private String name, id, pw;

    public void Userdb() { name = ""; id = ""; pw = ""; }
    public void setName(String name) { this.name = name; }
    public void setId(String id) { this.id = id; }
    public void setPw(String pw) { this.pw = pw; }
    public String getName() { return name; }
    public String getId() { return id; }
    public String getPw() { return pw; }
}
class Bookdb {
    private String bookName, author, publisher, year, loaner, loanDate, returnDate;

    public void Bookdb() {
        bookName = "";
        author = "";
        publisher = "";
        year = "";
        loaner = "";
        loanDate = "";
        returnDate = "";
    }
    public void setBookName(String bookname) { this.bookName = bookname; }
    public void setAuthor(String author) { this.author = author; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public void setYear(String year) { this.year = year; }
    public void setLoaner(String loaner) { this.loaner = loaner; }
    public void setLoanDate(String loandate) { this.loanDate = loandate; }
    public void setReturnDate(String returndate) { this.returnDate = returndate; }
    public String getBookName() { return bookName; }
    public String getAuthor() { return author; }
    public String getPublisher() { return publisher; }
    public String getYear() { return year; }
    public String getLoaner() { return loaner; }
    public String getLoanDate() { return loanDate; }
    public String getReturnDate() { return returnDate; }
}
public class UserActivity extends AppCompatActivity {

    static {
        System.loadLibrary("native-lib");
    }

    BooklistFragment booklistFragment = new BooklistFragment();
    LoanFragment loanFragment = new LoanFragment();
    SearchFragment searchFragment = new SearchFragment();
    BottomNavigationView bottomNavigationView;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;

    ArrayList<Bookdb> bookdb = bookdb();
    ArrayList<Userdb> userdb = userdb();
    ArrayList<Bookdb> search;

    String id;
    int idx;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        idx = intent.getIntExtra("idx", -1);

        login(id, idx);

        final View include_list = findViewById(R.id.frame_layout);
        final View include_loan = findViewById(R.id.include_loan);
        final View include_search = findViewById(R.id.include_search);

        SearchView searchView = (SearchView)findViewById(R.id.search_user);

        for(int i=0; i<userdb.size(); i++) {
            System.out.println(userdb.get(i).getName());
            System.out.println(userdb.get(i).getId());
            System.out.println(userdb.get(i).getPw());
        }

        System.out.println("북 디비 사이즈는 ? "+bookdb.size());

        for(int i=0; i<bookdb.size(); i++) {
            System.out.println(bookdb.get(i).getBookName());
            System.out.println(bookdb.get(i).getAuthor());
            System.out.println(bookdb.get(i).getPublisher());
            System.out.println(bookdb.get(i).getYear());
        }

        listView = (ListView)findViewById(R.id.listview1);
        ListView listView2 = (ListView)findViewById(R.id.listview2);
        final ListView listView3 = (ListView)findViewById(R.id.listview_search);

        final BookListAdapter bookListAdapter = new BookListAdapter(this, bookdb, listView);
        final LoanAdapter loanAdapter = new LoanAdapter(this, bookdb, id);
        final SearchAdapter searchAdapter = new SearchAdapter(this, search, listView3);

        listView.setAdapter(bookListAdapter);
        listView2.setAdapter(loanAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                for(int i=0; i<bookdb.size(); i++) {
                    if(bookdb.get(i).getBookName().matches(query)) {
                        search.add(bookdb.get(i));
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame_layout, searchFragment).commitAllowingStateLoss();
                        listView3.setAdapter(searchAdapter);
                        include_list.setVisibility(View.INVISIBLE);
                        include_loan.setVisibility(View.INVISIBLE);
                        include_search.setVisibility(View.VISIBLE);
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, booklistFragment).commitAllowingStateLoss();

        bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                bookdb = bookdb();
                switch(menuItem.getItemId()) {
                    case R.id.action_list:
                        include_list.setVisibility(View.VISIBLE);
                        include_loan.setVisibility(View.INVISIBLE);
                        include_search.setVisibility(View.INVISIBLE);
                        transaction.replace(R.id.frame_layout, booklistFragment).commitAllowingStateLoss();
                        break;
                    case R.id.action_loan:
                        include_list.setVisibility(View.INVISIBLE);
                        include_loan.setVisibility(View.VISIBLE);
                        include_search.setVisibility(View.INVISIBLE);
                        transaction.replace(R.id.frame_layout, loanFragment).commitAllowingStateLoss();
                        break;
                    case R.id.action_logout:
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
                        logout();
                        /*
                        Intent intent = new Intent(UserActivity.this, MainActivity.class);
                        startActivity(intent);
                        */
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

    public native void login(String id, int idx);
    public native void logout();
    public native ArrayList<Bookdb> bookdb();
    public native ArrayList<Userdb> userdb();
}
