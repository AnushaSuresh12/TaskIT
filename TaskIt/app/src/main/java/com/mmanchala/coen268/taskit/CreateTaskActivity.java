package com.mmanchala.coen268.taskit;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CreateTaskActivity extends DialogFragment
 {

    Button create;
    EditText text1,datetext,timetext;
    TextView datename,timename;
    View rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_create_task, container,
                false);
        getDialog().setTitle("NEW TASK");
/*
        text1 = (EditText) rootView.findViewById(R.id.editText);
        datetext=(EditText)rootView.findViewById(R.id.editText5);
        timetext=(EditText)rootView.findViewById(R.id.editText3);
        create=(Button)rootView.findViewById(R.id.button3);*/
        //datename=(TextView)rootView.findViewById(R.id.datename);
        //timename=(TextView)rootView.findViewById(R.id.timename);
        getDialog().requestWindowFeature(STYLE_NO_TITLE);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity listener= (HomeActivity) getActivity();
                getDialog().dismiss();
            }
        });
        return rootView;
    }
}
