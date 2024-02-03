package utils;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtils {
    public static void drawCircle(float x, float y, float radius, Color color) {
        glPushMatrix();
        glBegin(GL_LINE_LOOP);

        glColor4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        for (int i = 0; i <= 360; i++) {
            glVertex2f((float) Math.cos(i * Math.PI / 180) * radius + x,(float) Math.sin(i * Math.PI / 180) * radius + y);
        }

        glEnd();
        glPopMatrix();
    }

    public static void drawCircle(float x, float y, float z ,float radius, Color color) {
        glPushMatrix();
        glBegin(GL_LINE_LOOP);

        glColor4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        for (int i = 0; i <= 360; i++) {
            glVertex3f((float) Math.cos(i * Math.PI / 180) * radius + x,(float) Math.sin(i * Math.PI / 180) * radius + y, z);
        }

        glEnd();
        glPopMatrix();
    }
}
