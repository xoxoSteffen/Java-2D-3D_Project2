#version 330

in vec3 mvNormal;
in vec3 mvPosition;

out vec4 fragColor;

// Light
uniform vec3 lColor;
uniform vec3 lPosition;
uniform float lIntensity;

// Material
uniform vec4 ambient;
uniform vec4 diffuse;
uniform vec4 specular;

uniform vec3 ambientLight;
uniform float shininess;


void main()
{ 
    vec4 diffuseColor = vec4(0, 0, 0, 0);
    vec4 specColor = vec4(0, 0, 0, 0);

    // Diffuse Light
    vec3 lightDirection = lPosition - mvPosition;
    vec3 toLightSource  = normalize(lightDirection);
    float diffuseFactor = max(dot(mvNormal, toLightSource ), 0.0);
    diffuseColor = diffuse * vec4(lColor, 1.0) * lIntensity * diffuseFactor;

    // Specular Light
    vec3 cameraDirection = normalize(-mvPosition);
    vec3 fromLightSource = -toLightSource;
    vec3 reflectedLight = normalize(reflect(fromLightSource, mvNormal));
    float specularFactor = max( dot(cameraDirection, reflectedLight), 0.0);
    specularFactor = pow(specularFactor, shininess);
    specColor = specular * specularFactor * vec4(lColor, 1.0);

    fragColor = ambient * vec4(ambientLight, 1) + diffuseColor + specColor;
}
