#version 330 core
layout (location = 0) in vec3 position;
layout (location = 1) in vec3 normal;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

out vec3 vColor;

void main() {
    gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 1.0);

    // normal [-1,1] to color [0,1]
    vColor = 0.5 * (normalize(normal) + vec3(1.0));
}
