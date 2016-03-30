package com.example.hp.consumerapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.consumerapp.Generator.ApiGenerator;
import com.example.hp.consumerapp.api.MvisaApi;
import com.example.hp.consumerapp.utils.ParseUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

public class Main2Activity extends AppCompatActivity {
    TextView mvisaId;
    String content;
    Toolbar toolbar;
    Button bu;
    ProgressDialog pd;
    MvisaApi mvisaApi;
    TextView name;
    EditText amount;
    Boolean b;
    TextView mvisaid;
    String qrcode;
    String strDate;
    StringBuffer sb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


      //  getActionBar()/* or getSupportActionBar() */.setTitle(Html.fromHtml("<font color=\"red\">" + getString(R.string.app_name) + "</font>"));

        setContentView(R.layout.activity_main2);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        strDate = sdf.format(c.getTime());
        sb=new StringBuffer(strDate);
        Log.d("date", strDate);

        for(int i=0;i<sb.length();i++)
        {
            if(sb.charAt(i)==' ')
            {
                sb.replace(10,11,"T");
            }
//            a[i]=a[i]+strDate.charAt(i);
        }

        Log.d("date", sb.toString());


        Intent i=getIntent();
        qrcode= i.getStringExtra("scancontent");



        name=(TextView)findViewById(R.id.name);
        amount=(EditText)findViewById(R.id.gg);
        mvisaid=(TextView)findViewById(R.id.mvisaid);

        mvisaid.setText("mVisa ID :"+""+ qrcode);

        pd=new ProgressDialog(this);
        pd.setMessage("Please Wait...");
        pd.hide();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#FFFFFF\">Amount To Pay</font>"));


       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bu=(Button)findViewById(R.id.button);


        bu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);


                pd.show();



                if (TextUtils.isEmpty(amount.getText().toString()) || Integer.parseInt(amount.getText().toString()) < 50) {


                    pd.hide();
                    amount.requestFocus();
                    amount.setError("Minimum Transaction amount should be Rs 50");
                } else  {
                    sendToApi(v);
                }
            }
        });









                  /*  Intent i=getIntent();
        content= i.getStringExtra("scancontent");

        mvisaId=(TextView)findViewById(R.id.mvisaid);
        mvisaId.setText("mvisa Id :"+" "+content);*/



    }

    private void sendToApi(View v)
    {
        InputStream cert = getApplicationContext().getResources().openRawResource(R.raw.cert);
        BufferedInputStream bis = new BufferedInputStream(cert);

        InputStream bundle = getApplicationContext().getResources().openRawResource(R.raw.myapp_keyandcertbundle);

        BufferedInputStream bundle1 = new BufferedInputStream(bundle);


        mvisaApi = ApiGenerator.createService(MvisaApi.class, bis, bundle);


        JSONObject jsonObject = new JSONObject();

        JSONObject cardAcceptor = new JSONObject();

        JSONObject address = new JSONObject();

        JSONObject purchaseIdentifier=new JSONObject();


        try {
            purchaseIdentifier.put("referenceNumber","REF_123456789123456789123");
            purchaseIdentifier.put("type","1");
        }
        catch (JSONException e){
        }

        try {
            address.put("city", "Bangalore");
            address.put("country", "IND");
        } catch (JSONException e) {

        }


        try {
            cardAcceptor.put("address", address);
            cardAcceptor.put("idCode", "ID-Code123");
            cardAcceptor.put("name", "ABC");
        } catch (JSONException e) {

        }

        try {
            jsonObject.put("acquirerCountryCode", "643");
            jsonObject.put("acquiringBin", "400171");

            jsonObject.put("amount", "112");
            jsonObject.put("businessApplicationId", "MP");
            jsonObject.put("cardAcceptor", cardAcceptor);

            jsonObject.put("feeProgramIndicator","123");

            jsonObject.put("localTransactionDateTime", sb);


            jsonObject.put("purchaseIdentifier",purchaseIdentifier);

            jsonObject.put("recipientName","Jasper");

            jsonObject.put("recipientPrimaryAccountNumber", "4123640062698797");

            jsonObject.put("retrievalReferenceNumber","412770451035");
            jsonObject.put("secondaryId","123TEST");


            jsonObject.put("senderAccountNumber", "4541237895236");

            jsonObject.put("senderName", "Mohammed Qasim");


            jsonObject.put("senderReference", "1234");
            jsonObject.put("systemsTraceAuditNumber", "313042");
            jsonObject.put("transactionCurrencyCode", "INR");

            jsonObject.put("transactionIdentifier", "381228649430015");













            try {


                //   final TypedInput in1 = new TypedByteArray("application/json", json1.getBytes("UTF-8"));
                final TypedInput in = new TypedByteArray("application/json", jsonObject.toString().getBytes("UTF-8"));
                mvisaApi.merchantpush(in, new Callback<Response>() {


                    @Override
                    public void success(Response response1, Response response) {

                        ParseUtils.subscribeWithEmail();

                        pd.hide();
                        Logger.d("ss", response1.toString());
                        JSONObject responseJson = new JSONObject();
                        try {
                            responseJson = new JSONObject(new String(((TypedByteArray) response1.getBody()).getBytes()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Intent i=new Intent(getApplicationContext(),PaymentSent.class);
                        i.putExtra("name",name.getText().toString());
                        i.putExtra("amount",amount.getText().toString());
                        startActivity(i);
                        finish();








                    }

                    @Override
                    public void failure(RetrofitError error) {
                        pd.hide();

                        Logger.d("error", error.getMessage());
                        Toast.makeText(getApplicationContext(),"Please Try Again",Toast.LENGTH_SHORT).show();

                    }
                });

            } catch (UnsupportedEncodingException e) {

            }

        } catch (JSONException e) {

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }
}
