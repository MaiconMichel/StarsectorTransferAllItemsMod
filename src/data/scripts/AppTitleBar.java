package data.scripts;

import java.awt.*;
import java.awt.image.BufferedImage;

public class AppTitleBar {
    private static final int MAX_PIXELS_X = 10;
    private static final int MAX_PIXELS_Y = 50;
    //First 3 vertical pixels of the upper left menu.
    private static final Color[] PIXEL_COLORS = {new Color( 58,  52,  48),
                                                 new Color(113, 110, 107),
                                                 new Color(137, 134, 132)};
    private int height = 0;
    private int correctionX = 0;
    private Robot robot;

    public int getHeight() {
        return height;
    }

    public int getCorrectionX() {
        return correctionX;
    }

    public AppTitleBar() throws AWTException {
        if (!StarsectorAppInfo.isFullScreen()) {
            robot = new Robot();
            height = calcHeight(StarsectorAppInfo.getX(), StarsectorAppInfo.getY());
        }
    }

    private int calcHeight(int startX, int startY) {
        BufferedImage image = createScreenCapture(startX, startY, MAX_PIXELS_X, MAX_PIXELS_Y);

        try {
            for (int x = 0; x < MAX_PIXELS_X; x++) {
                for (int y = 0; y < MAX_PIXELS_Y; y++) {
                    if (sameColor(image, x, y)) {
                        correctionX = x;
                        return y - 1;
                    }
                }
            }
        } finally {
            image.flush();
        }

        return 0;
    }

    private boolean sameColor(BufferedImage image, int x, int y) {
        for (int i = 0; i < PIXEL_COLORS.length; i++) {
            if (image.getRGB(x, y + i) != PIXEL_COLORS[i].getRGB()) {
                return false;
            }
        }

        return true;
    }

    private BufferedImage createScreenCapture(int x, int y, int width, int height) {
        return robot.createScreenCapture(new Rectangle(x, y, width, height));
    }
}