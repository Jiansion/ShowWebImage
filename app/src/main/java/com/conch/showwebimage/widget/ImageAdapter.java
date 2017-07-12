package com.conch.showwebimage.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.conch.showwebimage.R;
import com.conch.showwebimage.util.ImageLoader;

import java.util.List;

/**
 * Author : Jiansion
 * Create : 2017/7/11.
 * Description : 类别说明
 * Email : amazeconch@gmail.com
 */

public class ImageAdapter extends PagerAdapter {

    private List<String> imageList;
    private Context context;

    public ImageAdapter(Context context, List<String> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(context, R.layout.item_image, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        ImageLoader.loadImage(context, imageList.get(position), imageView);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        container.removeView((View) object);
    }
}
