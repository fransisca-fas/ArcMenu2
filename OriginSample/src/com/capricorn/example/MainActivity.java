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

package com.capricorn.example;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.capricorn.ArcMenu;
import com.capricorn.RayMenu;

/**
 * 
 * @author Capricorn
 * 
 */
public class MainActivity extends Activity {
	private static final int[] ITEM_DRAWABLES = { R.drawable.composer_camera, R.drawable.composer_music,
			R.drawable.composer_place, R.drawable.composer_sleep, R.drawable.composer_thought, R.drawable.composer_with };

	ArcMenu arcMenu;
	RelativeLayout rlBase;
	int x, y;
	/** Called when the activity is first created. */
	
	Handler h = new Handler() {
		@Override
    	public void handleMessage(Message msg) {
			if(msg.what==1)
				arcMenu.opencloseItem();
			else if(msg.what==2)
				rlBase.removeView(arcMenu);
    	}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		rlBase = (RelativeLayout)findViewById(R.id.llBase); 
		rlBase.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				final int action = event.getAction();
				switch(action & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:
					x = (int)event.getX();
					y = (int)event.getY();
					Log.d("location touch","x:"+x+" y:"+y+" w:"+rlBase.getWidth()+" h:"+rlBase.getHeight());
					break;
				}
				return false;
			}
		});
		
		rlBase.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				final int itemCount = ITEM_DRAWABLES.length;
				final int childSize = dpToPx(30);
				rlBase.removeView(arcMenu);
				arcMenu = new ArcMenu(MainActivity.this, ArcMenu.FAN, x, y, rlBase.getWidth(), rlBase.getHeight(), childSize, itemCount);

				for (int i = 0; i < itemCount; i++) {
		            ImageView item = new ImageView(MainActivity.this);
		            item.setImageResource(ITEM_DRAWABLES[i]);

		            final int position = i;
		            arcMenu.addItem(item, new OnClickListener() {

		                @Override
		                public void onClick(View v) {
		                    Toast.makeText(MainActivity.this, childSize+" position:" + position, Toast.LENGTH_SHORT).show();
		                }
		            });
		        }
				Runnable runnable = new Runnable() {
					
					@Override
					public void run() {
						h.sendEmptyMessage(1);
					}
				};
				Thread t = new Thread(runnable);
				try {
					t.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				t.start();
		        rlBase.addView(arcMenu);
				return true;
			}
		});

		final ImageView ivButton = (ImageView)findViewById(R.id.button);
		/* add menu via xml */
		/*arcMenu = (ArcMenu)findViewById(R.id.arc_menu);
		initArcMenu();*/
		/* add menu via xml */

		/* add menu via code */
		final int itemCount = ITEM_DRAWABLES.length;
		final int childSize = dpToPx(50);
		rlBase.removeView(arcMenu);
		/* add menu via code */
		
		/*ivButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(arcMenu.isItemExpanded()) {
					arcMenu.opencloseItem();
				} else
					Toast.makeText(MainActivity.this, "click", Toast.LENGTH_SHORT).show();
			}
		});
		
		ivButton.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				 add menu via xml 
				arcMenu.setVisibility(View.VISIBLE); 
				 add menu via xml 
				

				 add menu via code 
				arcMenu = new ArcMenu(MainActivity.this, ArcMenu.RAINBOW, childSize, itemCount);
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.addRule(RelativeLayout.CENTER_IN_PARENT);
				arcMenu.setLayoutParams(params);

		        rlBase.addView(arcMenu);
		        ivButton.bringToFront();
				
				for (int i = 0; i < itemCount; i++) {
		            ImageView item = new ImageView(MainActivity.this);
		            item.setImageResource(ITEM_DRAWABLES[i]);

		            final int position = i;
		            arcMenu.addItem(item, new OnClickListener() {

		                @Override
		                public void onClick(View v) {
		                    Toast.makeText(MainActivity.this, childSize+" position:" + position, Toast.LENGTH_SHORT).show();
		                }
		            });
		        }
				 add menu via code 
				
				Runnable runnable = new Runnable() {
					
					@Override
					public void run() {
						h.sendEmptyMessage(1);
					}
				};
				Thread t = new Thread(runnable);
				try {
					t.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				t.start();
		        
				return true;
			}
		});
	*/
	}
	
	private void initArcMenu() {
		final int itemCount = ITEM_DRAWABLES.length;
		final int childSize = (int)getResources().getDimension(R.dimen.menuChildSize);
		arcMenu.setChildSize(childSize);
		arcMenu.setArcMode(ArcMenu.RAINBOW);
		for (int i = 0; i < itemCount; i++) {
            ImageView item = new ImageView(MainActivity.this);
            item.setImageResource(ITEM_DRAWABLES[i]);

            final int position = i;
            arcMenu.addItem(item, new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, childSize+" position:" + position, Toast.LENGTH_SHORT).show();
                }
            });
        }
	}
	
	private int dpToPx(int dp)  {
    	DisplayMetrics displayMetrics = MainActivity.this.getResources().getDisplayMetrics();
        return (int) ((dp*displayMetrics.density)+0.5);
	}

}
