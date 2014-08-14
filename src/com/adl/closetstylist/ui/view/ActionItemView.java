package com.adl.closetstylist.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adl.closetstylist.R;
import com.adl.closetstylist.ui.ActionDescriptor;

public class ActionItemView extends RelativeLayout {

	
	private ActionDescriptor actionDescriptor;

	public ActionItemView(Context context) {
		this(context, null);
	}
	
	public ActionItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		LayoutInflater.from(context).inflate(R.layout.action_item, this, true);
		
		/**
		 * do not set this clickable to true, it will make the item unclickable when the view is populated
		 * to a list view
		 */
//        this.setClickable(true);
	}

	public ActionItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public void setActionDescriptor(ActionDescriptor actionDescriptor) {
		this.actionDescriptor = actionDescriptor;
		TextView label = (TextView) findViewById(R.id.action_label);
		ImageView icon = (ImageView) findViewById(R.id.action_icon);
		
		label.setText(actionDescriptor.getLabelResourceId());
		icon.setImageResource(actionDescriptor.getIconResourceId());
	}
	
	public ActionDescriptor getActionDescriptor() {
		return this.actionDescriptor;
	}

}
