package com.example.swapswop.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.swapswop.Classes.User;
import com.example.swapswop.Classes.UserAdapter;
import com.example.swapswop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is the backend that is used to handle the users view.
 * @author Süleyman Semih Demir, Muhammed İkbal Doğan, Burak Yetiştiren
 * @version 1.2, 10 May 2019
 */
public class UsersFragment extends Fragment
{
    // firebase variabes
    private FirebaseUser      firebaseUser;
    private DatabaseReference reference;

    // other variables
    private RecyclerView recyclerView;
    private UserAdapter  userAdapter;
    private List<User>   mUsers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate( R.layout.fragment_users, container, false);

        // initializing firebse variables
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        // initializing other variables
        recyclerView = view.findViewById( R.id.recycler_view);
        recyclerView.setHasFixedSize( true);
        recyclerView.setLayoutManager( new LinearLayoutManager( getContext()));

        mUsers = new ArrayList<>();
        readUsers();

        return view;
    }

    private void readUsers() {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);

                        //Log.d("My log bla bla bla bla", "" + (user.getUserID() == null));
                        // Log.d("My log bla bla bla bla firebase user", firebaseUser.getUid());

                        if (user != null) {
                            if (!user.getUserID().equals(firebaseUser.getUid())) {
                                mUsers.add(user);
                            }
                        }
                    }
                }

                userAdapter = new UserAdapter(getContext(), mUsers);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
