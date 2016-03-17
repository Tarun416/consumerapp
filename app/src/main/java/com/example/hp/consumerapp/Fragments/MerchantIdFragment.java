package com.example.hp.consumerapp.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hp.consumerapp.Generator.ApiGenerator;
import com.example.hp.consumerapp.InputFilterMinMax;
import com.example.hp.consumerapp.Logger;
import com.example.hp.consumerapp.PaymentSent;
import com.example.hp.consumerapp.R;
import com.example.hp.consumerapp.api.MvisaApi;
import com.example.hp.consumerapp.utils.ParseUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;


/**
 * Created by rahul on 16/03/16.
 */
public class MerchantIdFragment  extends Fragment {

    EditText mid;
    EditText amt;
    Button continu;
    ProgressDialog pd;
    MvisaApi mvisaApi;
    Boolean b=true;
    String name;

    public MerchantIdFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.merchant_fragment,container,false);
        pd=new ProgressDialog(getActivity());
        pd.setMessage("Please Wait ...");
        pd.setCancelable(false);
        pd.hide();

        mid=(EditText)v.findViewById(R.id.mvisaid);



       // mid.setFilters(new InputFilter[]{new InputFilterMinMax("13", "16")});
        amt=(EditText)v.findViewById(R.id.gg);
        continu=(Button)v.findViewById(R.id.button);


        continu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                /*InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);*/

                pd.show();

                if(TextUtils.isEmpty(mid.getText().toString()) || mid.getText().toString().length()<13 || mid.length()>16)
                {
                    b=false;

                    pd.hide();
                    mid.requestFocus();
                   mid.setError("Mvisa Id should be of 13-16 digits");
                }

                if(TextUtils.isEmpty(amt.getText().toString())|| Integer.parseInt( amt.getText().toString())<50)
                {
                    b=false;

                    pd.hide();
                    amt.requestFocus();
                    amt.setError("Minimum Transaction amount should be Rs 50");
                }

                else if(b) {
                    sendToApi(v);
                }
                b=true;
            }
        });




        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        mid.setText("");
        amt.setText("");
    }

    private void  sendToApi(View v)
    {
        InputStream cert = getActivity().getResources().openRawResource(R.raw.cert);
        BufferedInputStream bis = new BufferedInputStream(cert);

        InputStream bundle =getActivity().getResources().openRawResource(R.raw.myapp_keyandcertbundle);
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

            jsonObject.put("localTransactionDateTime", "2016-03-17T07:01:15");


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
                         JSONObject ob=   responseJson.getJSONObject("cardAcceptor");
                          name=  ob.getString("name");
                            Log.d("name",name);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                     //   {"actionCode":"00","responseCode":"5","feeProgramIndicator":"123","retrievalReferenceNumber":"412770451035","transactionIdentifier":23423432,"cardAcceptor":{"terminalId":"TERMID01","address":{"country":"IN","city":"mVisa City"},"idCode":"IDCode","name":"mVisa Merchant"},"purchaseIdentifier":{"type":"1","referenceNumber":"REF_123456789123456789123"},"approvalCode":"21324K","transmissionDateTime":"2016-03-17T10:18:45.000Z"}

                        Intent i=new Intent(getActivity(),PaymentSent.class);
                        i.putExtra("name",name);
                        i.putExtra("amount",amt.getText().toString());
                        startActivity(i);








                    }




                    @Override
                    public void failure(RetrofitError error) {
                        pd.hide();

                        Logger.d("error", error.getMessage());
                        Toast.makeText(getActivity(), "Please Try Again", Toast.LENGTH_SHORT).show();

                    }
                });

            } catch (UnsupportedEncodingException e) {

            }

        } catch (JSONException e) {

        }


    }



}
