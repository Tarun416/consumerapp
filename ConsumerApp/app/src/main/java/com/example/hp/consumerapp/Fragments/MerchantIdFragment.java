package com.example.hp.consumerapp.Fragments;

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
public class MerchantIdFragment  extends Fragment {

    public MerchantIdFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.merchant_fragment,container,false);
        return v;
    }
}
