package com.hlb.activity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class MoodIconAdapter extends BaseAdapter {

	private static final Integer[] mood_icon_images;//存放所有表情图片的ID
	private Context mcontext;

	public MoodIconAdapter(Context paramContext) {
		this.mcontext = paramContext;
	}
    
	//得到所有表情图片的总数
	public int getCount() {
		return mood_icon_images.length;
	}
    
	//由参数paramInt得到mood_icon_images数组中对应的表情对象
	public Object getItem(int paramInt) {
		return mood_icon_images[paramInt];
	}
	//得到对应表情图片的ID
	public long getItemId(int paramInt) {
		return mood_icon_images[paramInt].intValue();
	}

	//得到呈现数据集合中内容的View对象
	public View getView(int position, View paramView, ViewGroup parent) {
		ImageView localImageView = null;
		if (paramView == null) {
			localImageView = new ImageView(this.mcontext);
			localImageView
					.setLayoutParams(new AbsListView.LayoutParams(85, 85));// 设置ImageView对象布局
			localImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);// 设置刻度的类型
			localImageView.setPadding(8, 8, 8, 8);// 设置间距
		} else {
			localImageView = (ImageView) paramView;
		}
		localImageView.setImageResource(mood_icon_images[position].intValue());// 为ImageView设置图片资源
		return localImageView;
	}

	//将R.java中每个图片对应ID添加到mood_icon_images中
	static {
		Integer[] arrayOfIconInteger = new Integer[88];
//		arrayOfIconInteger[0] = Integer.valueOf(R.drawable.a1);
//		arrayOfIconInteger[1] = Integer.valueOf(R.drawable.a2);
//		arrayOfIconInteger[2] = Integer.valueOf(R.drawable.a3);
//		arrayOfIconInteger[3] = Integer.valueOf(R.drawable.a4);
//		arrayOfIconInteger[4] = Integer.valueOf(R.drawable.a5);
//		arrayOfIconInteger[5] = Integer.valueOf(R.drawable.a6);
//		arrayOfIconInteger[6] = Integer.valueOf(R.drawable.a7);
//		arrayOfIconInteger[7] = Integer.valueOf(R.drawable.a8);
//		arrayOfIconInteger[8] = Integer.valueOf(R.drawable.a9);
//		arrayOfIconInteger[9] = Integer.valueOf(R.drawable.a10);
//		arrayOfIconInteger[10] = Integer.valueOf(R.drawable.a11);
//		arrayOfIconInteger[11] = Integer.valueOf(R.drawable.a12);
//		arrayOfIconInteger[12] = Integer.valueOf(R.drawable.a13);
//		arrayOfIconInteger[13] = Integer.valueOf(R.drawable.a14);
//		arrayOfIconInteger[14] = Integer.valueOf(R.drawable.a15);
//		arrayOfIconInteger[15] = Integer.valueOf(R.drawable.a16);
//		arrayOfIconInteger[16] = Integer.valueOf(R.drawable.a17);
//		arrayOfIconInteger[17] = Integer.valueOf(R.drawable.a18);
//		arrayOfIconInteger[18] = Integer.valueOf(R.drawable.a19);
//		arrayOfIconInteger[19] = Integer.valueOf(R.drawable.a20);
//		arrayOfIconInteger[20] = Integer.valueOf(R.drawable.a21);
//		arrayOfIconInteger[21] = Integer.valueOf(R.drawable.a22);
//		arrayOfIconInteger[22] = Integer.valueOf(R.drawable.a23);
//		arrayOfIconInteger[23] = Integer.valueOf(R.drawable.a24);
//		arrayOfIconInteger[24] = Integer.valueOf(R.drawable.a25);
//		arrayOfIconInteger[25] = Integer.valueOf(R.drawable.a26);
//		arrayOfIconInteger[26] = Integer.valueOf(R.drawable.a27);
//		arrayOfIconInteger[27] = Integer.valueOf(R.drawable.a28);
//		arrayOfIconInteger[28] = Integer.valueOf(R.drawable.a29);
//		arrayOfIconInteger[29] = Integer.valueOf(R.drawable.a30);
//		arrayOfIconInteger[30] = Integer.valueOf(R.drawable.a31);
//		arrayOfIconInteger[31] = Integer.valueOf(R.drawable.a32);
//		arrayOfIconInteger[32] = Integer.valueOf(R.drawable.a33);
//		arrayOfIconInteger[33] = Integer.valueOf(R.drawable.a34);
//		arrayOfIconInteger[34] = Integer.valueOf(R.drawable.a35);
//		arrayOfIconInteger[35] = Integer.valueOf(R.drawable.a36);
//		arrayOfIconInteger[36] = Integer.valueOf(R.drawable.a37);
//		arrayOfIconInteger[37] = Integer.valueOf(R.drawable.a38);
//		arrayOfIconInteger[38] = Integer.valueOf(R.drawable.a39);
//		arrayOfIconInteger[39] = Integer.valueOf(R.drawable.a40);
//		arrayOfIconInteger[40] = Integer.valueOf(R.drawable.a41);
//		arrayOfIconInteger[41] = Integer.valueOf(R.drawable.a42);
//		arrayOfIconInteger[42] = Integer.valueOf(R.drawable.a43);
//		arrayOfIconInteger[43] = Integer.valueOf(R.drawable.a44);
//		arrayOfIconInteger[44] = Integer.valueOf(R.drawable.a45);
//		arrayOfIconInteger[45] = Integer.valueOf(R.drawable.a46);
//		arrayOfIconInteger[46] = Integer.valueOf(R.drawable.a47);
//		arrayOfIconInteger[47] = Integer.valueOf(R.drawable.a48);
//		arrayOfIconInteger[48] = Integer.valueOf(R.drawable.a105);
//		arrayOfIconInteger[49] = Integer.valueOf(R.drawable.a106);
//		arrayOfIconInteger[50] = Integer.valueOf(R.drawable.a107);
//		arrayOfIconInteger[51] = Integer.valueOf(R.drawable.a108);
//		arrayOfIconInteger[52] = Integer.valueOf(R.drawable.a109);
//		arrayOfIconInteger[53] = Integer.valueOf(R.drawable.a110);
//		arrayOfIconInteger[54] = Integer.valueOf(R.drawable.a111);
//		arrayOfIconInteger[55] = Integer.valueOf(R.drawable.a112);
//		arrayOfIconInteger[56] = Integer.valueOf(R.drawable.a113);
//		arrayOfIconInteger[57] = Integer.valueOf(R.drawable.a114);
//		arrayOfIconInteger[58] = Integer.valueOf(R.drawable.a115);
//		arrayOfIconInteger[59] = Integer.valueOf(R.drawable.a116);
//		arrayOfIconInteger[60] = Integer.valueOf(R.drawable.a117);
//		arrayOfIconInteger[61] = Integer.valueOf(R.drawable.a118);
//		arrayOfIconInteger[62] = Integer.valueOf(R.drawable.a119);
//		arrayOfIconInteger[63] = Integer.valueOf(R.drawable.a120);
//		arrayOfIconInteger[64] = Integer.valueOf(R.drawable.a121);
//		arrayOfIconInteger[65] = Integer.valueOf(R.drawable.a122);
//		arrayOfIconInteger[66] = Integer.valueOf(R.drawable.a123);
//		arrayOfIconInteger[67] = Integer.valueOf(R.drawable.a124);
//		arrayOfIconInteger[68] = Integer.valueOf(R.drawable.a125);
//		arrayOfIconInteger[69] = Integer.valueOf(R.drawable.a126);
//		arrayOfIconInteger[70] = Integer.valueOf(R.drawable.a127);
//		arrayOfIconInteger[71] = Integer.valueOf(R.drawable.a128);
//		arrayOfIconInteger[72] = Integer.valueOf(R.drawable.a129);
//		arrayOfIconInteger[73] = Integer.valueOf(R.drawable.a130);
//		arrayOfIconInteger[74] = Integer.valueOf(R.drawable.a131);
//		arrayOfIconInteger[75] = Integer.valueOf(R.drawable.a132);
//		arrayOfIconInteger[76] = Integer.valueOf(R.drawable.a133);
//		arrayOfIconInteger[77] = Integer.valueOf(R.drawable.a134);
//		arrayOfIconInteger[78] = Integer.valueOf(R.drawable.a135);
//		arrayOfIconInteger[79] = Integer.valueOf(R.drawable.a136);
//		arrayOfIconInteger[80] = Integer.valueOf(R.drawable.a137);
//		arrayOfIconInteger[81] = Integer.valueOf(R.drawable.a138);
//		arrayOfIconInteger[82] = Integer.valueOf(R.drawable.a139);
//		arrayOfIconInteger[83] = Integer.valueOf(R.drawable.a140);
//		arrayOfIconInteger[84] = Integer.valueOf(R.drawable.a141);
//		arrayOfIconInteger[85] = Integer.valueOf(R.drawable.a142);
//		arrayOfIconInteger[86] = Integer.valueOf(R.drawable.a143);
//		arrayOfIconInteger[87] = Integer.valueOf(R.drawable.a144);
		mood_icon_images = arrayOfIconInteger;
	}
}
