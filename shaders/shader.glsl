//simple vertex shader example

[Vertex_Shader]

struct surface 
{
	
};

void main()
{
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
	
	OUT.Tex1=(IN.UV*WaterScale+(time*Speed1));
	OUT.Tex2=(IN.UV*WaterScale*4+(time*Speed2));
	float3 WPos=mul(IN.Pos,World);  
	float3 VP=ViewInv[3].xyz-WPos; 
 	OUT.ViewVec=0.5f+(-VP.xz/((VP.y+5)*FresPow))*0.5;
	OUT.RefrProj=float4(OUT.OPos.x*0.5+0.5*OUT.OPos.w,0.5*OUT.OPos.w-OUT.OPos.y*0.5,OUT.OPos.w,OUT.OPos.w);
  	OUT.ReflProj=float4(OUT.OPos.x*0.5+0.5*OUT.OPos.w,0.5*OUT.OPos.w+OUT.OPos.y*0.5,OUT.OPos.w,OUT.OPos.w);
	OUT.WaterFog=VP/WaterFogRange;
	return OUT;
}


//simple fragment shader example

[Pixel_Shader]

void main()
{
	gl_FragColor = vec4(0.4,0.4,0.8,1.0);
}

