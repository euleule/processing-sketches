package name.euleule.processing;

import processing.core.PApplet;

/**
 * Source: http://hamoid.tumblr.com/post/118729348854/busy-but-still-here
 */
public class Asdf extends PApplet {

    @Override
    public void settings(){
        size(900,900);
    }

    @Override
    public void setup(){
        background(0);
        blendMode(ADD);
        noStroke();
        fill(8,4,2);
        doit(width/2, height/2, 400);
        noLoop();
    }

    void doit(float x, float y, float rad){
        if(rad<2){
            return;
        }

        for(float a = 0; a<TWO_PI; a+=TWO_PI/6){
            float nx = x + rad * cos(a + x * 0.05f);
            float ny = y + rad * sin(a + y * 0.05f);
            ellipse(nx, ny, rad, rad);
            doit(nx, ny, rad/2);
        }
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{"--present", "name.euleule.processing.Asdf"});
    }
}
