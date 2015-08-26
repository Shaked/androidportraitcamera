package demo_camera.shakedos;

import android.hardware.Camera;
import android.os.AsyncTask;
import android.view.View;

/**
 * Created by Shaked on 8/5/15.
 */
public class TakePictureTask extends AsyncTask<Boolean, byte[], Void> {
    private MainActivity activity;
    private CameraFragment cameraFragment;
    private Wrapper wrapper;
    private Picture picture;

    public TakePictureTask(
            MainActivity activity,
            CameraFragment cameraFragment,
            Wrapper wrapper,
            Picture picture
    ) {
        this.cameraFragment = cameraFragment;
        this.wrapper = wrapper;
        this.picture = picture;
    }

    @Override
    public Void doInBackground(Boolean... data) {
        if (null == cameraFragment.getCamera()) {
            return null;
        }
        Camera.Parameters parameters = cameraFragment.getCamera().getParameters();
        String focus_mode = parameters.getFocusMode();
        boolean focusSupported = data[0];
        if (focus_mode.equals(Camera.Parameters.FOCUS_MODE_AUTO) && focusSupported) {
            cameraFragment.getCamera().autoFocus(
                    new Camera.AutoFocusCallback() {
                        @Override
                        public void onAutoFocus(boolean success, Camera camera) {
                            takePicture();
                        }
                    }
            );
        } else {
            takePicture();
        }
        return null;
    }

    private void takePicture() {
        cameraFragment.getCamera().takePicture(
                null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        //taken from
                        //https://github
                        // .com/commonsguy/cwac-camera/blob/4bf4488a88fa17835db849ebc580e4a0e4f1e6b7/camera/src/com/commonsware/cwac/camera/CameraView.java#L705
                        cameraFragment.getCamera().setParameters(
                                cameraFragment.getCameraParameters
                                        ()
                        );
                        publishProgress(data);
                    }
                }
        );
    }

    @Override
    public void onProgressUpdate(byte[]... data) {
        try {
            this.picture.setCurrentPicture(data[0]);
        } catch (Exception e) {
            this.activity.finishWithError();
        }
    }

    public void onPostExecute(Void result) {
        this.wrapper.getApproveButton().setClickable(true);
        this.wrapper.getSnapButton().setVisibility(View.GONE);
        this.wrapper.getApproveButton().setVisibility(View.VISIBLE);
        this.wrapper.getBtnRetake().setVisibility(View.VISIBLE);

        if (null == this.picture.getCurrentPicture()) {
            return;
        }
        //on some devices, when taking a picture, the camera preview doesn't freeze.
        if (null != cameraFragment.getCamera()) {
            try {
                cameraFragment.getCamera().stopPreview();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }

    }
}
