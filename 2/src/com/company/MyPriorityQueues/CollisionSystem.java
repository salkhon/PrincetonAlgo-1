package com.company.MyPriorityQueues;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

public class CollisionSystem {
    private double drawDelay = 1.5;

    private final MyMinPriorityQueue<Event> eventMyMinPriorityQueue;
    private double t;
    private Particle[] particles;

    public CollisionSystem(Particle[] particles) {
        this.particles = particles;
        this.eventMyMinPriorityQueue = new MyMinPriorityQueue<>();
        this.t = 0.0;
    }

    private void predict(Particle a) {
        if (a == null) {
            return;
        }
        for (Event event : this.eventMyMinPriorityQueue) {
            if (event.getA() == a || event.getB() == a) {
                event.setCountA(-1);
            }
        }
        for (Particle particle : this.particles) {
            this.eventMyMinPriorityQueue.insert(new Event(this.t + a.timeToHit(particle), a, particle));
        }
        this.eventMyMinPriorityQueue.insert(new Event(this.t + a.timeToHitVerticalWall(), a, null));
        this.eventMyMinPriorityQueue.insert(new Event(this.t + a.timeToHitHorizontalWall(), null, a));
    }

    private void redraw() {
        StdDraw.clear();
        for (Particle particle : this.particles) {
            particle.draw();
        }
        StdDraw.show();
        this.eventMyMinPriorityQueue.insert(new Event(this.t + this.drawDelay, null, null));
        System.out.println("redrawing");
    }

    public void simulate() {
        StdDraw.setCanvasSize(600, 600);
        this.eventMyMinPriorityQueue.insert(new Event(0, null, null));

        for (Particle particle : this.particles) {
            predict(particle);
        }

        while (!this.eventMyMinPriorityQueue.isEmpty()) {
            Event nextEvent = this.eventMyMinPriorityQueue.delMin();
            if (!nextEvent.isValid()) {
                continue;
            }

            Particle a = nextEvent.getA();
            Particle b = nextEvent.getB();

            // Event driven simulation, NOT time-driven
            // so simulation is moving between events, whereas time-driven was moving in time quanta
            double eventTime = nextEvent.getTime();
            if (eventTime < 0) {
                System.out.println("we're getting negative time: " + eventTime);
            }
            for (Particle particle : this.particles) {
                particle.move(eventTime - this.t);
            }
            this.t = eventTime;

            if (a == null && b == null) {
                redraw();
            } else if (a == null) {
                b.bounceOffHorizontalWall();
            } else if (b == null) {
                a.bounceOffVerticalWall();
            } else {
                a.bounceOff(b);
                nextEvent.setCountA(nextEvent.getCountA() + 1);
                nextEvent.setCountB(nextEvent.getCountB() + 1);
            }

            predict(a);
            predict(b);
            System.out.println("in main loop");
        }
    }

    // test client
    public static void main(String[] args) {
        final int N = 50;
        Particle[] particles = new Particle[N];
        for (int i = 0; i < N; i++) {
            particles[i] = new Particle(.01, 0.5, StdRandom.uniform(-0.005, .005), StdRandom.uniform(-0.005, .005));
        }
        CollisionSystem collisionSystem = new CollisionSystem(particles);
        collisionSystem.simulate();
    }
}
