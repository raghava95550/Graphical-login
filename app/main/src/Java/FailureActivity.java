package com.example.graphicallogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class FailureActivity extends AppCompatActivity {
    TextView tvattempts,tvtryagain;
    String textpasswd,str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_failure);
        tvattempts = findViewById(R.id.tvattmpt);
        tvtryagain = findViewById(R.id.tvTryagain);
        Intent intent=getIntent();
        str=intent.getStringExtra("attempts");
        textpasswd = intent.getStringExtra("pass");
        tvattempts.setText(Integer.toString(3-Integer.parseInt(str)));

        tvtryagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FailureActivity.this,LoginActivity2.class);
                intent.putExtra("pass",textpasswd);
                intent.putExtra("attempts",str);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent intent=new Intent(FailureActivity.this,LoginActivity2.class);
        intent.putExtra("pass",textpasswd);
        intent.putExtra("attempts",str);
        startActivity(intent);
        finish();
    }
}
