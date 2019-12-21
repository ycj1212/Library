package com.example.library;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    static {
        System.loadLibrary("native-lib");
    }

    EditText signup_id, signup_pw, signup_name;
    TextView id_msg, pw_msg, name_msg;
    Button join;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signup_name=(EditText)findViewById(R.id.signup_name);
        signup_id=(EditText)findViewById(R.id.signup_id);
        signup_pw=(EditText)findViewById(R.id.signup_pw);

        name_msg=(TextView)findViewById(R.id.name_msg);
        id_msg=(TextView)findViewById(R.id.id_msg);
        pw_msg=(TextView)findViewById(R.id.pw_msg);

        join=(Button)findViewById(R.id.join);

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = signup_id.getText().toString();
                String pw = signup_pw.getText().toString();
                String name = signup_name.getText().toString();

                if(id_verify(id) != -1 || name.equals("") || id.equals("") || pw.equals("")) {
                    Toast.makeText(getApplicationContext(), ""+id_verify(id), Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "가입 성공", Toast.LENGTH_LONG).show();
                    join(name, id, pw);
                    /*
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    startActivity(intent);
                    */
                    finish();
                }
            }
        });

        // edittext 포커스가 변경되었을 때 해당되는 상태메시지 표시
        signup_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(signup_name.getText().toString().equals("")) {
                    name_msg.setVisibility(View.VISIBLE);
                }
            }
        });
        signup_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(signup_id.getText().toString().equals("")) {
                    id_msg.setVisibility(View.VISIBLE);
                }
            }
        });
        signup_pw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(signup_pw.getText().toString().equals("")) {
                    pw_msg.setVisibility(View.VISIBLE);
                }
            }
        });

        // edittext 텍스트 입력 시 해당되는 상태메시지 표시 및 숨김
        signup_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                name_msg.setVisibility(View.INVISIBLE);
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(signup_name.getText().toString().equals("")) {
                    name_msg.setVisibility(View.VISIBLE);
                }
            }
        });
        signup_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                id_msg.setVisibility(View.INVISIBLE);
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(signup_id.getText().toString().equals("")) {
                    id_msg.setText("아이디가 입력되지 않았습니다.");
                    id_msg.setVisibility(View.VISIBLE);
                }
                else if(id_verify(signup_id.getText().toString()) != -1) {
                    id_msg.setText("아이디가 이미 존재합니다.");
                    id_msg.setVisibility(View.VISIBLE);
                }
            }
        });
        signup_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pw_msg.setVisibility(View.INVISIBLE);
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(signup_pw.getText().toString().equals("")) {
                    pw_msg.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public native int id_verify(String id);
    public native void join(String name, String id, String pw);
}
