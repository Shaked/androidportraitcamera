package demo_camera.shakedos;

import android.app.Activity;
import android.graphics.Point;
import android.hardware.Camera;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 *
 */
public class Preview extends ViewGroup implements SurfaceHolder.Callback, Serializable {
    private Activity activity;
    private CameraFragment cameraFragment;
    private List<Camera.Size> mSupportedPreviewSizes;
    private List<Camera.Size> mSupportedPictureSizes;
    private Camera.Size mPreviewSize;
    private Camera.Size mPictureSize;
    private SurfaceView sv;
    private LinearLayout footer;
    private int takenPictureQuality;

    public Preview(
            Activity activity,
            CameraFragment cameraFragment,
            SurfaceView sv,
            LinearLayout footer
    ) {
        super(activity);
        this.activity = activity;
        this.cameraFragment = cameraFragment;
        this.footer = footer;
        this.sv = sv;
        this.takenPictureQuality = takenPictureQuality;
        sv.getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (null != cameraFragment.getCamera()) {
            // force camera in portrait mode
            cameraFragment.getCamera().setDisplayOrientation(Device.getOrientation());
            try {
                cameraFragment.getCamera().setPreviewDisplay(holder);
            } catch (IOException exception) {
                //Camera doesn't work
                return;
            }
        }
    }

    /**
     * from http://stackoverflow.com/a/6394063/1211174
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        cameraFragment.close();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = resolveSize(
                getSuggestedMinimumWidth(),
                widthMeasureSpec
        );
        final int height = resolveSize(
                getSuggestedMinimumHeight(),
                heightMeasureSpec
        );
        setMeasuredDimension(width, height);
        if (null == this.cameraFragment.getCamera()) {
            return;
        }
        if (null == mSupportedPreviewSizes) {
            mSupportedPreviewSizes = this.cameraFragment.getCamera()
                    .getParameters().getSupportedPreviewSizes();
        }
        if (null == mSupportedPictureSizes) {
            mSupportedPictureSizes = this.cameraFragment.getCamera()
                    .getParameters().getSupportedPictureSizes();
        }
        if (null != mSupportedPreviewSizes) {
            mPreviewSize = getOptimalPreviewSize(
                    mSupportedPreviewSizes, width,
                    height
            );
        }
        if (null != mSupportedPictureSizes) {
            mPictureSize = getOptimalPreviewSize(
                    mSupportedPictureSizes, width,
                    height
            );
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        if (null == cameraFragment.getCamera()) {
            return;
        }
        if (sv.getHolder().getSurface() == null) {
            return;
        }
        try {
            cameraFragment.getCamera().stopPreview();
        } catch (Exception e) {
            //Camera preview failed
            finishWithError();
            return;
        }
        try {
            Camera.Parameters cameraSettings = cameraFragment.getCamera().getParameters();
            cameraSettings.setPreviewSize(
                    mPreviewSize.width,
                    mPreviewSize.height
            );
            requestLayout();
            Point point = new Point();
            this.activity.getWindowManager().getDefaultDisplay().getSize(point);
            int screenWidth = point.x;
            int screenHeight = point.y;
            if (screenWidth < screenHeight) {
                this.setCameraLayout(mPreviewSize.height, mPreviewSize.width);
            } else {
                this.setCameraLayout(mPreviewSize.width, mPreviewSize.height);
            }
            cameraFragment.getCamera().setParameters(cameraSettings);
            cameraSettings = cameraFragment.getCamera().getParameters();
            cameraSettings.setPictureSize(mPictureSize.width, mPictureSize.height);
            cameraFragment.getCamera().setParameters(cameraSettings);
            cameraFragment.setCameraParameters(cameraSettings);
            cameraFragment.getCamera().setPreviewDisplay(holder);
            cameraFragment.getCamera().startPreview();
        } catch (Exception e) {
            cameraFragment.close();
            //camera failed
            finishWithError();
        }
    }

    /**
     * calculate preview layout to be fit to full screen. This function keeps
     * the ratio of the preview so the result preview wont be stretch assume the
     * width < height so (width/height) < 1
     *
     * @param width
     * @param height
     */
    public void setCameraLayout(int width, int height) {
        float newProportion = (float) width / (float) height;
        // Get the width of the screen
        Point point = new Point();
        this.activity.getWindowManager().getDefaultDisplay().getSize(point);
        int screenWidth = point.x;
        int screenHeight = point.y;
        float screenProportion = (float) screenWidth / (float) screenHeight;
        // Get the SurfaceView layout parameters
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) this
                .getLayoutParams();
        float scaleFactor = 1;
        /*
         * assume width is smaller than height in screen and in input
		 * parameters. Therefore if newProportion > screenProportion then
		 * The desire proportion is more wider than higher therefore we match it against
		 * screen width and scale it height with the new proportion
		 *
		 */
        if (newProportion > screenProportion) {
            lp.width = screenWidth;
            lp.height = (int) ((float) screenWidth * (1 / newProportion));
            scaleFactor = (screenHeight / lp.height); // calculate the factor to make it full screen
        } else {
            lp.width = (int) (newProportion * (float) screenHeight);
            lp.height = screenHeight;
            scaleFactor = screenWidth / lp.width; // calculate the factor to make it full screen.

        }
        lp.width = (int) (lp.width * scaleFactor);
        lp.height = (int) (lp.height * scaleFactor);
        adjustFooterToFullScreen(screenHeight, lp);
        lp.gravity = Gravity.CENTER;
        sv.setLayoutParams(lp);
        this.footer.setBottom(sv.getBottom());
        this.footer.setLayoutParams(
                new LinearLayout.LayoutParams(
                        screenWidth,
                        this.footer.getHeight()
                )
        );

    }

    private void adjustFooterToFullScreen(int screenHeight, FrameLayout.LayoutParams lp) {
        if (screenHeight > lp.height) {
            lp.height += (screenHeight - lp.height);
            lp.width += (screenHeight - lp.height);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int width = r - l;
        final int height = b - t;
        if (mPreviewSize == null) {
            return;
        }
        if (0 == Device.getOrientation()) {
            this.setCameraLayout(mPreviewSize.width, mPreviewSize.height);
            return;
        }
        this.setCameraLayout(mPreviewSize.height, mPreviewSize.width);
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w,
                                              int h) {
        return getOptimalPreviewSize(sizes, w, h, -1, -1);
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w,
                                              int h, int maxWidth, int maxHeight) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) h / w;
        if (sizes == null) {
            return null;
        }
        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        int targetHeight = h;
        for (Camera.Size size : sizes) {
            if ((-1 != maxWidth && -1 != maxHeight)
                    && (maxWidth > size.width || maxHeight > size.height)) {
                optimalSize = size;
                break;
            }
            double ratio = (double) size.height / size.width;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) {
                continue;
            }
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    private void finishWithError() {
        this.activity.setResult(2);
        this.activity.finish();
    }

}
