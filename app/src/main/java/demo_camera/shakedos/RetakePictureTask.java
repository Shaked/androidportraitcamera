package demo_camera.shakedos;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Shaked on 8/19/15.
 */
public class RetakePictureTask extends AsyncTask<Void, RuntimeException, Void> {
    private Wrapper wrapper;
    private CameraFragment cameraFragment;

    public RetakePictureTask(Wrapper wrapper, CameraFragment cameraFragment) {
        this.wrapper = wrapper;
        this.cameraFragment = cameraFragment;
    }

    @Override
    public void onPreExecute() {
    }

    @Override
    public Void doInBackground(Void... params) {
        try {
            this.cameraFragment.getCamera().startPreview();
        } catch (RuntimeException e) {
            this.cameraFragment.close();
            this.publishProgress(e);
        }
        return null;
    }

    @Override
    public void onProgressUpdate(RuntimeException... params) {
        MainActivity activity = (MainActivity) this.cameraFragment.getActivity();
        activity.finishWithError();
        Toast.makeText(
                activity.getApplicationContext(), params[0].getMessage(), Toast.LENGTH_LONG
        ).show();
    }

    @Override
    public void onPostExecute(Void result) {
        this.wrapper.getBtnRetake().setVisibility(View.GONE);
        this.wrapper.getApproveButton().setVisibility(View.GONE);
        this.wrapper.getSnapButton().setVisibility(View.VISIBLE);
    }
}
