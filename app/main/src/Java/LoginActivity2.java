package com.example.graphicallogin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.common.util.Strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class LoginActivity2 extends AppCompatActivity
{
    GridView gv;
    Button btnLogin,btnBack,btnNext;
    EditText edtPasswd;

    // Array of letters
    private String intialletters[]={"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S",
            "T","U","V","W","X","Y","Z","0","1","2","3","4","5","6","7","8","9"};

    // Array of colors
    private int intialColors[]={Color.GREEN,Color.RED,Color.BLUE,Color.YELLOW,Color.WHITE,Color.MAGENTA };

    String textPasswd;
    String colorpasswd;
    int currinx;
    int attempts;
    GridAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        colorpasswd=new String();
        currinx = 0;
        gv= findViewById(R.id.gridPass);
        //final String letter[]=rearrangeLetters(intialletters);
        intialColors=rearrangeColors(intialColors);
        adapter=new GridAdapter(LoginActivity2.this,intialletters,intialColors,currinx);
        gv.setAdapter(adapter);

        // Getting pass from previous intent
        Intent intent=getIntent();
        textPasswd = intent.getStringExtra("pass");
        attempts = Integer.parseInt(intent.getStringExtra("attempts"));
        Log.e("database passwd",textPasswd);

        //instantiating all widgets
        btnLogin= findViewById(R.id.btnLogin);
        edtPasswd = findViewById(R.id.edtPasswdlogin);
        edtPasswd.setEnabled(false);
        btnBack = findViewById(R.id.btnLoginback);
        btnNext = findViewById(R.id.btnLoginnext);
        btnNext.setEnabled(false);

        // Call the method to show the alert dialog
        //showAlertDialog();

        //Adding item click listener to the grid view and on clicking any item it will display in the edit text password
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String text=edtPasswd.getText().toString();
                text=text+adapter.letters[i];
                edtPasswd.setText(text);
                int color=adapter.Colors[i%6];
                btnBack.setEnabled(true);
                gv.setEnabled(false);
                btnNext.setEnabled(true);
            }
        });

        // On pressing the back button it will remove one letter from the edittext , and it will not allow further removals of letters
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=edtPasswd.getText().toString();
                text=text.substring(0,text.length()-1);
                edtPasswd.setText(text);
                btnBack.setEnabled(false);
                gv.setEnabled(true);
            }
        });

        // On pressing next button it will generate the next grid.
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String letters2[]=letter;
                //letters2=rearrangeLetters(letters2);
                try {
                    int index = Arrays.asList(intialletters).indexOf(textPasswd.charAt(currinx) + "");
                    colorpasswd = colorpasswd + letterForColor(intialColors[adapter.colormatrix[index / 6][index % 6] - 1]);
                }
                catch (Exception ex)
                {
                    Log.e("Error","Array out of index");
                }
                //rearranging the colors
                intialColors=rearrangeColors(intialColors);
                currinx++;
                adapter=new GridAdapter(LoginActivity2.this,intialletters,intialColors,currinx);
                gv.setAdapter(adapter);
                btnBack.setEnabled(true);
                gv.setEnabled(true);
                btnNext.setEnabled(false);
            }
        });

        // On pressing login button validate the password and show the result to user.
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usertext=edtPasswd.getText().toString();
                try {
                    int index=Arrays.asList(intialletters).indexOf(textPasswd.charAt(currinx)+"");
                    colorpasswd = colorpasswd+letterForColor(intialColors[adapter.colormatrix[index/6][index%6]-1]);
                }
                catch (Exception ex)
                {
                    Log.e("Error","Array out of Index");
                }


                if(validate(usertext,colorpasswd)) {
                    startActivity(new Intent(LoginActivity2.this,Success.class));
                }
                else {
                    attempts++;
                    if(attempts==3) {
                        startActivity(new Intent(LoginActivity2.this,BlockActivity.class));
                        finish();
                        Toast.makeText(LoginActivity2.this, "Your account is blocked", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent intent1 = new Intent(LoginActivity2.this, FailureActivity.class);
                        intent1.putExtra("attempts", Integer.toString(attempts));
                        intent1.putExtra("pass", textPasswd);
                        startActivity(intent1);
                        finish();
                    }
                }
            }
        });
    }

    //randomizing letters by shuffling the indexes
    public String[] rearrangeLetters(String letters[])
    {
        int len=letters.length;
        List<Integer> list=new ArrayList<Integer>();
        for(int i=0;i<len;i++)
            list.add(i);
        Collections.shuffle(list);
        int index[]=new int[len];

        for(int i=0;i<len;i++)
            index[i]=list.get(i);
        String newLtters[]=new String[len];
        for(int i=0;i<len;i++) {
            newLtters[i]=letters[index[i]];
        }
        return newLtters;
    }


    //randomizing the colors by shuffling the integers values of colors
    public int[] rearrangeColors(int colors[])
    {
        int len=colors.length;
        List<Integer> list=new ArrayList<Integer>();
        for(int i=0;i<len;i++)
            list.add(colors[(i)%len]);
        Collections.shuffle(list);
        int newColors[]=ArrayUtils.toPrimitiveArray(list);
        return newColors;
    }

    /*
    //To show alert dialog
    public void showAlertDialog()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity2.this);
        builder.setTitle("Username exists").setMessage("Click next for adjacent grid").setPositiveButton("Ok",null);
        AlertDialog dialog= builder.create();
        dialog.show();
    }
    */

    //To validate passwords
    public boolean validate(String inputed,String typed)
    {
        if(inputed.equals(typed))
            return true;
        return false;
    }

    // Returning letter from color
    public String letterForColor(int color)
    {
        switch (color)
        {
            case Color.RED: return "R";
            case Color.BLUE: return "B";
            case Color.GREEN: return "G";
            case Color.MAGENTA: return "P";
            case Color.WHITE: return "W";
            case Color.YELLOW: return "Y";
        }
        return null;
    }
}
