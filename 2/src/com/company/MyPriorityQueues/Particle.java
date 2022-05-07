package com.company.MyPriorityQueues;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

import java.awt.*;

public class Particle {
    private double rx, ry;
    private double vx, vy;
    private final double rad;
    private final double mass;
    private int count;

    public Particle(double rad, double mass, double vx, double vy) {
        this.rad = rad;
        this.mass = mass;
        this.count = 0;
        this.vx = vx;
        this.vy = vy;
        this.rx = StdRandom.uniform(this.rad, 1.0 - this.rad);
        this.ry = StdRandom.uniform(this.rad, 1.0 - this.rad);
    }

    public void move(double dt) {
        this.rx += this.vx * dt;
        this.ry += this.vy * dt;
    }

    public void draw() {
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.filledCircle(this.rx, this.ry, this.rad);
    }

    // prediction
    public double timeToHit(Particle that) {
        if (this == that) {
            return Double.POSITIVE_INFINITY;
        }
        double delRx = that.rx - this.rx, delRy = that.ry - this.ry;
        double delVx = that.vx - this.vx, delVy = that.vy - this.vy;
        double delV_delR = delVx * delRx + delVy * delRy;
        if (delV_delR > 0) {
            return Double.POSITIVE_INFINITY;
        }

        double delV_delV = delVx * delVx + delVy * delVy;
        if (delV_delV == 0) {
            return Double.POSITIVE_INFINITY;
        }

        double delR_delR = delRx * delRx + delRy * delRy;
        double sigma = this.rad + that.rad;
        double d = delV_delR * delV_delR - delV_delV * (delR_delR - sigma * sigma);

        double time;
         if (d < 0) {
            time = Double.POSITIVE_INFINITY;
        } else {
            time = -(delV_delR + Math.sqrt(d)) / delV_delV;
        }
        return time;
    }

    public double timeToHitVerticalWall() {
        double time;
        if (this.vx < 0) {
            time = (this.rx - this.rad) / -this.vx;
        } else if (this.vx > 0) {
            time = (1.0 - (this.rx + this.rad)) / this.vx;
        } else {
            time = Double.POSITIVE_INFINITY;
        }
        return time;
    }

    public double timeToHitHorizontalWall() {
        double time;
        if (this.vy < 0) {
            time = (this.ry - this.rad) / -this.vy;
        } else if (this.vy > 0)  {
            time = (1.0 - (this.ry + this.rad)) / this.vy;
        } else {
            time = Double.POSITIVE_INFINITY;
        }
        return time;
    }

    // resolution
    public void bounceOff(Particle that) {
        double delRx = that.rx - this.rx, delRy = that.ry - this.ry;
        double delVx = that.vx - this.vx, delVy = that.vy - this.vy;
        double delV_delR = delVx * delRx + delVy * delRy;
        double sigma = this.rad + that.rad;

        double J = 2 * this.mass * that.mass * (delV_delR) / (sigma * (this.mass + that.mass));
        double Jx = J * delRx / sigma, Jy = J * delRy / sigma;

        this.vx += Jx / this.mass;
        this.vy += Jy / this.mass;

        that.vx -= Jx / that.mass;
        that.vy -= Jx / that.mass;

        this.count++;
        that.count++;
    }

    public void bounceOffVerticalWall() {
        this.vx = -this.vx;
    }

    public void bounceOffHorizontalWall() {
        this.vy = -this.vy;
    }

    public int getCount() {
        return count;
    }
}
