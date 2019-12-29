package com.example.swapswop.Classes;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.swapswop.Fragments.MessageFragment;
import com.example.swapswop.R;

import java.util.List;

/**
 * This class is used to design an adapter for displaying the users in the chat pane.
 * @author Mohammed Sameer Yaseen, Suleyman Semih Demir
 * @version 1.30, 13 May 2019
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mContext;
    private List<User> mUsers;

    public UserAdapter(Context mContext, List<User> mUsers)
    {
        this.mUsers   = mUsers;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i)
    {
        View view = LayoutInflater.from( mContext).inflate( R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder( view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        final User user = mUsers.get( position);
        holder.username.setText( user.getUsername());

        if ( user.getProfilePicUrl().equals( "default"))
        {
            holder.profile_image.setImageResource( R.mipmap.ic_launcher);
        }
        else
        {
            Glide.with( mContext).load( user.getProfilePicUrl()).into( holder.profile_image);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent( mContext, MessageActivity.class);
//                Intent intent = new Intent();
//                intent.putExtra( "userid", user.getUserID());
//                mContext.startActivity( intent);

                AppCompatActivity activity = ( AppCompatActivity) view.getContext();
                Fragment myFragment = new MessageFragment();

                activity.getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, myFragment).addToBackStack( null).commit();

                Bundle bundle = new Bundle();
                bundle.putString("userid", user.getUserID());
                myFragment.setArguments(bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    /**
     * This class is a view holder, it is used to initialize the buttons.
     * @author Mohammed Sameer Yaseen, Suleyman Semih Demir
     * @version 1.20, 13 May 2019
     */
    public class ViewHolder extends RecyclerView.ViewHolder
    {

        public TextView username;
        public ImageView profile_image;

        public ViewHolder( View itemView)
        {
            super( itemView);

            username = itemView.findViewById( R.id.username);
            profile_image = itemView.findViewById( R.id.profile_image);
        }
    }
}
