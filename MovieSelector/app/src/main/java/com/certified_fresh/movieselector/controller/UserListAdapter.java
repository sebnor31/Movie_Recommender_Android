package com.certified_fresh.movieselector.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.certified_fresh.movieselector.R;
import com.certified_fresh.movieselector.model.UserInfoHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aysha on 3/16/16.
 */
public class UserListAdapter extends ArrayAdapter<UserInfoHolder> {
    // List of users
    List<UserInfoHolder> list = new ArrayList<>();
    static class DataHolder {
        TextView usernameView;
        TextView lockedView;
        TextView bannedView;
    }
    /**
     * Constructor of this ArrayAdapter
     * @param context inputs context for adapter
     * @param resource integer resource for adapter
     */
    public UserListAdapter(Context context, int resource) {
        super(context, resource);
    }

    /**
     * Add a new user entry in the list
     * @param user a data holder of user attributes
     */
    @Override
    public void add(UserInfoHolder user) {
        super.add(user);
        list.add(user);
    }

    /**
     * Get the number of users in the list
     * @return size of user list
     */
    @Override
    public int getCount() {
        return list.size();
    }

    /**
     * Clears adapter to prevent duplicates
     */
    @Override
    public void clear() {
        list.clear();
        super.clear();
    }


    /**
     * Get a user from list
     * @param position Position of the user in the list
     * @return a user
     */
    @Override
    public UserInfoHolder getItem(int position) {
        return super.getItem(position);
    }

    /**
     * Set or recycle an item of the user ListView
     * @param position position of the user in the list to be displayed
     * @param convertView a ViewGroup to be populated with a user's attributes
     * @param parent ListView parent
     * @return a ViewGroup populated with a user's attributes
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Initialize the convenience user ListView's items data holder
        DataHolder userInfoHolder;

        // Set the ViewGroup convertView
        if (convertView == null) {
            // Set the layout of a new item to the activity_admin_user
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_admin_user, parent, false);

            // Set the user data holder attributes to this view's UI elements
            userInfoHolder = new DataHolder();
            userInfoHolder.usernameView = (TextView) convertView.findViewById(R.id.username);
            userInfoHolder.lockedView = (TextView) convertView.findViewById(R.id.lockedView);
            userInfoHolder.bannedView = (TextView) convertView.findViewById(R.id.banned);

            // Not sure what Tags are for...but needed
            convertView.setTag(userInfoHolder);
        }
        else {
            // Set the user data holder attributes to this existing view's UI elements
            userInfoHolder = (DataHolder) convertView.getTag();
        }

        // Get the user at specified position
        UserInfoHolder user = getItem(position);

        // Set the view's UI elements to the selected movie's attributes
        userInfoHolder.usernameView.setText(user.getUsername());
        userInfoHolder.lockedView.setText(user.getLocked());
        userInfoHolder.bannedView.setText(user.getBanned());


        return convertView;
    }
}
