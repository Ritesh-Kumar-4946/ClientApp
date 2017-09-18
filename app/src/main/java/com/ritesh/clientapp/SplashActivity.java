package com.ritesh.clientapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class SplashActivity extends AppCompatActivity {
    String SIgnupScreen = "", VarificationScreen = "", ImageuploadScreen = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        // here initializing the shared preference
        Appconstant.sh = getSharedPreferences("myprefe", 0);
        Appconstant.editor = Appconstant.sh.edit();

        // check here if user is login or not
        Appconstant.str_login_test = Appconstant.sh.getString("loginTest", null);

        SIgnupScreen = Appconstant.sh.getString("signupOk", null);
        VarificationScreen = Appconstant.sh.getString("varificationOk", null);
        ImageuploadScreen = Appconstant.sh.getString("imageuploadOk", null);


        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
            return;
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                if (Appconstant.str_login_test != null
                        && !Appconstant.str_login_test.toString().trim().equals("")) {
                    Log.e("Login detail found :", " Now Check Business Create Status");

                    Intent Screen = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(Screen);
                    finish();

                }

//
                /* if user login test is false on oncreate then redirect the user to login & registration page */
                else {

                    Log.e("Login detail not found :", "Go to Login Screen");
                    Intent Gologincreen = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(Gologincreen);
                    finish();

                }
            }
        }, 2000);


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    
}