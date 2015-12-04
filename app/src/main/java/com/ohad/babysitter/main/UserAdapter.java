package com.ohad.babysitter.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ohad.babysitter.R;
import com.ohad.babysitter.pojo.UserPojo;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ohad on 26/11/2015.
 *
 */
public class UserAdapter extends BaseAdapter {

    private Context mContext;
    private List<UserPojo> mUsers;

    public UserAdapter(Context context, List<UserPojo> users) {
        this.mContext = context;
        this.mUsers = users;
    }

    @Override
    public int getCount() {
        return mUsers.size();
    }

    @Override
    public UserPojo getItem(int position) {
        return mUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final UserPojo user = mUsers.get(position);
        final ViewHolder holder;

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_user, null);

            holder = new ViewHolder();

            holder.pic = (CircleImageView) convertView.findViewById(R.id.profile_imageView);
            holder.name = (TextView) convertView.findViewById(R.id.name_textView);
            holder.home = (TextView) convertView.findViewById(R.id.home_textView);
            holder.phone = (TextView) convertView.findViewById(R.id.phone_textView);
            holder.show = (TextView) convertView.findViewById(R.id.show_textView);

            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
        }

        Glide.with(mContext).load(user.getProfilePictureUrl()).into(holder.pic);

        holder.name.setText(user.getFullname() + " (" + user.getAge() + ")");
        holder.home.setText(user.getCity());
        holder.phone.setText(user.getPhone());

        return convertView;
    }

    private static class ViewHolder {
        CircleImageView pic;
        TextView name;
        TextView home;
        TextView phone;
        TextView show;
    }

}
