varying vec3 N;
varying vec3 L;

void main()
{    
    N = normalize(gl_NormalMatrix*gl_Normal);
	L = vec3(gl_ModelViewMatrix*(vec4(gl_LightSource[0].position)-gl_Vertex));
    gl_Position = ftransform();
}
