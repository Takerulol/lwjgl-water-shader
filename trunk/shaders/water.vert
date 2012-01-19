
uniform int numWavesX;
uniform float offsetX;
uniform float amplitudeX;

uniform int numWavesY;
uniform float offsetY;
uniform float amplitudeY;

uniform float camPosX;
uniform float camPosY;
uniform float camPosZ;

varying vec3 N1;
varying vec3 N2;
varying vec3 L;
varying vec3 R;

void main(){

	gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
	gl_TexCoord[0] = gl_MultiTexCoord0;
	gl_TexCoord[0].x += (offsetX * 0.1);
	gl_TexCoord[1] = gl_MultiTexCoord0;
	gl_TexCoord[1].y += (offsetY * 0.01);
	gl_Position.y = gl_Position.y + sin((gl_Vertex.x * numWavesX)+offsetX) * amplitudeX * 0.5;
	gl_Position.y = gl_Position.y + sin((gl_Vertex.y * numWavesY)+offsetY) * amplitudeY * 0.5;
	
	N1 = normalize(vec3(0.0, cos((gl_Vertex.x * numWavesX)+offsetX) , 1.0));
	N2 = normalize(vec3(0.0, cos((gl_Vertex.y * numWavesY)+offsetY) , 1.0));
	L = vec3(gl_ModelViewMatrix*(vec4(gl_LightSource[0].position)-gl_Vertex));
	
	vec3 V = gl_Position.xyz;
	V.x -= camPosX;
	V.y -= camPosY;
	V.z -= camPosZ;
	
	R = reflect(V, normalize(N1+0.1*N2));
}