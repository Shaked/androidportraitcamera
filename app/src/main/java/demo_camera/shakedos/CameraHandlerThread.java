package demo_camera.shakedos;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;

/**
 * Created by Shaked on 6/29/15.
 */
public class CameraHandlerThread extends AsyncTask<Void, Void, Void> {
    private Activity activity;
    private Preview preview;
    private Wrapper wrapper;
    private ProgressDialog progressDialog;

    private CameraManager cameraManager;

    public CameraHandlerThread(
            Activity activity,
            Preview preview,
            Wrapper wrapper,
            CameraManager cameraManager
    ) {
        this.activity = activity;
        this.preview = preview;
        this.wrapper = wrapper;
        this.cameraManager = cameraManager;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(
                this.activity,
                "Loading",
                "Camera is being loaded",
                true
        );
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            cameraManager.close();
            cameraManager.open();
            cameraManager.getCamera().startPreview();
        } catch (Exception e) {
            cameraManager.close();
            publishProgress();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        this.activity.setResult(2);
        this.activity.finish();
    }

    @Override
    protected void onPostExecute(Void value) {
        preview.requestLayout();
        wrapper.getUiContainer().setVisibility(View.VISIBLE);
        wrapper.getLayout().setVisibility(View.VISIBLE);
        progressDialog.dismiss();
    }

}
