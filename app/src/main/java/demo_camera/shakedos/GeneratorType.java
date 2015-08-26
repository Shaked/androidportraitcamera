package demo_camera.shakedos;

import java.io.IOException;

/**
 * Created by Shaked on 5/22/15.
 */
public interface GeneratorType {
    public byte[] generate(byte[] data) throws IOException;
}

