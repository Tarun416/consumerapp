package com.example.hp.consumerapp.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hp.consumerapp.R;


/**
 * Created by rahul on 16/03/16.
 */
public class QRCodeFragment extends Fragment {
    ProgressDialog pd;

    public QRCodeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.qr_code_fragmnet,container,false);

        pd=new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        return v;
    }
}
