package com.example.spellingbee;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class FileActivity extends Activity {

	FileListAdapter adapter;
	ListView lv;
	ArrayList<String> files;
	Button b;
	Button quickList;
	EditText e;
	MediaPlayer mp;
	int quickListLen;
	String currQuickList;
	String currQuickListName;
	AlertDialog.Builder dlgAlert;
	File qDir;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file);
		mp=MediaPlayer.create(getBaseContext(), R.raw.switch16); 
		b = (Button)findViewById(R.id.button_new_list);
		e = (EditText)findViewById(R.id.editText_new_list);
		lv = (ListView)findViewById(R.id.listView1);
		files = new ArrayList<String>();
		adapter = new FileListAdapter(this,this.files);
		quickList = (Button)findViewById(R.id.QuickList);
		lv.setAdapter(adapter);
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		quickListLen = Integer.parseInt(sharedPref.getString("quickListLength","10"));
		System.out.println(quickListLen);
		final File dirD = new File(Environment.getExternalStorageDirectory().getPath()+"/"+getPackageName()+"/");
		if(dirD.exists() && dirD.isDirectory()){
			//Get files from directory
			getFileNames(dirD);
			
		}
		else{
			//Build the directory
			dirD.mkdirs();
		}
		
		
		this.e.setOnKeyListener(new OnKeyListener()
		{  
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(event.getAction() == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER))
				{
					mp.start();
					String wordText = e.getText().toString().trim();
				
					if(wordText != null && !wordText.equals(""))
					{
						File dir = new File(Environment.getExternalStorageDirectory().getPath()+"/"+getPackageName()+"/" + wordText + ".txt");
						if(!dir.exists()){
						files.add(wordText);}
					}
					else{
						
					}
					e.setText("");
					return true;
				}
				if(event.getAction() == KeyEvent.ACTION_UP && (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER))
				{
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(e.getWindowToken(), 0);
					return true;
				}
				return false;
			}
		});	
		
		this.b.setOnClickListener(new OnClickListener() {
			public void onClick(View view)
			{
				mp.start();
				String wordText = e.getText().toString().trim().replace("/", "-");;

				if(wordText != null && !wordText.equals(""))
				{
					File dir = new File(Environment.getExternalStorageDirectory().getPath()+"/"+getPackageName()+"/" + wordText + ".txt");
					if(!dir.exists()){
					files.add(wordText);}
				}
				else{
					
				}
				e.setText("");
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(e.getWindowToken(), 0);
			}
		});
		
		this.quickList.setOnClickListener(new OnClickListener() {
			public void onClick(View view)
			{
				
				
				
				currQuickList = "1stGrade";
				
				
				
				mp.start();
				onCreateDialogSingleChoice();

				
				
				e.setText("");				
			}
		});
		
	}
	
	public String packageWords2(ArrayList<String> words){
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
	
	public void getFileNames(File d){
		files.clear();
		File[] listOfFiles = d.listFiles();
	    for (int i = 0; i < listOfFiles.length; i++) {
	      if (listOfFiles[i].isFile()) {
	        System.out.println("File " + listOfFiles[i].getName()); 
	        files.add(listOfFiles[i].getName().replaceAll(".txt",""));
	      } else if (listOfFiles[i].isDirectory()) {
	        System.out.println("Directory " + listOfFiles[i].getName());
	      }
	    }
	    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.file, menu);
		return true;
	}
	
	public int[] createOrderedList(int leng,int n){
	ArrayList<Integer> l = new ArrayList<Integer>();
	//ArrayList<Integer> sorted = new ArrayList<Integer>();
	int[] arr = new int[n];
	Random rand = new Random();
		for(int i = 0; i < leng; i++){
			l.add(i);
		}
		for (int c = 0; c< n;c++)
		{
		   int random = rand.nextInt(l.size());
		   arr[c]=(l.remove(random));
		}
		Arrays.sort(arr);
		
		return arr;
		
	}
	
	public void onCreateDialogSingleChoice() {
		final String[] listNames={"SAT List", "1st Grade"};
		final String[] items = {"SatList1","1stGrade"};

		new AlertDialog.Builder(this)
        .setSingleChoiceItems(listNames, 0, null)
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	
                dialog.dismiss();
                int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                System.out.println(items[selectedPosition]);
                currQuickList = items[selectedPosition];
				currQuickListName = listNames[selectedPosition];
                createQuickList();
                
                // Do something useful withe the position of the selected radio button
            }
        })
        .show();		
		}

	public void createQuickList(){
		
		 AsyncTask<Void,Void,ArrayList<String>> task = new AsyncTask<Void, Void, ArrayList<String>>() {
				
				@Override
				protected void onPreExecute() {
					String wordText = currQuickListName + " QuickList";
					files.remove(wordText);
					File f = new File( Environment.getExternalStorageDirectory().getPath()+"/"+"com.example.spellingbee"+"/" + wordText + ".txt");
					f.delete();	
					
					if(wordText != null && !wordText.equals(""))
					{
						qDir = new File(Environment.getExternalStorageDirectory().getPath()+"/"+getPackageName()+"/" + wordText + ".txt");
						if(!qDir.exists()){
						files.add(wordText);
						adapter.notifyDataSetChanged();}
					}
					else{
					cancel(true);
					}
				}
					
				@Override
				protected ArrayList<String> doInBackground(Void... arg0) {
					ArrayList<String> words = new ArrayList<String>(); 
					try{ 
						 URL url = new URL("http://users.csc.calpoly.edu/~cpauley/spellingBeeAndroid/" + currQuickList + ".txt");
					       URLConnection urlConnection = url.openConnection();
					       urlConnection.setUseCaches(false);
					       int i = 0;  
					       
					           // Read all the text returned by the server
					   BufferedReader in = new BufferedReader(new InputStreamReader(
					            urlConnection.getInputStream()));
					    String str;
					    int count = Integer.parseInt(in.readLine());
					    int[] a = createOrderedList(count,quickListLen);
					    int d = 0;
					    int g = 0;
					    while ((str = in.readLine()) != null && d < a.length) {
					        // str is one line of text; readLine() strips the newline
					        // character(s)
					    	
					    	if(a[d] == g)
					        {words.add(str);
					        d++;
					        }
					    	g++;
					        
					    }
					 }  
					 
					 catch(Exception e){
				        	e.printStackTrace();
				        	errorDialog(e);
				        }					       
					return words;
				}
				
				@Override
				protected void onPostExecute(ArrayList<String> words) {
					try{
					OutputStream fo = new FileOutputStream(qDir);  
				     PrintWriter p = new PrintWriter(fo);
				     p.print(packageWords2(words) +"\n");
				     
				     p.close();
				     fo.close();
				     
				     System.out.println("file created: "+qDir);
				     //adapter.notifyDataSetChanged();
				     //files.add(wordText);
					}
					 catch(Exception e){
				        	e.printStackTrace();
				        	errorDialog(e);
				        }						
			        
			       
				}
					
			};
			task.execute((Void[])null);	 	
	}
	
	protected void errorDialog(Exception e){
		dlgAlert = new AlertDialog.Builder(this);
		String message = e.getMessage();
		
		dlgAlert.setMessage(message);
		dlgAlert.setTitle("SQuickListError");
		dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               //wordEdit.setText("");
	           }});
		dlgAlert.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               //wordEdit.setText("");
	           }});
		dlgAlert.setCancelable(true);
		dlgAlert.create().show();	
		}
	
	public static boolean isConnectingToInternet(Context context) {
	    ConnectivityManager connectivity = (ConnectivityManager) context
	            .getSystemService(Context.CONNECTIVITY_SERVICE);
	    if (connectivity != null) {
	        NetworkInfo[] info = connectivity.getAllNetworkInfo();
	        if (info != null)
	            for (int i = 0; i < info.length; i++)
	                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
	                    return true;
	                }

	    }
	    return false;
	}
}
