package com.ritesh.clientapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.github.hujiaweibujidao.wava.Techniques;
import com.github.hujiaweibujidao.wava.YoYo;
import com.ritesh.clientapp.ucropImagepicker.PickerBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ritesh on 18/9/17.
 */

public class SignupActivity extends AppCompatActivity {


    @BindView(R.id.rl_create_profile_btn)
    RelativeLayout Rl_create_profile_btn;

    @BindView(R.id.edt_name)
    EditText Edt_name;

    @BindView(R.id.edt_mobile)
    EditText Edt_mobile;

    @BindView(R.id.edt_email)
    EditText Edt_email;

    @BindView(R.id.cv_create_profile_btn)
    CardView cv_create_profile_btn;

    @BindView(R.id.civ_create_profile_image)
    CircleImageView Civ_create_profile_image;
    private ProgressDialog pDialog;


    File ProfileImage;

    String
            Str_profileImage_path = "",
            Str_Get_User_ImageID = "",
            Str_Get_User_ID = "",
            Deviceid = "",
            Str_Get_User_Name = "",
            Str_Get_User_Mobile = "",
            Str_Get_User_Email = "",
            Str_Get_Control_Status = "",
            Str_Get_Control_Message = "";


    private static final String UPDATE_IMAGE = "Customer/uploadImage";
    private static final String USER_SIGNUP = "Customer/signUp";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        AndroidNetworking.initialize(getApplicationContext());

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        Deviceid = telephonyManager.getDeviceId();
        String Deviceidd = telephonyManager.getDeviceId().toString();
        String Hell = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.e("Device ID :", "" + Deviceid);
        Log.e("Deviceidd ID :", "" + Deviceidd);
        Log.e("Hell ID :", "" + Hell.toString());
        Log.e("Hell IDd :", "" + Hell);

