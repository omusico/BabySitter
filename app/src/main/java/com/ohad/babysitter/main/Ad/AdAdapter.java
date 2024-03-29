package com.ohad.babysitter.main.Ad;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ohad.babysitter.R;
import com.ohad.babysitter.user.UserProfileActivity;
import com.ohad.babysitter.pojo.UserPojo;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ohad on 26/11/2015.
 *
 */
public class AdAdapter extends BaseAdapter {

    private Context mContext;
    private List<UserPojo> mUsers;

    public AdAdapter(Context context, List<UserPojo> users) {
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
            convertView = View.inflate(mContext, R.layout.item_ad, null);

            holder = new ViewHolder();

            holder.pic = (CircleImageView) convertView.findViewById(R.id.profile_imageView);
            holder.name = (TextView) convertView.findViewById(R.id.name_textView);
            holder.home = (TextView) convertView.findViewById(R.id.city_textView);
            holder.phone = (TextView) convertView.findViewById(R.id.phone_textView);
            holder.email = (TextView) convertView.findViewById(R.id.email_textView);
            holder.salary = (TextView) convertView.findViewById(R.id.salary_textView);
            holder.show = (TextView) convertView.findViewById(R.id.show_textView);
            holder.gender= (TextView) convertView.findViewById(R.id.gender_textView);

            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
        }

        if (user.getBitmap() != null) {
            holder.pic.setImageBitmap(user.getBitmap());
        } else {
            Glide.with(mContext).load(user.getString(UserPojo.KEY_PROFILE_IMAGE_URL_COLUMN)).into(holder.pic);
        }

        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(user.getFullName());
        stringBuilder.append(" (");
        stringBuilder.append(user.getAge());
        stringBuilder.append(")");

        holder.name.setText(stringBuilder);
        holder.home.setText(user.getCity());
        holder.phone.setText(user.getPhone());
        holder.email.setText(user.getEmail());
        holder.salary.setText(user.getSalary());
        holder.gender.setText(user.getGender());

        holder.show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userId = user.getUserId();
                if (userId != null) {
                    UserProfileActivity.start(mContext, userId);
                }
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        CircleImageView pic;
        TextView name;
        TextView home;
        TextView phone;
        TextView email;
        TextView salary;
        TextView show;
        TextView gender;
    }

}
