package demo_camera.shakedos;

import android.app.Activity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.io.Serializable;

/**
 * Created by Shaked on 6/29/15.
 */
public class Wrapper implements Serializable {
    private FrameLayout layout;
    private LinearLayout footer;
    private SurfaceView fullScreen;
    private LinearLayout uiContainer;
    private ImageButton snapButton;
    private ImageButton approveButton;
    private ImageButton btnRetake;
    private LinearLayout gridContainer;

    public Wrapper(Activity activity) {
        layout = (FrameLayout) findViewById(activity, "layout");
        footer = (LinearLayout) findViewById(activity, "footer");
        fullScreen = (SurfaceView) findViewById(activity, "fullScreen");
        uiContainer = (LinearLayout) findViewById(activity, "uiContainer");
        snapButton = (ImageButton) findViewById(activity, "snapButton");
        approveButton = (ImageButton) findViewById(activity, "approveButton");
        btnRetake = (ImageButton) findViewById(activity, "btnRetake");
        gridContainer = (LinearLayout) findViewById(activity, "gridContainer");
    }

    private View findViewById(Activity activity, String id) {
        return activity.findViewById(
                activity.getResources().getIdentifier(
                        id, "id",
                        activity.getPackageName()
                )
        );
    }

    public FrameLayout getLayout() { return layout; }

    public LinearLayout getFooter() { return footer; }

    public SurfaceView getFullScreen() { return fullScreen; }

    public LinearLayout getUiContainer() { return uiContainer; }

    public ImageButton getSnapButton() { return snapButton; }

    public ImageButton getApproveButton() { return approveButton; }

    public ImageButton getBtnRetake() { return btnRetake; }

    public LinearLayout getGridContainer() { return gridContainer; }
}
