package com.programminghub.simplechat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Administrator on 3/13/2018.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    List<User> userList;

    public UserListAdapter(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user,null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.bindView(userList.get(position));

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView email;
        ImageView showUserProfile;
        public ViewHolder(View itemView) {
            super(itemView);
            email=itemView.findViewById(R.id.show_user_email);
            showUserProfile=itemView.findViewById(R.id.show_user_profile);
        }
        public void bindView(User user){
            email.setText(user.getEmailAddress());
            Glide.with(showUserProfile.getContext()).load(user.getProfilePic()).into(showUserProfile);

        }
    }
}
