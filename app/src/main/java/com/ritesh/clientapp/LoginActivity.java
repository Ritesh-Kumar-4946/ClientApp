package com.ritesh.clientapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.hujiaweibujidao.wava.Techniques;
import com.github.hujiaweibujidao.wava.YoYo;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ritesh on 18/9/17.
 */

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.rl_login_text)
    RelativeLayout Rl_login_text;

    @BindView(R.id.rl_otp_text)
    RelativeLayout Rl_otp_text;

    @BindView(R.id.edt_login_email_mobile)
    EditText Edt_login_email_mobile;

    @BindView(R.id.edt_login_otp)
    EditText Edt_login_otp;

    @BindView(R.id.cv_login_get_otp)
    CardView Cv_login_get_otp;

    @BindView(R.id.cv_login_send_otp)
    CardView Cv_login_send_otp;


    String
            Str_Get_Control_Status = "",
            Str_Get_Control_Message = "",
            Str_Get_Enter_email_mobile = "",
            Str_Get_Enter_OTP = "";

    private ProgressDialog pDialog;


    private static final String GET_OTP = "Customer/sendOTP";
    private static final String SEND_OTP = "Customer/validateOTP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        AndroidNetworking.initialize(getApplicationContext());

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        Cv_login_get_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean iserror = false;
                Str_Get_Enter_email_mobile = Edt_login_email_mobile.getText().toString().trim();

                if (Str_Get_Enter_email_mobile.equalsIgnoreCase("")) {
                    iserror = true;
                    Log.e(" Error :", "Ok");
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(Edt_login_email_mobile);
                    /**************** End Animation ****************/

                    Toast.makeText(getApplicationContext(), "Enter Email-Id / Mobile Number",
                            Toast.LENGTH_SHORT).show();


                }

                if (!iserror) {
                    Log.e("No Error :", "Ok");
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                    Log.e("OK Valid :", "OOOO");
                    Get_OTP_Fast();


                }

            }
        });




        Cv_login_send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean iserror = false;
                Str_Get_Enter_OTP = Edt_login_otp.getText().toString().trim();

                if (Str_Get_Enter_OTP.equalsIgnoreCase("")) {
                    iserror = true;
                    Log.e(" Error :", "Ok");
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(Edt_login_otp);
                    /**************** End Animation ****************/

                    Toast.makeText(getApplicationContext(), "Enter OTP",
                            Toast.LENGTH_SHORT).show();


                }

                if (!iserror) {
                    Log.e("No Error :", "Ok");
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                    Log.e("OK Valid :", "OOOO");
                    Send_OTP_Fast();


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


    private void Get_OTP_Fast() {

        Log.e("Get_OTP_Fast********************* :", "*************************");
        pDialog.setMessage("Please Wait...");
        showDialog();
        AndroidNetworking.post(HttpUrlPath.urlPathMain + GET_OTP)
                .addBodyParameter("Username", Str_Get_Enter_email_mobile)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        hideDialog();
                        Log.e("SignupResponse :", "" + response.toString());

                        try {
                            JSONObject jobjSignup = new JSONObject(response.toString());
                            Log.e("jobjresponse List:", "" + jobjSignup);

                            JSONObject jobjControl = jobjSignup.getJSONObject("Control");
                            Str_Get_Control_Status = jobjControl.getString("Status");
                            Str_Get_Control_Message = jobjControl.getString("Message");


                            if (Str_Get_Control_Status.equalsIgnoreCase("1")) {
                                Log.e("Str_Get_Control_Status is:", "1");

                                Str_Get_Control_Message = jobjControl.getString("Message");
                                Log.e("Str_Get_Control_Message :", "" + Str_Get_Control_Message);

                                Toast.makeText(getApplicationContext(),
                                        "" + Str_Get_Control_Message, Toast.LENGTH_SHORT).show();
//                                JSONObject JobjData = jobjSignup.getJSONObject("Data");


                                Rl_login_text.setVisibility(View.GONE);
                                Cv_login_get_otp.setVisibility(View.GONE);

                                Rl_otp_text.setVisibility(View.VISIBLE);
                                Cv_login_send_otp.setVisibility(View.VISIBLE);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        hideDialog();
                        Log.e("OTPResponse :", "" + error.toString());
                    }
                });


    }


    private void Send_OTP_Fast() {

        Log.e("Send_OTP_Fast********************* :", "*************************");
        pDialog.setMessage("Please Wait...");
        showDialog();
        AndroidNetworking.post(HttpUrlPath.urlPathMain + SEND_OTP)
                .addBodyParameter("Username", Str_Get_Enter_email_mobile)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        hideDialog();
                        Log.e("SignupResponse :", "" + response.toString());

                        try {
                            JSONObject jobjSignup = new JSONObject(response.toString());
                            Log.e("jobjresponse List:", "" + jobjSignup);

                            JSONObject jobjControl = jobjSignup.getJSONObject("Control");
                            Str_Get_Control_Status = jobjControl.getString("Status");
                            Str_Get_Control_Message = jobjControl.getString("Message");


                            if (Str_Get_Control_Status.equalsIgnoreCase("1")) {
                                Log.e("Str_Get_Control_Status is:", "1");

                                Str_Get_Control_Message = jobjControl.getString("Message");
                                Log.e("Str_Get_Control_Message :", "" + Str_Get_Control_Message);

                                Toast.makeText(getApplicationContext(),
                                        "" + Str_Get_Control_Message, Toast.LENGTH_SHORT).show();
//                                JSONObject JobjData = jobjSignup.getJSONObject("Data");


                                Rl_login_text.setVisibility(View.GONE);
                                Cv_login_get_otp.setVisibility(View.GONE);

                                Rl_otp_text.setVisibility(View.VISIBLE);
                                Cv_login_send_otp.setVisibility(View.VISIBLE);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        hideDialog();
                        Log.e("OTPResponse :", "" + error.toString());
                    }
                });


    }


}
