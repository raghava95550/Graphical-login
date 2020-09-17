package com.example.graphicallogin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String ringmaster="&#aB=FVd$c!v0I8j";

    Button next;
    TextView tvSignUp;
    EditText edtUserId;
    DatabaseReference reference;
    List<Users> list;
    String pass;
    int attempts=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtUserId=(EditText)findViewById(R.id.edtUsername);
        tvSignUp=(TextView)findViewById(R.id.tvSignup);
        next = (Button)findViewById(R.id.button);
        list=new ArrayList<>();
        if(checkConnection()) {

            // Call the function to retrieve all previous users details
            populateData();

            // Code to redirect to sign up page
            tvSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                    startActivity(intent);
                }
            });

            // on clicking then next button first it will check whether the emailid already exists in the database or not.
            // if exists it will move next page where user has to type password.
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isUsernameExists(edtUserId.getText().toString().trim())) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity2.class);
                        intent.putExtra("attempts",Integer.toString(attempts));
                        intent.putExtra("pass",pass);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "User doesnot exists", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    // Function to retrieve all previous users details.
    public void populateData()
    {
        reference= FirebaseDatabase.getInstance().getReference("Users");
        /////      retrieving values from database
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                list.clear();
                for(DataSnapshot usersnapshot:dataSnapshot.getChildren())
                {
                    Users user=usersnapshot.getValue(Users.class);
                    list.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    // Function that will check internet connection.
    public boolean checkConnection()
    {
        ConnectivityManager manager=(ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activenet= manager.getActiveNetworkInfo();
        if(activenet == null)
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("No internet Connection").setMessage("Switch on data and start the app").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    MainActivity.super.onBackPressed();
                }
            });
            AlertDialog dialog=builder.create();
            dialog.show();
            return false;
        }
        return true;
    }

    // function that will check whether username exists or not
    public boolean isUsernameExists(String username)
    {
        int len=list.size();
        AES aes=new AES();
        Log.e("size list=",Integer.toString(len));
        for(int i=0;i<len;i++)
            if(list.get(i).emailId.equals(username)) {
                pass = aes.decrypt(list.get(i).passwd,ringmaster);
                //Log.e("password retieved",pass);
                return true;
            }
        return  false;
    }

    //Function that enables user to really exit the app.
    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Exit").setMessage("Do you really want to exit?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();
                MainActivity.super.onBackPressed();
            }
        }).setNegativeButton("No",null);
        AlertDialog dialog=builder.create();
        dialog.show();
    }
}
