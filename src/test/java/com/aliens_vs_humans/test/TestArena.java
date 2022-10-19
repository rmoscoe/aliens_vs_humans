package com.aliens_vs_humans.test;
import static org.junit.Assert.*;

import org.junit.*;
import com.aliens_vs_humans.src.*;

import java.lang.Math;

public class TestArena {
    static Arena arena;

    @BeforeClass
    public static void testBuildArena() {
        arena = new Arena("Previa", "Planet", 900, 1000, 800);
    }

    @Test
    public void testArena() {
        assertEquals("Previa", arena.getName());
        assertEquals("Planet", arena.getArenaType());
        assertEquals(900, arena.getLength());
        assertEquals(1000, arena.getWidth());
        assertEquals(800, arena.getHeight());
    }

    @Test
    public void testToString() {
        assertEquals("This battle will take place on Planet Previa.", arena.toString());
    }

    @Test
    public void testSpawnHuman() {
        assertTrue(arena.spawnHuman() instanceof Human);
        assertTrue(arena.humanLocation[0] > 0 && arena.humanLocation[0] <= arena.getLength());
        assertTrue(arena.humanLocation[1] > 0 && arena.humanLocation[1] <= arena.getWidth());
        assertEquals(0, arena.humanLocation[2], 0);
    }

    @Test
    public void testSpawnAlien() {
        assertTrue(arena.spawnAlien() instanceof Alien);
        assertTrue(arena.alienLocation[0] > 0 && arena.alienLocation[0] <= arena.getLength());
        assertTrue(arena.alienLocation[1] > 0 && arena.alienLocation[1] <= arena.getWidth());
        assertEquals(0, arena.alienLocation[2], 0);
    }

    @Test
    public void testFindDistance() {
        assertEquals((int) Math.sqrt(Math.pow(Math.abs(arena.humanLocation[0] - arena.alienLocation[0]), 2) + Math.pow(Math.abs(arena.humanLocation[1] - arena.alienLocation[1]), 2) + Math.pow(Math.abs(arena.humanLocation[2] - arena.alienLocation[2]), 2)), arena.findDistance(), 0.9);
    }

    @Test
    public void testStartRound() {
        arena = new Arena("Previa", "Planet", 900, 1000, 800);
        arena.spawnHuman();
        arena.spawnAlien();
        arena.startRound();
        if (arena.human.hidden) {
            assertEquals(0, arena.alienInitiative);
        } else {
            assertTrue(arena.alienInitiative >= arena.alien.getAgility());
        }
        if (arena.alien.hidden) {
            assertEquals(0, arena.humanInitiative);
        } else {
            assertTrue(arena.humanInitiative >= arena.human.getAgility());
        }
        if (arena.humanInitiative >= arena.alienInitiative) {
            assertEquals(arena.human, arena.hasInitiative);
            assertEquals(arena.alien, arena.defender);
        } else {
            assertEquals(arena.alien, arena.hasInitiative);
            assertEquals(arena.human, arena.defender);
        }
        assertEquals(arena.humanActions, arena.human.getFinesse());
        assertEquals(arena.alienActions, arena.alien.getFinesse());
    }

    @Test
    public void testChooseAction() {
        arena.spawnAlien();
        arena.spawnHuman();
        arena.startRound();
        assertNotNull(arena.chooseAction(arena.hasInitiative));
        assertNotEquals("", arena.chooseAction(arena.hasInitiative));
        if ((arena.hasInitiative == arena.alien && arena.alien.power.active) || arena.alien.power == null) {
            assertNotEquals("activate power", arena.chooseAction(arena.hasInitiative));
        }
        if (arena.hasInitiative.hidden) {
            assertNotEquals("hide", arena.chooseAction(arena.hasInitiative));
        }
        if (arena.hasInitiative.hidden && (arena.alien.power.toString().equals("Invisibility") || arena.alien.power.toString().equals("Shapeshifting"))) {
            assertNotEquals("activate power", arena.chooseAction(arena.hasInitiative));
        }
        if (arena.hasInitiative.getEquipment().equals("none")) {
            assertNotEquals("ranged attack", arena.chooseAction(arena.hasInitiative));
            assertNotEquals("melee attack", arena.chooseAction(arena.hasInitiative));
        }
        if (arena.getDistance() > arena.hasInitiative.getRange()) {
            assertNotEquals("ranged attack", arena.chooseAction(arena.hasInitiative));
        }
    }

