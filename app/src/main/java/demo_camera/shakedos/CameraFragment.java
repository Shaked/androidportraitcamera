package demo_camera.shakedos;

import android.app.Fragment;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Shaked on 8/19/15.
 */
public class CameraFragment extends Fragment {
    private CameraManager cameraManager;
    private Preview preview;
    private Wrapper wrapper;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        this.cameraManager = new CameraManager();
        this.wrapper = (Wrapper)getArguments().getSerializable("wrapper");

        this.preview = new Preview(
                this.getActivity(),
                this,
                this.wrapper.getFullScreen(),
                this.wrapper.getFooter()
        );
        this.wrapper.getLayout().addView(this.preview);
        return this.preview;
    }

    @Override
    public void onResume() {
        super.onResume();
        int numCams = Camera.getNumberOfCameras();
        if (numCams > 0) {
            (new CameraHandlerThread(
                    this.getActivity(),
                    this.preview,
                    this.wrapper,
                    cameraManager
            )).execute();
        }
    }

    public Camera getCamera() {
        return cameraManager.getCamera();
    }

    public void setCameraParameters(Camera.Parameters cameraParameters) {
        this.cameraManager.setCameraParameters(cameraParameters);
    }

    public Camera.Parameters getCameraParameters() {
        return this.cameraManager.getCameraParameters();
    }

    public void close() {
        this.cameraManager.close();
    }
}
