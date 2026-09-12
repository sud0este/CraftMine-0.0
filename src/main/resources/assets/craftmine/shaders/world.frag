#version 330 core
in vec2 vUv;
in vec3 vColor;
in float vLight;

uniform sampler2D uAtlas;
uniform vec4 uTint;

out vec4 fragColor;

void main() {
    vec4 texel = texture(uAtlas, vUv);
    if (texel.a < 0.05) discard;
    float light = 0.35 + 0.65 * clamp(vLight, 0.0, 1.0);
    fragColor = vec4(texel.rgb * vColor * light, texel.a) * uTint;
}
