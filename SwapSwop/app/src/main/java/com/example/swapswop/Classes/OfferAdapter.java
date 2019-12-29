package com.example.swapswop.Classes;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.swapswop.Fragments.ChooseFromOffersFragment;
import com.example.swapswop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * This class if for presenting the offer feed.
 * RecyclerView.Adapter  -> this one binds the data to the view
 * RecyclerView.Holder   -> this one holds the view (nested class)
 * @author Mohammed Sameer Yaseen
 * @version 1.00 19/05/04
 */

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferViewHolder> {

    // variables
    private Context           mCtx;
    private List<Offer>       offerList;
    private Offer             offer;
    private int               descriptionLength;

    private FirebaseDatabase  database;
    private DatabaseReference usersReference;
    private FirebaseUser      fuser;

    /**
     * A constructor for OfferAdapter.
     * @param mCtx the context
     * @param offerList the list of offers to display
     */
    public OfferAdapter(Context mCtx, List<Offer> offerList) {
        this.mCtx         = mCtx;
        this.offerList    = offerList;
        descriptionLength = 2;
        fuser             = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view               = inflater.inflate(R.layout.offer_layout, null);

        OfferViewHolder holder  = new OfferViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final OfferViewHolder offerViewHolder, int i) {
        offer               = offerList.get(i);
        database            = FirebaseDatabase.getInstance();
        usersReference      = database.getReference("Users/" + offer.getUserId());

        Log.d("My log", " the index of the arrayList that is read is: " + i + "");

        // retrieving the user's information from the database
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                //Log.d("My Log - OfferAdapter", "user is retrieved id is : " + user.getUserID());

                if (user != null && user.getUserID().equals(offer.getUserId())){
                    offer.setUserName(user.getUsername());
                    offer.setProfileImageUrl(user.getProfilePicUrl());

                    // setting the texts
                    offerViewHolder.title.setText(makeTitleWithStyle( offer.getUserName(),
                            offer.getTitle(), offer.getProcessType()));
                    offerViewHolder.description.setText(formatDescription(offer.getDescription(),
                            descriptionLength, offerViewHolder));
                    offerViewHolder.dateNtime.setText(offer.getDate().toString());

                    // setting the view pictures
                    Picasso.with(mCtx).load(offer.getImageUrl()).into(offerViewHolder.offerPicture);
                    Picasso.with(mCtx).load(offer.getProfileImageUrl()).into(offerViewHolder.profilePicture);

                    // setting the onClick listener for the button
                    offerViewHolder.buttonExchange.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            // setting up the chat class for one swap request
                            Chat chat = new Chat();
                            chat.setSender( fuser.getUid());
                            chat.setReceiver( offer.getUserId());
                            chat.setMessage( "I want to exchange your " + offer.getTitle() + " with my " );

                            // starting a fragment to set the second part of the message in
                            // the  chat object
                            AppCompatActivity activity = ( AppCompatActivity) view.getContext();
                            Fragment chooseFromOffersFragment = new ChooseFromOffersFragment();

                            // adding the chat object to be edited by ChooseFromOffersFragment
                            ((ChooseFromOffersFragment) chooseFromOffersFragment).setChat(chat);
                            activity.getSupportFragmentManager().beginTransaction()
                                    .replace( R.id.fragment_container, chooseFromOffersFragment)
                                    .addToBackStack( null).commit();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }

    /**
     * This method sets the style with which the title is displayed.
     * @param userName the username of the uploader
     * @param title the title of the offer
     * @param processType the type of the offer, exchange or loan
     * @return
     */
    private SpannableStringBuilder makeTitleWithStyle(String userName, String title, String processType) {
        // constants
        final String OFFERS = " - offers ";
        final String TO     = " to ";

        // variables
        StyleSpan bold, italic;
        SpannableStringBuilder ss;
        int userNameStartingIndex, userNameEndingIndex, offerTitleStartingIndex, offerTitleEndingIndex;

        // initializing variables
        bold                    = new StyleSpan(Typeface.BOLD);
        italic                  = new StyleSpan(Typeface.ITALIC);
        ss                      =  new SpannableStringBuilder("@" + userName + OFFERS
                                                                       + title + TO + processType);
        userNameStartingIndex   = 0;
        userNameEndingIndex     = userName.length();
        offerTitleStartingIndex = userName.length() + OFFERS.length();
        offerTitleEndingIndex   = userName.length() + OFFERS.length() + title.length();

        ss.setSpan(bold, userNameStartingIndex,userNameEndingIndex , Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ss.setSpan(italic, offerTitleStartingIndex, offerTitleEndingIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE );

        return ss;
    }

    /**
     * This method sets the design of the writings of the description.
     * @param description the description of the offer
     * @return ss the formatted description
     */
    private SpannableStringBuilder formatDescription(String description, int descriptionSize, final OfferViewHolder offerView) {
        // constants
        final String DESCRIPTION = "Description : ";
        final String DOTS        = ".... ";
        final String MORE        = " More";
        final String LESS        = " Less";
        final int SHORT_DESCRIPTION_LENGTH = 500;
        final int MORE_CODE_FOR_DESCRIPTION = 1;
        final int LESS_CODE_FOR_DESCRIPTION = 2;

        // variables
        int                    lengthAfterDeleting;
        int                    moreStartingIndex;
        int                    moreEndingIndex;
        SpannableStringBuilder ss;
        StyleSpan              bold;
        ForegroundColorSpan    blue;
        ClickableSpan          moreClick;

        ss      = new SpannableStringBuilder(DESCRIPTION + description);
        bold    = new StyleSpan(Typeface.BOLD);
        blue    = new ForegroundColorSpan(Color.BLUE);

        ss.setSpan(bold, 0, DESCRIPTION.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        // if the description is long and the user pressed on less to see less text
        if (ss.length() > SHORT_DESCRIPTION_LENGTH && descriptionSize == LESS_CODE_FOR_DESCRIPTION) {

            ss = ss.delete(SHORT_DESCRIPTION_LENGTH, ss.length());
            lengthAfterDeleting = ss.length();
            moreStartingIndex   = lengthAfterDeleting + DOTS.length();
            moreEndingIndex     = lengthAfterDeleting + DOTS.length() + MORE.length();

            ss.append(DOTS);
            ss.append(MORE);
            ss.setSpan(blue, moreStartingIndex, moreEndingIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            moreClick = new ClickableSpan() {
                @Override
                public void onClick( View widget) {
                    descriptionLength = MORE_CODE_FOR_DESCRIPTION;
                    Log.d("My log","more executed");
                    notifyDataSetChanged();
                }
            };

            ss.setSpan(moreClick, moreStartingIndex, moreEndingIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            offerView.description.setText(ss);
        }

        // if the description is long and the user pressed on more to see more text
        if (ss.length() > SHORT_DESCRIPTION_LENGTH && descriptionSize == MORE_CODE_FOR_DESCRIPTION){
            ss.append(LESS);
            ss.setSpan(blue, (ss.length() - LESS.length()), (ss.length() + LESS.length()),
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            ClickableSpan less = new ClickableSpan() {
                @Override
                public void onClick( View widget) {
                    descriptionLength = LESS_CODE_FOR_DESCRIPTION;
                    Log.d("My log","less executed");
                    notifyDataSetChanged();
                }
            };

            ss.setSpan(less,(ss.length() - LESS.length()), (ss.length() + LESS.length()), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            offerView.description.setText(ss);
        }
        return ss;
    }

    /**
     * This inner class initializes the holder.
     * @author Mohammed Sameer Yaseen
     * @version 1.00, 19/05/04
     */
    public class OfferViewHolder extends RecyclerView.ViewHolder{

        private ImageView profilePicture, offerPicture;
        private TextView  title, description, dateNtime;
        private Button    buttonExchange;

        OfferViewHolder(@NonNull View itemView) {
            super(itemView);

            // initializing variables
            profilePicture = itemView.findViewById(R.id.image_view_profile_pic);
            offerPicture   = itemView.findViewById(R.id.image_view_offer_pic);
            title          = itemView.findViewById(R.id.text_view_title);
            description    = itemView.findViewById(R.id.text_view_description);
            dateNtime      = itemView.findViewById(R.id.text_view_datentime);
            buttonExchange = itemView.findViewById(R.id.details);
        }
    }
}
