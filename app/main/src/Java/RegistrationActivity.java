package com.example.graphicallogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class RegistrationActivity extends AppCompatActivity {
    private static final String ringmaster="&#aB=FVd$c!v0I8j";

    EditText edtName,edtEmail,edtPhone,edtAddress,edtPasswd;
    Button btnSignup;
    DatabaseReference databaseUsers;
    List<Users> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initializing all fields
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtMail);
        edtPhone = findViewById(R.id.edtPhNo);
        edtAddress = findViewById(R.id.edtAddress);
        btnSignup = findViewById(R.id.btnSignUp);
        edtPasswd = findViewById(R.id.edtPasswdreg);
        list =new ArrayList<Users>(); // List for users retrieved from database
        //Database References instantiating
        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");

        // Retrieving previous users details.
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

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

        // On clicking on the method adduser will be called .
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser();
            }
        });

        // On long click password field it will show the alert dialog with password rules.
        edtPasswd.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showPasswordRules();
                return false;
            }
        });
    }

    public void addUser()
    {
        String userId = databaseUsers.push().getKey();
        String emailId = edtEmail.getText().toString().trim();
        String passwd = edtPasswd.getText().toString().trim().toUpperCase();
        // Creating new user

        if(!emailId.isEmpty()&&!passwd.isEmpty()&&!edtName.getText().toString().isEmpty()&&!edtPhone.getText().toString().isEmpty()) {
            //Checking whether any emailid exists with same emailid as user
            if(isEmailIdExists(emailId))
            {
                Toast.makeText(RegistrationActivity.this,"Emaild id Already Exists, Choose another one",Toast.LENGTH_SHORT).show();
            }

            //Checking whether it is an valid emailid
            else if(!Patterns.EMAIL_ADDRESS.matcher(emailId).matches())
            {
                Toast.makeText(RegistrationActivity.this,"Not a valid email address",Toast.LENGTH_SHORT).show();
            }

            //Checking whether it is valid password.
            else if(!validatePassword(passwd.toUpperCase()))
            {
                Toast.makeText(RegistrationActivity.this,"Password is not as specified",Toast.LENGTH_SHORT).show();
                showPasswordRules();
            }

            // If all are valid then it will enter into this condition
            else {

                //Checking if phone number is valid
                if(edtPhone!=null)
                {
                    Pattern pattern=Pattern.compile("^[0-9](?=\\S+$).{9,10}$");
                    if(!pattern.matcher(edtPhone.getText().toString()).matches()) {
                        Toast.makeText(RegistrationActivity.this, "Enter valid phone number", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                AES aes=new AES();
                //If all are valid then add the user
                Users user = new Users(userId, edtName.getText().toString(), emailId, edtAddress.getText().toString(), edtPhone.getText().toString(),aes.encrypt(passwd.toUpperCase(),ringmaster));
                databaseUsers.child(userId).setValue(user);

                //After addition of the user is finished empty all fields
                edtEmail.setText("");
                edtName.setText("");
                edtPhone.setText("");
                edtAddress.setText("");
                edtPasswd.setText("");
                Toast.makeText(RegistrationActivity.this,"User successfully added",Toast.LENGTH_SHORT).show();

                //Upon successful registration it will redirect to login activity(i.e main activity)
                Intent intent=new Intent(RegistrationActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }

        // If the user doesn't enter all the mandatory fields
        else
        {
            Toast.makeText(RegistrationActivity.this,"Fill all the mandatory fields",Toast.LENGTH_SHORT).show();
            return;
        }
    }

    // Checking if the email id already exists or not.
    // If exists then specify the user that add another email.
    public boolean isEmailIdExists(String emailid)
    {
        for(Users user:list)
        {
            if(user.emailId.equals(emailid))
                return true;
        }
        return false;
    }

    // Checking password is as per specified
    public boolean validatePassword(String passwd)
    {
        Log.e("password ",passwd);
        final Pattern PASSWORD_PATEERN = Pattern.compile("^" +
                "[A-Z0-9]"+
                "(?!.*?[#?!@$%^&*-])"+ // Allows only uppercase and digits
                ".{5,20}" + // Minimum length is six
                "$");

        if(!PASSWORD_PATEERN.matcher(passwd).matches())
            return false;
        return true;
    }

    // Alert dialog to show the user set of password rules.
    public void showPasswordRules() {
        AlertDialog.Builder builer = new AlertDialog.Builder(RegistrationActivity.this);
        builer.setTitle("Password rules").
                setMessage("Contain Upper or lower case Letters or digits(While login consider all letters as Uppercase only) and no special characters allowed and minimum length should be 6 and maximum length is 20.").
                setPositiveButton("Ok", null);

        AlertDialog dialog = builer.create();
        dialog.show();
    }

}
