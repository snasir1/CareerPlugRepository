package com.mycareerplug.iamokah.mycareerplug;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import static com.mycareerplug.iamokah.mycareerplug.SignupActivity.PREF_SIGNUP_USER_AUTHENTICATED;
import static com.mycareerplug.iamokah.mycareerplug.SignupActivity.SIGNUP_PREFNAME;

public class LoginUIActivity extends AppCompatActivity {

    private Button signup_btn, login_btn;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginui);

        getSupportActionBar().hide();

        signup_btn = findViewById(R.id.loginui_signupbtn);
        login_btn = findViewById(R.id.loginui_loginbtn);
        textView = findViewById(R.id.API);

        textView.setText("Version: 5.3" + ", API: " + Integer.valueOf(Build.VERSION.SDK_INT)
                + "\n Model: " + android.os.Build.MODEL);

        btn_click();


     /*   SharedPreferences settings = getSharedPreferences(SIGNUP_PREFNAME,
                Context.MODE_PRIVATE);
        Boolean isAuthenticated = settings.getBoolean(PREF_SIGNUP_USER_AUTHENTICATED, false);

  /*      SharedPreferences storeuserinfo = getSharedPreferences(SIGNUP_PREFNAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = storeuserinfo.edit();
        editor.putBoolean(PREF_SIGNUP_USER_AUTHENTICATED, false);
        editor.commit();
        System.out.println("LOGINUIACTIVITY PRINTING iSAuthenticated value: " + isAuthenticated);

        System.out.println("LOGINUIACTIVITY PRINTING iSAuthenticated value: " + isAuthenticated); */

    }


    private void btn_click() {

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginUIActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginUIActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }


}
