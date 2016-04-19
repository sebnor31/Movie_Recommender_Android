package com.certified_fresh.movieselector.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.certified_fresh.movieselector.R;
import com.certified_fresh.movieselector.controller.MovieListAdapter;
import com.certified_fresh.movieselector.controller.UserListAdapter;
import com.certified_fresh.movieselector.controller.UserManagerDB;
import com.certified_fresh.movieselector.model.UserDB;
import com.certified_fresh.movieselector.model.UserInfoHolder;

import java.util.List;


public class UserListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    // Instance variables
    ListView userListView;     // Handle to the list view where movies are displayed
    UserListAdapter adapter;   // Custom ArrayAdapter for the List View
    UserManagerDB userManager;

    public final static String EXTRA_USERNAME = "Username";
    public final static String EXTRA_LOCKED = "Locked";
    public final static String EXTRA_BANNED = "Banned";

    /**
     * Creates activity
     * @param savedInstanceState state that has been saved
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        userListView = (ListView) findViewById(R.id.user_list);
        adapter = new UserListAdapter(getApplicationContext(), R.layout.activity_admin_user);
        userListView.setAdapter(adapter);
        userListView.setOnItemClickListener(this);

        userManager = new UserManagerDB(this, null);
    }

    /**
     * Resumes activity
     */
    @Override
    protected void onResume() {
        super.onResume();

        if (adapter.getCount() > 0) {
            adapter.clear();
        }
        List<UserDB> users = userManager.getUsersList();
        for (int i = 0; i < users.size(); i++) {
            String lock = users.get(i).getLocked() >= 3 ? "Locked" : "Unlocked";
            String ban = users.get(i).getBanned() > 0 ? "Banned" : "Unbanned";
            adapter.add(new UserInfoHolder(users.get(i).getUsername(), lock, ban));
        }
    }

    /**
     * Set or recycle an item of the movie ListView
     * @param parent parent view adapter
     * @param view of item clicked
     * @param position position of the user in the list to be displayed
     * @param id of user
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UserInfoHolder user = adapter.getItem(position);
        Intent intent = new Intent(this, AdminUserActivity.class);
        intent.putExtra(EXTRA_USERNAME, user.getUsername());
        intent.putExtra(EXTRA_LOCKED, user.getLocked().equals("Locked") ? true : false);
        intent.putExtra(EXTRA_BANNED, user.getBanned().equals("Banned") ? true : false);
        startActivity(intent);
    }

    /**
     * Asks user if wants to logot on pressing back button
     */
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
