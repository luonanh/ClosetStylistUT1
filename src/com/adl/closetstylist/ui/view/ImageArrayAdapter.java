package com.adl.closetstylist.ui.view;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adl.closetstylist.ItemColorEnum;
import com.adl.closetstylist.R;
import com.adl.closetstylist.ui.EditItemActivity.SpinnerValue;
import com.adl.closetstylist.ui.utils.PixelUtils;

public class ImageArrayAdapter<T> extends android.widget.ArrayAdapter<T> {

	public ImageArrayAdapter(Context context, int resource, int textViewResourceId,
			List<T> objects) {
		super(context, resource, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
	}

	public ImageArrayAdapter(Context context, int resource, int textViewResourceId,
			T[] objects) {
		super(context, resource, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
	}

	public ImageArrayAdapter(Context context, int resource, int textViewResourceId) {
		super(context, resource, textViewResourceId);
		// TODO Auto-generated constructor stub
	}

	public ImageArrayAdapter(Context context, int textViewResourceId, List<T> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
	}

	public ImageArrayAdapter(Context context, int resource, T[] objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
	}

	public ImageArrayAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = null;
		if (convertView == null) {
			v = LayoutInflater.from(getContext()).inflate(R.layout.simple_image_spinner_dropdown_item, parent, false);
		} else {
			v = convertView;
		}

		TextView textView = (TextView) v.findViewById(android.R.id.text1);
		ImageView image = (ImageView) v.findViewById(android.R.id.icon);
		if (position == getCount()) {
			textView.setText("");
			textView.setHint((CharSequence) getItem(getCount())); // "Hint to be displayed"
			image.setVisibility(View.GONE);
		} else {
			textView.setText((String) getItem(position));
			
			int colorImageResourceId = ItemColorEnum.valueOf((String) getItem(position)).getColorImageResourceId();
			if (colorImageResourceId > 0) {
				image.setImageResource(colorImageResourceId);
				image.setVisibility(View.VISIBLE);
			}
			else
				image.setVisibility(View.GONE);
		}
		textView
				.setTextColor(getContext().getResources().getColor(
						R.color.register_textcolor));
		textView
				.setHintTextColor(getContext().getResources().getColor(
						R.color.register_texthintcolor));
		textView.setTextSize(PixelUtils
				.pixelsToSp(getContext(), getContext().getResources()
						.getDimension(R.dimen.register_textsize)));
		return v;
	}
	
	@Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

	@Override
	public int getCount() {
		return super.getCount() - 1; // you dont display last item. It is used
										// as hint.
	}

}
