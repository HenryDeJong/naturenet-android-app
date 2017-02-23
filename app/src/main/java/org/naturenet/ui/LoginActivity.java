package org.naturenet.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.naturenet.R;

public class LoginActivity extends AppCompatActivity {

    public static final String EXTRA_LOGIN = "login";
    public static final String EXTRA_JOIN = "join";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        goToLoginFragment();
    }

    public void continueAsSignedUser() {
        Intent resultIntent = new Intent(this, MainActivity.class);
        setResult(Activity.RESULT_OK, resultIntent);
        resultIntent.putExtra(EXTRA_LOGIN, EXTRA_LOGIN);
        finish();
    }

    public void goToLoginFragment() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new LoginFragment(), LoginFragment.FRAGMENT_TAG)
                .commit();
    }

    public void goToJoinActivity() {
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra(EXTRA_LOGIN, EXTRA_JOIN);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}