    @Test
    public void testResolveActivatePower() {
        arena.spawnHuman();
        arena.spawnAlien();
        arena.startRound();
        int vitalityBefore = arena.alien.getVitality();
        int actionsBefore = arena.alienActions;
        arena.resolveActivatePower();
        if (!arena.alien.power.toString().equals("Regeneration")) {
            assertTrue(arena.alien.power.active);
        }
        if (arena.alien.power.toString().equals("Invisibility")) {
            assertTrue(arena.alien.hidden);
        }
        if (arena.alien.power.toString().equals("Shapeshifting")) {
            assertTrue(arena.alien.hidden);
        }
        if (arena.alien.power.toString().equals("Thermal Vision")) {
            assertFalse(arena.human.hidden);
        }
        if (arena.alien.power.toString().equals("Regeneration")) {
            assertFalse(arena.alien.getVitality() > 100);
            if (vitalityBefore < 100) {
                assertTrue(arena.alien.getVitality() > vitalityBefore);
            }
        }
        assertEquals(actionsBefore - 1, arena.alienActions);
        assertEquals(arena.human, arena.hasInitiative);
        assertEquals(arena.alien, arena.defender);
    }

    @Test
    public void testResolveHide() {
        arena.spawnHuman();
        arena.spawnAlien();
        arena.startRound();
        int actionsBefore;
        Being activeCharacter = arena.hasInitiative;
        if (arena.hasInitiative == arena.alien) {
            actionsBefore = arena.alienActions;
        } else {
            actionsBefore = arena.humanActions;
        }
        if (arena.resolveHide(arena.hasInitiative).equals("Success")) {
            assertTrue(arena.defender.hidden);
        } else {
            assertFalse(arena.defender.hidden);
        }
        if (activeCharacter == arena.alien) {
            assertEquals(actionsBefore - 1, arena.alienActions);
        } else {
            assertEquals(actionsBefore - 1, arena.humanActions);
        }
        if (activeCharacter == arena.alien) {
            assertEquals(arena.human, arena.hasInitiative);
            assertEquals(arena.alien, arena.defender);
        } else {
            assertEquals(arena.human, arena.defender);
            assertEquals(arena.alien, arena.hasInitiative);
        }
    }

    @Test
    public void testResolveCloseDistance() {
        arena.spawnHuman();
        arena.spawnAlien();
        arena.startRound();
        int activeActionsBefore;
        int defenderActionsBefore;
        Being activeCharacter = arena.hasInitiative;
        int distanceBefore = arena.findDistance();
        if (arena.hasInitiative == arena.alien) {
            activeActionsBefore = arena.alienActions;
            defenderActionsBefore = arena.humanActions;
        } else {
            activeActionsBefore = arena.humanActions;
            defenderActionsBefore = arena.alienActions;
        }
        if (arena.closeDistance(arena.hasInitiative).equals("Success")) {
            assertTrue(arena.findDistance() < distanceBefore);
        } else {
            assertTrue(arena.findDistance() >= distanceBefore);
        }
        if (activeCharacter == arena.alien) {
            assertEquals(activeActionsBefore - 1, arena.alienActions);
        } else {
            assertEquals(activeActionsBefore - 1, arena.humanActions);
        }
        if ((activeCharacter == arena.alien && defenderActionsBefore > arena.humanActions) || (activeCharacter == arena.human && defenderActionsBefore == arena.alienActions)) {
            assertEquals(arena.alien, arena.hasInitiative);
            assertEquals(arena.human, arena.defender);
        } else {
            assertEquals(arena.alien, arena.defender);
            assertEquals(arena.human, arena.hasInitiative);
        }
    }

