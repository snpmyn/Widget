package com.zsp.library.picture.luban;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import value.WidgetLibraryMagic;

/**
 * @decs: Engine
 * Responsible for starting compress and managing active and cached resources.
 * @author: 郑少鹏
 * @date: 2019/8/28 19:03
 */
class Engine {
    private InputStreamProvider srcImg;
    private File tagImg;
    private int srcWidth;
    private int srcHeight;
    private boolean focusAlpha;

    Engine(InputStreamProvider srcImg, File tagImg, boolean focusAlpha) throws IOException {
        this.tagImg = tagImg;
        this.srcImg = srcImg;
        this.focusAlpha = focusAlpha;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;
        BitmapFactory.decodeStream(srcImg.open(), null, options);
        this.srcWidth = options.outWidth;
        this.srcHeight = options.outHeight;
    }

    private int computeSize() {
        srcWidth = srcWidth % 2 == 1 ? srcWidth + 1 : srcWidth;
        srcHeight = srcHeight % 2 == 1 ? srcHeight + 1 : srcHeight;
        int longSide = Math.max(srcWidth, srcHeight);
        int shortSide = Math.min(srcWidth, srcHeight);
        float scale = ((float) shortSide / longSide);
        if (scale <= 1 && scale > WidgetLibraryMagic.FLOAT_ZERO_DOT_FIVE_SIX_TWO_FIVE) {
            if (longSide < WidgetLibraryMagic.INT_ONE_THOUSAND_SIX_HUNDRED_SIXTY_FOUR) {
                return 1;
            } else if (longSide < WidgetLibraryMagic.INT_FOUR_THOUSAND_NINE_HUNDRED_NINETY) {
                return 2;
            } else if (longSide > WidgetLibraryMagic.INT_FOUR_THOUSAND_NINE_HUNDRED_NINETY && longSide < WidgetLibraryMagic.INT_ONE_THOUSAND_TWO_HUNDRED_FIFTY) {
                return 4;
            } else {
                return longSide / 1280;
            }
        } else if (scale <= WidgetLibraryMagic.FLOAT_ZERO_DOT_FIVE_SIX_TWO_FIVE && scale > WidgetLibraryMagic.FLOAT_ZERO_DOT_FIVE) {
            return longSide / 1280 == 0 ? 1 : longSide / 1280;
        } else {
            return (int) Math.ceil(longSide / (1280.0 / scale));
        }
    }

    private Bitmap rotatingImage(Bitmap bitmap, int angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    File compress() throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = computeSize();
        Bitmap tagBitmap = BitmapFactory.decodeStream(srcImg.open(), null, options);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if (Checker.SINGLE.isJpg(srcImg.open())) {
            tagBitmap = rotatingImage(tagBitmap, Checker.SINGLE.getOrientation(srcImg.open()));
        }
        if (tagBitmap != null) {
            tagBitmap.compress(focusAlpha ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG, 60, stream);
            tagBitmap.recycle();
        }
        FileOutputStream fos = new FileOutputStream(tagImg);
        fos.write(stream.toByteArray());
        fos.flush();
        fos.close();
        stream.close();
        return tagImg;
    }
}