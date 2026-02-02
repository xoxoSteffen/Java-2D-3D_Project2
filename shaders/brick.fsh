#version 330

// Light
uniform vec3 lPosition;

in vec3 position;
in vec3 normal;
in vec2 mcPosition;

out vec4 fragmentColor;

const vec3 BrickColor = vec3(1.0, 0.3, 0.2);
const vec3 MortarColor = vec3(0.85, 0.86, 0.84);
const vec2 BrickSize = vec2(0.3, 0.15);
const vec2 BrickPct = vec2(0.9, 0.85);

void main() {
  vec3 n = normalize(normal);
  vec3 l = normalize(lPosition - position);
  float illumination = 0.2 + 0.8*clamp(0, dot(n, l), 1);

  vec3 color;

  vec2 pos;
  vec2 useBrick;

  pos = mcPosition / BrickSize;
  if (fract(pos.y * 0.5) > 0.5) {
    pos.x += 0.5;
  }

  pos = fract(pos);
  useBrick = step(pos, BrickPct);

  color = mix(MortarColor, BrickColor, useBrick.x * useBrick.y);
  color *= illumination;

  fragmentColor = vec4(color, 1.0);
}