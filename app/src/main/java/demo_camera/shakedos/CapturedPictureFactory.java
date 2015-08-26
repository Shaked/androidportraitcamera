package demo_camera.shakedos;

import android.content.Intent;
import android.graphics.ImageFormat;
import android.os.Environment;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;

import java.io.File;

import util.FileWriter;
import util.Rotator;

/**
 * Created by Shaked on 5/23/15.
 */
public class CapturedPictureFactory {
    private View fullScreen;
    private LinearLayout gridContainer;
    private int pictureSquareSize;
    private String extension;
    private String prefix;
    private String pathStr;

    private CapturedPictureFactory(
            SurfaceView fullScreen,
            LinearLayout gridContainer,
            int pictureSquareSize
    ) {
        this.fullScreen = fullScreen;
        this.gridContainer = gridContainer;
        this.pictureSquareSize = pictureSquareSize;
    }

    /**
     * This is a "named constructor".
     *
     * @param fullScreen
     * @param gridContainer
     * @param pictureSquareSize
     * @return CapturedPictureFactory
     */
    public static CapturedPictureFactory with(
            SurfaceView fullScreen,
            LinearLayout gridContainer,
            int pictureSquareSize
    ) {
        return new CapturedPictureFactory(
                fullScreen,
                gridContainer,
                pictureSquareSize
        );
    }

    public CapturedPictureFactory asType(int imageType) {
        switch (imageType) {
            case ImageFormat.JPEG:
                this.extension = ".jpg";
        }
        return this;
    }

    public CapturedPictureFactory prefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public CapturedPictureFactory pathStr(String pathStr) {
        this.pathStr = pathStr;
        return this;
    }

    public Intent generate(byte[] data) throws Exception {
        Intent result = new Intent();
        String pathStr = this.pathStr;
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!path.exists()) {
            path.mkdirs();
            path.createNewFile();
        }

        byte[] rotatedPicture = Rotator.rotate(data, Device.getOrientation());
        Cropped cropped = new Cropped(
                this.fullScreen.getTop(),
                this.fullScreen.getLeft(),
                this.fullScreen.getBottom(),
                this.fullScreen.getRight(),
                this.gridContainer.getRight(),
                this.gridContainer.getBottom(),
                this.gridContainer.getTop()
        );
        CroppedAndResized croppedAndResized = new CroppedAndResized(
                cropped,
                this.pictureSquareSize
        );
        FileWriter fw = new FileWriter(prefix, extension, path);
        File fileCroppedAndResized = fw.Write(croppedAndResized.generate(rotatedPicture));
        result.putExtra(CroppedAndResized.getResultName(), fileCroppedAndResized.getAbsolutePath());
        return result;
    }
}

