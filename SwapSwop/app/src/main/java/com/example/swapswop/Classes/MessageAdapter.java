package com.example.swapswop.Classes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.swapswop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

/**
 * This class is used to represent a message. It contains information about the sender, receiver,
 * and the message.
 * @author Süleyman Semih Demir, Mohammed Sameer Yaseen, Muhammed İkbal Doğan
 * @version 1.0, 10 May 2019
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<Chat> mChat;
    private String imageURL;

    FirebaseUser fuser;

    /**
     * A constructor for the MessageAdapter class
     * @param mContext the context
     * @param mChat the list of chat objects
     * @param imageURL the profile picture of the other user
     */
    public MessageAdapter(Context mContext, List<Chat> mChat, String imageURL)
    {
        this.mChat    = mChat;
        this.mContext = mContext;
        this.imageURL = imageURL;
    }

    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if ( viewType == MSG_TYPE_RIGHT)
        {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder( MessageAdapter.ViewHolder holder, int position)
    {
        Chat chat = mChat.get( position);

        holder.show_message.setText( chat.getMessage());

        if ( imageURL.equals( "default"))
        {
            holder.profile_image.setImageResource( R.mipmap.ic_launcher);
        }
        else
        {
            Glide.with( mContext).load( imageURL).into( holder.profile_image);
        }
    }


    @Override
    public int getItemCount()
    {
        return mChat.size();
    }

    /**
     * This class contains the view of the chat. It shows the message and the profile image.
     */
    public class ViewHolder extends RecyclerView.ViewHolder
    {

        public TextView  show_message;
        public ImageView profile_image;

        public ViewHolder( View itemView)
        {
            super( itemView);

            show_message  = itemView.findViewById( R.id.show_message);
            profile_image = itemView.findViewById( R.id.profile_image);
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        if ( mChat.get( position).getSender().equals( fuser.getUid()))
        {
            return MSG_TYPE_RIGHT;
        }
        else
        {
            return MSG_TYPE_LEFT;
        }
    }
}
