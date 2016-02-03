package com.example.spellingbee;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends Activity {

	Button b;
	Button b2;
	Button b3;
	Button b4;
	ImageView bee;
	TextView title;
	MediaPlayer mp;
	String use_tts;
	SharedPreferences s;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		bee = (ImageView)findViewById(R.id.imageView1);
		b = (Button)findViewById(R.id.deleteButtonFile);
		b2 = (Button)findViewById(R.id.settingsButton);
		b3 = (Button)findViewById(R.id.button3);
		b4 = (Button)findViewById(R.id.creditsButton);
        title = (TextView)findViewById(R.id.textView1);

		mp = MediaPlayer.create(getBaseContext(), R.raw.switch16);
		initAnimations();
		
		initWordListeners();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	public void initAnimations(){	
		
		final AnimationSet as2 = new AnimationSet(true);
		final Animation animation2 = new ArcTranslateAnimation(0, 120,0, 0);
		animation2.setDuration(3001);
		as2.addAnimation(animation2);

		Animation animation3 = new ArcTranslateAnimation(0, -120,0, 0);
		animation3.setDuration(3001);
		animation3.setStartOffset(3000);
		as2.addAnimation(animation3);			
		as2.setAnimationListener(new AnimationListener() {
			@Override public void onAnimationStart(Animation animation) {}
			@Override public void onAnimationRepeat(Animation animation) {}
			@Override public void onAnimationEnd(Animation animation) {
				title.startAnimation(as2);
			}
		});	
		
		title.startAnimation(as2);
		
		
			AnimationSet as = new AnimationSet(true);
			Animation animation = new ArcTranslateAnimation(0, -120,0, 0);
			animation.setDuration(3001);
			as.addAnimation(animation);

			Animation animation1 = new ArcTranslateAnimation(0, 120,0, 0);
			animation1.setDuration(3001);
			animation1.setStartOffset(3000);
			as.addAnimation(animation1);			
			as.setAnimationListener(new AnimationListener() {
				@Override public void onAnimationStart(Animation animation) {}
				@Override public void onAnimationRepeat(Animation animation) {}
				@Override public void onAnimationEnd(Animation animation) {
					bee.startAnimation(animation);
				}
			});
			bee.startAnimation(as);
			
			
			
			
			//spellWords.startAnimation(animation);
			//wordEdit.startAnimation(animation2);
			//lv.startAnimation(animation3);		
		
	}
	
	protected void credits()
	{
		AlertDialog.Builder dlgAlert;
		
		dlgAlert = new AlertDialog.Builder(HomeActivity.this);
		dlgAlert.setTitle("Credits");
		String str = getString(R.string.credits);
		dlgAlert.setMessage(str);
		dlgAlert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
	        
			public void onClick(DialogInterface dialog, int id) {
		       dialog.dismiss();
	           }});
		//dlgAlert.setCancelable(true);
		AlertDialog dlg = dlgAlert.create();
		dlg.show();
	}
	
	protected void initWordListeners() {
		
		this.b.setOnClickListener(new OnClickListener() {
			public void onClick(View view)
			{
				mp.start();
				android.content.Intent i;
				   i = new android.content.Intent(getBaseContext(),FileActivity.class);
				   i.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
				   getBaseContext().startActivity(i);}
			
		});
		
		this.b2.setOnClickListener(new OnClickListener() {
			public void onClick(View view)
			{
				mp.start();
				android.content.Intent i;
				   i = new android.content.Intent(getBaseContext(),SettingsActivity.class);
				   i.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
				   getBaseContext().startActivity(i);}
			
		});	
		
		this.b3.setOnClickListener(new OnClickListener() {
			public void onClick(View view)
			{
				mp.start();
				android.content.Intent i;
				   i = new android.content.Intent(getBaseContext(),FullscreenActivity.class);
				   i.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
				   getBaseContext().startActivity(i);}
			
		});
		
		this.b4.setOnClickListener(new OnClickListener() {
			public void onClick(View view)
			{
				mp.start();
                credits();
			}
			
		});	
		
	}	

}
