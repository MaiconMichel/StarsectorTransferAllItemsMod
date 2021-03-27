package data.scripts;

import com.fs.starfarer.api.Global;
import org.lwjgl.opengl.Display;

import java.awt.*;

@SuppressWarnings("SpellCheckingInspection")
public final class StarsectorAppInfo {
    private static int titleBarHeight = 0;
    private static int correctionX    = 0;

    static
    {
        try {
            AppTitleBar appTitleBar = new AppTitleBar();
            titleBarHeight = appTitleBar.getHeight();
            correctionX    = appTitleBar.getCorrectionX();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static Point getMousePoint() {
        return MouseInfo.getPointerInfo().getLocation();
    }

    public static int getScreenHeight() {
        return (int)Global.getSettings().getScreenHeight();
    }

    public static int getScreenWidth() {
        return (int)Global.getSettings().getScreenWidth();
    }

    public static boolean isFullScreen() {
        return Display.isFullscreen();
    }

    public static int getX() {
        return isFullScreen() ? 0 : Display.getX() + correctionX;
    }

    public static int getY() {
        return isFullScreen() ? 0 : Display.getY() + titleBarHeight;
    }
}