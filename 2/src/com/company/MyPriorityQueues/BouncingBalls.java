package com.company.MyPriorityQueues;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

public class BouncingBalls {
    private static int DIMENSION = 500;

    public static void main(String[] args) {
        final int N = 20;
        StdDraw.setCanvasSize(DIMENSION, DIMENSION);
        Ball[] balls = new Ball[N];
        for (int i = 0; i < balls.length; i++) {
            balls[i] = new Ball(.01, .07);
        }

        while (true) {
            StdDraw.clear();
            for (Ball ball : balls) {
                ball.move(2);
                ball.draw();
            }
            StdDraw.show();
        }
    }

    public static class Ball {
        private double x;
        private double y;
        private double velX;
        private double velY;
        private double rad;

        public Ball(double rad, double vel) {
            this.x = StdRandom.uniform(rad, 1 - rad);
            this.y = StdRandom.uniform(rad, 1 - rad);
            this.velX = this.velY = vel;
            this.rad = rad;
        }

        public void move(double dt) {
            if (this.x + this.velX * dt + this.rad >= 1 || this.x + this.velX * dt - this.rad <= 0) {
                this.velX = -this.velX;
            }
            if (this.y + this.velY * dt + this.rad >= 1 || this.y + this.velY * dt - this.rad <= 0) {
                this.velY = -this.velY;
            }

            this.x += this.velX;
            this.y += this.velY;
        }

        public void draw() {
            StdDraw.filledCircle(this.x, this.y, this.rad);
        }
    }
}
