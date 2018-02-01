package com.hletong.mob.util;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Build;

import com.xcheng.view.util.LocalDisplay;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by cc on 2017/2/17.
 */
public class BitmapUtils {

    /**
     * @param pathToOriginal   原始图片
     * @param compressFileName 被压缩后的图片
     * @param quality
     * @return
     */
    public static String compressImage(String pathToOriginal, String compressFileName, int quality) {
        Bitmap bm = getSmallBitmap(pathToOriginal);//获取一定尺寸的图片
        int degree = getOrientation(pathToOriginal);//获取相片拍摄角度
        if (degree != 0) {//旋转照片角度，防止头像横着显示
            bm = rotateImage(bm, degree);
        }
        File outputFile = new File(AppManager.getContext().getCacheDir(), compressFileName);
        try {
            if (!outputFile.exists()) {
                File parentFile = outputFile.getParentFile();
                //内部会判断是否存在此文件
                if (parentFile != null && !parentFile.exists() && !parentFile.mkdirs()) {
                    return pathToOriginal;
                }
            } else {
                //noinspection ResultOfMethodCallIgnored 忽略返回值
                outputFile.delete();
            }
            FileOutputStream out = new FileOutputStream(outputFile);
            bm.compress(Bitmap.CompressFormat.JPEG, quality, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputFile.getPath();
    }

    /**
     * 根据路径获得图片信息并按比例压缩，返回bitmap
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//只解析图片边沿，获取宽高
        BitmapFactory.decodeFile(filePath, options);
        // 计算缩放比
        options.inSampleSize = calculateInSampleSize(options, LocalDisplay.widthPixel(), LocalDisplay.heightPixel());
        // 完整解析图片返回bitmap
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static int getOrientation(String pathToOriginal) {
        try {
            final int degreesToRotate;
            ExifInterface exif = new ExifInterface(pathToOriginal);
            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degreesToRotate = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degreesToRotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degreesToRotate = 270;
                    break;
                default:
                    degreesToRotate = 0;
            }
            return degreesToRotate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * This is an expensive operation that copies the image in place with the pixels rotated.
     * If possible rather use getOrientationMatrix, and set that as the imageMatrix on an ImageView.
     *
     * @param imageToOrient   Image Bitmap to orient.
     * @param degreesToRotate number of degrees to rotate the image by. If zero the original image is returned
     *                        unmodified.
     * @return The oriented bitmap. May be the imageToOrient without modification, or a new Bitmap.
     */
    public static Bitmap rotateImage(Bitmap imageToOrient, int degreesToRotate) {
        Bitmap result = imageToOrient;
        try {
            if (degreesToRotate != 0) {
                Matrix matrix = new Matrix();
                matrix.setRotate(degreesToRotate);
                result = Bitmap.createBitmap(
                        imageToOrient,
                        0,
                        0,
                        imageToOrient.getWidth(),
                        imageToOrient.getHeight(),
                        matrix,
                        true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取圆形小红点
     */
    public static Bitmap getCircleRedBitmap(int radius) {
        Bitmap bitmap = Bitmap.createBitmap(radius * 2, radius * 2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(radius, radius, radius, paint);
        return bitmap;
    }

    /**
     * drawable转换成 Bitmap
     */
    public static Bitmap convert(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(new Canvas(bitmap));
        return bitmap;

    }

    /**
     * Drawable右上角添加圆形小红点
     *
     * @param drawable
     * @param radius
     * @return
     */
    public static Bitmap getBubbleBitmap(Drawable drawable, int radius) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        drawable.setBounds(0, 0, w, h);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(w - radius, radius, radius, paint);
        return bitmap;
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
