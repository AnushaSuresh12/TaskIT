package com.mmanchala.coen268.taskit;

import android.app.AlertDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mmanchala.coen268.taskit.Model.Task;
import com.mmanchala.coen268.taskit.Model.User;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GroupDetailActivity extends FragmentActivity {

    private ImageView add, memberAdd;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private RecyclerView recyclerView;

    private String groupID,userName;

    private Spinner memberListSpinner;

    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        String groupName = getIntent().getStringExtra("group_name");
        groupID = getIntent().getStringExtra("group_id");
        TextView groupNameHeader = findViewById(R.id.txtGroupName);
        groupNameHeader.setText(groupName);



        add=findViewById(R.id.add);
        memberAdd = findViewById(R.id.addMember);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        userName = mUser.getEmail();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("TaskInfo");

        mDatabase.keepSynced(true);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myDialog = new AlertDialog.Builder(GroupDetailActivity.this);
                LayoutInflater layoutInflater = LayoutInflater.from(GroupDetailActivity.this);
                View view = layoutInflater.inflate(R.layout.group_task,null);
                myDialog.setView(view);
                final AlertDialog dialog = myDialog.create();

                final EditText title = view.findViewById(R.id.titleTask);
                final EditText time = view.findViewById(R.id.time);
                memberListSpinner = (Spinner) view.findViewById(R.id.spinner2);
                ArrayList<String> list = getIntent().getStringArrayListExtra("member_info");

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(GroupDetailActivity.this,
                        android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                memberListSpinner.setAdapter(dataAdapter);
               // final EditText assigned = view.findViewById(R.id.textAssignTo);
                Button saveData = view.findViewById(R.id.addTask);

                saveData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mTitle = title.getText().toString().trim();
                        String mTime = time.getText().toString().trim();
                        String mAssignTo = String.valueOf(memberListSpinner.getSelectedItem());

                        if(TextUtils.isEmpty(mTitle)){
                            title.setError("Connot be empty");
                            return;
                        }
                        if(TextUtils.isEmpty(mTime)){
                            time.setError("Connot be empty");
                            return;
                        }
                        if(TextUtils.isEmpty(mAssignTo)){
                         //   memberListSpinner.setError("Cant be empty");
                            return;
                        }

                        String id = mDatabase.push().getKey();
                        String date = DateFormat.getDateInstance().format(new Date());

                        Task task = new Task(mTitle,date,id,mTime,groupID,"incomplete",mAssignTo);
                        mDatabase.child(id).setValue(task);

                        Toast.makeText(getApplicationContext(),"Instered",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }

        });

        memberAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myDialog = new AlertDialog.Builder(GroupDetailActivity.this);
                LayoutInflater layoutInflater = LayoutInflater.from(GroupDetailActivity.this);
                final View view = layoutInflater.inflate(R.layout.add_member,null);
                myDialog.setView(view);
                final AlertDialog dialog = myDialog.create();

                email = view.findViewById(R.id.txtEmail);
                Button addMem = view.findViewById(R.id.btnAddMember);

                addMem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mEmail = email.getText().toString().trim();

                        if(TextUtils.isEmpty(mEmail)){
                            email.setError("Connot be empty");
                            return;
                        }
                        Query query = FirebaseDatabase.getInstance().getReference().
                                child("MemberInfo").orderByChild("email").equalTo(mEmail);

                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                // TODO should change the toast to text message with proper errors
                                if(dataSnapshot.exists()){
                                    Toast.makeText(getApplicationContext(),"Added",Toast.LENGTH_SHORT).show();

                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"User Not Available",Toast.LENGTH_SHORT).show();
                                    /*email.setError("Given email is not available");
                                    return;*/
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
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
                        .setQuery(mDatabase.orderByChild("groupID").equalTo(groupID),Task.class)
                        .setLifecycleOwner(this)
                        .build();
        FirebaseRecyclerAdapter<Task,TaskHolder> adapter = new FirebaseRecyclerAdapter<Task,TaskHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull TaskHolder holder, int position, @NonNull Task model) {
                holder.setTitle(model.getTitle());
                holder.setDate(model.getDate());
                holder.setTimeLength(model.getTimeTaking());
            }

            @NonNull
            @Override
            public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new TaskHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.task_list, parent, false));
            }
        };
        recyclerView.setAdapter(adapter);
    }

    public static class TaskHolder extends RecyclerView.ViewHolder{
        View myview;
        public TaskHolder(View view){
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
