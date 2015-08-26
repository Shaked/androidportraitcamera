package demo_camera.shakedos;

import android.app.Activity;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Shaked on 8/5/15.
 */
public class ApprovePictureTask extends AsyncTask<Void, Void, Intent> {
    private MainActivity activity;
    private LinearLayout gridContainer;
    private SurfaceView fullScreen;
    private String pathStr;
    private int pictureSquareSize;
    private Picture picture;

    public ApprovePictureTask(
            MainActivity activity,
            LinearLayout gridContainer,
            SurfaceView fullScreen,
            String pathStr,
            int pictureSquareSize,
            Picture picture
    ) {
        this.activity = activity;
        this.gridContainer = gridContainer;
        this.fullScreen = fullScreen;
        this.pathStr = pathStr;
        this.pictureSquareSize = pictureSquareSize;
        this.picture = picture;
    }

    @Override
    public Intent doInBackground(Void... params) {
        Intent newPicture = null;
        try {
            newPicture = CapturedPictureFactory.with(
                    fullScreen,
                    gridContainer,
                    pictureSquareSize
            ).
                    pathStr(pathStr).
                    asType(ImageFormat.JPEG).
                    prefix("ShakedosExample").
                    generate(picture.getCurrentPicture());
        } catch (Exception e) {
            Log.v("ApprovePictureTask", e.toString());
            e.printStackTrace();
            publishProgress();
        }
        return newPicture;
    }

    @Override
    public void onProgressUpdate(Void... params) {
        this.activity.finishWithError();
    }

    @Override
    public void onPostExecute(Intent newPicture) {
        if (newPicture == null) {
            this.activity.finishWithError();
            return;
        }
        this.activity.setResult(
                Activity.RESULT_OK,
                newPicture
        );
        galleryAddPic((String) newPicture.getExtras().get(CroppedAndResized.getResultName()));
        this.activity.finish();
    }

    /**
     * A mess.
     * @param image
     */
    private void galleryAddPic(String image) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File("file:" + image);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.activity.sendBroadcast(mediaScanIntent);
        MediaScannerConnection.scanFile(
                this.activity.getApplicationContext(), new String[]{image}, null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        // TODO Auto-generated method stub
                    }
                }
        );
        Toast.makeText(
                this.activity.getApplicationContext(), "Picture added to photo album:" + "file:"
                        + image,
                Toast.LENGTH_LONG
        ).show();

    }
}
