package com.adl.closetstylist.ui.navigationdrawer;


import com.adl.closetstylist.ui.ActionDescriptor;
import com.adl.closetstylist.ui.view.ActionItemView;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

public class ActionArrayAdapter extends BaseAdapter {

	private ActionDescriptor[] actionDescriptors;
	private Context context;

	public ActionArrayAdapter(Context context, ActionDescriptor[] actionDescriptors) {
		if (context == null)
			throw new IllegalArgumentException("context cannot be null");
		if (actionDescriptors == null || actionDescriptors.length == 0)
			throw new IllegalArgumentException("array cannot be null or empty");
		
		this.actionDescriptors = actionDescriptors;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return actionDescriptors.length;
	}

	@Override
	public Object getItem(int position) {
		if (position < getCount())
			return actionDescriptors[position];
		return null;
	}

	@Override
	public long getItemId(int position) {
		return ((ActionDescriptor)getItem(position)).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Object item = getItem(position);
		if (item == null || !(item instanceof ActionDescriptor))
			return null;
		
		ActionDescriptor actionDescriptor = (ActionDescriptor) item;
		
		ActionItemView actionItemView = null;
		if (convertView != null) {
			actionItemView = (ActionItemView) convertView;
		} else {
			actionItemView = new ActionItemView(context);
		}
		actionItemView.setActionDescriptor(actionDescriptor);
		
		return actionItemView;
	}
	
	public boolean hasStableIds() {
		return true;
	}

}
