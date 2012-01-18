uniform sampler2D sampler01;

void main(){
	vec3 theColor=vec3(texture2D(sampler01, (gl_TexCoord[0].st)));
	gl_FragColor = vec4(theColor.xyz, 0.5);
}