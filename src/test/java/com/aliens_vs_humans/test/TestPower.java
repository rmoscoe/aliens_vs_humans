package com.aliens_vs_humans.test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import com.aliens_vs_humans.src.*;
import org.junit.Ignore;

public class TestPower {
    Power power;
    Arena arena;
    Alien alien;


    @Before
    public void createTestArena() {
        arena = new Arena("Terra", "Planet", 4000, 4000, 500);
    }

    @Before
    public void createTestAlien() {
        alien = new Alien("Zyken", arena);
    }

    @Before
    public void createTestPower() {
        power = new Power(arena, alien);
    }

    @Test
    public void testPower() {
        assertFalse(power.active);
        assertTrue(power.roundsRemaining > 0);
        assertTrue(power.roundsRemaining <= 5);
    }

    @Test
    public void testActivate() {
        power.activate(5);
        assertTrue(power.active);
    }

    @Test
    public void testDeactivate() {
        power.deactivate();
        assertFalse(power.active);
    }
}
