package lo1qsk1der;

import java.awt.*;
import java.nio.IntBuffer;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;
import utils.MouseUtils;
import utils.RenderUtils;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {
    private static boolean isPressed = false;
    private static int begin_mouse_posX, begin_mouse_posY, begin_window_posX, begin_window_posY;

    // ErrorCallBack - Print error in terminal
    private static final GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(System.err);

    // KeyCallBack - In this para code, I will show you press the ESC key to stop
    private static final GLFWKeyCallback keyCallback = new GLFWKeyCallback() {

        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                glfwSetWindowShouldClose(window, true);
            }
        }
    };

    // MouseCallBack - I will show you how to detective mouse EVENT
    private static final GLFWMouseButtonCallback callback = new GLFWMouseButtonCallback() {
        @Override
        public void invoke(long window, int button, int action, int mods) {
            if (button == GLFW_MOUSE_BUTTON_1)
                isPressed = action == GLFW_PRESS;
        }
    };

    // Is focused window
    private static boolean isFocusedWindow(long window) {
        return glfwGetWindowAttrib(window, GLFW_FOCUSED) == 1;
    }

    // This is main function
    public static void main(String[] args) {
        long window;

        // Set error call back
        glfwSetErrorCallback(errorCallback);

        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Delete border
        glfwWindowHint(GLFW_TRANSPARENT_FRAMEBUFFER, GLFW_TRUE);
        glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);  // glfwWindowHint(arg, state);

        // Create window
        int winWidth = 500, winHeight = 500;
        float opacity = 255;

        window = glfwCreateWindow(winWidth, winHeight, "Simple example", NULL, NULL);
        glfwSetWindowOpacity(window, opacity / 255f);

        if (window == NULL) {
            glfwTerminate();
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Change the window's position to the center of the screen
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if (vidMode != null) {
            glfwSetWindowPos(window, (vidMode.width() - winWidth) / 2, (vidMode.height() - winHeight) / 2);
        }

        // Create opengl context
        glfwMakeContextCurrent(window);
        GL.createCapabilities();

        // Enable vertical synchronization
        glfwSwapInterval(1);

        // Set different kinds of call back
        glfwSetKeyCallback(window, keyCallback);
        glfwSetMouseButtonCallback(window, callback);

        // Declare buffers for using inside the loop
        IntBuffer width = MemoryUtil.memAllocInt(1);
        IntBuffer height = MemoryUtil.memAllocInt(1);

        // Loop until window gets closed
        for (;!glfwWindowShouldClose(window);) {
            float ratio;

            int[] posX = new int[1];
            int[] posY = new int[1];
            glfwGetWindowPos(window,posX,posY);

            if (!isPressed) {
                begin_mouse_posX = new MouseUtils().x;
                begin_mouse_posY = new MouseUtils().y;
                begin_window_posX = posX[0];
                begin_window_posY = posY[0];
            }
            int deltaX = (new MouseUtils().x - begin_mouse_posX),deltaY = (new MouseUtils().y - begin_mouse_posY);

            if (new MouseUtils().isInWindow(new MouseUtils().x, new MouseUtils().y, posX[0], posY[0], winWidth, winHeight) && isPressed && isFocusedWindow(window))
               glfwSetWindowPos(window, begin_window_posX + deltaX, begin_window_posY + deltaY);
            // System.out.println(deltaX + ", " + deltaY + (isPressed ? " Is Pressed" : " Not pressed"));

            // Get width and height to calculate the ratio
            glfwGetFramebufferSize(window, width, height);
            ratio = width.get() / (float) height.get();

            // Rewind buffers for next get
            width.rewind();
            height.rewind();

            // Set viewport and clear screen
            glViewport(0, 0, width.get(), height.get());
            glClear(GL_COLOR_BUFFER_BIT);

            // Set orthographic projection
            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            glOrtho(-ratio, ratio, -1f, 1f, 1f, -1f);
            glMatrixMode(GL_MODELVIEW);

            // Rotate matrix
            glLoadIdentity();
            glRotatef((float) glfwGetTime() * 50f, 0f, 1f, 0f); //glfwGetTime() 初始值0，类型是浮点，单位是秒

            // Begin render
            glBegin(GL_LINE_LOOP);

            RenderUtils.drawCircle(0f,0f,0f,0.5f, Color.RED);
            RenderUtils.drawCircle(0f,0f,-0.05f,0.5f, Color.BLACK);

            glEnd();

            // Swap buffers and poll Events
            glfwSwapBuffers(window);
            glfwPollEvents();

            // Flip buffers for next loop
            width.flip();
            height.flip();
        }

        // Free buffers
        MemoryUtil.memFree(width);
        MemoryUtil.memFree(height);

        // Release window and its callbacks
        glfwDestroyWindow(window);
        keyCallback.free();

        // Terminate GLFW and release the error callback
        glfwTerminate();
        errorCallback.free();
    }
}