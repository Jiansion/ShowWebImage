package com.conch.showwebimage.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Author : Jiansion
 * Create : 2017/7/11.
 * Description : 类别说明
 * Email : amazeconch@gmail.com
 */

public final class ImageLoader {

    public static void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).into(imageView);
    }
}
