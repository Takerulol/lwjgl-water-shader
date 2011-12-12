//simple vertex shader example

[Vertex_Shader]

void main()
{
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}


//simple fragment shader example

[Pixel_Shader]

void main()
{
	gl_FragColor = vec4(0.4,0.4,0.8,1.0);
}

