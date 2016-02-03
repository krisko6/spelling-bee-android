package com.example.spellingbee;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;


public class FileListAdapter extends BaseAdapter {

	/** The application Context in which this JokeListAdapter is being used. */
	private Context m_context;

	/** The data set to which this JokeListAdapter is bound. */
	private List<String> m_fileList;

	/**
	 * Parameterized constructor that takes in the application Context in which
	 * it is being used and the Collection of Joke objects to which it is bound.
	 * m_nSelectedPosition will be initialized to Adapter.NO_SELECTION.
	 * 
	 * @param context
	 *            The application Context in which this JokeListAdapter is being
	 *            used.
	 * 
	 * @param jokeList
	 *            The Collection of Joke objects to which this JokeListAdapter
	 *            is bound.
	 */
	public FileListAdapter(Context context, List<String> wList) {
		this.m_context = context;
		this.m_fileList = wList;
	}

	public int getCount() {
		return this.m_fileList.size();
	}

	public Object getItem(int position) {
		return this.m_fileList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		FileView wView = null;
		
		if (convertView == null) {
			wView = new FileView(m_context, this.m_fileList.get(position),position);
		}
		else {
			wView = (FileView)convertView;
		}
		wView.setfName(this.m_fileList.get(position));
		final String s = wView.getfName();
		wView.getButton().setOnClickListener(new OnClickListener() {
			public void onClick(View view)
			{
				deleteFile(s);
			}
		});
		return wView;
	}
	
	public void deleteFile(String w){
		m_fileList.remove(w);
		File f = new File( Environment.getExternalStorageDirectory().getPath()+"/"+"com.example.spellingbee"+"/" + w + ".txt");
		f.delete();
		this.notifyDataSetChanged();
	}
}
