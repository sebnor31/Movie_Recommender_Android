package com.certified_fresh.movieselector.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.certified_fresh.movieselector.R;
import com.certified_fresh.movieselector.controller.UserListAdapter;
import com.certified_fresh.movieselector.controller.UserManagerDB;

/**
 * Created by Ryan on 3/17.
 */
public class AdminUserActivity extends AppCompatActivity {

    String username;
    TextView usernameView;
    CheckBox lockedView;
    CheckBox bannedView;
    UserManagerDB userManager;

    /**
     * Creates activity
     * @param savedInstanceState state that has been saved
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_display);

        usernameView = (TextView) findViewById(R.id.usernameView);
        lockedView = (CheckBox) findViewById(R.id.lockedView);
        bannedView = (CheckBox) findViewById(R.id.bannedView);

        userManager = new UserManagerDB(this, null);
    }

    /**
     * Resumes activity
     */
    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        usernameView.setText(intent.getStringExtra(UserListActivity.EXTRA_USERNAME));
        username = usernameView.getText().toString();
        lockedView.setChecked(intent.getBooleanExtra(UserListActivity.EXTRA_LOCKED, false));
        bannedView.setChecked(intent.getBooleanExtra(UserListActivity.EXTRA_BANNED, false));
    }

    /**
     * Locks account of a user
     * @param view of button clicked
     */
    public void onLockedClicked(View view) {
        userManager.setLock(username, lockedView.isChecked());
        Log.i("DEBUG", "Lock clicked");
    }

    /**
     * Bans account of a user
     * @param view of button clicked
     */
    public void onBannedClicked(View view) {
        userManager.setBan(username, bannedView.isChecked());
        Log.i("DEBUG", "Ban clicked");
    }
}
