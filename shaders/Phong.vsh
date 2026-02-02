#version 330

in vec3 vPosition;
in vec3 vNormal;

out vec3 mvNormal;
out vec3 mvPosition;

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;
uniform mat4 normalMatrix;

void main()
{
    vec4 mvPos = modelViewMatrix * vec4(vPosition, 1.0);
    gl_Position = projectionMatrix * mvPos;
    mvNormal = normalize(normalMatrix * vec4(vNormal, 0.0)).xyz;
    mvPosition = mvPos.xyz;
}
