#version 330

uniform mat4 modelViewMatrix;
uniform mat4 normalMatrix;
uniform mat4 projectionMatrix;

in vec3 vPosition;
in vec3 vNormal;

out vec3 position;
out vec3 normal;
out vec2 mcPosition;

void main() {
  normal = normalize(normalMatrix * vec4(vNormal, 0.0)).xyz;
  position = (modelViewMatrix * vec4(vPosition, 1.0)).xyz;
  mcPosition = vPosition.xy;
  gl_Position = projectionMatrix * modelViewMatrix * vec4(vPosition, 1.0);
}
