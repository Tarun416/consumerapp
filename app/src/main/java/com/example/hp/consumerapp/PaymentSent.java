package com.example.hp.consumerapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class PaymentSent extends AppCompatActivity {

    TextView amt;
    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_sent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });


        amt=(TextView)findViewById(R.id.amt);
         name=(TextView) findViewById(R.id.name);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Intent i=getIntent();
      String name1=  i.getStringExtra("name");
        String amount =i.getStringExtra("amount");

        amt.setText("Rs"+" "+ amount);

        name.setText("To:"+" "+name1);





    }

    @Override
    public void onBackPressed() {
     //   super.onBackPressed();
        this.finish();

        Intent i=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }
}
