package com.lyy.seeyou.traslation;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class Screen {

	public  int ScreenOrient(Activity activity){
		int orient=activity.getRequestedOrientation();
		if (orient!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE&&orient!=
				ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			WindowManager windowsManager=activity.getWindowManager();
			Display display=windowsManager.getDefaultDisplay();
			int screenWidth=display.getWidth();
			int screenHeight=display.getHeight();
			orient=screenWidth<screenHeight? ActivityInfo.
			SCREEN_ORIENTATION_PORTRAIT:ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
		}
		return orient;
	}
	
	public  void AotoBackground(Activity activity,View view,
			int Background_v,int Background_h){
		int orient=ScreenOrient(activity);
		if (orient==ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			view.setBackgroundResource(Background_v);
		}else{
			view.setBackgroundResource(Background_h);
		}
	}
}
