package demo_camera.shakedos;

import util.Resizer;

/**
 * Created by Shaked on 5/22/15.
 */
public class CroppedAndResized implements GeneratorType {
    private Cropped cropped;
    private int pictureSquareSize;

    public CroppedAndResized(Cropped cropped, int pictureSquareSize) {
        this.cropped = cropped;
        this.pictureSquareSize = pictureSquareSize;
    }

    public byte[] generate(byte[] data) {
        byte[] croppedPicture = this.cropped.generate(data);
        byte[] resized = Resizer.resize(
                croppedPicture,
                pictureSquareSize
        );
        return resized;
    }

    public static String getResultName() {
        return "PictureCroppedAndResized";
    }
}
