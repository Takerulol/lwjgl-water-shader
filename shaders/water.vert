
uniform int numWavesX;
uniform float offsetX;
uniform float amplitudeX;

uniform int numWavesY;
uniform float offsetY;
uniform float amplitudeY;

void main(){
	gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
	gl_TexCoord[0] = gl_MultiTexCoord0;
	gl_Position.y = gl_Position.y + sin((gl_Vertex.x * numWavesX)+offsetX) * amplitudeX * 0.5;
	gl_Position.y = gl_Position.y + sin((gl_Vertex.y * numWavesY)+offsetY) * amplitudeY * 0.5;
}