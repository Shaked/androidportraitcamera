package util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by Shaked on 6/15/15.
 *
 * @Todo maybe think about a better name as the word "Resizer" is not an English word.
 */
public class Resizer {
    private final static String TAG = "RESIZER";
    public static final int INT_RESIZE_BUILTIN = 1;

    /**
     * @return
     * @see "http://stackoverflow.com/questions/4837715/how-to-resize-a-bitmap-in-android"
     */
    public static byte[] resize(byte[] data, int pictureSquareSize) {
        Bitmap resizedBitmap;
        Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
        resizedBitmap = Bitmap.createScaledBitmap(
                bm, pictureSquareSize,
                pictureSquareSize, true
        );
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        // create a file to write bitmap data
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        // Convert bitmap to byte array
        return bos.toByteArray();
    }
}
