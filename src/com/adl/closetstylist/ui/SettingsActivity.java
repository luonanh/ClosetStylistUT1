package com.adl.closetstylist.ui;
 

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.adl.closetstylist.R;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
 
public class SettingsActivity extends Activity {
	
	// FB code starts
	private final static String TAG = SettingsActivity.class.getCanonicalName();
	private LoginButton fbLoginBtn;
	private GraphUser user;
	private boolean isResumed = false;
    private UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };
    // FB code ends
 
    private Context context;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        context = getApplicationContext();

        // FB code starts
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        
        fbLoginBtn = (LoginButton) findViewById(R.id.facebook_login_button);       
        fbLoginBtn.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
			@Override
			public void onUserInfoFetched(GraphUser user) {
				Log.d(TAG, "onUserInfoFetched");
				SettingsActivity.this.user = user;
				updateUI();
			}
		});
        
        showHashKey();
        // FB code ends
    }
    
    // FB code starts
    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
        isResumed = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
        isResumed = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    /*
     * Ported from HelloFacebookSample/src/com/facebook/samples/hellofacebook/HelloFacebookSampleActivity.java
     */
    private void updateUI() {
    	/*
        profilePictureView = (ProfilePictureView) findViewById(R.id.profilePicture);
        greeting = (TextView) findViewById(R.id.greeting);

        Session session = Session.getActiveSession();
        boolean enableButtons = (session != null && session.isOpened());

        Log.i(TAG, "updateUI");
        if (enableButtons && user != null) {
            profilePictureView.setProfileId(user.getId());
            greeting.setText(user.getFirstName() + " " + user.getLastName());
        } else {
            profilePictureView.setProfileId(null);
            greeting.setText(null);
        }
        */
    }
    
    /*
     * Copied from https://developers.facebook.com/docs/android/login-with-facebook/v2.0
     */
    private void showHashKey() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.facebook.samples.hellofacebook", 
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i(TAG, "KeyHash:" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
    
    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        updateUI();
    }
    // FB code ends
}
