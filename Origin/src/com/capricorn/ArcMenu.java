/*
 * Copyright (C) 2012 Capricorn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.capricorn;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

/**
 * A custom view that looks like the menu in <a href="https://path.com">Path
 * 2.0</a> (for iOS).
 * 
 * @author Capricorn
 * 
 */
public class ArcMenu extends RelativeLayout {
    private ArcLayout mArcLayout;

    private ImageView mHintView;
    
    /* fas */
    private int mMenuMode = -1;
	public static final int FAN = 0;
	public static final int RAINBOW = 1;
	public static final int UPWARD = 2;
	public static final int CUSTOM = 3;
    /* eof */

    public ArcMenu(Context context) {
        super(context);
        init(context);
    }

    public ArcMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        applyAttrs(attrs);
    }
    
    /* fas */
    Context c;
    public ArcMenu(Context context, int menuMode, /*View v, int baseWidth, int baseHeight,*/ int childSize, int childCount) {
    	super(context);
    	c = context;
    	init(context);
    	setArcMode(menuMode);
    	setChildSize(childSize);
    }
    
    int mX, mY, mBaseWidth, mBaseHeight;
    int mChildSize, mChildCount;
    public ArcMenu(Context context, int menuMode, int x, int y, int baseWidth, int baseHeight, int childSize, int childCount) {
    	super(context);
    	c = context;
    	mX = x;
    	mY = y;
    	mBaseWidth = baseWidth;
    	mBaseHeight = baseHeight;
    	mChildSize = childSize;
    	mChildCount = childCount;
    	mMenuMode = menuMode;
    	init(context);
    	setChildSize(childSize);
    }
    
    LayoutParams params;
    private void applyLayoutParams(int menuMode) {
    	params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	int padding;
    	switch (menuMode) {
		case FAN:
			padding = (mArcLayout.getChildPadding() + mArcLayout.getLayoutPadding()) * (mChildCount+1);
    		params.leftMargin = mX - ((mChildSize*mChildCount)/2) - padding;
    		Log.d("", "mx"+mX+" size*count"+((mChildSize*mChildCount)/2)+" padding"+padding+" margin"+params.leftMargin);
    		Log.d(mChildSize+"."+mChildCount,"test"+(mChildSize*mChildCount)+" measure"+mArcLayout.getMeasuredWidth()+"/"+mArcLayout.getMeasuredHeight());
    		params.topMargin = mY - ((mChildSize*mChildCount)/2)/* - (controlLayout.getMeasuredHeight())*/;
    		setArcMode(menuMode);
    		mArcLayout.setLayoutParams(params);
			break;

		default:
			break;
		}
    }
    
    public void setArcMode(int menuMode) {
    	switch(menuMode) {
    	case FAN:
    		mArcLayout.setArc(270.0f, 360.0f);
    		Log.d("params","r:"+params.rightMargin+" l:"+params.leftMargin+" t:"+params.topMargin+"b:"+params.bottomMargin);
    		if(params.rightMargin>0 || params.leftMargin<=0) {
    			//kanan
    			if(params.topMargin>=0)
    				mArcLayout.setArc(270.0f, 360.0f);
    			else 
    				mArcLayout.setArc(0.0f, 90.0f);
    		} else {
    			//kiri
    			if(params.topMargin>=0)
    				mArcLayout.setArc(180.0f, 270.0f);
    			else 
    				mArcLayout.setArc(90.0f, 180.0f);
    		}
    		break;
    	case RAINBOW:
    		mArcLayout.setArc(210.0f, 330.0f);
    		break;
    	case UPWARD:
    		break;
    	case CUSTOM:
    		break;
    	}
    }
    
    public void setChildSize(int size) {
//        int newChildSize = c.getResources().getDimensionPixelSize(size);
        mArcLayout.setChildSize(size);
    }
    
    /* eof */

    private void init(Context context) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.arc_menu, this);

        mArcLayout = (ArcLayout) findViewById(R.id.item_layout);
        applyLayoutParams(mMenuMode);
        mArcLayout.setPosition(mBaseWidth, mBaseHeight, mX, mY, params);

        final ViewGroup controlLayout = (ViewGroup) findViewById(R.id.control_layout);
        controlLayout.setClickable(false);
        controlLayout.setVisibility(View.GONE);

        mHintView = (ImageView) findViewById(R.id.control_hint);
    }
    
    /* fas */
    public boolean isItemExpanded() {
    	return mArcLayout.isExpanded();
    }
    
    public void opencloseItem() {
    	mHintView.startAnimation(createHintSwitchAnimation(mArcLayout.isExpanded()));
		mArcLayout.switchState(true);
    }
    
    /* eof */

    private void applyAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ArcLayout, 0, 0);

            float fromDegrees = a.getFloat(R.styleable.ArcLayout_fromDegrees, ArcLayout.DEFAULT_FROM_DEGREES);
            float toDegrees = a.getFloat(R.styleable.ArcLayout_toDegrees, ArcLayout.DEFAULT_TO_DEGREES);
            mArcLayout.setArc(fromDegrees, toDegrees);

            int defaultChildSize = mArcLayout.getChildSize();
            int newChildSize = a.getDimensionPixelSize(R.styleable.ArcLayout_childSize, defaultChildSize);
            mArcLayout.setChildSize(newChildSize);

            a.recycle();
        }
    }

    public void addItem(View item, OnClickListener listener) {
        mArcLayout.addView(item);
        item.setOnClickListener(getItemClickListener(listener));
    }

    private OnClickListener getItemClickListener(final OnClickListener listener) {
        return new OnClickListener() {

            @Override
            public void onClick(final View viewClicked) {
                Animation animation = bindItemAnimation(viewClicked, true, 400);
                animation.setAnimationListener(new AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                itemDidDisappear();
                            }
                        }, 0);
                    }
                });

                final int itemCount = mArcLayout.getChildCount();
                for (int i = 0; i < itemCount; i++) {
                    View item = mArcLayout.getChildAt(i);
                    if (viewClicked != item) {
                        bindItemAnimation(item, false, 300);
                    }
                }

                mArcLayout.invalidate();
                mHintView.startAnimation(createHintSwitchAnimation(true));

                if (listener != null) {
                    listener.onClick(viewClicked);
                }
            }
        };
    }

    private Animation bindItemAnimation(final View child, final boolean isClicked, final long duration) {
        Animation animation = createItemDisapperAnimation(duration, isClicked);
        child.setAnimation(animation);

        return animation;
    }

    private void itemDidDisappear() {
        final int itemCount = mArcLayout.getChildCount();
        for (int i = 0; i < itemCount; i++) {
            View item = mArcLayout.getChildAt(i);
            item.clearAnimation();
        }

        mArcLayout.switchState(false);
    }

    private static Animation createItemDisapperAnimation(final long duration, final boolean isClicked) {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(new ScaleAnimation(1.0f, isClicked ? 2.0f : 0.0f, 1.0f, isClicked ? 2.0f : 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f));
        animationSet.addAnimation(new AlphaAnimation(1.0f, 0.0f));

        animationSet.setDuration(duration);
        animationSet.setInterpolator(new DecelerateInterpolator());
        animationSet.setFillAfter(true);

        return animationSet;
    }

    private static Animation createHintSwitchAnimation(final boolean expanded) {
        Animation animation = new RotateAnimation(expanded ? 45 : 0, expanded ? 0 : 45, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setStartOffset(0);
        animation.setDuration(100);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setFillAfter(true);

        return animation;
    }
}
