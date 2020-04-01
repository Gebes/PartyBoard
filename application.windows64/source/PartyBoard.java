import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class PartyBoard extends PApplet {


ArrayList<Particle> pts;
boolean onPressed;
PFont f;

public void setup() {
  
  
  colorMode(RGB);
  rectMode(CENTER);

  pts = new ArrayList<Particle>();
  clearBoard();
}

public void draw() {
  

  if (onPressed) {
    for (int i=0; i<10; i++) {
      Particle newP = new Particle(mouseX, mouseY, i+pts.size(), i+pts.size());
      pts.add(newP);
    }
  }

  for (int i=0; i<pts.size(); i++) {
    Particle p = pts.get(i);
    p.update();
    p.display();
  }

  for (int i=pts.size()-1; i>-1; i--) {
    Particle p = pts.get(i);
    if (p.dead) {
      pts.remove(i);
    }
  }
}

public void clearBoard() {
  pts.clear();
  background(255);
  textAlign(CENTER, CENTER);
  fill(0);
  textSize(48);
  text("Party Board v1.2020.11",width/2, 100);
  textSize(24);
  text("von Christoph Krassnigg",width/2, 140);
  textSize(20);
  text("Linksklick zum Zeichnen",width/2, 200);
  textSize(20);
  text("\"c\" drückem um die Leinwand zu leeren",width/2, 225);
}

public void mousePressed() {
  onPressed = true;
}

public void mouseReleased() {
  onPressed = false;
}

public void keyPressed() {
  if (key == 'c') {
    clearBoard();
  }
}

class Particle {
  PVector loc, vel, acc;
  int lifeSpan, passedLife;
  boolean dead;
  float alpha, weight, weightRange, decay, xOffset, yOffset;
  int c;

  Particle(float x, float y, float xOffset, float yOffset) {
    loc = new PVector(x, y);

    float randDegrees = random(360);
    vel = new PVector(cos(radians(randDegrees)), sin(radians(randDegrees)));
    vel.mult(random(5));

    acc = new PVector(0, 0);
    lifeSpan = PApplet.parseInt(random(30, 90));
    decay = random(0.75f, 0.9f);
    c = color(random(255), random(255), 255);
    weightRange = random(3, 50);

    this.xOffset = xOffset;
    this.yOffset = yOffset;
  }

  public void update() {
    if (passedLife>=lifeSpan) {
      dead = true;
    } else {
      passedLife++;
    }

    alpha = PApplet.parseFloat(lifeSpan-passedLife)/lifeSpan * 70+50;
    weight = PApplet.parseFloat(lifeSpan-passedLife)/lifeSpan * weightRange;

    acc.set(0, 0);

    float rn = (noise((loc.x+frameCount+xOffset)*0.01f, (loc.y+frameCount+yOffset)*0.01f)-0.5f)*4*PI;
    float mag = noise((loc.y+frameCount)*0.01f, (loc.x+frameCount)*0.01f);
    PVector dir = new PVector(cos(rn), sin(rn));
    acc.add(dir);
    acc.mult(mag);

    float randDegrees = random(360);
    PVector randV = new PVector(cos(radians(randDegrees)), sin(radians(randDegrees)));
    randV.mult(0.5f);
    acc.add(randV);

    vel.add(acc);
    vel.mult(decay);
    vel.limit(3);
    loc.add(vel);
  }

  public void display() {
    strokeWeight(weight+1.5f);
    stroke(0, alpha);

    noStroke();
    point(loc.x, loc.y);

    strokeWeight(weight);
    stroke(c);
    point(loc.x, loc.y);
  }
}
  public void settings() {  fullScreen();  smooth(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "PartyBoard" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}