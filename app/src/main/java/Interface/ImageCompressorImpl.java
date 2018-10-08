package Interface;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ImageCompressorImpl implements ImageCompressor {


    private Bitmap scaledBitmap, originalBitmap;
    private InputStream inputStream;
    private float maxHeight, maxWidth, imgRatio, maxRatio, ratioX, ratioY, middleX, middleY;
    private int actualHeight, actualWidth;
    private Canvas canvas;
    private Matrix scaleMatrix;

    @Override
    public Bitmap compressBitmap(Context context, Uri imageUri) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inJustDecodeBounds = true;

            inputStream = context.getContentResolver().openInputStream(imageUri);

            originalBitmap = BitmapFactory.decodeStream(inputStream, null, options);

            actualHeight = options.outHeight;
            actualWidth = options.outWidth;

            if (actualHeight > actualWidth) {
                //its portrait.
                maxHeight = 816;
                maxWidth = 612;
            } else {
                //its landscape.
                maxHeight = 612;
                maxWidth = 816;
            }

            //calculate image ratio of original image.
            imgRatio = actualWidth / actualHeight;

            //calculate image ratio for required image.
            maxRatio = maxWidth / maxHeight;

            //width and height values are set maintaining the aspect ratio of the image
            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;
                }
            }

            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

            options.inJustDecodeBounds = false;

            options.inTempStorage = new byte[16 * 1024];

            inputStream = context.getContentResolver().openInputStream(imageUri);

            originalBitmap = BitmapFactory.decodeStream(inputStream, null, options);

            // calculate x-axis and y-axis ratios.
            ratioX = actualWidth / (float) options.outWidth;
            ratioY = actualHeight / (float) options.outHeight;

            // calculate x-axis and y-axis middle points.
            middleX = actualWidth / 2.0f;
            middleY = actualHeight / 2.0f;

            scaleMatrix = new Matrix();

            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);

            canvas = new Canvas(scaledBitmap);

            canvas.setMatrix(scaleMatrix);

            canvas.drawBitmap(originalBitmap, middleX - originalBitmap.getWidth() / 2, middleY - originalBitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), scaleMatrix, true);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return scaledBitmap;
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {

            final int heightRatio = Math.round((float) height / (float) reqHeight);

            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        return inSampleSize;
    }


}
