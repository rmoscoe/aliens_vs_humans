package com.aliens_vs_humans.test;

import static org.junit.Assert.*;

import com.aliens_vs_humans.src.Arena;
import com.aliens_vs_humans.src.Being;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import com.aliens_vs_humans.test.*;
import org.junit.Ignore;

public class TestBeing {
    Being being;

    @Before
    public void createTestBeing() {
        Arena arena = new Arena("Alpha", "Starbase", 750, 890, 180);
        being = new Being(arena);
    }

    @Test
    public void testBeing() {
        assertEquals(100, being.getVitality());
        assertEquals(100, being.getIntelligence() + being.getPerception() + being.getBrawn() + being.getAgility());
        assertTrue(being.getFinesse() > 0);
        assertNotNull(being.getEquipment());
        assertTrue(being.getFinesse() <= being.getAgility());
        assertTrue(being.getIntelligence() > 0);
        assertTrue(being.getPerception() > 0);
        assertTrue(being.getBrawn() > 0);
        assertTrue(being.getAgility() > 0);
        assertTrue(being.getFinesse() >= 1 && being.getFinesse() <= being.getAgility());
        switch (being.getEquipment()) {
            case "none":
                assertEquals(20, being.getRange());
                assertEquals(0, being.getAttackBonus());
                assertEquals(8, being.getDamageRating());
                assertEquals(being.getBrawn()/5, being.getDamageModifier());
                assertEquals(0, being.getAgilityPenalty());
                assertEquals(0, being.getCoverage());
                assertEquals(0, being.getDurability());
                assertEquals(0, being.getStructurePoints());
                break;
            case "ancient":
                assertEquals(100, being.getRange());
                assertEquals(5, being.getAttackBonus());
                assertEquals(7, being.getDamageRating());
                assertEquals(being.getBrawn()/5, being.getDamageModifier());
                assertEquals(25, being.getAgilityPenalty());
                assertEquals(50, being.getCoverage());
                assertEquals(2, being.getDurability());
                assertEquals(50, being.getStructurePoints());
                break;
            case "modern defensive":
                assertEquals(250, being.getRange());
                assertEquals(5, being.getAttackBonus());
                assertEquals(6, being.getDamageRating());
                assertEquals(15, being.getDamageModifier());
                assertEquals(15, being.getAgilityPenalty());
                assertEquals(90, being.getCoverage());
                assertEquals(5, being.getDurability());
                assertEquals(150, being.getStructurePoints());
                break;
            case "modern offensive":
                assertEquals(500, being.getRange());
                assertEquals(10, being.getAttackBonus());
                assertEquals(5, being.getDamageRating());
                assertEquals(25, being.getDamageModifier());
                assertEquals(5, being.getAgilityPenalty());
                assertEquals(50, being.getCoverage());
                assertEquals(4, being.getDurability());
                assertEquals(100, being.getStructurePoints());
                break;
            case "modern balanced":
                assertEquals(500, being.getRange());
                assertEquals(10, being.getAttackBonus());
                assertEquals(5, being.getDamageRating());
                assertEquals(25, being.getDamageModifier());
                assertEquals(15, being.getAgilityPenalty());
                assertEquals(90, being.getCoverage());
                assertEquals(5, being.getDurability());
                assertEquals(150, being.getStructurePoints());
                break;
            case "futuristic defensive":
                assertEquals(500, being.getRange());
                assertEquals(10, being.getAttackBonus());
                assertEquals(4, being.getDamageRating());
                assertEquals(30, being.getDamageModifier());
                assertEquals(10, being.getAgilityPenalty());
                assertEquals(100, being.getCoverage());
                assertEquals(7, being.getDurability());
                assertEquals(250, being.getStructurePoints());
                break;
            case "futuristic offensive":
                assertEquals(1000, being.getRange());
                assertEquals(25, being.getAttackBonus());
                assertEquals(3, being.getDamageRating());
                assertEquals(40, being.getDamageModifier());
                assertEquals(0, being.getAgilityPenalty());
                assertEquals(80, being.getCoverage());
                assertEquals(6, being.getDurability());
                assertEquals(200, being.getStructurePoints());
                break;
            default:
                assertEquals("futuristic balanced", being.getEquipment());
                assertEquals(1000, being.getRange());
                assertEquals(25, being.getAttackBonus());
                assertEquals(3, being.getDamageRating());
                assertEquals(40, being.getDamageModifier());
                assertEquals(10, being.getAgilityPenalty());
                assertEquals(100, being.getCoverage());
                assertEquals(7, being.getDurability());
                assertEquals(250, being.getStructurePoints());
                break;
        }
        assertFalse(being.hidden);
    }

    @Test
    public void testBeingToString() {
        assertTrue(being.toString().contains("Agility: "));
    }

    @Test
    public void testRollDie() {
        assertTrue(being.rollDie() > 0 && being.rollDie() <= 10);
    }

    @Test
    public void testBeingRollInitiative() {
        assertTrue(being.rollInitiative() > being.getAgility() +1 && being.rollInitiative() < being.getAgility() + 21);
    }

    @Test
    public void testBeingDefend() {
        assertTrue(being.defend() > being.getAgility() - being.getAgilityPenalty() +1 && being.defend() < being.getAgility() - being.getAgilityPenalty() + 21);
    }

    @Test
    public void testBeingUnarmedAttack() {
        assertTrue(being.unarmedAttack() > being.getAgility() - being.getAgilityPenalty() +1 && being.unarmedAttack() < being.getAgility() - being.getAgilityPenalty() + 21);
    }

    @Test
    public void testBeingMeleeAttack() {
        assertTrue(being.meleeAttack() > being.getAgility() - being.getAgilityPenalty() + being.getAttackBonus() + 1 && being.meleeAttack() < being.getAgility() - being.getAgilityPenalty() + being.getAttackBonus() + 21);
    }

    @Test
    public void testBeingRangedAttack() {
        assertTrue(being.rangedAttack() > being.getPerception() + being.getAttackBonus() + 1 && being.rangedAttack() < being.getPerception() + being.getAttackBonus() + 21);
    }

    @Test
    public void testBeingHide() {
        assertTrue(being.hide() > being.getIntelligence() - being.getAgilityPenalty() + 1 && being.hide() < being.getIntelligence() - being.getAgilityPenalty() + 21);
    }

}
