package com.ritesh.clientapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.chaos.view.PinView;
import com.github.hujiaweibujidao.wava.Techniques;
import com.github.hujiaweibujidao.wava.YoYo;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ritesh on 2/9/17.
 */

public class VarifyMobileActivity extends AppCompatActivity {

    @BindView(R.id.otp_varify)
    PinView pinView;

    @BindView(R.id.cv_otp_varify_btn)
    CardView Cv_otp_varify_btn;

    String Str_OTP = "1001";

    private ProgressDialog pDialog;

    private static final String VARIFYOTP = "Store/validateOTP";

    String Sh_pre_User_ID = "",
            Str_Get_Control_Status = "",
            Str_Get_Mobile_Shrd = "",
            Str_Get_Pinview_OTP = "",
            Str_Get_Control_Message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_varification);
        ButterKnife.bind(this);
        AndroidNetworking.initialize(getApplicationContext());
        Appconstant.sh = getSharedPreferences(Appconstant.MyPREFERENCES, Context.MODE_PRIVATE);

        Appconstant.sh = getSharedPreferences(Appconstant.MyPREFERENCES, Context.MODE_PRIVATE);
        Sh_pre_User_ID = Appconstant.sh.getString("id", null);
        Str_Get_Mobile_Shrd = Appconstant.sh.getString("id", null);
        Log.e("Merchant_ID from SharedPref :", "" + Sh_pre_User_ID);


        pinView.setAnimationEnable(true);// start animation when adding text

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        Cv_otp_varify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean iserror = false;
                Str_Get_Pinview_OTP = pinView.getText().toString().trim();

                Log.e(" Varify OTP Fields data :", "\n"
                        + "UserID :" + "" + Str_Get_Mobile_Shrd + "\n"
                        + "OTP :" + "" + Str_Get_Pinview_OTP );

                if (Str_Get_Pinview_OTP.equalsIgnoreCase("")) {
                    iserror = true;
                    Log.e(" Error :", "Ok");
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(pinView);
                    /**************** End Animation ****************/

                    Toast.makeText(getApplicationContext(),
                            "Please Enter OTP", Toast.LENGTH_SHORT).show();


                }

                if (!iserror) {
                    Log.e("No Error :", "Ok");
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                    Log.e("OK Valid :", "OOOO");
//                    Signup_Fast();


                }


            }
        });


    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }


    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
