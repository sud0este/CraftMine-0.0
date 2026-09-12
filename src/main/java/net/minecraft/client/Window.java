package net.minecraft.client;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.opengl.GL;

public final class Window {
    private final String title;
    private int width;
    private int height;
    private long handle;
    private GLFWFramebufferSizeCallback framebufferCallback;

    public Window(String title, int width, int height) {
        this.title = title; this.width = width; this.height = height;
    }

    public void create() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!GLFW.glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        handle = GLFW.glfwCreateWindow(width, height, title, 0, 0);
        if (handle == 0) throw new IllegalStateException("Unable to create GLFW window");
        GLFW.glfwMakeContextCurrent(handle);
        GLFW.glfwSwapInterval(1);
        GL.createCapabilities();
        framebufferCallback = new GLFWFramebufferSizeCallback() {
            @Override public void invoke(long window, int newWidth, int newHeight) {
                width = Math.max(1, newWidth); height = Math.max(1, newHeight);
            }
        };
        GLFW.glfwSetFramebufferSizeCallback(handle, framebufferCallback);
        GLFW.glfwShowWindow(handle);
    }

    public boolean shouldClose() { return GLFW.glfwWindowShouldClose(handle); }
    public void swapBuffers() { GLFW.glfwSwapBuffers(handle); }
    public void close() {
        if (handle != 0) GLFW.glfwDestroyWindow(handle);
        GLFW.glfwTerminate();
    }
    public long getHandle() { return handle; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
