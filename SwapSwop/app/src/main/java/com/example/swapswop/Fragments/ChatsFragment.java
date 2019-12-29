package com.example.swapswop.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.swapswop.Classes.Chat;
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
 * This class is used as a backend handler of the chat fragment.
 * @author Suleyman Semih Demir, Mohammed Sameer Yaseen, Osama Tanveer
 * @version 1.6, 13 May 2019
 */
public class ChatsFragment extends Fragment {



    private RecyclerView recyclerView;
    private UserAdapter  userAdapter;
    private List<User>   mUsers;
    private List<String> usersList;

    private FirebaseUser      fuser;
    private DatabaseReference reference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate( R.layout.fragment_chats, container, false);

        recyclerView = view.findViewById( R.id.recycler_view);
        recyclerView.setHasFixedSize( true);
        recyclerView.setLayoutManager( new LinearLayoutManager(getContext()));

        fuser     = FirebaseAuth.getInstance().getCurrentUser();
        usersList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference( "Chats");
        reference.addValueEventListener( new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                usersList.clear();

                for ( DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Chat chat = snapshot.getValue( Chat.class);

                    if ( chat.getSender().equals( fuser.getUid()))
                    {
                        int count = 0;
                        for (String user : usersList) {
                            if (!user.equals(chat.getReceiver())){
                                count++;
                            }
                        }
                        if ( count == usersList.size())
                        {
                            usersList.add(chat.getReceiver());
                        }
                    }
                    if ( chat.getReceiver().equals( fuser.getUid()))
                    {
                        int count = 0;
                        for (String user : usersList) {
                            if (!user.equals(chat.getSender())) {
                                count++;
                            }
                        }
                        if ( count == usersList.size())
                        {
                            usersList.add(chat.getSender());
                        }
                    }
                }
                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){

            }
        });

        return view;
    }

    /**
     * This method gets the chats from the database.
     */
    private void readChats()
    {
        mUsers    = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference( "Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();

                for ( DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    User user = snapshot.getValue( User.class);

                    // display one user from chats
                    for ( String id : usersList)
                    {
                        if ( user.getUserID().equals( id))
                        {
                            if ( mUsers.size() != 0)
                            {
                                for ( int i = 0; i < mUsers.size(); i++)
                                {
                                    if ( !user.getUserID().equals( mUsers.get( i).getUserID()))
                                    {
                                        mUsers.add( user);
                                    }
                                }
                            }
                            else
                            {
                                mUsers.add( user);
                            }
                        }
                    }
                }
                userAdapter = new UserAdapter( getContext(), mUsers);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
