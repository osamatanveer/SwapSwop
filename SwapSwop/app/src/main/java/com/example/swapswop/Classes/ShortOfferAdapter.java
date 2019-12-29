package com.example.swapswop.Classes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.swapswop.Fragments.HomeFragment;
import com.example.swapswop.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * This class creates the mini version of the view of the offer
 * @author Mohammed Sameer Yaseen
 */

public class ShortOfferAdapter extends RecyclerView.Adapter<ShortOfferAdapter.ShortOfferViewHolder>{

    private Context     mCtx;
    private List<Offer> offers;
    private Chat        chat;

    public ShortOfferAdapter(Context mCtx, List<Offer> offers, Chat chat){
        this.mCtx   = mCtx;
        this.offers = offers;
        this.chat   = chat;
    }

    @NonNull
    @Override
    public ShortOfferViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // getting the context for inflater
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        // getting the view using the inflater
        View           view     = (View) inflater.inflate(R.layout.short_item_layout, null);

        // making the view holder using a view and returning it
        ShortOfferAdapter.ShortOfferViewHolder holder = new ShortOfferViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShortOfferViewHolder shortOfferViewHolder, int i) {

        final Offer offer = offers.get(i);

        shortOfferViewHolder.title.setText(offer.getTitle());
        shortOfferViewHolder.description.setText(offer.getDescription());
        Picasso.with(mCtx).load(offer.getImageUrl()).into(shortOfferViewHolder.offerImg);

        shortOfferViewHolder.chooseThis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // getting the offer to be exchanged with and setting the second part of
                // the message that had its first part set in the OfferAdapter
                String tempText = chat.getMessage();
                chat.setMessage(tempText + offer.getTitle());

                // adding the message to the database
                DatabaseReference ChatsReference = FirebaseDatabase.getInstance().getReference().child( "Chats");
                ChatsReference.child(ChatsReference.push().getKey()).setValue( chat);

                // go to home fragment
                AppCompatActivity activity = ( AppCompatActivity) view.getContext();
                Fragment homeFragment      = new HomeFragment();
                activity.getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, homeFragment).addToBackStack( null).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return offers.size();
    }

    public class ShortOfferViewHolder extends RecyclerView.ViewHolder {

        private ImageView offerImg;
        private TextView  title, description;
        private Button    chooseThis;

        public ShortOfferViewHolder(@NonNull View itemView) {
            super(itemView);

            // initializing the variables
            offerImg    = itemView.findViewById(R.id.short_offer_image);
            title       = itemView.findViewById(R.id.short_offer_title);
            description = itemView.findViewById(R.id.short_offer_description);
            chooseThis  = itemView.findViewById(R.id.choose_this_fragment);
        }
    }
}
