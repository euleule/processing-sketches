package name.euleule.processing;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.Date;

public class FunWithAgents extends PApplet {

    int MAX_ITERATIONS = 10000;
    int iterations = 0;
    // A4
//    int WIDTH = 3508;
//    int HEIGHT = 2480;

    // A0
    int WIDTH =  11858;
    int HEIGHT = 16735;

    // Test
//    int WIDTH = 1000;
//    int HEIGHT = 1000;


    Agent a;
    Agent[] agents;

//    float noiseScale = 255f / max(WIDTH, HEIGHT);
    float noiseScale = 900f;
    float noiseStrength = 5.f;


    class Agent {
        PVector p, pOld;
        float stepSize, angle;
        boolean isOutside = false;

        Agent() {
            p = new PVector(random(WIDTH), random(HEIGHT));
            pOld = new PVector(p.x, p.y);
            stepSize = random(1, 5);
        }

        void update() {
            angle = noise(p.x / noiseScale, p.y / noiseScale) * noiseStrength;
            p.x += cos(angle) * stepSize;
            p.y += sin(angle) * stepSize;

            isOutside = p.x > width + 10 || p.x < -10 || p.y > height + 10 || p.y < -10;

            if (isOutside) {
                p.x = random(WIDTH);
                p.y = random(HEIGHT);
                pOld.set(p);
                isOutside = false;
            }

//            line(pOld.x, pOld.y, p.x, p.y);
            point(p.x, p.y);
            pOld.set(p);
        }
    }

    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Set up scene.
     */
    @Override
    public void setup() {
        background(255);
        strokeWeight(1);
        stroke(30,128.f);
        noiseSeed(1466888400L);
        a = new Agent();
        int n = 100000;
        agents = new Agent[n];
        for (int i = 0; i < n; i++) {
            agents[i] = new Agent();
        }
    }

    /**
     * Draw scene.
     */
    @Override
    public void draw() {
        for (Agent a : agents) {
            a.update();
        }

        if (iterations > MAX_ITERATIONS) {
            noLoop();
            save("/Users/robert/Desktop/Sketches/" + System.currentTimeMillis() + ".tif");
            exit();
        }

        iterations++;
    }

    @Override
    public void keyPressed() {
        if (key == 's') {
            System.out.println("Saving ...");
            System.out.println("Scale " + noiseScale);
            System.out.println("Strength " + noiseStrength);
            save("/Users/robert/Desktop/Sketches/" + System.currentTimeMillis() + ".tif");
        } else if (key == 'r') {
            background(255);
            redraw();
        }
    }


    public static void main(String args[]) {
        PApplet.main(new String[]{"--present", "name.euleule.processing.FunWithAgents"});
    }
}