        Rl_create_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new PickerBuilder(SignupActivity.this, PickerBuilder.SELECT_FROM_GALLERY)
                        .setOnImageReceivedListener(new PickerBuilder.onImageReceivedListener() {
                            @Override
                            public void onImageReceived(final Uri imageUri) {
//                                Iv_pan_card.setImageURI(imageUri);
                                Glide.with(Civ_create_profile_image.getContext())
                                        .load(imageUri)
                                        .asBitmap().centerCrop()
                                        .crossFade()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(new BitmapImageViewTarget(Civ_create_profile_image) {
                                            @Override
                                            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                                                super.onResourceReady(bitmap, anim);
                                                Glide.with(Civ_create_profile_image
                                                        .getContext())
                                                        .load(imageUri)
                                                        .centerCrop()
                                                        .crossFade()
                                                        .into(Civ_create_profile_image);
                                            }
                                        });


                                Log.e("Iv_pan_card imageUri path SELECT_FROM_GALLERY :", "" + imageUri);
                                File f = new File(imageUri.getPath());
                                Str_profileImage_path = imageUri.getPath();
                                Log.e("Str_Pan_Card_path SELECT_FROM_GALLERY :", "" + Str_profileImage_path);
                                if (!Str_profileImage_path.equals("")) {

                                    Log.e("Pan_Card_Image File Image availabe :", "" + ProfileImage);
                                    ProfileImage = new File(Str_profileImage_path);
                                    Log.e("Pan_Card_Image File Image:", "" + Str_profileImage_path);
                                    Civ_create_profile_image.setVisibility(View.VISIBLE);
                                    Update_Image_volley();

                                } else {

                                    Log.e("Pan_Card_Image File Image blank:", "" + ProfileImage);
                                    ProfileImage = new File("Pan_Card_Image");
                                    Log.e("Pan_Card_Image File Image:", "" + ProfileImage);
                                    Civ_create_profile_image.setVisibility(View.GONE);

                                }

                            }
                        })
                        .setImageName("ImageID")
                        .setImageFolderName("BusinessApp")
                        .setCropScreenColor(Color.CYAN)
                        .setOnPermissionRefusedListener(new PickerBuilder.onPermissionRefusedListener() {
                            @Override
                            public void onPermissionRefused() {

                            }
                        })
                        .start();

            }
        });


        cv_create_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                boolean iserror = false;

                Str_Get_User_Name = Edt_name.getText().toString().trim();
                Str_Get_User_Mobile = Edt_mobile.getText().toString().trim();
                Str_Get_User_Email = Edt_email.getText().toString().trim();

                Log.e(" Sign Up Fields data :", "\n"
                        + "Deviceid :" + "" + Deviceid + "\n"
                        + "ImageName :" + "" + Str_Get_User_ImageID + "\n"
                        + "Str_Get_User_Name :" + "" + Str_Get_User_Name + "\n"
                        + "Str_Get_User_Mobile :" + "" + Str_Get_User_Mobile + "\n"
                        + "Str_Get_User_Email :" + "" + Str_Get_User_Email);


                if (Str_Get_User_Name.equalsIgnoreCase("")) {
                    iserror = true;
                    Log.e(" Error :", "Ok");
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(Edt_name);
                    /**************** End Animation ****************/

                    Toast.makeText(getApplicationContext(),
                            "Please Enter Name", Toast.LENGTH_SHORT).show();


                } else if (Str_Get_User_Mobile.equalsIgnoreCase("")) {
                    iserror = true;
                    Log.e(" Error :", "Ok");
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(Edt_mobile);
                    /**************** End Animation ****************/

                    Toast.makeText(getApplicationContext(), "Enter Mobile Number",
                            Toast.LENGTH_SHORT).show();


                } else if (Str_Get_User_Email.equalsIgnoreCase("")) {
                    iserror = true;
                    Log.e(" Error :", "Ok");
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(Edt_email);
                    /**************** End Animation ****************/

                    Toast.makeText(getApplicationContext(), "Enter Valid Email ID",
                            Toast.LENGTH_SHORT).show();


                } else if (Str_Get_User_Email.equalsIgnoreCase("")) {
                    iserror = true;
                    Log.e(" Error :", "Ok");
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(Edt_email);
                    /**************** End Animation ****************/

                    Toast.makeText(getApplicationContext(),
                            "Enter Valid Email ID", Toast.LENGTH_SHORT).show();


                } else if (!isValidEmaill(Str_Get_User_Email)) {
                    iserror = true;
                    Log.e(" Error :", "Ok");
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    //	emailedit.requestFocus();
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(Edt_email);
                    /**************** End Animation ****************/
                    Toast.makeText(getApplicationContext(),
                            "Please enter valid email address.", Toast.LENGTH_SHORT).show();


                }
                if (!iserror) {
                    Log.e("No Error :", "Ok");
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                    Log.e("OK Valid :", "OOOO");
                    Signup_Fast();


                }




            }
        });


    }


    public final static boolean isValidEmaill(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    private void Update_Image_volley() {
        Log.e("Update_Image_volley********************* :", "*************************");


        Log.e("Fast Update_Image_volley Update data :", "\n"
                + "Str_profileImage_path :" + "" + Str_profileImage_path + "\n");

        pDialog.setMessage("Uploading...");
        showDialog();
        //Auth header
        Map<String, String> mHeaderPart = new HashMap<>();
        mHeaderPart.put("Content-type", "multipart/form-data;");

        //File part
        Map<String, File> mFilePartData = new HashMap<>();
        mFilePartData.put("UserImage", new File(Str_profileImage_path));

        //String part
        Map<String, String> mStringPart = new HashMap<>();
//        mStringPart.put("MerchantId", Sh_pre_User_ID);


        CustomMultipartRequest mCustomRequest = new CustomMultipartRequest(Request.Method.POST,
                SignupActivity.this, HttpUrlPath.urlPathMain + UPDATE_IMAGE,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        hideDialog();
                        Log.e("jsonObject Response :", "" + jsonObject.toString());
                        try {
                            JSONObject jobjresponse = new JSONObject(jsonObject.toString());
                            Log.e("jobjresponse List:", "" + jobjresponse);

                            JSONObject jobjControl = jobjresponse.getJSONObject("Control");
                            Str_Get_Control_Status = jobjControl.getString("Status");
                            Str_Get_Control_Message = jobjControl.getString("Message");


                            if (Str_Get_Control_Status.equalsIgnoreCase("1")) {
                                Log.e("Str_Get_Control_Status is:", "1");

                                Str_Get_Control_Message = jobjControl.getString("Message");
                                Log.e("Str_Get_Control_Message :", "" + Str_Get_Control_Message);

                                JSONObject JobjImage = jobjresponse.getJSONObject("Data");
                                Log.e("DataIMage :", "" + JobjImage);

                                Str_Get_User_ImageID = JobjImage.getString("ImageName");
                                Log.e("Str_Get_User_ImageID :", "" + Str_Get_User_ImageID);


//                                Appconstant.editor.putString("imageuploadOk", "ImageUploadTrue");
//                                Appconstant.editor.putString("loginTest", "true");
//                                Appconstant.editor.commit();
//                                Toast.makeText(ImageUpload.this, "Image Upload Completed", Toast.LENGTH_SHORT).show();
////                                Toast.makeText(SignupActivity.this, "Your ID is :" + Str_MerchantID.toString(), Toast.LENGTH_SHORT).show();
//                                Intent Gologincreen = new Intent(ImageUpload.this, WebViewActivity.class);
//                                startActivity(Gologincreen);
//                                finish();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                listener.onErrorResponse(volleyError);
                hideDialog();
                Log.e("Response volleyError:", "" + volleyError);

            }
        }, mFilePartData, mStringPart, mHeaderPart);


        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //Adding request to the queue
        mCustomRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(mCustomRequest);
    }


    private void Signup_Fast() {

        Log.e("SignupFast********************* :", "*************************");
        pDialog.setMessage("Please Wait...");
        showDialog();
        AndroidNetworking.post(HttpUrlPath.urlPathMain + USER_SIGNUP)
                .addBodyParameter("Name", Str_Get_User_Name)
                .addBodyParameter("DeviceId", Deviceid)
                .addBodyParameter("Mobile", Str_Get_User_Mobile)
                .addBodyParameter("Email", Str_Get_User_Email)
                .addBodyParameter("Image", Str_Get_User_ImageID)
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

                                JSONObject JobjData = jobjSignup.getJSONObject("Data");
                                Str_Get_User_ID = JobjData.getString("CustomerId");


                                Appconstant.editor.putString("imageuploadOk", "ImageUploadTrue");
                                Appconstant.editor.putString("loginTest", "true");
                                Appconstant.editor.commit();

                                Log.e("Value save in Sharedpre", "ok");
                                Appconstant.editor.putString("id", Str_Get_User_ID);
                                Appconstant.editor.putString("loginTest", "true");
                                Appconstant.editor.commit();
                                Toast.makeText(SignupActivity.this, "Signup Completed", Toast.LENGTH_SHORT).show();
//





//                                Toast.makeText(SignupActivity.this, "Your ID is :" + Str_MerchantID.toString(), Toast.LENGTH_SHORT).show();
                                Intent Gologincreen = new Intent(SignupActivity.this, VarifyMobileActivity.class);
                                startActivity(Gologincreen);
                                finish();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        hideDialog();
                        Log.e("SignupResponse :", "" + error.toString());
                    }
                });


    }

}
