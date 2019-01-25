package name.euleule.processing.lsystems;

import processing.core.PApplet;
import processing.sound.*;

public class LSystemSound extends PApplet {


    // Oscillator and envelope
    private TriOsc[] voices = new TriOsc[20];
    private Env[] envelopes = new Env[voices.length];
    private int v = 0;

    // Times and levels for the ASR envelope
    private float attackTime = 0.001f;
    private float sustainTime = 0.004f;
    private float sustainLevel = 0.5f;
    private float releaseTime = 0.4f;

    // This is an octave in MIDI notes.
    private int[] midiNotes = {
//            60, 62, 64, 65, 67, 69, 71  // CDUR
            57, 59, 60, 62, 64, 65, 67  // AMOLL?
    };

    // Set the duration between the notes
    private int duration = 200;
    // Set the note trigger
    private int trigger = 0;

    // An index to count up the notes
    private int note = 0;

    private int currentLevels = 1;

    @Override
    public void settings() {
        size(640, 360);
    }

    @Override
    public void setup() {
        // Create triangle wave and envelope
        for (int i = 0; i < voices.length; i++) {
            voices[i] = new TriOsc(this);
            envelopes[i] = new Env(this);
        }
    }

    @Override
    public void draw() {

        String rules = recursiveFractalPlant(currentLevels, "A");

        background(GRAY);
        text(rules, 10, 10, 620, 340);

        char[] song = rules.toCharArray();

        // If value of trigger is equal to the computer clock and if not all
        // notes have been played yet, the next note gets triggered.
        if ((millis() > trigger) && (note < song.length)) {

            // midiToFreq transforms the MIDI value into a frequency in Hz which we use
            //to control the triangle oscillator with an amplitute of 0.8
            voices[v].play(midiToFreq(mapToMidiNote(song[note])), random(0.9f, 0.8f));

            // The envelope gets triggered with the oscillator as input and the times and
            // levels we defined earlier
            envelopes[v].play(voices[v], noisify(attackTime), noisify(sustainTime), noisify(sustainLevel), noisify(releaseTime));

            // Create the new trigger according to predefined durations and speed
            trigger = millis() + duration;

            // Advance by one note in the midiSequence;
            note++;
            v = ++v % voices.length;
        }
    }

    private float noisify(float f) {
        return random(f - f * 0.1f, f);
    }

    private int mapToMidiNote(char c) {
        switch (c) {
            case 'A':
                return midiNotes[5];
            case 'B':
                return midiNotes[6];
            case 'C':
                return midiNotes[0];
            case 'D':
                return midiNotes[1];
            case 'E':
                return midiNotes[2];
            case 'F':
                return midiNotes[3];
            case 'G':
                return midiNotes[4];
        }
        return midiNotes[0];
    }

    // This function calculates the respective frequency of a MIDI note
    private float midiToFreq(int note) {
        return (pow(2, ((note - 69) / 12.0f))) * 440;
    }

    /**
     * http://en.wikipedia.org/wiki/L-system
     * variables : A B C D E F
     * constants :
     * start  : A
     * rules  :  (A -> AB),
     * (B -> DFE),
     * (D -> CA),
     * (E -> FB)
     */
    private String recursiveFractalPlant(int levels, String sentence) {

        String next = "";
        if (levels > 0) { // check if there are any levels left to render
            char[] characters = sentence.toCharArray();
            for (char c : characters) {
                switch (c) {  //interprets the rules derived from the nature
                    case 'A':
                        next += recursiveFractalPlant(levels - 1, "AB");
                        break;
                    case 'B':
                        next += recursiveFractalPlant(levels - 1, "DFE");
                        break;
                    case 'D':
                        next += recursiveFractalPlant(levels - 1, "CGA");
                        break;
                    case 'E':
                        next += recursiveFractalPlant(levels - 1, "FB");
                        break;
                    case 'F':
                        next += recursiveFractalPlant(levels - 1, "CGCA");
                        break;
                    case 'G':
                        next += recursiveFractalPlant(levels - 1, "AA");
                        break;
                    default:
                        next += c;
                        break;
                }
            }
        } else {
            next = sentence;
        }
        return next;
    }

    @Override
    public void keyPressed() {
        if (key == 'a') {
            currentLevels = Math.max(0, --currentLevels);
            note = 0;
        }

        if (key == 's') {
            currentLevels++;
            note = 0;
        }
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{"--present", "name.euleule.processing.lsystems.LSystemSound"});
    }
}
