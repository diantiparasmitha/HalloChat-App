package com.aangps01.hallochat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aangps01.hallochat.Model.User;
import com.aangps01.hallochat.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class PsikiaterAdapter extends RecyclerView.Adapter<PsikiaterAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUsers;

    public PsikiaterAdapter(Context mContext, List<User> mUsers) {
        this.mContext = mContext;
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.psikiater_item,parent,false);
        return new PsikiaterAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = mUsers.get(position);
        holder.email.setText(user.getUsername());
        if(user.getImageURL().equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(mContext).load(user.getImageURL()).into(holder.profile_image);
        }
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView email;
        public ImageView profile_image;

        public ViewHolder(View view){
            super(view);

            email = view.findViewById(R.id.email);
            profile_image = view.findViewById(R.id.profile_image);
        }
    }
}
