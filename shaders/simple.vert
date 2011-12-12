//simple vertex shader example

void main()
{
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}