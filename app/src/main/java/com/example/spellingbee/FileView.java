package com.example.spellingbee;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Button;

public class FileView extends LinearLayout {

	protected String fName;
	protected MediaPlayer mp;
	protected int id;
	protected TextView text;
	protected Button dButton;
	ListActivity a;
	
	public FileView(Context context, String w, int i) {
		super(context);
		
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.file_view, this, true);
		this.text = (TextView)findViewById(R.id.text_file);
		this.dButton = (Button)findViewById(R.id.deleteButtonFile);
		mp=MediaPlayer.create(context, R.raw.switch16); 
		setfName(w);
		setId(i);
		final Context c = context;
		this.setOnClickListener(new OnClickListener() {
			public void onClick(View view)
			{
				/*InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(wordEdit.getWindowToken(), 0);
				
				if(words.size() > 0){
				
				android.content.Intent i;
				   i = new android.content.Intent(getBaseContext(),MainActivity.class);
				   i.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
				   i.putExtra("words",packageWords());
				   packageWordsFile(fName);
				   getBaseContext().startActivity(i);}*/
				//view.setSelected(true);
				mp.start();
				android.content.Intent i;
				   i = new android.content.Intent(c,ListActivity.class);
				   i.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
				   i.putExtra("fname",fName);
				   c.startActivity(i);				
				
			}
		});
	}	
	
	public void setfName(String w){
		this.fName = w;
		this.text.setText(w);
	}
	
	public void setId(int i){
		this.id = i;
	}
	
	public int getId(){
		return this.id;
	}
	
	public String getfName(){
		return this.fName;
	}
	
	public Button getButton(){
		return this.dButton;
	}
		
	
}
