package com.lyy.seeyou.activity;
import java.util.ArrayList;

import com.hlb.R;
import com.hlb.activity.NewMoodAddit;
import com.hlb.activity.WelcomeActivity;
import com.lyy.seeyou.Index;
import com.lyy.seeyou.loading.Net;
import com.lyy.seeyou.loading.NetPage;
import com.lyy.seeyou.setting.About;
import com.lyy.seeyou.setting.FeedBack;
import com.lyy.seeyou.setting.MyAlarmManagerDemo;
import com.lyy.seeyou.traslation.Exit;
import com.lyy.seeyou.traslation.ExitFromSettings;
import com.lyy.seeyou.traslation.Image;
import com.lyy.seeyou.traslation.MainTopRightDialog;
import com.lyy.seeyou.traslation.Screen;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	public static MainActivity instance = null;
	private MediaPlayer music = null;// ���������� 
	private ViewPager mTabPager;	
	private ImageView mTabImg;// ����ͼƬ
	private ImageView mTab1,mTab2,mTab3,mTab4;
	private int zero = 0;// ����ͼƬƫ����
	private int currIndex = 0;// ��ǰҳ�����
	private int one;//����ˮƽ����λ��
	private int two;
	private int three;
	private LinearLayout mClose;
    private LinearLayout mCloseBtn;
    private View layout;	
	private boolean menu_display = false;
	private PopupWindow menuWindow;
	private LayoutInflater inflater;
	final ArrayList<View> views = new ArrayList<View>();
	//private Button mRightBtn;
	
	private RelativeLayout mainweixin;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        
        mainweixin=(RelativeLayout) findViewById(R.id.main);
        Screen screen=new Screen();
		screen.AotoBackground(this, mainweixin,R.drawable.bg_v1,R.drawable.bg_h1);
         //����activityʱ���Զ����������
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
        instance = this;
        /*
        mRightBtn = (Button) findViewById(R.id.right_btn);
        mRightBtn.setOnClickListener(new Button.OnClickListener()
		{	@Override
			public void onClick(View v)
			{	showPopupWindow (MainWeixin.this,mRightBtn);
			}
		  });*/
        
        mTabPager = (ViewPager)findViewById(R.id.tabpager);
        mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());
        
        mTab1 = (ImageView) findViewById(R.id.img_weixin);
        mTab2 = (ImageView) findViewById(R.id.img_address);
        mTab3 = (ImageView) findViewById(R.id.img_friends);
        mTab4 = (ImageView) findViewById(R.id.img_settings);
        mTabImg = (ImageView) findViewById(R.id.img_tab_now);
        mTab1.setOnClickListener(new MyOnClickListener(0));
        mTab2.setOnClickListener(new MyOnClickListener(1));
        mTab3.setOnClickListener(new MyOnClickListener(2));
        mTab4.setOnClickListener(new MyOnClickListener(3));
        Display currDisplay = getWindowManager().getDefaultDisplay();//��ȡ��Ļ��ǰ�ֱ���
        int displayWidth = currDisplay.getWidth();
        int displayHeight = currDisplay.getHeight();
        one = displayWidth/4; //����ˮƽ����ƽ�ƴ�С
        two = one*2;
        three = one*3;
        //Log.i("info", "��ȡ����Ļ�ֱ���Ϊ" + one + two + three + "X" + displayHeight);
        
        //InitImageView();//ʹ�ö���
        
        Net net=new Net();
        LayoutInflater mLi = LayoutInflater.from(this);
        
        if (net.isNetworkAvailable(MainActivity.this)){
            Toast.makeText(getApplicationContext(), "��ǰ�п������磡", Toast.LENGTH_LONG).show();          
        }
        else
        {
            Toast.makeText(getApplicationContext(), "��ǰû�п������磡", Toast.LENGTH_LONG).show();
            Intent in=new Intent(MainActivity.this,Index.class);
            startActivity(in);                  
        }     
        
      //��Ҫ��ҳ��ʾ��Viewװ��������
       
          View view1 = mLi.inflate(R.layout.main_tab1, null);
          View view2 = mLi.inflate(R.layout.main_tab_address, null);
          View view3 = mLi.inflate(R.layout.main_tab_friends, null);
          View view4 = mLi.inflate(R.layout.main_tab_settings, null);
          views.add(view1);
          views.add(view2);
          views.add(view3);
          views.add(view4);
         
        
      
         
        
        
      //���ViewPager������������
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
			
			//@Override
			//public CharSequence getPageTitle(int position) {
				//return titles.get(position);
			//}
			
			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager)container).addView(views.get(position));
				return views.get(position);
			}
		};
		
		mTabPager.setAdapter(mPagerAdapter);
    }
    /**
	 * ͷ��������
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}
		@Override
		public void onClick(View v) {
			mTabPager.setCurrentItem(index);
		}
	};
    
	 /* ҳ���л�����(ԭ����:D.Winter)
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				PlayMusic(R.raw.a);
				mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_tx_pressed));
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_jsb_normal));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
					mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_normal));
				}
				else if (currIndex == 3) {
					animation = new TranslateAnimation(three, 0, 0, 0);
					mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
				}
				break;
			case 1:
				PlayMusic(R.raw.a);
				mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_jsb_pressed));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, one, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_tx_normal));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
					mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_normal));
				}
				else if (currIndex == 3) {
					animation = new TranslateAnimation(three, one, 0, 0);
					mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
				}
				break;
			case 2:
				PlayMusic(R.raw.a);
				mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_pressed));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, two, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_tx_normal));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_jsb_normal));
				}
				else if (currIndex == 3) {
					animation = new TranslateAnimation(three, two, 0, 0);
					mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
				}
				break;
			case 3:
				PlayMusic(R.raw.a);
				mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_pressed));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, three, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_tx_normal));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, three, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_jsb_normal));
				}
				else if (currIndex == 2) {
					animation = new TranslateAnimation(two, three, 0, 0);
					mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_normal));
				}
				break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);// True:ͼƬͣ�ڶ�������λ��
			animation.setDuration(150);
			mTabImg.startAnimation(animation);
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {  //��ȡ back��
    		
        	if(menu_display){         //��� Menu�Ѿ��� ���ȹر�Menu
        		menuWindow.dismiss();
        		menu_display = false;
        		}
        	else {
        		Intent intent = new Intent();
            	intent.setClass(MainActivity.this,Exit.class);
            	startActivity(intent);
        	}
    	}
    	
    	else if(keyCode == KeyEvent.KEYCODE_MENU){   //��ȡ Menu��			
			if(!menu_display){
				//��ȡLayoutInflaterʵ��
				inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
				//�����main��������inflate�м����Ŷ����ǰ����ֱ��this.setContentView()�İɣ��Ǻ�
				//�÷������ص���һ��View�Ķ����ǲ����еĸ�
				layout = inflater.inflate(R.layout.main_menu, null);
				
				//��������Ҫ�����ˣ����������ҵ�layout���뵽PopupWindow���أ������ܼ�
				menuWindow = new PopupWindow(layout,LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT); //������������width��height
				//menuWindow.showAsDropDown(layout); //���õ���Ч��
				//menuWindow.showAsDropDown(null, 0, layout.getHeight());
				menuWindow.showAtLocation(this.findViewById(R.id.main), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //����layout��PopupWindow����ʾ��λ��
				//��λ�ȡ����main�еĿؼ��أ�Ҳ�ܼ�
				mClose = (LinearLayout)layout.findViewById(R.id.menu_close);
				mCloseBtn = (LinearLayout)layout.findViewById(R.id.menu_close_btn);
				
				
				//�����ÿһ��Layout���е����¼���ע��ɡ�����
				//���絥��ĳ��MenuItem��ʱ�����ı���ɫ�ı�
				//����׼����һЩ����ͼƬ������ɫ
				mCloseBtn.setOnClickListener (new View.OnClickListener() {					
					@Override
					public void onClick(View arg0) {						
						//Toast.makeText(Main.this, "�˳�", Toast.LENGTH_LONG).show();
						Intent intent = new Intent();
			        	intent.setClass(MainActivity.this,Exit.class);
			        	startActivity(intent);
			        	menuWindow.dismiss(); //��Ӧ����¼�֮��ر�Menu
					}
				});				
				menu_display = true;				
			}else{
				//�����ǰ�Ѿ�Ϊ��ʾ״̬������������
				menuWindow.dismiss();
				menu_display = false;
				}
			
			return false;
		}
    	return false;
    }
	
	//���ñ������Ҳఴť������
	public void btnmainright(View v) { 
		Intent intent = new Intent (MainActivity.this,MainTopRightDialog.class);			
		startActivity(intent);	
		//Toast.makeText(getApplicationContext(), "����˹��ܰ�ť", Toast.LENGTH_LONG).show();
      }  	
//	public void startchat(View v) {      //С��  �Ի�����
//		Intent intent = new Intent (MainActivity.this,ChatActivity.class);			
//		startActivity(intent);	
//		//Toast.makeText(getApplicationContext(), "��¼�ɹ�", Toast.LENGTH_LONG).show();
//      }  
	public void exit_settings(View v) { 
		PlayMusic(R.raw.a);//�˳�  α���Ի��򡱣���ʵ��һ��activity
		Intent intent = new Intent (MainActivity.this,ExitFromSettings.class);			
		startActivity(intent);	
	 }
	private void PlayMusic(int MusicId) {
		// TODO Auto-generated method stub
		music = MediaPlayer.create(this, MusicId);
		music.start();
	}

	public void images(View v) { 
		Intent intent = new Intent (MainActivity.this,Image.class);			
		startActivity(intent);	
	 }
	public void alerm(View v) { 
		Intent intent = new Intent (MainActivity.this,MyAlarmManagerDemo.class);			
		startActivity(intent);	
	 }
	public void add(View v) {  
		Intent intent = new Intent (MainActivity.this,AddressActivity.class);			
		startActivity(intent);	
	 }
	public void jsb(View v) {
		Intent intent = new Intent (MainActivity.this,WelcomeActivity.class);			
		startActivity(intent);	
	 }
	public void about(View v) {  
		Intent intent = new Intent (MainActivity.this,About.class);			
		startActivity(intent);	
	 }
	public void fk(View v) {
		Intent intent = new Intent (MainActivity.this,FeedBack.class);			
		startActivity(intent);	
	 }
//	public void btn_shake(View v) {                                   //�ֻ�ҡһҡ
//		Intent intent = new Intent (MainActivity.this,ShakeActivity.class);			
//		startActivity(intent);	
//	}
}
		

