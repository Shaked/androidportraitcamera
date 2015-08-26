package demo_camera.shakedos;

/**
 * Created by Shaked on 8/5/15.
 */
public class Picture {
    private byte[] currentPicture;

    public void setCurrentPicture(byte[] currentPicture) {
        this.currentPicture = currentPicture;
    }

    public byte[] getCurrentPicture() {
        return this.currentPicture;
    }
}
