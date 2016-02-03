package com.example.spellingbee;

import java.net.URL;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Button;

public class WordView extends LinearLayout implements TextToSpeech.OnInitListener {

	protected String word;
	String st;
	String audioURL;
	protected int id;
	protected TextView text;
	protected Button dButton;
	protected TextToSpeech tts;
	AlertDialog.Builder dlgAlert;
	ListActivity a;
	
	public WordView(Context context, String w, int i) {
		super(context);
		tts = new TextToSpeech(context,this);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.word_view, this, true);
		this.text = (TextView)findViewById(R.id.text_file);
		this.dButton = (Button)findViewById(R.id.deleteButtonFile);
		setWord(w);
		setId(i);
		final Context c = context;
 
		this.setOnClickListener(new OnClickListener() {
			public void onClick(View view)
			{
				//view.setSelected(true);
				URLReader r = new URLReader(word);
				getDefAndWord(c,r);	
				
				
			}
		});		
		
	}	
	
	public void setWord(String w){
		this.word = w;
		this.text.setText(w);
	}
	
	public void setId(int i){
		this.id = i;
	}
	
	public int getId(){
		return this.id;
	}
	
	public String getWord(){
		return this.word;
	}
	
	public Button getButton(){
		return this.dButton;
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
	
	protected void definitionDialog(final Context c,String message){
    
		

	final MediaPlayer mp=new MediaPlayer();
	mp.setAudioStreamType(android.media.AudioManager.STREAM_ALARM);

	dlgAlert = new AlertDialog.Builder(c);
	dlgAlert.setMessage(message);
	dlgAlert.setTitle("Definition");
	dlgAlert.setPositiveButton("Play Sound",new DialogInterface.OnClickListener() {
        
		public void onClick(DialogInterface dialog, int id) {
	  
           }});
	dlgAlert.setNegativeButton("Close",new DialogInterface.OnClickListener() {
       
		public void onClick(DialogInterface dialog, int id) {
    		try{
    			mp.reset();
    		mp.setDataSource(c,Uri.parse("android.resource://com.example.spellingbee/raw/switch16"));
    		mp.prepare();
    		mp.setVolume(2.0f, 2.0f);
    		mp.start();
    		}
    		catch(Exception e){
    			
    		}
			dialog.dismiss();    
        }});
	//dlgAlert.setCancelable(true);
	final AlertDialog dlg = dlgAlert.create();
	dlg.show();
	dlg.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
    {            
        @Override
        public void onClick(View v)
        {
			if(audioURL.equals("")){

				tts.speak(getWord(), TextToSpeech.QUEUE_FLUSH, null);
				return;
			}

			try{
    			mp.reset();
    		mp.setDataSource(audioURL);
    		mp.prepare();
    		mp.setVolume(2.0f, 2.0f);
    		mp.start();
    		}
    		catch(Exception e){
    			
    		}
            //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
        }
    });
	
    //tts.speak(message, TextToSpeech.QUEUE_FLUSH, null);
	}
		
	public void getDefAndWord(final Context c, final URLReader r){
		 AsyncTask<Void,Void,String[]> task = new AsyncTask<Void, Void, String[]>() {
				
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
				protected String[] doInBackground(Void... arg0) {
					String starr[] = new String[2];
					starr[0] = r.getDefinition();
					starr[1] = r.parseURL();
					return starr;
					
				}
				
				@Override
				protected void onPostExecute(String[] result) {
					/*if (pd!=null) {
						pd.dismiss();
						businesses = result;
						adapter = new BusinessAdapter(context,businesses);
						lv.setAdapter(adapter);
						
						//b.setEnabled(true);
					}*/
					st = result[0];
					System.out.println(result[0]);
					audioURL = result[1];
					definitionDialog(c,result[0]);
				}
					
			};
			task.execute((Void[])null);	 
	}
}
