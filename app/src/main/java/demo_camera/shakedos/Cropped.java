package demo_camera.shakedos;

import util.CroppingTool;

/**
 * Created by Shaked on 5/22/15.
 */
public class Cropped implements GeneratorType {
    private int previewTop;
    private int previewLeft;
    private int previewBottom;
    private int previewRight;
    private int gridRight;
    private int gridBottom;
    private int gridTop;
    private int pictureSquareSize;

    public Cropped(
            int previewTop,
            int previewLeft,
            int previewBottom,
            int previewRight,
            int gridRight,
            int gridBottom,
            int gridTop) {
        this.previewTop = previewTop;
        this.previewLeft = previewLeft;
        this.previewBottom = previewBottom;
        this.previewRight = previewRight;
        this.gridRight = gridRight;
        this.gridBottom = gridBottom;
        this.gridTop = gridTop;
    }

    public byte[] generate(byte[] data) {
        byte[] cropped = CroppingTool.cropRect(
                data,
                previewTop,
                previewLeft,
                previewBottom,
                previewRight,
                gridRight,
                gridBottom,
                gridTop
        );
        return cropped;
    }

    public static String getResultName() {
        return "PictureCropped";
    }

}