    @Test
    public void testResolveOpenDistance() {
        arena.spawnHuman();
        arena.spawnAlien();
        arena.startRound();
        int activeActionsBefore;
        int defenderActionsBefore;
        Being activeCharacter = arena.hasInitiative;
        int distanceBefore = arena.findDistance();
        if (arena.hasInitiative == arena.alien) {
            activeActionsBefore = arena.alienActions;
            defenderActionsBefore = arena.humanActions;
        } else {
            activeActionsBefore = arena.humanActions;
            defenderActionsBefore = arena.alienActions;
        }
        if (arena.resolveOpenDistance(arena.hasInitiative).equals("Success")) {
            assertTrue(arena.findDistance() > distanceBefore);
        } else {
            assertTrue(arena.findDistance() <= distanceBefore);
        }
        if (activeCharacter == arena.alien) {
            assertEquals(activeActionsBefore - 1, arena.alienActions);
        } else {
            assertEquals(activeActionsBefore - 1, arena.humanActions);
        }
        if ((activeCharacter == arena.alien && defenderActionsBefore == arena.humanActions) || (activeCharacter == arena.human && defenderActionsBefore > arena.alienActions)) {
            assertEquals(arena.human, arena.hasInitiative);
            assertEquals(arena.alien, arena.defender);
        } else {
            assertEquals(arena.human, arena.defender);
            assertEquals(arena.alien, arena.hasInitiative);
        }
    }

    @Test
    public void testResolveRangedAttack() {
        arena.spawnHuman();
        arena.spawnAlien();
        arena.startRound();
        int activeActionsBefore;
        int defenderActionsBefore;
        Being activeCharacter = arena.hasInitiative;
        boolean attackerHidden = arena.hasInitiative.hidden;
        int vitalityBefore = arena.defender.getVitality();
        if (arena.hasInitiative == arena.alien) {
            activeActionsBefore = arena.alienActions;
            defenderActionsBefore = arena.humanActions;
        } else {
            activeActionsBefore = arena.humanActions;
            defenderActionsBefore = arena.alienActions;
        }
        if (arena.resolveRangedAttack(arena.hasInitiative).equals("Hit")) {
            assertTrue(arena.defender.getVitality() <= vitalityBefore);
        } else {
            assertEquals(vitalityBefore, arena.defender.getVitality());
        }
        if (activeCharacter == arena.alien) {
            assertEquals(activeActionsBefore - 1, arena.alienActions);
        } else {
            assertEquals(activeActionsBefore - 1, arena.humanActions);
        }
        if (activeCharacter == arena.alien && (attackerHidden || defenderActionsBefore == 0)) {
            assertEquals(arena.human, arena.hasInitiative);
            assertEquals(arena.alien, arena.defender);
        } else if (activeCharacter == arena.alien) {
            assertEquals(arena.human, arena.defender);
            assertEquals(arena.alien, arena.hasInitiative);
            assertEquals(defenderActionsBefore - 1, arena.humanActions);
        } else if (activeCharacter == arena.human && (attackerHidden || defenderActionsBefore == 0)) {
            assertEquals(arena.alien, arena.hasInitiative);
            assertEquals(arena.human, arena.defender);
        } else if (activeCharacter == arena.human) {
            assertEquals(arena.alien, arena.defender);
            assertEquals(arena.human, arena.hasInitiative);
            assertEquals(defenderActionsBefore - 1, arena.alienActions);
        }
        if (activeCharacter != arena.alien || (arena.alien.power == null || !(arena.alien.power.toString().equals("Invisibility") && arena.alien.power.active))) {
            assertFalse(activeCharacter.hidden);
        }
    }

