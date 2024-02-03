package utils;

import java.awt.*;

public class MouseUtils {
    public PointerInfo pointer = MouseInfo.getPointerInfo();
    public Point point = pointer.getLocation();
    public int x = point.x;
    public int y = point.y;

    public boolean isInWindow(int mouseX, int mouseY, int windowX, int windowY, int windowWidth, int windowHeight) {
        // System.out.println("mouseX: " + mouseX + " mouseY: " + mouseY + " windowX: " + windowX + " windowY: " + windowY + " winWidth: " + windowWidth + " winHeight: " + windowHeight);
        return (((windowX < mouseX) && (mouseX < windowX + windowWidth)) && ((windowY < mouseY) && (mouseY < windowY + windowHeight)));
    }


}
