package com.example.library;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    EditText input_id, input_pw;
    Button login, signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input_id=(EditText)findViewById(R.id.input_id);
        input_pw=(EditText)findViewById(R.id.input_pw);

        login = (Button)findViewById(R.id.login);
        signup = (Button)findViewById(R.id.signup);

        read_file();

        // 로그인 버튼 클릭 시
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = input_id.getText().toString();
                String pw = input_pw.getText().toString();
                // 아이디 검증 실패
                if(id_verify(id) == -1) {
                    Toast.makeText(getApplicationContext(), "로그인 실패" , Toast.LENGTH_LONG).show();
                }
                // 아이디 검증 성공
                else {
                    // 비밀번호 검증 실패
                    if(pw_verify(pw, id_verify(id)) == -1) {
                        Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_LONG).show();
                    }
                    // 비밀번호 검증 성공
                    else {
                        // 관리자
                        if(id.equals("admin")) {
                            Intent intent = new Intent(MainActivity.this, AdministratorActivity.class);
                            intent.putExtra("idx", id_verify(id));
                            intent.putExtra("id", id);
                            startActivity(intent);
                        }
                        // 사용자
                        else {
                            Intent intent = new Intent(MainActivity.this, UserActivity.class);
                            intent.putExtra("idx", id_verify(id));
                            intent.putExtra("id", id);
                            startActivity(intent);
                        }
                        input_id.setText("");
                        input_pw.setText("");
                    }
                }
            }
        });

        // 회원가입 버튼 클릭 시
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        write_file();
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native int id_verify(String id);
    public native int pw_verify(String pw, int i);
    public native void read_file();
    public native void write_file();
}
