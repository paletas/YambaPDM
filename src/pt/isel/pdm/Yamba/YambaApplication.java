package pt.isel.pdm.yamba;

import pt.isel.pdm.yamba.TwitterAsync.TwitterAsync;
import pt.isel.pdm.yamba.TwitterAsync.listeners.TwitterExceptionListener;
import pt.isel.pdm.yamba.exceptions.BadCredentialsException;
import pt.isel.pdm.yamba.exceptions.InvalidAPIException;
import pt.isel.pdm.yamba.exceptions.TwitterException;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.widget.Toast;


public class YambaApplication extends Application{
	
	public static final String TweetExtra = "TWEET_EXTRA";
	
	private OnSharedPreferenceChangeListener _preferenceListener;
	
	private static YambaApplication _application;
	
	public static YambaApplication getApplication() { return _application; }
	
	@Override
	public void onCreate() {	

		_application = this;
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			
		_preferenceListener = new OnSharedPreferenceChangeListener() {
			
			@Override
			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
					String key) {

				if(key.equalsIgnoreCase(YambaPreference.SERVICEURI_PREFERENCE)) {
					
					TwitterAsync.setServiceUri(sharedPreferences.getString(key, null));
					
				} else if(key.equalsIgnoreCase(YambaPreference.USERNAME_PREFERENCE)) {
					
					TwitterAsync.setUsername(sharedPreferences.getString(key, null));
					
				} else if(key.equalsIgnoreCase(YambaPreference.PASSWORD_PREFERENCE)) {
					
					TwitterAsync.setPassword(sharedPreferences.getString(key, null));
					
				}
				
			}
		};
		
		preferences.registerOnSharedPreferenceChangeListener(_preferenceListener);
		
		TwitterAsync.setServiceUri(PreferenceManager.getDefaultSharedPreferences(this).getString(YambaPreference.SERVICEURI_PREFERENCE, null));
		TwitterAsync.setUsername(PreferenceManager.getDefaultSharedPreferences(this).getString(YambaPreference.USERNAME_PREFERENCE, null));
		TwitterAsync.setPassword(PreferenceManager.getDefaultSharedPreferences(this).getString(YambaPreference.PASSWORD_PREFERENCE, null));
		
		final Context context = this;
		TwitterAsync.setTwitterExceptionListener(new TwitterExceptionListener() {
			
			@Override
			public void onExceptionThrown(TwitterException e) {
				
				boolean showSettings = false;
				
				if(e instanceof BadCredentialsException) {
					showSettings = true;
					Toast.makeText(context, R.string.badCredentials, Toast.LENGTH_LONG);
				} else if(e instanceof InvalidAPIException) {
					showSettings = true;
					Toast.makeText(context, R.string.invalidApi, Toast.LENGTH_LONG);
				}
				else {
					Toast.makeText(context, R.string.errorMessage, Toast.LENGTH_SHORT);					
				}
				
				if(showSettings) {
					Intent intent = new Intent(context, YambaPreference.class);			
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
			}
		});
	}
	
}
