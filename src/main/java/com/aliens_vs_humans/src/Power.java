package com.aliens_vs_humans.src;

public class Power {
    public boolean active;
    static Alien alien;
    static Arena arena;
    public int roundsRemaining;

    public Power(Arena arena, Alien alien) {
        Power.arena = arena;
        Power.alien = alien;
    }
    public void activate(int dieRoll) {
        this.active = true;
        this.roundsRemaining = (dieRoll + 1) / 2;
    }

    public void deactivate() {
        this.active = false;
        this.roundsRemaining = 0;
    }
}
