package com.adl.closetstylist.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adl.closetstylist.ImageSubSampler;
import com.adl.closetstylist.ItemData;
import com.adl.closetstylist.R;

public class ImageAndTwoLineItem extends LinearLayout {

    private int iconVisibility;
    private TextView mainText;
    private TextView subText;
	private ImageView image;

    public ImageAndTwoLineItem(Context context) {
        this(context, null);
    }

    public ImageAndTwoLineItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        LayoutInflater.from(context).inflate(R.layout.image_and_2_line_item, this, true);
//        this.setClickable(true);
//        this.setBackgroundResource(android.R.drawable.list_selector_background);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ImageAndTwoLineItem, 0, 0);

        String text = array.getString(R.styleable.ImageAndTwoLineItem_main_text);
        if (text == null)
            text = "NULL";
        setMainText((TextView) findViewById(R.id.mainText));
        getMainText().setText(text);

        text = array.getString(R.styleable.ImageAndTwoLineItem_sub_text);
        if (text == null)
            text = "NULL";
        setSubText((TextView) findViewById(R.id.subText));
        getSubText().setText(text);
        
        setImageView((ImageView) findViewById(R.id.icon));

        array.recycle();

    }

	public ImageView getImageView() {
    	return this.image;
	}
    
    private void setImageView(ImageView image) {
    	this.image = image;
	}
    
    public void setImage(Drawable image) {
    	this.getImageView().setImageDrawable(image);
    }

	public void setMainText(String text) {
        if (text == null)
            text = "";
        mainText.setText(text);
    }

    public void setSubText(String text) {
        if (text == null)
            text = "";
        subText.setText(text);
    }
    
    public void setSubTextVisibility(int visibility) {
    	View subText = findViewById(R.id.subText);
        if (subText.getVisibility() == visibility)
            return;

        subText.setVisibility(iconVisibility);
    }

    public void setIconVisibility(int visibility) {
        if (this.getIconVisibility() == visibility)
            return;

        iconVisibility = visibility;

        ImageView icon = (ImageView) findViewById(R.id.icon);

        icon.setVisibility(iconVisibility);
    }

    public int getIconVisibility() {
        return iconVisibility;
    }

	public TextView getMainText() {
		return mainText;
	}

	private void setMainText(TextView mainText) {
		this.mainText = mainText;
	}

	public TextView getSubText() {
		return subText;
	}

	private void setSubText(TextView subText) {
		this.subText = subText;
	}
	
	public void setBlackTheme(boolean setBlack){
		if (setBlack) {
//			this.setBackgroundResource(R.drawable.list_black_background_selector);
	        getMainText().setTextColor(getContext().getResources().getColor(android.R.color.primary_text_dark));
	        getSubText().setTextColor(getContext().getResources().getColor(android.R.color.secondary_text_dark));
		} else {
//			this.setBackgroundResource(R.drawable.list_white_background_selector);
	        getMainText().setTextColor(getContext().getResources().getColor(android.R.color.primary_text_light));
	        getSubText().setTextColor(getContext().getResources().getColor(android.R.color.secondary_text_light));
		}
	}

	@Override
	public void draw(Canvas canvas) {
//		setBlackTheme((Boolean) AppSettings.getSetting(AppSettings.RESULT_VIEW_BLACKTHEME).getSettingValue());
		super.draw(canvas);
	}

	
//    @Override
//    public void setOnClickListener(OnClickListener l) {
//        findViewById(R.id.clickZone).setOnClickListener(l);
//    }
//    
    

	public void setImage(ItemData item) {
		new ImageSubSampler(this.getContext()).subSampleCroppedUri(item, image, this.getContext());
	}
}
