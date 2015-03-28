package com.lyy.seeyou.activity;

import java.util.ArrayList;

import com.hlb.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

public class AddressActivity extends Activity{
	private ViewPager mViewPager;	
	private ImageView mPage0;
	private ImageView mPage1;
	
	private int currIndex = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_one);
		   mViewPager = (ViewPager)findViewById(R.id.whatsnew_viewpager);        
	        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	       
	        
	        mPage0 = (ImageView)findViewById(R.id.page0);
	        mPage1 = (ImageView)findViewById(R.id.page1);
	        
	      //将要分页显示的View装入数组中
	        LayoutInflater mLi = LayoutInflater.from(this);
	        View view1 = mLi.inflate(R.layout.add2, null);
	        View view2 = mLi.inflate(R.layout.add1, null);
	        
	      //每个页面的view数据
	        final ArrayList<View> views = new ArrayList<View>();
	        views.add(view1);
	        views.add(view2);
	        
	        //填充ViewPager的数据适配器
	        PagerAdapter mPagerAdapter = new PagerAdapter() {
				
				@Override
				public boolean isViewFromObject(View arg0, Object arg1) {
					return arg0 == arg1;
				}
				
				@Override
				public int getCount() {
					return views.size();
				}

				@Override
				public void destroyItem(View container, int position, Object object) {
					((ViewPager)container).removeView(views.get(position));
				}
				
				
				
				@Override
				public Object instantiateItem(View container, int position) {
					((ViewPager)container).addView(views.get(position));
					return views.get(position);
				}
			};
			
			mViewPager.setAdapter(mPagerAdapter);
	    }    
	    

	    public class MyOnPageChangeListener implements OnPageChangeListener {
			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:				
					mPage0.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
					mPage1.setImageDrawable(getResources().getDrawable(R.drawable.page));
					break;
				case 1:
					mPage1.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
					mPage0.setImageDrawable(getResources().getDrawable(R.drawable.page));
					break;
				
				}
				currIndex = arg0;
				//animation.setFillAfter(true);// True:图片停在动画结束位置
				//animation.setDuration(300);
				//mPageImg.startAnimation(animation);
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		}
	   
	    
	}