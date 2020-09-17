package com.example.graphicallogin;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.util.ArrayUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class GridAdapter extends BaseAdapter
{
    Context context;
    public String letters[];
    public int Colors[];
    LayoutInflater inflater;
    int turn;
    public String textColor;

    int m=0,j=0,t=0;

    private int intialcolormatrix[][]={{1,2,3,4,5,4},{4,5,1,6,3,2},{2,3,6,2,1,5},{5,4,1,3,6,6},{6,5,2,4,3,1},{4,6,3,1,2,5}};

    public int colormatrix[][];

    public GridAdapter(Context context,String letters[],int Colors[],int turn)
    {
        this.letters=letters;
        this.context=context;
        this.Colors = Colors;
        textColor = new String();
        this.turn = turn;

        colormatrix=new int[6][6];

        Random rand=new Random();
        int randomnumber=rand.nextInt(6);
        for(int i=0;i<6;i++)
            for(int j=0;j<6;j++)
                colormatrix[i][j]=intialcolormatrix[(i+randomnumber)%6][(j+randomnumber)%6];
    }

    @Override
    public int getCount() {
        return letters.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        if(inflater == null)
            inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(view == null)
            view = inflater.inflate(R.layout.grid_view,null);
        TextView tvletter=view.findViewById(R.id.tvGridLetter);
        tvletter.setText(letters[i]);
        view.setBackgroundColor(Colors[colormatrix[i/6][i%6]-1]);

        return view;
    }

}