    @Test
    public void testResolveMeleeAttack() {
        arena.spawnHuman();
        arena.spawnAlien();
        arena.startRound();
        int activeActionsBefore;
        int defenderActionsBefore;
        Being activeCharacter = arena.hasInitiative;
        boolean attackerHidden = arena.hasInitiative.hidden;
        int vitalityBefore = arena.defender.getVitality();
        if (arena.hasInitiative == arena.alien) {
            activeActionsBefore = arena.alienActions;
            defenderActionsBefore = arena.humanActions;
        } else {
            activeActionsBefore = arena.humanActions;
            defenderActionsBefore = arena.alienActions;
        }
        if (arena.resolveMeleeAttack(arena.hasInitiative).equals("Hit")) {
            assertTrue(arena.defender.getVitality() <= vitalityBefore);
        } else {
            assertEquals(vitalityBefore, arena.defender.getVitality());
        }
        if (activeCharacter == arena.alien) {
            assertEquals(activeActionsBefore - 1, arena.alienActions);
        } else {
            assertEquals(activeActionsBefore - 1, arena.humanActions);
        }
        if (activeCharacter == arena.alien && (attackerHidden || defenderActionsBefore == 0)) {
            assertEquals(arena.human, arena.hasInitiative);
            assertEquals(arena.alien, arena.defender);
        } else if (activeCharacter == arena.alien) {
            assertEquals(arena.human, arena.defender);
            assertEquals(arena.alien, arena.hasInitiative);
            assertEquals(defenderActionsBefore - 1, arena.humanActions);
        } else if (activeCharacter == arena.human && (attackerHidden || defenderActionsBefore == 0)) {
            assertEquals(arena.alien, arena.hasInitiative);
            assertEquals(arena.human, arena.defender);
        } else if (activeCharacter == arena.human) {
            assertEquals(arena.alien, arena.defender);
            assertEquals(arena.human, arena.hasInitiative);
            assertEquals(defenderActionsBefore - 1, arena.alienActions);
        }
        if (activeCharacter != arena.alien || !(arena.alien.power.toString().equals("Invisibility") && arena.alien.power.active)) {
            assertFalse(activeCharacter.hidden);
        }
    }

    @Test
    public void testResolveUnarmedAttack() {
        arena.spawnHuman();
        arena.spawnAlien();
        arena.startRound();
        int activeActionsBefore;
        int defenderActionsBefore;
        Being activeCharacter = arena.hasInitiative;
        boolean attackerHidden = arena.hasInitiative.hidden;
        int vitalityBefore = arena.defender.getVitality();
        if (arena.hasInitiative == arena.alien) {
            activeActionsBefore = arena.alienActions;
            defenderActionsBefore = arena.humanActions;
        } else {
            activeActionsBefore = arena.humanActions;
            defenderActionsBefore = arena.alienActions;
        }
        if (arena.resolveUnarmedAttack(arena.hasInitiative).equals("Success")) {
            assertTrue(arena.defender.getVitality() <= vitalityBefore);
        } else {
            assertEquals(vitalityBefore, arena.defender.getVitality());
        }
        if (activeCharacter == arena.alien) {
            assertEquals(activeActionsBefore - 1, arena.alienActions);
        } else {
            assertEquals(activeActionsBefore - 1, arena.humanActions);
        }
        if (activeCharacter == arena.alien && (attackerHidden || defenderActionsBefore == 0)) {
            assertEquals(arena.human, arena.hasInitiative);
            assertEquals(arena.alien, arena.defender);
        } else if (activeCharacter == arena.alien) {
            assertEquals(arena.human, arena.defender);
            assertEquals(arena.alien, arena.hasInitiative);
            assertEquals(defenderActionsBefore - 1, arena.humanActions);
        } else if (activeCharacter == arena.human && (attackerHidden || defenderActionsBefore == 0)) {
            assertEquals(arena.alien, arena.hasInitiative);
            assertEquals(arena.human, arena.defender);
        } else if (activeCharacter == arena.human) {
            assertEquals(arena.alien, arena.defender);
            assertEquals(arena.human, arena.hasInitiative);
            assertEquals(defenderActionsBefore - 1, arena.alienActions);
        }
        if (activeCharacter != arena.alien || !(arena.alien.power.toString().equals("Invisibility") && arena.alien.power.active)) {
            assertFalse(activeCharacter.hidden);
        }
    }

    @Test
    public void testEndRound() {
        int powerRoundsRemaining = 0;
        if (arena.alien.power != null && arena.alien.power.active) {
            powerRoundsRemaining = arena.alien.power.roundsRemaining;
        }
        arena.endRound();
        if (powerRoundsRemaining > 0) {
            assertEquals(powerRoundsRemaining - 1, arena.alien.power.roundsRemaining);
        } else {
            assertFalse(arena.alien.power.active);
        }
    }

    @Test
    public void testEndBattle() {
        arena.spawnHuman();
        arena.spawnAlien();
        arena.alien.vitality = 0;
        arena.endBattle();
        assertTrue(arena.winner.getVitality() > 0);
        assertEquals(0, arena.loser.getVitality());
    }
}