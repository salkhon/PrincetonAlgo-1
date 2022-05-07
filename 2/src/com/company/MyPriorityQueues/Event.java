package com.company.MyPriorityQueues;

public class Event implements Comparable<Event> {
    private double time;
    private Particle a, b;
    private int countA = 0, countB = 0;

    public Event(double time, Particle a, Particle b) {
        this.time = time;
        this.a = a;
        this.b = b;
        if (a != null) {
            this.countA = a.getCount();
        }
        if (b != null) {
            this.countB = b.getCount();
        }
    }

    public Particle getA() {
        return a;
    }

    public Particle getB() {
        return b;
    }

    public int getCountB() {
        return countB;
    }

    public void setCountB(int countB) {
        this.countB = countB;
    }

    public int getCountA() {
        return countA;
    }

    public void setCountA(int countA) {
        this.countA = countA;
    }

    public double getTime() {
        return time;
    }

    public boolean isValid() {
        return this.countA >= 0;
    }

    @Override
    public int compareTo(Event that) {
        return Double.compare(this.time, that.time);
    }
}
