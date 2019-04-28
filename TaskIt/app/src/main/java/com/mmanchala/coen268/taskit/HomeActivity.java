package com.mmanchala.coen268.taskit;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.mmanchala.coen268.taskit.Model.Task;

import java.text.DateFormat;
import java.util.Date;

public class HomeActivity extends FragmentActivity //implements CreateTaskActivity.EditNameDialogListener
 {

   private ImageView add;

   private DatabaseReference mDatabase;
   private FirebaseAuth mAuth;

   private RecyclerView recyclerView;

   private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_home);
        add=findViewById(R.id.add);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();
        userName = mUser.getEmail();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("TaskInfo");

        mDatabase.keepSynced(true);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        View header = findViewById(R.id.header1);
        Log.d("String header", header.toString());

        ImageView group = header.findViewById(R.id.group);

        Log.d("String group", group.toString());

        group.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("group click header", "Activity");
                startActivity(new Intent(getApplicationContext(),GroupActivity.class));
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder myDialog = new AlertDialog.Builder(HomeActivity.this);
                LayoutInflater layoutInflater = LayoutInflater.from(HomeActivity.this);
                View view = layoutInflater.inflate(R.layout.activity_create_task,null);
                myDialog.setView(view);
                final AlertDialog dialog = myDialog.create();

                final EditText title = view.findViewById(R.id.titleTask);
                final EditText time = view.findViewById(R.id.time);

                Button saveData = view.findViewById(R.id.addTask);


                saveData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mTitle = title.getText().toString().trim();
                        String mTime = time.getText().toString().trim();


                        if(TextUtils.isEmpty(mTitle)){
                            title.setError("Connot be empty");
                            return;
                        }
                        if(TextUtils.isEmpty(mTime)){
                            time.setError("Connot be empty");
                            return;
                        }

                        String id = mDatabase.push().getKey();

                        String date = DateFormat.getDateInstance().format(new Date());

                        Task task = new Task(mTitle,date,id,mTime,null,"incomplete",userName);
                        mDatabase.child(id).setValue(task);

                        Toast.makeText(getApplicationContext(),"Instered",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                dialog.show();


            }

        });

    }

     @Override
     protected void onStart() {
         super.onStart();
       //retrieving the information specific to the user
         FirebaseRecyclerOptions<Task> options=
                 new FirebaseRecyclerOptions.Builder<Task>()
                         .setQuery(mDatabase.orderByChild("assignedTo").equalTo(userName),Task.class)
                         .setLifecycleOwner(this)
                         .build();
         FirebaseRecyclerAdapter<Task,MyViewHolder> adapter = new FirebaseRecyclerAdapter<Task,MyViewHolder>(options) {

             @Override
             protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Task model) {
                 holder.setTitle(model.getTitle());
                 holder.setDate(model.getDate());
                 holder.setTimeLength(model.getTimeTaking());
             }

             @NonNull
             @Override
             public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                 return new MyViewHolder(LayoutInflater.from(parent.getContext())
                         .inflate(R.layout.task_list, parent, false));
             }
         };
         recyclerView.setAdapter(adapter);
     }

     public static class MyViewHolder extends RecyclerView.ViewHolder{
        View myview;
        public MyViewHolder(View view){
            super(view);
            myview = view;

        }

        public void setTitle(String title){
            TextView mTitle = myview.findViewById(R.id.title);
            mTitle.setText(title);
        }

        public void setDate(String date){
            TextView mDate = myview.findViewById(R.id.date);
            mDate.setText(date);
        }

        public void setTimeLength(String time){
            TextView mTime = myview.findViewById(R.id.time);
            mTime.setText(time);
        }
     }

}
