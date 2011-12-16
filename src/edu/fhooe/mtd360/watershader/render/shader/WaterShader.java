package edu.fhooe.mtd360.watershader.render.shader;

public class WaterShader extends Shader {

	public WaterShader(int speed1, int speed2) {
		super("water");
		addAttribute1f("speed1", speed1);
		addAttribute1f("speed2", speed2);
	}

}
