package com.adl.closetstylist.ui;
 
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.adl.closetstylist.R;
import com.adl.closetstylist.UserProfile;
import com.adl.closetstylist.db.ItemDatabaseHelper;
 
public class LoginActivity extends Activity {
	
	private final static String TAG = LoginActivity.class.getCanonicalName();
 
    private Context context;
    private ItemDatabaseHelper itemDatabaseHelper;
    private EditText usr;
    private EditText pwd;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        context = getApplicationContext();
        itemDatabaseHelper = new ItemDatabaseHelper(this);
        
        View btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				UserProfile up = itemDatabaseHelper.getCurrentUserProfile();
				if (up != null) {
					usr = (EditText) findViewById(R.id.username);
					pwd = (EditText) findViewById(R.id.password);
					if ((usr.getText().toString().equalsIgnoreCase(up.getUsr()))
							&& (pwd.getText().toString().equalsIgnoreCase(up.getPwd()))) {
						// Start your app main activity when username and password match
		                Intent i = new Intent(LoginActivity.this, MainActivity.class);
		                startActivity(i);
		                
		                // close this activity
		                finish();
					} else {
						Toast.makeText(context, 
								R.string.login_message_username_password_not_found, 
								Toast.LENGTH_SHORT)
								.show();
					}					
				} else { // if there's no UserProfile in the database, prompt user to register
					Toast.makeText(context, 
							R.string.login_message_prompt_registration, 
							Toast.LENGTH_SHORT)
							.show();					
				}
			}
		});
        
        View linkRegister = findViewById(R.id.link_register);
        linkRegister.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Start your app main activity
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);					
			}
		});
        
		Button clearMyClosetBtn = (Button) findViewById(R.id.clear_my_closet);
		clearMyClosetBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				itemDatabaseHelper.deleteDatabase();
			}
			
		});
    }    
}
