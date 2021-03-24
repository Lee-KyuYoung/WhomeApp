package com.lky.whome.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.ViewGroup;

public class CreateImageView extends androidx.appcompat.widget.AppCompatImageView {

    private ViewGroup viewGroup;

    public CreateImageView(Context context, ViewGroup viewGroup) {
        super(context);
        this.viewGroup = viewGroup;
    }
    public CreateImageView setGravity(int gravity){
        super.setForegroundGravity(gravity);
        return this;
    }
    public CreateImageView setImgDrawable(Drawable drawable){
        super.setImageDrawable(drawable);
        return this;
    }
    public CreateImageView setImgScaleType(ScaleType scaleType){
        super.setScaleType(scaleType);
        return this;
    }
    public CreateImageView setLayoutParam(ViewGroup.LayoutParams params){
        super.setLayoutParams(params);
        return this;
    }
    public void addImageView(){
        viewGroup.addView(this);
    }


}
