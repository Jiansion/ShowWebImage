package com.conch.showwebimage.widget;

import android.support.v4.view.PagerAdapter;
import android.view.View;

import java.util.List;

/**
 * Author : Jiansion
 * Create : 2017/7/11.
 * Description : 类别说明
 * Email : amazeconch@gmail.com
 */

public class ImageAdapter extends PagerAdapter {

    private List<String> imageList;

    public ImageAdapter(List<String> imageList) {
        this.imageList = imageList;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return false;
    }
}
