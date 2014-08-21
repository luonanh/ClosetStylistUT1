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

public class OutfitItemView extends LinearLayout {

    private ImageView image1;
    private ImageView image2;
	private ImageView image3;

    public OutfitItemView(Context context) {
        this(context, null);
    }

    public OutfitItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        View view = LayoutInflater.from(context).inflate(R.layout.outfit_item, this, true);
//        this.setClickable(true);
//        this.setBackgroundResource(android.R.drawable.list_selector_background);
        
        image1 = (ImageView) view.findViewById(R.id.image1);
        image2 = (ImageView) view.findViewById(R.id.image2);
        image3 = (ImageView) view.findViewById(R.id.image3);
        
    }

	public ImageView getImage1() {
		return image1;
	}

	public ImageView getImage2() {
		return image2;
	}

	public ImageView getImage3() {
		return image3;
	}
	
	public void showImage1(ItemData item) {
		new ImageSubSampler(this.getContext()).subSampleCroppedUri(item, image1, this.getContext());
	}

	public void showImage2(ItemData item) {
		new ImageSubSampler(this.getContext()).subSampleCroppedUri(item, image2, this.getContext());
	}
	
	public void showImage3(ItemData item) {
		new ImageSubSampler(this.getContext()).subSampleCroppedUri(item, image3, this.getContext());
	}
 

}
