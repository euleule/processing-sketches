package name.euleule.processing;

import name.euleule.processing.elements.One;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

public class FunWithAgents extends PApplet {

    int MAX_ITERATIONS = 10000;
    int iterations = 0;
    int WIDTH = 3508;
    int HEIGHT = 2480;

//    int WIDTH = 1000;
//    int HEIGHT = 1000;


    Agent a;
    Agent[] agents;

    float noiseScale;
    float noiseStrength;

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
            angle = noise(p.x/noiseScale, p.y/noiseScale)*noiseStrength;
            p.x += cos(angle) * stepSize;
            p.y += sin(angle) * stepSize;

            isOutside = p.x > width + 10 || p.x < -10 || p.y > height + 10 || p.y < -10;

            if (isOutside) {
                p.x = random(WIDTH);
                p.y = random(HEIGHT);
                pOld.set(p);
                isOutside = false;
            }

            line(pOld.x, pOld.y, p.x, p.y);
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
        a = new Agent();
        int n = 10000;
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
        noiseScale = 90.f;
        noiseStrength = 30.f;
        for (Agent a : agents) {
            a.update();
        }

        if (iterations > MAX_ITERATIONS) {
            noLoop();
            save("/Users/robert/Desktop/Sketches/" + System.currentTimeMillis() + ".jpg");
            exit();
        }

        iterations++;
    }

    @Override
    public void keyPressed() {
        if (key == 's') {
            System.out.println("Saving ...");
            System.out.println("Scale "+noiseScale);
            System.out.println("Strength "+noiseStrength);
            save("/Users/robert/Desktop/Sketches/" + System.currentTimeMillis() + ".jpg");
        } else if (key == 'r') {
            background(255);
            redraw();
        }
    }


    public static void main(String args[]) {
        PApplet.main(new String[]{"--present", "name.euleule.processing.FunWithAgents"});
    }
}
