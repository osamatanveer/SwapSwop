package com.example.swapswop.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.swapswop.Classes.Chat;
import com.example.swapswop.Classes.Offer;
import com.example.swapswop.R;
import com.example.swapswop.Classes.ShortOfferAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * ChooseFromOffersFragment class : creates the offers fragment to exchange and shows them
 * @author Mohammed Sameer Yaseen
 */

public class ChooseFromOffersFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    // database variables
    private FirebaseDatabase  dbRef;
    private DatabaseReference myRef;
    private Offer             offerRetrieved;
    private Query             reverseOrderQuery;

    // other variables
    private RecyclerView      recyclerView;
    private ShortOfferAdapter adapter;
    private List<Offer>       offerList;
    private Chat              chat;

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_from_offers, container, false);
        // initializing database variables
        dbRef       = FirebaseDatabase.getInstance();
        myRef       = dbRef.getReference("Offers");

        // initializing other variables
        offerList    = new ArrayList <Offer>();
        recyclerView = (RecyclerView) view.findViewById(R.id.short_recycle_view);
        adapter      = new ShortOfferAdapter(view.getContext(), offerList, chat);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        reverseOrderQuery = myRef.orderByChild("order");

        // adding an eventListener to the child
        reverseOrderQuery.addListenerForSingleValueEvent(valueEventListener);

        return view;
    }


    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            FirebaseUser firebaseUser;
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            for(DataSnapshot offerSnapShot : dataSnapshot.getChildren()){
                offerRetrieved = offerSnapShot.getValue(Offer.class);
                if (offerRetrieved.getUserId().equals( firebaseUser.getUid())) {
                    offerList.add(offerRetrieved);
                    adapter.notifyDataSetChanged();
                }
                Log.d("My log" , " the retrieved data is : " + offerRetrieved.getUserId());
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
