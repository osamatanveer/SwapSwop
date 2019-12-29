package com.example.swapswop.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.swapswop.Classes.Offer;
import com.example.swapswop.Classes.OfferAdapter;
import com.example.swapswop.Classes.User;
import com.example.swapswop.MainActivity;
import com.example.swapswop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the backend of the home fragment. It controls the offers that are displayed in the
 * feed.
 * @author Osama Tanveer, Suleyman Semih Demir, Mohammed Sameer Yaseen, Ali Caner Kilci
 */

public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    // Properties
    private RecyclerView recyclerView;
    private OfferAdapter adapter;
    private List<Offer>  offerList;
    private EditText     searchArea;
    private Query        reverseOrderQuery;
    private Button       detailsButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // initializing database variables
        FirebaseDatabase  dbRef    = FirebaseDatabase.getInstance();
        DatabaseReference myRef    = dbRef.getReference("Offers");
        FirebaseStorage   mStorage = FirebaseStorage.getInstance();


        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getContext(),
                R.array.preferences, android.R.layout.simple_spinner_item);
        Spinner                    spinner  = view.findViewById(R.id.sorter);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);
        spinner.setOnItemSelectedListener(this);

        // initializing other variables
        offerList    = new ArrayList <Offer>();
        recyclerView = view.findViewById(R.id.recycler_view);
        searchArea   = view.findViewById(R.id.searchArea);
        //detailsButton = view.findViewById(R.id.details);
        //detailsButton = itemView.findViewById(R.id.details);


        adapter      = new OfferAdapter(view.getContext(), offerList);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        reverseOrderQuery = myRef.orderByChild("order");

        // adding an eventListener to the child
        reverseOrderQuery.addListenerForSingleValueEvent(valueEventListener);

        searchArea.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<Offer> searchedOffers = (ArrayList<Offer>) search(s.toString(), (ArrayList<Offer>) offerList);
                adapter                         = new OfferAdapter(getContext(), searchedOffers);
                recyclerView.setAdapter(adapter);
            }
        });
        return view;
    }


    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            for(DataSnapshot offerSnapShot : dataSnapshot.getChildren()){
                Offer offerRetrieved = offerSnapShot.getValue(Offer.class);
                offerList.add(offerRetrieved);
                adapter.notifyDataSetChanged();
                Log.d("My log" , " the retrieved data is : " + offerRetrieved.getUserId());
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    };

    /**
     * This method is used to search through offers
     * @param input the string input
     * @param listToSearch the list to search in
     * @return the results of the search
     */
    public static ArrayList<Offer> search(String input , ArrayList<Offer> listToSearch) {

            ArrayList<Offer> results = new ArrayList<>();
            for (int i = 0; i < listToSearch.size(); i++) //for loop which is going to navigate through every element
            {
                if (listToSearch.get(i).getTitle().toLowerCase().indexOf(input.toLowerCase()) >= 0) //check whether the text is matching or not.
                {
                    results.add(listToSearch.get(i));
                }
            }
            return results;

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ArrayList<Offer> tmp  = (ArrayList<Offer>) offerList;
        ArrayList<Offer> temp = (ArrayList<Offer>) tmp.clone();
        String           text = parent.getItemAtPosition(position).toString();

        adapter = new OfferAdapter(getContext(), Offer.sort(text , temp));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
