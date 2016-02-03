package com.example.spellingbee;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View.OnFocusChangeListener;


public class ListActivity extends Activity {

	ArrayList<String> words;
	ListView lv;
	WordsListAdapter adapter;
	Button addWord;
	Button spellWords;
	Button shuffleButton;
	Button saveWordsButton;
	EditText wordEdit;
	AlertDialog.Builder dlgAlert;
	Boolean askOnError;
	String fName;
	MediaPlayer mp;
	static String foundST;
	static String capST;
	
	protected static final String SAVED_WORDS_VALUE = "words";	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
			

		addWord = (Button)findViewById(R.id.addWordButton);
		spellWords = (Button)findViewById(R.id.spellWordsButton);
		shuffleButton = (Button) findViewById(R.id.shuffleButton);
		saveWordsButton = (Button) findViewById(R.id.saveWordsButton);
		wordEdit = (EditText)findViewById(R.id.newWordEditText);
		wordEdit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		//wordEdit.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		dlgAlert  = new AlertDialog.Builder(this);			
		words = new ArrayList<String>();
		adapter = new WordsListAdapter(this,this.words);
		mp=MediaPlayer.create(getBaseContext(), R.raw.switch16); 
		SharedPreferences s = this.getPreferences(MODE_PRIVATE);
		android.os.Bundle b = this.getIntent().getExtras();
		if(b.getString("fname") != null){
			fName = b.getString("fname");
			setTitle(fName);
		}
		
		
		//initializeWords();
		this.lv = (ListView)this.findViewById(R.id.wordListView);
		this.lv.setAdapter(adapter);

