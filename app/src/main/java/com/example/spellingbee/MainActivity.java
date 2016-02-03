package com.example.spellingbee;

import android.net.Uri;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;


import android.speech.tts.TextToSpeech;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener {

	MediaPlayer mp;
	String audioURL;
	String currWord;
	
	int index;
	ArrayList<String> wordsList;
	ArrayList<String> wordsSpelledList;
	android.widget.Button b;
	android.widget.Button okButton;

	FileInputStream fis;
	URLReader r;
	EditText spellCheck;
	Vibrator vibrate;
	boolean canVibrate;
	boolean randomize;
	TextToSpeech tts;
	Boolean use_tts;
	TextView listLocation;
	TextView compView;
	ImageView jar;
	int a;
	AlertDialog.Builder dlgAlert;
	int wordsCorrect;
	int wordsIncorrect;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);			
		wordsList = new ArrayList<String>();
		wordsSpelledList = new ArrayList<String>();
		a = 200;
		index = 0;
		wordsCorrect = 0;
		wordsIncorrect = 0;
		tts = new TextToSpeech(this, this);
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		use_tts = sharedPref.getBoolean("ask_tts",false);
		randomize = sharedPref.getBoolean("askRandomize_checkbox", true);
		createList();
	    currWord = wordsList.get(index);
		r = new URLReader(currWord);
		getWord();
        	
		
		//audioURL = r.parseURL();
		audioURL = "";
		mp=new MediaPlayer();
		compView= (TextView) findViewById(R.id.compareText);
		b = (android.widget.Button) findViewById(R.id.deleteButtonFile);
		spellCheck = (EditText) findViewById(R.id.editText_new_list);
		spellCheck.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		okButton = (android.widget.Button) findViewById(R.id.button2);
		vibrate = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
		listLocation = (TextView) findViewById(R.id.textView1);
		jar = (ImageView) findViewById(R.id.imageView1);
		jar.setAlpha(a);
		listLocation.setText(index + 1 + "/" + wordsList.size() + " words");
		compView.setText("");
		addListeners();
		Animation animation = new TranslateAnimation(-500, 0,0, 0);
		animation.setDuration(1000);
		Animation animation2 = new TranslateAnimation(201,0,0,0);
		animation2.setDuration(1000);
		Animation animation3 = new TranslateAnimation(0, 0,-500, 0);
		animation3.setDuration(2000);
		mp.setOnPreparedListener(new OnPreparedListener() {
		    @Override
		    public void onPrepared(MediaPlayer mp) {
		        mp.start();
		    }
		}); 
		

		okButton.startAnimation(animation2);
		b.startAnimation(animation);
		spellCheck.startAnimation(animation3);
	    listLocation.startAnimation(animation2);
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		Animation animation = new TranslateAnimation(-500, 0,0, 0);
		animation.setDuration(1000);
		Animation animation2 = new TranslateAnimation(201,0,0,0);
		animation2.setDuration(1000);
		Animation animation3 = new TranslateAnimation(0, 0,-500, 0);
		animation3.setDuration(2000);
		okButton.startAnimation(animation2);
		b.startAnimation(animation);
		spellCheck.startAnimation(animation3);
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		canVibrate = sharedPref.getBoolean("askVibrate", true);
	}
	
	@Override
	protected void onPause(){
		super.onPause();	
		SharedPreferences s = this.getPreferences(MODE_PRIVATE);
		Editor e = s.edit();
		e.putBoolean("askVibrate",canVibrate);
		e.commit();
	}

	protected void getWord(){
	   AsyncTask<Void,Void,String> task = new AsyncTask<Void, Void, String>() {
			
			@Override
			protected void onPreExecute() {
				/*pd = new ProgressDialog(context);
				pd.setTitle("Loading...");
				pd.setMessage("Please wait.");
				pd.setCancelable(false);
				pd.setIndeterminate(true);
				pd.show();*/
			}
				
			@Override
			protected String doInBackground(Void... arg0) {
					return r.parseURL();
				
			}
			
			@Override
			protected void onPostExecute(String result) {
				/*if (pd!=null) {
					pd.dismiss();
					businesses = result;
					adapter = new BusinessAdapter(context,businesses);
					lv.setAdapter(adapter);
					
					//b.setEnabled(true);
				}*/
				audioURL = result;
			}
				
		};
		task.execute((Void[])null);	 	
	}
	
	protected void errorDialog(){
		dlgAlert = new AlertDialog.Builder(this);
		String message = "";
		if(wordsCorrect == wordsList.size()){
		message = "Done with list.\n Words Correct: " + wordsCorrect + "\n Amazing! You spelled all words correct! \n Do you want to try again?";	  	
		}
		else if( wordsCorrect == 0){
		message = "Done with list.\n Words Correct: " + wordsCorrect + "\n Do Better Next Time! \n Do you want to try again?";			
		}
		else {
		message = "Done with list.\n Words Correct: " + wordsCorrect + "\n Words Incorrect: " + wordsIncorrect + "\n Do you want to try again?";	
		}
		
		message = message + "\n";
		
		for(int r = 0; r<wordsList.size(); r++){
			message = message + "\n";
			message = message + wordsSpelledList.get(r) + " --> " + wordsList.get(r);
		}
		
		dlgAlert.setMessage(message);
		dlgAlert.setTitle("Spelling Bee");
		dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               index = -1;
	               wordsCorrect = 0;
	               wordsIncorrect = 0;
	               wordsSpelledList.clear();
	               nextWord();
	               //wordEdit.setText("");
	           }});
		dlgAlert.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
               
	        	   exitAnimation();
	               //wordEdit.setText("");
	           }});
		dlgAlert.setCancelable(true);
		dlgAlert.create().show();					
	
}	
	
	protected void definitionDialog(){
	String message = r.getDefinition();	
	
	dlgAlert = new AlertDialog.Builder(this);
	dlgAlert.setMessage(message);
	dlgAlert.setTitle("Definition");
	dlgAlert.setNegativeButton("OK",new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int id) {
           }});
	dlgAlert.setCancelable(true);
	dlgAlert.create().show();	
	
    //tts.speak(message, TextToSpeech.QUEUE_FLUSH, null);
	}
	
	public void createList(){
		String st = "cat,bat,chat,brat,wookie,supercalifragilisticexpialidocious";

		android.os.Bundle b = this.getIntent().getExtras();
		if(b.getString("words") != null){
			st = b.getString("words");
		}
		
		Scanner scan = new Scanner(st).useDelimiter(",");
		while(scan.hasNext()){
			wordsList.add(scan.next());
		}
		

		if(randomize == true){
			long seed = System.nanoTime();
			Collections.shuffle(wordsList, new Random(seed));	
		}
		
	}	
	
	public void playWord(){
					
		Log.i(audioURL,audioURL);

		if (use_tts){
			tts.speak(currWord, TextToSpeech.QUEUE_FLUSH, null);
			return;
		}

		if(audioURL.equals("")){
	        tts.speak(currWord, TextToSpeech.QUEUE_FLUSH, null);
			return;
		}
		

		
		try{
			mp.reset();
		mp.setDataSource(audioURL);
		mp.setVolume(2.0f, 2.0f);
		mp.prepareAsync();
		
		//mp.start();
		}
		catch(Exception e){

		}
		
		
	}
	
	
	public void playSoundCorrect(){
		mp.setAudioStreamType(android.media.AudioManager.STREAM_ALARM);
		
		try{
			mp.reset();
		mp.setDataSource(this, Uri.parse("android.resource://com.example.spellingbee/raw/ding"));
		mp.prepare();
		mp.setVolume(2.0f, 2.0f);
		mp.start();
		}
		catch(Exception e){
			
		}	
	}
	
	public void playSoundWrong(){
		mp.setAudioStreamType(android.media.AudioManager.STREAM_ALARM);
		
		try{
			mp.reset();
		mp.setDataSource(this, Uri.parse("android.resource://com.example.spellingbee/raw/buzzer"));
		mp.prepare();
		mp.setVolume(2.0f, 2.0f);
		mp.start();
		}
		catch(Exception e){
			
		}	
	}	
	
	public void nextWord(){
		index = index + 1;
		
		if(index < wordsList.size()){
			currWord = wordsList.get(index);
			r.setWord(currWord);
			getWord();
			audioURL = "";
		}
		listLocation.setText(index + 1 + "/" + wordsList.size() + " words");	
		if(index >= wordsList.size()){
			listLocation.setText("Done with list.");
			errorDialog();
		}
	}

	public void exitAnimation(){
    	listLocation.setText("");
		Animation animation = new TranslateAnimation(0, -500, 0, 0);
		animation.setDuration(1000);
		Animation animation2 = new TranslateAnimation(0,800,0,0);
		animation2.setDuration(1000);
		Animation animation3 = new TranslateAnimation(0, 0,0, -500);
		animation3.setDuration(2000);
		okButton.startAnimation(animation2);
		
		b.startAnimation(animation);
		spellCheck.startAnimation(animation3);
		animation.setAnimationListener(new AnimationListener() {
		    public void onAnimationStart(Animation animation) {}
		    public void onAnimationRepeat(Animation animation) {}
		    public void onAnimationEnd(Animation animation) {
		    b.setVisibility(View.GONE);
		    }
		});				
		animation2.setAnimationListener(new AnimationListener() {
		    public void onAnimationStart(Animation animation) {}
		    public void onAnimationRepeat(Animation animation) {}
		    public void onAnimationEnd(Animation animation) {
		    okButton.setVisibility(View.GONE);
		    }
		});			
		animation3.setAnimationListener(new AnimationListener() {
		    public void onAnimationStart(Animation animation) {}
		    public void onAnimationRepeat(Animation animation) {}
		    public void onAnimationEnd(Animation animation) {
		    spellCheck.setVisibility(View.GONE);	
		    MainActivity.this.finish();
		    }
		});	
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK){
	    	exitAnimation();
	    }

	    return false;
	}	
	
	public void addListeners(){
		
		b.setOnClickListener(new android.view.View.OnClickListener() {
			 

			  public void onClick(android.view.View view) {
				  (Toast.makeText(getBaseContext(),"Loading audio...",Toast.LENGTH_SHORT)).show();  
			  playWord();
			 } 
			});
		
		okButton.setOnClickListener(new android.view.View.OnClickListener() {
			 

			  public void onClick(android.view.View view) {
			  
			  String st = spellCheck.getText().toString().trim();
			  if(st != null && !st.equals("")){
				  wordsSpelledList.add(st);
				  if(checkWord(st) == true){
				  spellCheck.setText("");
				  compView.setText("");
				  playSoundCorrect();
				  nextWord();
				  }
				  else
				  {
					  spellCheck.setText("");
					  compView.setText(st + " --> " + currWord);
					  playSoundWrong();
					  if(canVibrate == true){
					  vibrate.vibrate(800);}
					  nextWord();
				  }
			  }
			  
			     //android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager)
					//  getSystemService(android.content.Context.INPUT_METHOD_SERVICE);

					// imm.hideSoftInputFromWindow(spellCheck.getWindowToken(), 0);
					//m_vwJokeEditText.clearFocus()
			 } 
			});
		
		this.spellCheck.setOnKeyListener(new OnKeyListener()
		{  
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(event.getAction() == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER))
				{
					  String st = spellCheck.getText().toString().trim();
					  if(st != null && !st.equals("")){
						  if(checkWord(st) == true){
						  spellCheck.setText("");
						  compView.setText("");
						  playSoundCorrect();
						  nextWord();
						  }
						  else
						  {
							  spellCheck.setText("");
							  compView.setText(st + " --> " + currWord);
							  playSoundWrong();
							  if(canVibrate==true){
							  vibrate.vibrate(800);}
							  nextWord();
						  }
					  }
					  return true;
				}
				if(event.getAction() == KeyEvent.ACTION_UP && (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER ))
				{
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(spellCheck.getWindowToken(), 0);
					return true;
				}
				return false;
			}
		});		
		
	}
	
	@SuppressWarnings("deprecation")
	public boolean checkWord(String check){
		if(currWord.equals(check)){
			wordsCorrect+=1;
			a = (int)(200 / wordsList.size()) * (wordsCorrect);
			Log.i("" + a,"" + a);
			jar.setAlpha(a);
			return true;
		}
		wordsIncorrect+=1;
		return false;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onInit(int arg0) {
		 
        if (arg0 == TextToSpeech.SUCCESS) {
 
            int result = tts.setLanguage(Locale.US);
 
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                Log.e("TTS", "This Language IS supported");
            }
 
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
		
	}

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }	
	
}
