package com.aangps01.hallochat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aangps01.hallochat.Model.Chat;
import com.aangps01.hallochat.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageUserAdapter extends RecyclerView.Adapter<MessageUserAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<Chat> mChats;
    private String imageurl;

    FirebaseUser firebaseUser;

    public MessageUserAdapter(Context mContext, List<Chat> mChats, String imageurl) {
        this.mContext = mContext;
        this.mChats = mChats;
        this.imageurl = imageurl;
    }

    @NonNull
    @Override
    public MessageUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right,parent,false);
            return new MessageUserAdapter.ViewHolder(view);
        }else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left,parent,false);
            return new MessageUserAdapter.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageUserAdapter.ViewHolder holder, int position) {
        Chat chat = mChats.get(position);

        holder.show_message.setText(chat.getMessage());

        if(imageurl.equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(mContext).load(imageurl).into(holder.profile_image);
        }


    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public ImageView profile_image;

        public ViewHolder(View view){
            super(view);

            show_message = view.findViewById(R.id.show_message);
            profile_image = view.findViewById(R.id.profile_image);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mChats.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }
}
