package com.example.david.testproject;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;

public class ImageHelper {

    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap =
                Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public static Rect collidePixel(Rect rect1, Rect rect2, Bitmap bitmap1, Bitmap bitmap2) {
        Rect rect = new Rect();
        if (!rect.setIntersect(rect1, rect2))
            return null;


        int[] pixels1;
        int[] pixels2;
        try{
            pixels1 = new int[rect.width() * rect.height()];
            bitmap1.getPixels(pixels1, 0, rect.width(), rect.left - rect1.left,
                    rect.top - rect1.top, rect.width(), rect.height());

            pixels2 = new int[rect.width() * rect.height()];
            bitmap2.getPixels(pixels2, 0, rect.width(), rect.left - rect2.left,
                    rect.top - rect2.top, rect.width(), rect.height());


            for (int i = 0; i < pixels1.length; i++) {
                if ((pixels1[i] & 0xff000000) != 0 && (pixels2[i] & 0xff000000) != 0){
                    return rect2;
                }

            }
            return null;
        }catch(Exception e){
            return null;
        }
    }

    public static Rect collideRect(Rect rect1, Rect rect2) {
        Rect rect = new Rect();
        return rect.setIntersect(rect1, rect2) ? rect : null;
    }


}
