package com.adl.closetstylist.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.adl.closetstylist.R;
import com.adl.closetstylist.ui.actionfragment.ActionFragment;
import com.adl.closetstylist.ui.navigationdrawer.NavigationDrawerFragment;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {
	
	public static final boolean DEVELOPING = true;

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	private Menu menu;

	private ActionDescriptor currentActionDescriptor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
	}

	private boolean firstAttachFragment = true;
	
	@Override
	public void onNavigationDrawerItemSelected(int position) {
		ActionDescriptor actionDescriptor = ActionDescriptor.getById(position);
		
		switch (actionDescriptor) {
		case Settings:
			Intent i = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(i);
			break;
		default:
			currentActionDescriptor = actionDescriptor;
			Fragment actionFragment = ActionFragment.getNewActionFragment(actionDescriptor);
			if (actionFragment == null)
				return;
			
			
			
			// update the main content by replacing fragments
			FragmentManager fragmentManager = getSupportFragmentManager();
			if (firstAttachFragment) {
				//do not put this 1st fragment transaction into backtrack stack
				fragmentManager.beginTransaction()
						.replace(R.id.container, actionFragment).commit();
				firstAttachFragment = false;
			} else {
				fragmentManager.beginTransaction()
				.replace(R.id.container, actionFragment).addToBackStack(null).commit();
			}
			break;
		}
		
	}

	public void onSectionAttached(int action_id) {
		ActionDescriptor actionDescriptor = ActionDescriptor.getById(action_id);
		currentActionDescriptor = actionDescriptor;
		mTitle = getString(actionDescriptor.getLabelResourceId());
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(mTitle);
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		
		if (currentActionDescriptor == null)
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		else 
			actionBar.setNavigationMode(currentActionDescriptor.getActionBarNavigationMode());
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.ab_bg));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			this.menu = menu;
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
	}

}
