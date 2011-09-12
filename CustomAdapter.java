package com.schen.pop;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {

	private final Context context;
	String strings[] = new String[10];
	Typeface typeface;
	ListView lv;

	public CustomAdapter(Context context, String s[], ListView lv) {
	    this.context = context;
	    this.strings = s;
	    this.lv = lv;
	    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/font.ttf");
	}
	public int getCount() {return strings.length;}
	public Object getItem(int position) {return strings[position];}
	public long getItemId(int position) {return position;}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    String ss = strings[position];
	    View child = View.inflate(context, R.layout.list_item, null);
	    RelativeLayout rl = (RelativeLayout)child.findViewById(R.id.list_rl);
	    TextView tvName = (TextView)child.findViewById(R.id.name_adapter);
	    TextView tvScore = (TextView)child.findViewById(R.id.score_adapter);
	    tvName.setText(ss.substring(0, ss.indexOf("_"))); // use whatever method you want for the label
	    tvScore.setText(ss.substring(ss.indexOf("_") + 1));
	    tvName.setTypeface(typeface);
	    tvScore.setTypeface(typeface);
	    return rl;
	}
}