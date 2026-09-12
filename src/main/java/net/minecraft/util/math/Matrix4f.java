package net.minecraft.util.math;

import java.nio.FloatBuffer;

/** Small column-major matrix implementation so rendering has no math engine dependency. */
public final class Matrix4f {
    private final float[] values = new float[16];

    public Matrix4f() { setIdentity(); }

    public Matrix4f setIdentity() {
        for (int i = 0; i < values.length; i++) values[i] = 0.0f;
        values[0] = values[5] = values[10] = values[15] = 1.0f;
        return this;
    }

    public Matrix4f copy() {
        Matrix4f copy = new Matrix4f();
        System.arraycopy(values, 0, copy.values, 0, values.length);
        return copy;
    }

    public Matrix4f multiply(Matrix4f right) {
        float[] result = new float[16];
        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 4; column++) {
                float value = 0.0f;
                for (int k = 0; k < 4; k++) value += values[row + k * 4] * right.values[k + column * 4];
                result[row + column * 4] = value;
            }
        }
        System.arraycopy(result, 0, values, 0, values.length);
        return this;
    }

    public Matrix4f translate(float x, float y, float z) {
        Matrix4f translation = new Matrix4f();
        translation.values[12] = x;
        translation.values[13] = y;
        translation.values[14] = z;
        return multiply(translation);
    }

    public Matrix4f scale(float x, float y, float z) {
        Matrix4f scaling = new Matrix4f();
        scaling.values[0] = x;
        scaling.values[5] = y;
        scaling.values[10] = z;
        return multiply(scaling);
    }

    public Matrix4f rotateY(float radians) {
        Matrix4f rotation = new Matrix4f();
        float c = (float) Math.cos(radians);
        float s = (float) Math.sin(radians);
        rotation.values[0] = c;
        rotation.values[2] = -s;
        rotation.values[8] = s;
        rotation.values[10] = c;
        return multiply(rotation);
    }

    public Matrix4f rotateX(float radians) {
        Matrix4f rotation = new Matrix4f();
        float c = (float) Math.cos(radians);
        float s = (float) Math.sin(radians);
        rotation.values[5] = c;
        rotation.values[6] = s;
        rotation.values[9] = -s;
        rotation.values[10] = c;
        return multiply(rotation);
    }

    public static Matrix4f perspective(float fovRadians, float aspect, float near, float far) {
        Matrix4f matrix = new Matrix4f();
        for (int i = 0; i < 16; i++) matrix.values[i] = 0.0f;
        float f = 1.0f / (float) Math.tan(fovRadians / 2.0f);
        matrix.values[0] = f / aspect;
        matrix.values[5] = f;
        matrix.values[10] = (far + near) / (near - far);
        matrix.values[11] = -1.0f;
        matrix.values[14] = (2.0f * far * near) / (near - far);
        return matrix;
    }

    public static Matrix4f lookAt(Vec3 eye, Vec3 center, Vec3 up) {
        Vec3 forward = center.subtract(eye).normalize();
        Vec3 side = forward.cross(up).normalize();
        Vec3 correctedUp = side.cross(forward);
        Matrix4f matrix = new Matrix4f();
        matrix.values[0] = (float) side.x;
        matrix.values[4] = (float) side.y;
        matrix.values[8] = (float) side.z;
        matrix.values[1] = (float) correctedUp.x;
        matrix.values[5] = (float) correctedUp.y;
        matrix.values[9] = (float) correctedUp.z;
        matrix.values[2] = (float) -forward.x;
        matrix.values[6] = (float) -forward.y;
        matrix.values[10] = (float) -forward.z;
        matrix.values[12] = (float) -side.dot(eye);
        matrix.values[13] = (float) -correctedUp.dot(eye);
        matrix.values[14] = (float) forward.dot(eye);
        return matrix;
    }

    public FloatBuffer store(FloatBuffer buffer) {
        buffer.clear();
        buffer.put(values);
        buffer.flip();
        return buffer;
    }

    public float[] raw() { return values; }
}
