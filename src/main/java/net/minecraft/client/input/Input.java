package net.minecraft.client.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

/** GLFW adapter. The simulation only sees this small, engine-independent input surface. */
public final class Input {
    private final long window;
    private final boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST + 1];
    private final boolean[] keyPresses = new boolean[GLFW.GLFW_KEY_LAST + 1];
    private final boolean[] mouseButtons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST + 1];
    private final boolean[] mousePresses = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST + 1];
    private double mouseX;
    private double mouseY;
    private double mouseDeltaX;
    private double mouseDeltaY;
    private int scroll;
    private boolean firstMouse = true;
    private boolean cursorCaptured = true;

    public Input(long window) {
        this.window = window;
        GLFW.glfwSetKeyCallback(window, new GLFWKeyCallback() {
            @Override public void invoke(long handle, int key, int scancode, int action, int mods) {
                if (key < 0 || key >= keys.length) return;
                if (action == GLFW.GLFW_PRESS) keyPresses[key] = true;
                keys[key] = action != GLFW.GLFW_RELEASE;
            }
        });
        GLFW.glfwSetMouseButtonCallback(window, new GLFWMouseButtonCallback() {
            @Override public void invoke(long handle, int button, int action, int mods) {
                if (button < 0 || button >= mouseButtons.length) return;
                if (action == GLFW.GLFW_PRESS) mousePresses[button] = true;
                mouseButtons[button] = action != GLFW.GLFW_RELEASE;
            }
        });
        GLFW.glfwSetCursorPosCallback(window, new GLFWCursorPosCallback() {
            @Override public void invoke(long handle, double x, double y) {
                if (firstMouse) { mouseX = x; mouseY = y; firstMouse = false; return; }
                mouseDeltaX += x - mouseX;
                mouseDeltaY += y - mouseY;
                mouseX = x; mouseY = y;
            }
        });
        GLFW.glfwSetScrollCallback(window, new GLFWScrollCallback() {
            @Override public void invoke(long handle, double xOffset, double yOffset) { scroll += (int) Math.signum(yOffset); }
        });
        setCursorCaptured(true);
    }

    public void poll() { GLFW.glfwPollEvents(); }
    public boolean isKeyDown(int key) { return key >= 0 && key < keys.length && keys[key]; }
    public boolean consumeKeyPress(int key) {
        if (key < 0 || key >= keyPresses.length) return false;
        boolean result = keyPresses[key]; keyPresses[key] = false; return result;
    }
    public boolean isMouseDown(int button) { return button >= 0 && button < mouseButtons.length && mouseButtons[button]; }
    public boolean consumeMousePress(int button) {
        if (button < 0 || button >= mousePresses.length) return false;
        boolean result = mousePresses[button]; mousePresses[button] = false; return result;
    }
    public double consumeMouseDeltaX() { double result = mouseDeltaX; mouseDeltaX = 0; return result; }
    public double consumeMouseDeltaY() { double result = mouseDeltaY; mouseDeltaY = 0; return result; }
    public int consumeScroll() { int result = scroll; scroll = 0; return result; }
    public double getMouseX() { return mouseX; }
    public double getMouseY() { return mouseY; }

    public void setCursorCaptured(boolean captured) {
        cursorCaptured = captured;
        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, captured ? GLFW.GLFW_CURSOR_DISABLED : GLFW.GLFW_CURSOR_NORMAL);
        firstMouse = true;
        mouseDeltaX = mouseDeltaY = 0;
    }
    public boolean isCursorCaptured() { return cursorCaptured; }
}
