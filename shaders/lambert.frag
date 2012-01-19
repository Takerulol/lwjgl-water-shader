uniform sampler2D sampler01;

varying vec3 N;
varying vec3 L;

void main()
{ 
    float lambert = max(0.0, dot(normalize(L),normalize(N)));
    gl_FragColor = texture2D(sampler01, (gl_TexCoord[0].st));
}