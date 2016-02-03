package com.example.spellingbee;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;


public class WordsListAdapter extends BaseAdapter {

	/** The application Context in which this JokeListAdapter is being used. */
	private Context m_context;

	/** The data set to which this JokeListAdapter is bound. */
	private List<String> m_wordList;

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
	public WordsListAdapter(Context context, List<String> wList) {
		this.m_context = context;
		this.m_wordList = wList;
	}

	public int getCount() {
		return this.m_wordList.size();
	}

	public Object getItem(int position) {
		return this.m_wordList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		WordView wView = null;
		
		if (convertView == null) {
			wView = new WordView(m_context, this.m_wordList.get(position),position);
		}
		else {
			wView = (WordView)convertView;
		}
		wView.setWord(this.m_wordList.get(position));
		final String s = wView.getWord();
		wView.getButton().setOnClickListener(new OnClickListener() {
			public void onClick(View view)
			{
				deleteWord(s);
			}
		});
		return wView;
	}
	
	public void deleteWord(String w){
		m_wordList.remove(w);
		this.notifyDataSetChanged();
	}
}
