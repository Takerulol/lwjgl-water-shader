
uniform sampler2D sampler01;
uniform sampler2D sampler02;
uniform samplerCube cubeMap;

varying vec3 N1;
varying vec3 N2;
varying vec3 L;
varying vec3 R;
varying vec3 R2;

void main(){
	float lambert1 = max(0.0, dot(normalize(L), normalize(N1)));
	float lambert2 = max(0.0, dot(normalize(L), normalize(N2)));
	float mixFactor = 0.9;
	float lambertMixFactor = 0.5;													//to influence the degree of shading in x-direction
	vec3 colorA1=vec3(texture2D(sampler01, (gl_TexCoord[0].st)));					//pure texture color
	vec3 colorA2=colorA1 * lambert1;												//shaded color
	vec3 colorA=colorA1 * lambertMixFactor + colorA2 * (1.0 - lambertMixFactor);	//mix (50:50 stock)
	vec3 colorB=vec3(texture2D(sampler02, (gl_TexCoord[1].st))) * lambert2;			//texture of 2nd wave (z)
	vec3 colorR=colorA * mixFactor + colorB * (1.0 - mixFactor);					//resulting color with both waves mixed

	//gl_FragColor = textureCube(cubeMap, R);										//pure reflection
	//gl_FragColor = textureCube(cubeMap, R2);										//pure refraction (for testing)
	float shininess = 0.3;

	gl_FragColor = vec4(colorR.xyz, 0.5) * (1.0 - shininess) + shininess * textureCube(cubeMap, R) + 0.1 * textureCube(cubeMap, R2);
}