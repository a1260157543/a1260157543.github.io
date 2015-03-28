package com.hlb.app;

import java.util.LinkedHashMap;
import java.util.Map;

import com.hlb.R;

import android.app.Application;

public class MyApplication extends Application {
	public static final int NUM_PAGE = 6;// 总共有多少页
	public static int NUM = 20;// 每页20个表情，还有1个删除button
	private static MyApplication mApplication;
	private Map<String, Integer> mFaceMap = new LinkedHashMap<String, Integer>();

	public synchronized static MyApplication getInstance() {
		return mApplication;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mApplication = this;
		initFaceMap();
	}

	public Map<String, Integer> getFaceMap() {
		if (!mFaceMap.isEmpty())
			return mFaceMap;
		return null;
	}

	private void initFaceMap() {
		// TODO Auto-generated method stub
		mFaceMap.put("[呲牙]", R.drawable.f021);
		mFaceMap.put("[调皮]", R.drawable.f022);
		mFaceMap.put("[流汗]", R.drawable.f023);
		mFaceMap.put("[偷笑]", R.drawable.f024);
		mFaceMap.put("[再见]", R.drawable.f025);
		mFaceMap.put("[敲打]", R.drawable.f005);
		mFaceMap.put("[擦汗]", R.drawable.f006);
		mFaceMap.put("[猪头]", R.drawable.f007);
		mFaceMap.put("[玫瑰]", R.drawable.f008);
		mFaceMap.put("[流泪]", R.drawable.f009);
		mFaceMap.put("[大哭]", R.drawable.f010);
		mFaceMap.put("[嘘]", R.drawable.f011);
		mFaceMap.put("[酷]", R.drawable.f012);
		mFaceMap.put("[抓狂]", R.drawable.f013);
		mFaceMap.put("[委屈]", R.drawable.f014);
		mFaceMap.put("[便便]", R.drawable.f015);
		mFaceMap.put("[炸弹]", R.drawable.f016);
		mFaceMap.put("[菜刀]", R.drawable.f017);
		mFaceMap.put("[可爱]", R.drawable.f018);
		mFaceMap.put("[色]", R.drawable.f019);
		mFaceMap.put("[害羞]", R.drawable.f020);

		mFaceMap.put("[得意]", R.drawable.f021);
		mFaceMap.put("[吐]", R.drawable.f022);
		mFaceMap.put("[微笑]", R.drawable.f023);
		mFaceMap.put("[发�?]", R.drawable.f024);
		mFaceMap.put("[尴尬]", R.drawable.f025);
		mFaceMap.put("[惊恐]", R.drawable.f026);
		mFaceMap.put("[冷汗]", R.drawable.f027);
		mFaceMap.put("[爱心]", R.drawable.f028);
		mFaceMap.put("[示爱]", R.drawable.f029);
		mFaceMap.put("[白眼]", R.drawable.f030);
		mFaceMap.put("[傲慢]", R.drawable.f031);
		mFaceMap.put("[难过]", R.drawable.f032);
		mFaceMap.put("[惊讶]", R.drawable.f033);
		mFaceMap.put("[疑问]", R.drawable.f034);
		mFaceMap.put("[睡]", R.drawable.f035);
		mFaceMap.put("[亲亲]", R.drawable.f006);
		mFaceMap.put("[憨笑]", R.drawable.f007);
		mFaceMap.put("[爱情]", R.drawable.f008);
		mFaceMap.put("[衰]", R.drawable.f009);
		mFaceMap.put("[撇嘴]", R.drawable.f010);
		mFaceMap.put("[阴险]", R.drawable.f011);

		mFaceMap.put("[奋斗]", R.drawable.f012);
		mFaceMap.put("[发呆]", R.drawable.f013);
		mFaceMap.put("[右哼哼]", R.drawable.f014);
		mFaceMap.put("[拥抱]", R.drawable.f015);
		mFaceMap.put("[坏笑]", R.drawable.f016);
		mFaceMap.put("[飞吻]", R.drawable.f017);
		mFaceMap.put("[鄙视]", R.drawable.f018);
		mFaceMap.put("[晕]", R.drawable.f018);
		mFaceMap.put("[大兵]", R.drawable.f019);
		mFaceMap.put("[可�?]", R.drawable.f020);
		mFaceMap.put("[强]", R.drawable.f021);
		mFaceMap.put("[弱]", R.drawable.f022);
		mFaceMap.put("[握手]", R.drawable.f023);
		mFaceMap.put("[胜利]", R.drawable.f024);
		mFaceMap.put("[抱拳]", R.drawable.f025);
		mFaceMap.put("[凋谢]", R.drawable.f026);
		mFaceMap.put("[饭]", R.drawable.f027);
		mFaceMap.put("[蛋糕]", R.drawable.f028);
		mFaceMap.put("[西瓜]", R.drawable.f029);
		mFaceMap.put("[啤酒]", R.drawable.f030);
		mFaceMap.put("[飘虫]", R.drawable.f031);

		mFaceMap.put("[勾引]", R.drawable.f032);
		mFaceMap.put("[OK]", R.drawable.f033);
		mFaceMap.put("[爱你]", R.drawable.f034);
		mFaceMap.put("[咖啡]", R.drawable.f035);
		mFaceMap.put("[钱]", R.drawable.f006);
		mFaceMap.put("[月亮]", R.drawable.f007);
		mFaceMap.put("[美女]", R.drawable.f008);
		mFaceMap.put("[�?", R.drawable.f009);
		mFaceMap.put("[发抖]", R.drawable.f010);
		mFaceMap.put("[差劲]", R.drawable.f011);
		mFaceMap.put("[拳头]", R.drawable.f012);
		mFaceMap.put("[心碎]", R.drawable.f013);
		mFaceMap.put("[太阳]", R.drawable.f014);
		mFaceMap.put("[礼物]", R.drawable.f015);
		mFaceMap.put("[足球]", R.drawable.f016);
		mFaceMap.put("[骷髅]", R.drawable.f017);
		mFaceMap.put("[挥手]", R.drawable.f018);
		mFaceMap.put("[闪电]", R.drawable.f019);
		mFaceMap.put("[饥饿]", R.drawable.f020);
		mFaceMap.put("[困]", R.drawable.f021);
		mFaceMap.put("[咒骂]", R.drawable.f022);

		mFaceMap.put("[折磨]", R.drawable.f023);
		mFaceMap.put("[抠鼻]", R.drawable.f024);
		mFaceMap.put("[鼓掌]", R.drawable.f025);
		mFaceMap.put("[糗大了]", R.drawable.f026);
		mFaceMap.put("[左哼哼]", R.drawable.f027);
		mFaceMap.put("[哈欠]", R.drawable.f028);
		mFaceMap.put("[快哭了]", R.drawable.f029);
		mFaceMap.put("[吓]", R.drawable.f030);
		mFaceMap.put("[篮球]", R.drawable.f031);
		mFaceMap.put("[乒乓球]", R.drawable.f032);
		mFaceMap.put("[NO]", R.drawable.f033);
		mFaceMap.put("[跳跳]", R.drawable.f034);
		mFaceMap.put("[怄火]", R.drawable.f035);
		mFaceMap.put("[转圈]", R.drawable.f020);
		mFaceMap.put("[磕头]", R.drawable.f021);
		mFaceMap.put("[回头]", R.drawable.f022);
		mFaceMap.put("[跳绳]", R.drawable.f023);
		mFaceMap.put("[�?��]", R.drawable.f024);
		mFaceMap.put("[街舞]", R.drawable.f025);
		mFaceMap.put("[献吻]", R.drawable.f026);
		mFaceMap.put("[左太极]", R.drawable.f027);

		mFaceMap.put("[右太极]", R.drawable.f028);
		mFaceMap.put("[闭嘴]", R.drawable.f029);
	}
}
