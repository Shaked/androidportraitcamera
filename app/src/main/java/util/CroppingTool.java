package util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;

import java.io.ByteArrayOutputStream;

/**
 * Created by Shaked on 6/15/15.
 */
public class CroppingTool {
    public static byte[] cropRect(
            byte data[],
            int previewTop,
            int previewLeft,
            int previewBottom,
            int previewRight,
            int gridRight,
            int gridBottom,
            int gridTop
    ) {
        Bitmap bitmapPicture = BitmapFactory.decodeByteArray(data, 0, data.length);
        // Here previewRect is a rectangle which holds the camera's preview size,
        // pictureRect and nativeResRect hold the camera's picture size and its
        // native resolution, respectively.
        RectF previewRect = new RectF(previewLeft, previewTop, previewRight, previewBottom);
        RectF pictureRect = new RectF(0, 0, bitmapPicture.getWidth(), bitmapPicture.getHeight());
        RectF resultRect = new RectF(0, gridTop, gridRight, gridBottom);
        final Matrix scaleMatrix = new Matrix();
        // map the result rectangle to the new coordinates
        scaleMatrix.mapRect(resultRect);
        // create a matrix which scales coordinates of picture size rectangle into the
        // camera's native resolution.
        scaleMatrix.setRectToRect(previewRect, pictureRect, Matrix.ScaleToFit.CENTER);
        // invert it, so that we get the matrix which downscales the rectangle from
        // the native resolution to the actual picture size
        scaleMatrix.mapRect(resultRect);
        Bitmap cropped = Bitmap.createBitmap(
                bitmapPicture,
                (int) resultRect.left,
                (int) resultRect.top,
                (int) (resultRect.width()),
                (int) (resultRect.height())
        );
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        // create a file to write bitmap data
        cropped.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        // Convert bitmap to byte array
        byte[] bm = bos.toByteArray();
        return bm;
    }
}
