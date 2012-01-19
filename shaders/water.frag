
uniform sampler2D sampler01;
uniform sampler2D sampler02;
uniform samplerCube cubeMap;

varying vec3 N1;
varying vec3 N2;
varying vec3 L;
varying vec3 R;

void main(){
	float lambert1 = max(0.0, dot(normalize(L), normalize(N1)));
	float lambert2 = max(0.0, dot(normalize(L), normalize(N2)));
	float mixFactor = 0.9;
	float lambertMixFactor = 0.5;
	vec3 colorA1=vec3(texture2D(sampler01, (gl_TexCoord[0].st)));
	vec3 colorA2=vec3(texture2D(sampler01, (gl_TexCoord[0].st))) * lambert1;
	vec3 colorA=colorA1 * lambertMixFactor + colorA2 * (1.0 - lambertMixFactor);
	vec3 colorB=vec3(texture2D(sampler02, (gl_TexCoord[1].st))) * lambert2;
	vec3 colorR=colorA * mixFactor + colorB * (1.0 - mixFactor);
	
	//gl_FragColor = textureCube(cubeMap, R);	//pure reflection
	float shininess = 0.6;
	gl_FragColor = vec4(colorR.xyz, 0.5) * (1.0 - shininess) + shininess * textureCube(cubeMap, R);
}