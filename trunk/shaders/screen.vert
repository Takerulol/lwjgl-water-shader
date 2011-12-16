varying vec4 vertColor;

void main(){
    gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
    //vertColor = vec4(0.3, 0.8, 0.1, 1.0);
    //vertColor = gl_Position;
    gl_FrontColor = gl_FrontMaterial.diffuse;
}