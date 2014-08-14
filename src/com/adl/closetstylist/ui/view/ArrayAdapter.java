package com.adl.closetstylist.ui.view;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adl.closetstylist.R;
import com.adl.closetstylist.ui.utils.PixelUtils;

public class ArrayAdapter<T> extends android.widget.ArrayAdapter<T> {

	public ArrayAdapter(Context context, int resource, int textViewResourceId,
			List<T> objects) {
		super(context, resource, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
	}

	public ArrayAdapter(Context context, int resource, int textViewResourceId,
			T[] objects) {
		super(context, resource, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
	}

	public ArrayAdapter(Context context, int resource, int textViewResourceId) {
		super(context, resource, textViewResourceId);
		// TODO Auto-generated constructor stub
	}

	public ArrayAdapter(Context context, int textViewResourceId, List<T> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
	}

	public ArrayAdapter(Context context, int resource, T[] objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
	}

	public ArrayAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = super.getView(position, convertView, parent);

		if (position == getCount()) {
			((TextView) v.findViewById(android.R.id.text1)).setText("");
			((TextView) v.findViewById(android.R.id.text1))
					.setHint((CharSequence) getItem(getCount())); // "Hint to be displayed"
		}
		((TextView) v.findViewById(android.R.id.text1))
				.setTextColor(getContext().getResources().getColor(
						R.color.register_textcolor));
		((TextView) v.findViewById(android.R.id.text1))
				.setHintTextColor(getContext().getResources().getColor(
						R.color.register_texthintcolor));
		((TextView) v.findViewById(android.R.id.text1)).setTextSize(PixelUtils
				.pixelsToSp(getContext(), getContext().getResources()
						.getDimension(R.dimen.register_textsize)));
		return v;
	}

	@Override
	public int getCount() {
		return super.getCount() - 1; // you dont display last item. It is used
										// as hint.
	}

}
