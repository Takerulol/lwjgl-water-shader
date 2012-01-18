uniform sampler2D sampler01;
uniform sampler2D sampler02;

void main(){
	float mixFactor = 0.9;
	vec3 colorA=vec3(texture2D(sampler01, (gl_TexCoord[0].st)));
	vec3 colorB=vec3(texture2D(sampler02, (gl_TexCoord[1].st)));
	vec3 colorR=colorA * mixFactor + colorB * (1.0 - mixFactor);
	gl_FragColor = vec4(colorR.xyz, 0.5);
}