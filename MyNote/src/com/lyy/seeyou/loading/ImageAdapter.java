package com.lyy.seeyou.loading;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter{
private Context mContext;
private Integer[] mImageId;
public ImageAdapter(Context context,Integer[] images)
{
           mContext=context;
           mImageId=images;
}
            @Override

             public int getCount() {
                        // TODO Auto-generated method stub
                        return mImageId.length;
               }

               @Override
               public Object getItem(int position) {
                        // TODO Auto-generated method stub
                        return position;
               }

               @Override
               public long getItemId(int position) {
                        // TODO Auto-generated method stub
                        return position;
               }
               
               @Override
               public View getView(int position, View convertView, ViewGroup parent)
               {
                        ImageView i = new ImageView(mContext);
                        //����ͼƬ��Դ
                        i.setImageResource(mImageId[position]);
                        //����ͼƬ��ʽ
                        i.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        //����ͼƬ����
                        i.setBackgroundResource(android.R.color.background_dark);
                        //����ͼƬ��С
//                     i.setLayoutParams(new Gallery.LayoutParams(intScreenW, intScreenH));
                        return i;
               }

}
