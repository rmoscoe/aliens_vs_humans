package com.aliens_vs_humans.test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import com.aliens_vs_humans.src.*;
import org.junit.Ignore;

public class TestAlien {
    Alien alien;
    Arena arena;

    @Before
    public void createTestArena() {
        arena = new Arena("Dominator", "Battleship", 300, 250, 8);
    }
    
    @Before
    public void createTestAlien() {
        alien = new Alien("Splorg", arena);
    }

    @Test
    public void testAlien() {
        assertEquals(100, alien.getVitality());
        assertEquals(100, alien.getIntelligence() + alien.getPerception() + alien.getBrawn() + alien.getAgility());
        assertTrue(alien.getFinesse() > 0);
        assertNotNull(alien.getEquipment());
        assertTrue(alien.getFinesse() <= alien.getAgility());
        assertTrue(alien.getIntelligence() > 0);
        assertTrue(alien.getPerception() > 0);
        assertTrue(alien.getBrawn() > 0);
        assertTrue(alien.getAgility() > 0);
        assertTrue(alien.getFinesse() >= 1 && alien.getFinesse() <= alien.getAgility());
        if (alien.power != null) {
            assertFalse(alien.power.active);
        }
        switch (alien.getEquipment()) {
            case "none":
                assertEquals(20, alien.getRange());
                assertEquals(0, alien.getAttackBonus());
                assertEquals(8, alien.getDamageRating());
                assertEquals(alien.getBrawn()/5, alien.getDamageModifier());
                assertEquals(0, alien.getAgilityPenalty());
                assertEquals(0, alien.getCoverage());
                assertEquals(0, alien.getDurability());
                assertEquals(0, alien.getStructurePoints());
                break;
            case "ancient":
                assertEquals(100, alien.getRange());
                assertEquals(5, alien.getAttackBonus());
                assertEquals(7, alien.getDamageRating());
                assertEquals(alien.getBrawn()/5, alien.getDamageModifier());
                assertEquals(25, alien.getAgilityPenalty());
                assertEquals(50, alien.getCoverage());
                assertEquals(2, alien.getDurability());
                assertEquals(50, alien.getStructurePoints());
                break;
            case "modern defensive":
                assertEquals(250, alien.getRange());
                assertEquals(5, alien.getAttackBonus());
                assertEquals(7, alien.getDamageRating());
                assertEquals(15, alien.getDamageModifier());
                assertEquals(15, alien.getAgilityPenalty());
                assertEquals(90, alien.getCoverage());
                assertEquals(5, alien.getDurability());
                assertEquals(150, alien.getStructurePoints());
                break;
            case "modern offensive":
                assertEquals(500, alien.getRange());
                assertEquals(10, alien.getAttackBonus());
                assertEquals(6, alien.getDamageRating());
                assertEquals(25, alien.getDamageModifier());
                assertEquals(5, alien.getAgilityPenalty());
                assertEquals(50, alien.getCoverage());
                assertEquals(4, alien.getDurability());
                assertEquals(100, alien.getStructurePoints());
                break;
            case "modern balanced":
                assertEquals(500, alien.getRange());
                assertEquals(10, alien.getAttackBonus());
                assertEquals(6, alien.getDamageRating());
                assertEquals(25, alien.getDamageModifier());
                assertEquals(15, alien.getAgilityPenalty());
                assertEquals(90, alien.getCoverage());
                assertEquals(5, alien.getDurability());
                assertEquals(150, alien.getStructurePoints());
                break;
            case "futuristic defensive":
                assertEquals(500, alien.getRange());
                assertEquals(10, alien.getAttackBonus());
                assertEquals(5, alien.getDamageRating());
                assertEquals(30, alien.getDamageModifier());
                assertEquals(10, alien.getAgilityPenalty());
                assertEquals(100, alien.getCoverage());
                assertEquals(7, alien.getDurability());
                assertEquals(250, alien.getStructurePoints());
                break;
            case "futuristic offensive":
                assertEquals(1000, alien.getRange());
                assertEquals(25, alien.getAttackBonus());
                assertEquals(4, alien.getDamageRating());
                assertEquals(40, alien.getDamageModifier());
                assertEquals(0, alien.getAgilityPenalty());
                assertEquals(80, alien.getCoverage());
                assertEquals(6, alien.getDurability());
                assertEquals(200, alien.getStructurePoints());
                break;
            default:
                assertEquals("futuristic balanced", alien.getEquipment());
                assertEquals(1000, alien.getRange());
                assertEquals(25, alien.getAttackBonus());
                assertEquals(4, alien.getDamageRating());
                assertEquals(40, alien.getDamageModifier());
                assertEquals(10, alien.getAgilityPenalty());
                assertEquals(100, alien.getCoverage());
                assertEquals(7, alien.getDurability());
                assertEquals(250, alien.getStructurePoints());
                break;
        }
        assertFalse(alien.hidden);
    }

    @Test
    public void testAlienToString() {
        assertTrue(alien.toString().contains("Agility: "));
    }

    @Test
    public void testRollDie() {
        assertTrue(alien.rollDie() > 0 && alien.rollDie() <= 10);
    }

    @Test
    public void testAlienRollInitiative() {
        assertTrue(alien.rollInitiative() > alien.getAgility() +1 && alien.rollInitiative() < alien.getAgility() + 21);
    }

    @Test
    public void testAlienDefend() {
        assertTrue(alien.defend() > alien.getAgility() - alien.getAgilityPenalty() +1 && alien.defend() < alien.getAgility() - alien.getAgilityPenalty() + 21);
    }

    @Test
    public void testAlienUnarmedAttack() {
        assertTrue(alien.unarmedAttack() > alien.getAgility() - alien.getAgilityPenalty() +1 && alien.unarmedAttack() < alien.getAgility() - alien.getAgilityPenalty() + 21);
    }

    @Test
    public void testAlienMeleeAttack() {
        assertTrue(alien.meleeAttack() > alien.getAgility() - alien.getAgilityPenalty() + alien.getAttackBonus() + 1 && alien.meleeAttack() < alien.getAgility() - alien.getAgilityPenalty() + alien.getAttackBonus() + 21);
    }

    @Test
    public void testAlienRangedAttack() {
        assertTrue(alien.rangedAttack() > alien.getPerception() + alien.getAttackBonus() + 1 && alien.rangedAttack() < alien.getPerception() + alien.getAttackBonus() + 21);
    }

    @Test
    public void testAlienHide() {
        assertTrue(alien.hide() > alien.getIntelligence() - alien.getAgilityPenalty() + 1 && alien.hide() < alien.getIntelligence() - alien.getAgilityPenalty() + 21);
    }
}
