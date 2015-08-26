package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileWriter {
    private String prefix;
    private String suffix;
    private File path;

    public FileWriter(String prefix, String suffix, File path){
        this.prefix = prefix;
        this.suffix = suffix;
        this.path = path;
    }

    public File Write(byte[] data) throws IOException {
        File f = File.createTempFile(prefix, suffix, path);
        return writeToFile(f, data);
    }

    /**
     * write file bytes into a file
     * @param file
     * @param data
     * @return
     * @throws IOException
     */
    public static File writeToFile(File file,byte[] data) throws IOException {
        FileOutputStream fos = null;
        try {
            fos =  new FileOutputStream(file);
            fos.write(data);
            fos.flush();
        } catch (IOException e){

        } finally{
            if (fos != null){
                fos.close();
            }
        }

        return file;
    }

}
