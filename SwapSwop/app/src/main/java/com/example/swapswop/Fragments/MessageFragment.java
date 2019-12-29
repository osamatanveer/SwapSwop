package com.example.swapswop.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.swapswop.Classes.Chat;
import com.example.swapswop.Classes.MessageAdapter;
import com.example.swapswop.Classes.User;
import com.example.swapswop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * This class is the backend controller of the chat fragment.
 * @author Süleyman Semih Demir, Muhammed İkbal Doğan
 * @version 1.0, 10 May 2019
 */
public class MessageFragment extends Fragment {
    // variables for database
    private FirebaseUser      fuser;
    private DatabaseReference reference;

    // other variables
    private CircleImageView     profileImage;
    private TextView            username;
    private ImageButton         btnSend;
    private EditText            textSend;
    private MessageAdapter      messageAdapter;
    private List<Chat>          mChat;
    private RecyclerView        recyclerView;
    private Intent              intent;
    private LinearLayoutManager linearLayoutManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        Toolbar toolbar = view.findViewById( R.id.toolbar);
/*
        setSupportActionBar( toolbar);
        getSupportActionBar().setTitle( "");
        getSupportActionBar().setDisplayHomeAsUpEnabled( true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
*/

        recyclerView = view.findViewById( R.id.recycler_view);
        recyclerView.setHasFixedSize( true);
        linearLayoutManager = new LinearLayoutManager( getContext());
        linearLayoutManager.setStackFromEnd( true);
        recyclerView.setLayoutManager( linearLayoutManager);

        profileImage = view.findViewById( R.id.profile_image);
        username     = view.findViewById( R.id.username);
        btnSend      = view.findViewById( R.id.btn_send);
        textSend     = view.findViewById( R.id.text_send);


//        Intent intent = new Intent( getContext(), MessageActivity.class);
//        AppCompatActivity activity = ( AppCompatActivity) view.getContext();
//        intent = getActivity().getIntent();
//        intent = getIntent();
//        final String userid = intent.getStringExtra( "userid");

        Bundle bundle = this.getArguments();
        final String userid = bundle.getString( "userid");
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        btnSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String msg = textSend.getText().toString();

                if ( !msg.equals( ""))
                {
                    sendMessage( fuser.getUid(), userid, msg);
                }
                else
                {
                    Toast.makeText(getContext(), "Cannot send an empty message!", Toast.LENGTH_SHORT).show();
                }
                textSend.setText( "");
            }
        });

        reference = FirebaseDatabase.getInstance().getReference( "Users").child( userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue( User.class);

                username.setText( user.getUsername());

                if ( user.getProfilePicUrl().equals( "default"))
                {
                    profileImage.setImageResource( R.mipmap.ic_launcher);
                }
                else
                {
                    Glide.with( MessageFragment.this).load( user.getProfilePicUrl()).into(profileImage);
                }

                readMessages( fuser.getUid(), userid, user.getProfilePicUrl());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

    /**
     * This method sends a message, and stores it to the database.
     * @param sender the sender of the message
     * @param receiver the receiver of the message
     * @param message the message
     */
    private void sendMessage( String sender, String receiver, String message)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put( "sender", sender);
        hashMap.put( "receiver", receiver);
        hashMap.put( "message", message);

        reference.child( "Chats").push().setValue( hashMap);
    }

    /**
     * This method is used to extract messages from the database
     * @param myId id of the current user
     * @param userId the id of the receiver
     * @param imageURL the profile picture url of the receiver
     */
    private void readMessages(final String myId, final String userId, final String imageURL)
    {
        mChat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference( "Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();

                for ( DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Chat chat = snapshot.getValue( Chat.class);

                    if ( chat.getReceiver().equals( myId) && chat.getSender().equals( userId) || chat.getReceiver().equals( userId) && chat.getSender().equals( myId))
                    {
                        mChat.add( chat);
                    }

                    messageAdapter = new MessageAdapter( getContext(), mChat, imageURL);
                    recyclerView.setAdapter( messageAdapter);
                    messageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
