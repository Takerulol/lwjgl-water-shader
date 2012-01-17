varying vec3 N;
varying vec3 L;

void main()
{ 
    float lambert = max(0.0, dot(normalize(L),normalize(N)));
    gl_FragColor = vec4(gl_FrontMaterial.diffuse.xyz*lambert,1.0);
    //gl_FragColor = vec4(gl_TexCoord[0].xy,gl_FrontMaterial.diffuse.z*lambert,1.0);
    //gl_FragColor = gl_MultiTexCoord3;
}