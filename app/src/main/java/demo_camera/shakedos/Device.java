package demo_camera.shakedos;

import android.os.Build;

/**
 * Created by Shaked on 6/29/15.
 */
public class Device {

    private static int orientation;

    public static void load() {
        //check if emulator is running
        if (Build.BRAND.toLowerCase().contains("generic")) {
            Device.orientation = 0;
        } else {
            Device.orientation = 90;
        }
    }

    public static int getOrientation() {
        return Device.orientation;
    }
}