		animateIntro();
		initWordListeners();

	}
	
	@Override
	protected void onPause(){
		super.onPause();
		SharedPreferences s = this.getPreferences(MODE_PRIVATE);
		Editor e = s.edit();
		e.putBoolean("askOnError",askOnError);
		e.commit();
		packageWordsFile(fName);
	}
	
	public void initializeWords(){
		
		int i = 0;
		while(i < words.size()){
			addWord(words.get(i));
			i++;
		}
		
		
		
	}
	
	
	
	public void animateIntro(){
		Animation animation = new TranslateAnimation(-500, 0,0, 0);
		animation.setDuration(1000);
		Animation animation2 = new TranslateAnimation(500,0,0,0);
		animation2.setDuration(2000);
		Animation animation3 = new ScaleAnimation(0.0f,1.0f,0.0f,1.0f);
		animation3.setDuration(3000);	
		addWord.startAnimation(animation);
		spellWords.startAnimation(animation);
		wordEdit.startAnimation(animation2);
		lv.startAnimation(animation3);		
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		words.clear();
		unpackWordsFile(fName);
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		askOnError = sharedPref.getBoolean("askTTS_checkbox", true);
		//initializeWords();
        animateIntro();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list, menu);
		return true;
	}
	
	public void addWord(final String word){
		words.add(word);
		this.adapter.notifyDataSetChanged();
		WordView w = (WordView)lv.getChildAt(words.size()-1);
		/*w.getButton().setOnClickListener(new OnClickListener() {
			public void onClick(View view)
			{
				deleteWord(word);
			}
		});*/
		if (w != null)
		{
			Button b = w.getButton();
		}
		
	}
	
	public void shuffleList(){
		Collections.shuffle(words);
		this.adapter.notifyDataSetChanged();
	}
	
	public String packageWords(){
		String wordsList="";
		if(words.size() == 0){
			return null;
		}
		int i = 0;
		while(i<words.size()-1){
        wordsList = wordsList + words.get(i).toString() + ",";
			i++;
		}
		wordsList = wordsList + words.get(i).toString();
		return wordsList;
	}
	
	public void packageWordsFile(String fname){
		File file = new File(Environment.getExternalStorageDirectory() + "/" + getPackageName() + "/" + fName + ".txt");
		try {
			file.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//write the bytes in file
		if(file.exists())
		{
		 try{
			OutputStream fo = new FileOutputStream(file);  
		     PrintWriter p = new PrintWriter(fo);
		     p.print(packageWords() +"\n");
		     
		     p.close();
		     fo.close();
		     
		     System.out.println("file created: "+file);
		     
		 }
		 catch(Exception e){
			 return;
		 }
		}               
	}
	
	public void unpackWords(String list){
		
	   Scanner scan = new Scanner(list).useDelimiter(",");
	   while(scan.hasNext()){
		   addWord(scan.next());
	   }
	   return;
	}
	
	public void unpackWordsFile(String fname){	
		File file = new File(Environment.getExternalStorageDirectory() + "/" + getPackageName() + "/" + fName + ".txt");	
	FileInputStream s;
	try {
		s = new FileInputStream(file);
	} catch (FileNotFoundException e1) {
		// TODO Auto-generated catch block
		return;
	}
	
    Scanner scan = new Scanner(s);
    String l = scan.nextLine();
    System.out.print (l);
    unpackWords(l);
    try {
		s.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
     
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState){
		
		outState.putString(SAVED_WORDS_VALUE,packageWords());
		//packageWordsFile(fName);
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState){
		
		String w = "";
		if(savedInstanceState.containsKey(SAVED_WORDS_VALUE) && savedInstanceState.getString(SAVED_WORDS_VALUE) != null)
		{w = savedInstanceState.getString(SAVED_WORDS_VALUE);}
		//words.clear();
		//unpackWordsFile(fName);
		
		super.onRestoreInstanceState(savedInstanceState);
	}
	
	protected void errorDialog(final String w){
		
		if(askOnError == true && URLReader.findWord(w).equals("")){

			dlgAlert.setMessage("Error getting pronunciation. Would you like to use text to speech to pronounce this word?");
			dlgAlert.setTitle("Spelling Bee");
			dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               if(!w.equals(""))
		        	   {addWord(w);}
		               //wordEdit.setText("");
		           }});
			dlgAlert.setNegativeButton("Cancel",null);
			dlgAlert.setCancelable(true);
			dlgAlert.create().show();					
		}
		else{
			if(!w.equals("")){
			addWord(w);
			}
		}
	}

	protected void addWord2(String wordText){
	
		findWord2(wordText);
	}
	
	protected void errorDialog2(final String w){
	    
		dlgAlert.setMessage("Is this word a proper name?");
			dlgAlert.setTitle("Spelling Bee");
			dlgAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               if(!w.equals(""))
		        	   {
		                 addWord2(w);
		        	   }
		               //wordEdit.setText("");
		           }});
			dlgAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               if(!w.equals(""))
		        	   {
		                 addWord2(w.toLowerCase());
		        	   }
		               //wordEdit.setText("");
		           }
	        });
			
			dlgAlert.setCancelable(true);
			dlgAlert.create().show();	
			
	}	
	

	
	protected void initWordListeners() {
		this.addWord.setOnClickListener(new OnClickListener() {
			public void onClick(View view)
			{
				mp.start();
				
				String wordText = wordEdit.getText().toString().toLowerCase().trim();
				if(!wordEdit.getText().toString().trim().equals(wordText)){
				   
					spellWords.setVisibility(1);					
					errorDialog2(wordEdit.getText().toString().trim());
					wordEdit.setText("");
					//InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					//imm.hideSoftInputFromWindow(wordEdit.getWindowToken(), 0);
					spellWords.setVisibility(0);
				}else{
					spellWords.setVisibility(1);
					addWord2(wordText);
					wordEdit.setText("");
					//InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					//imm.hideSoftInputFromWindow(wordEdit.getWindowToken(), 0);
					spellWords.setVisibility(0);	   
				}
			
			}
		});
		
		this.spellWords.setOnClickListener(new OnClickListener() {
			public void onClick(View view)
			{
				mp.start();
				//InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				//imm.hideSoftInputFromWindow(wordEdit.getWindowToken(), 0);

				if(words.size() > 0){

				android.content.Intent i;
				   i = new android.content.Intent(getBaseContext(),MainActivity.class);
				   i.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
				   i.putExtra("words", packageWords());
				   packageWordsFile(fName);
				   getBaseContext().startActivity(i);}
			}
		});
		
		this.shuffleButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view)
			{
				mp.start();
				shuffleList();
			}
		});
		
		this.spellWords.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if (arg1) {
					spellWords.setVisibility(View.INVISIBLE);
				} else {
					spellWords.setVisibility(View.INVISIBLE);
				}
			}
		});

		this.saveWordsButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				packageWordsFile(fName);
				finish();
			}
		});
		
		this.wordEdit.setOnKeyListener(new OnKeyListener()
		{
			public boolean onKey(View v, int keyCode, KeyEvent event) {


				if(event.getAction() == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER))
				{
					mp.start();

					String wordText = wordEdit.getText().toString().toLowerCase().trim();
					if(!wordEdit.getText().toString().trim().equals(wordText)){

						spellWords.setVisibility(1);
						errorDialog2(wordEdit.getText().toString().trim());
						wordEdit.setText("");
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(wordEdit.getWindowToken(), 0);
						spellWords.setVisibility(0);
					}else{
						spellWords.setVisibility(1);
						addWord2(wordText);
						wordEdit.setText("");
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(wordEdit.getWindowToken(), 0);
						spellWords.setVisibility(0);
					}
                    return true;
				}
				return false;
			}
		});
	}	

    public void findWord2(final String wordText){
        
    	foundST = "";
    	
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
					return URLReader.findWord(wordText);
				
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
				
				ListActivity.foundST = result;
				System.out.println(foundST);
				if(wordText != null && !wordText.equals("") && !foundST.equals(""))
				{
					addWord(wordText);
				}
				else{
					errorDialog(wordText);
				}  
			}
				
		};
		task.execute((Void[])null);	
		
    }	
	
	public void deleteWord(String word){
		this.words.remove(word);
		this.adapter.notifyDataSetChanged();
	}

}
