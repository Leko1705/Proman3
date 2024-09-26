package proman.filesys;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Resources {

    public static Icon loadResourceIcon(String name) {
        String location = FileSystem.getInstallationPath();
        location += File.separator + "images";

        File imageDir = new File(location);
        imageDir.mkdirs();

        try {
            File imageFile = new File(imageDir, name);
            return new ImageIcon(ImageIO.read(imageFile));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Icon loadIcon(String path) {
        File imageFile = new File(path);

        if (!imageFile.exists())
            return null;

        if (imageFile.isDirectory())
            return null;

        try {
            return new ImageIcon(ImageIO.read(imageFile));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
