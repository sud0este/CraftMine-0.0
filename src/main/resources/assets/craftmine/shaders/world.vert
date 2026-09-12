#version 330 core
layout(location = 0) in vec3 aPosition;
layout(location = 1) in vec2 aUv;
layout(location = 2) in vec3 aColor;
layout(location = 3) in float aLight;

uniform mat4 uViewProjection;
uniform mat4 uModel;

out vec2 vUv;
out vec3 vColor;
out float vLight;

void main() {
    gl_Position = uViewProjection * uModel * vec4(aPosition, 1.0);
    vUv = aUv;
    vColor = aColor;
    vLight = aLight;
}
