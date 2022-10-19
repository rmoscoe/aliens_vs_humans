package com.aliens_vs_humans.test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import com.aliens_vs_humans.src.*;
import org.junit.Ignore;


public class TestHuman {
    Arena arena;
    Human human;

    @Before
    public void createTestArena() {
        arena = new Arena("Mori", "Planet", 2500, 2500, 400);
    }
    @Before
    public void createTestHuman() {
        human = new Human("Ryan V. Rexfall", arena);
    }

    @Test
    public void testHuman() {
        assertEquals(100, human.getVitality());
        assertEquals(100, human.getIntelligence() + human.getPerception() + human.getBrawn() + human.getAgility());
        assertTrue(human.getFinesse() > 0);
        assertNotNull(human.getEquipment());
        assertTrue(human.getFinesse() <= human.getAgility());
        assertTrue(human.getIntelligence() > 0);
        assertTrue(human.getPerception() > 0);
        assertTrue(human.getBrawn() > 0);
        assertTrue(human.getAgility() > 0);
        assertTrue(human.getFinesse() >= 1 && human.getFinesse() <= human.getAgility());
        switch (human.getEquipment()) {
            case "none":
                assertEquals(20, human.getRange());
                assertEquals(0, human.getAttackBonus());
                assertEquals(8, human.getDamageRating());
                assertEquals(human.getBrawn()/5, human.getDamageModifier());
                assertEquals(0, human.getAgilityPenalty());
                assertEquals(0, human.getCoverage());
                assertEquals(0, human.getDurability());
                assertEquals(0, human.getStructurePoints());
                break;
            case "ancient":
                assertEquals(100, human.getRange());
                assertEquals(5, human.getAttackBonus());
                assertEquals(7, human.getDamageRating());
                assertEquals(human.getBrawn()/5, human.getDamageModifier());
                assertEquals(25, human.getAgilityPenalty());
                assertEquals(50, human.getCoverage());
                assertEquals(2, human.getDurability());
                assertEquals(50, human.getStructurePoints());
                break;
            case "modern defensive":
                assertEquals(250, human.getRange());
                assertEquals(5, human.getAttackBonus());
                assertEquals(6, human.getDamageRating());
                assertEquals(15, human.getDamageModifier());
                assertEquals(15, human.getAgilityPenalty());
                assertEquals(90, human.getCoverage());
                assertEquals(5, human.getDurability());
                assertEquals(150, human.getStructurePoints());
                break;
            case "modern offensive":
                assertEquals(500, human.getRange());
                assertEquals(10, human.getAttackBonus());
                assertEquals(5, human.getDamageRating());
                assertEquals(25, human.getDamageModifier());
                assertEquals(5, human.getAgilityPenalty());
                assertEquals(50, human.getCoverage());
                assertEquals(4, human.getDurability());
                assertEquals(100, human.getStructurePoints());
                break;
            case "modern balanced":
                assertEquals(500, human.getRange());
                assertEquals(10, human.getAttackBonus());
                assertEquals(5, human.getDamageRating());
                assertEquals(25, human.getDamageModifier());
                assertEquals(15, human.getAgilityPenalty());
                assertEquals(90, human.getCoverage());
                assertEquals(5, human.getDurability());
                assertEquals(150, human.getStructurePoints());
                break;
            case "futuristic defensive":
                assertEquals(500, human.getRange());
                assertEquals(10, human.getAttackBonus());
                assertEquals(4, human.getDamageRating());
                assertEquals(30, human.getDamageModifier());
                assertEquals(10, human.getAgilityPenalty());
                assertEquals(100, human.getCoverage());
                assertEquals(7, human.getDurability());
                assertEquals(250, human.getStructurePoints());
                break;
            case "futuristic offensive":
                assertEquals(1000, human.getRange());
                assertEquals(25, human.getAttackBonus());
                assertEquals(3, human.getDamageRating());
                assertEquals(40, human.getDamageModifier());
                assertEquals(0, human.getAgilityPenalty());
                assertEquals(80, human.getCoverage());
                assertEquals(6, human.getDurability());
                assertEquals(200, human.getStructurePoints());
                break;
            default:
                assertEquals("futuristic balanced", human.getEquipment());
                assertEquals(1000, human.getRange());
                assertEquals(25, human.getAttackBonus());
                assertEquals(3, human.getDamageRating());
                assertEquals(40, human.getDamageModifier());
                assertEquals(10, human.getAgilityPenalty());
                assertEquals(100, human.getCoverage());
                assertEquals(7, human.getDurability());
                assertEquals(250, human.getStructurePoints());
                break;
        }
        assertFalse(human.hidden);
    }

    @Test
    public void testHumanToString() {
        assertTrue(human.toString().contains("Agility: "));
    }

    @Test
    public void testRollDie() {
        assertTrue(human.rollDie() > 0 && human.rollDie() <= 10);
    }

    @Test
    public void testHumanRollInitiative() {
        assertTrue(human.rollInitiative() > human.getAgility() +1 && human.rollInitiative() < human.getAgility() + 21);
    }

    @Test
    public void testHumanDefend() {
        assertTrue(human.defend() > human.getAgility() - human.getAgilityPenalty() +1 && human.defend() < human.getAgility() - human.getAgilityPenalty() + 21);
    }

    @Test
    public void testHumanUnarmedAttack() {
        assertTrue(human.unarmedAttack() > human.getAgility() - human.getAgilityPenalty() +1 && human.unarmedAttack() < human.getAgility() - human.getAgilityPenalty() + 21);
    }

    @Test
    public void testHumanMeleeAttack() {
        assertTrue(human.meleeAttack() > human.getAgility() - human.getAgilityPenalty() + human.getAttackBonus() + 1 && human.meleeAttack() < human.getAgility() - human.getAgilityPenalty() + human.getAttackBonus() + 21);
    }

    @Test
    public void testHumanRangedAttack() {
        assertTrue(human.rangedAttack() > human.getPerception() + human.getAttackBonus() + 1 && human.rangedAttack() < human.getPerception() + human.getAttackBonus() + 21);
    }

    @Test
    public void testHumanHide() {
        assertTrue(human.hide() > human.getIntelligence() - human.getAgilityPenalty() + 1 && human.hide() < human.getIntelligence() - human.getAgilityPenalty() + 21);
    }
}
