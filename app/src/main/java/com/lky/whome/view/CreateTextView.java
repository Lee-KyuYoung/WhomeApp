package com.lky.whome.view;

import android.content.Context;
import android.view.ViewGroup;


public class CreateTextView extends androidx.appcompat.widget.AppCompatTextView {

    private ViewGroup viewGroup;

    public CreateTextView(Context context, ViewGroup viewGroup) {
        super(context);
        this.viewGroup = viewGroup;
    }

    public CreateTextView setText(String text){
        super.setText(text);
        return this;
    }
    public CreateTextView setTextSize(int size){
        super.setTextSize(size);
        return this;
    }
    public CreateTextView setColor(int color){
        super.setTextColor(color);
        return this;
    }
    public CreateTextView setTextPadding(int left, int top, int right, int bottom){
        super.setPadding(left,top,right,bottom);
        return this;
    }
    public CreateTextView setLayoutParam(ViewGroup.LayoutParams params){
        super.setLayoutParams(params);
        return this;
    }
    public CreateTextView setTextGravity(int gravity){
        super.setForegroundGravity(gravity);
        return this;
    }

    public void addTextView(){
        viewGroup.addView(this);
    }
}
