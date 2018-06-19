package com.tvcat.homepage;

import android.content.Context;
import android.view.OrientationEventListener;

class AlbumOrientationEventListener extends OrientationEventListener {
    private int mOrientation = 0;

    public AlbumOrientationEventListener(Context context) {
        super(context);  
    }  
          
    public AlbumOrientationEventListener(Context context, int rate) {  
        super(context, rate);  
    }  
  
    @Override  
    public void onOrientationChanged(int orientation) {  
        if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {  
            return;  
        }  
  
        //保证只返回四个方向  
        int newOrientation = ((orientation + 45) / 90 * 90) % 360 ;
        if (newOrientation != mOrientation) {
            mOrientation = newOrientation;  
                  
            //返回的mOrientation就是手机方向，为0°、90°、180°和270°中的一个  
        }  
    }  
}  