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
import com.mmanchala.coen268.taskit.Model.Group;
import com.mmanchala.coen268.taskit.Model.Task;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends FragmentActivity {
    private ImageView createGroup;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        createGroup = findViewById(R.id.createGroup);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        final String created_by = mUser.getEmail();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Groups");

        mDatabase.keepSynced(true);
        recyclerView = findViewById(R.id.groupRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        Log.i("Mounika Manchala","View");
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Text","View");
                AlertDialog.Builder createGroupDialog = new AlertDialog.Builder(GroupActivity.this);
                LayoutInflater layoutInflater = LayoutInflater.from(GroupActivity.this);
                View view = layoutInflater.inflate(R.layout.create_group, null);
                createGroupDialog.setView(view);
                final AlertDialog dialog = createGroupDialog.create();
                dialog.show();
                final EditText groupName = view.findViewById(R.id.groupName);
                Button createNewGroup = view.findViewById(R.id.addNewGroup);

                createNewGroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mGroupName = groupName.getText().toString().trim();
                        if(TextUtils.isEmpty(mGroupName)){
                            groupName.setError("Connot be empty");
                        }
                        String id = mDatabase.push().getKey();
                        ArrayList<String> members = new ArrayList<>();
                        members.add(created_by);

                        Group group = new Group(mGroupName,created_by,id,members);
                        mDatabase.child(id).setValue(group);

                        Toast.makeText(getApplicationContext(),"New Group Added",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Group> options=
                new FirebaseRecyclerOptions.Builder<Group>()
                        .setQuery(mDatabase,Group.class)
                        .setLifecycleOwner(this)
                        .build();
        mDatabase.keepSynced(true);
        FirebaseRecyclerAdapter<Group,GroupViewHolder> adapter = new FirebaseRecyclerAdapter<Group,GroupViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull GroupViewHolder holder, int position, @NonNull final Group model) {
                holder.setGroupName(model.getGroupName());
                holder.myview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(),"Testing",Toast.LENGTH_SHORT).show();
                        Intent groupInfo = new Intent(getApplicationContext(),GroupDetailActivity.class);
                        groupInfo.putExtra("group_name",model.getGroupName());
                        groupInfo.putExtra("group_id",model.getId());
                        groupInfo.putStringArrayListExtra("member_info",model.getGroupMembers());

                        startActivity(groupInfo);
                    }
                });

            }

            @NonNull
            @Override
            public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new GroupViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.group_list, parent, false));
            }
        };
        recyclerView.setAdapter(adapter);
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder{
        View myview;
        public GroupViewHolder(View view){
            super(view);
            myview = view;

        }

        public void setGroupName(String groupName){
            TextView mGroupName = myview.findViewById(R.id.groupName);
            mGroupName.setText(groupName);

        }
    }


}
