package com.example.qthjen.appsellcommodity.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.qthjen.appsellcommodity.R;

public class LienHe extends AppCompatActivity {

    Toolbar tbarLienHe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lien_he);

        tbarLienHe = (Toolbar) findViewById(R.id.tbarLienHe);

        setSupportActionBar(tbarLienHe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tbarLienHe.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
