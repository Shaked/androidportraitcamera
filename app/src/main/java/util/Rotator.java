package util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;

/**
 * Created by Shaked on 6/15/15.
 */
public class Rotator {
    final static private String TAG = "ROTATE";
    public static byte[] rotate(byte[] data, int orientation) {
        Matrix matrix = new Matrix();
        matrix.postRotate(orientation);
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        if (bitmap.getWidth() < bitmap.getHeight()) {
            return data;
        }

        Bitmap rotatedBitmap = Bitmap.createBitmap(
                bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true
        );
        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, blob);
        byte[] bitmapData = blob.toByteArray();
        return bitmapData;
    }
}
