package com.aliens_vs_humans.src;

import java.util.Random;

public class Being {
    public boolean hidden;
    int intelligence;
    int perception;
    int brawn;
    int agility;
    public int vitality = 100;
    String equipment;
    int finesse = 1;
    int range = 20;
    int attack_bonus = 0;
    int damage_rating = 8;
    int damage_modifier;
    int agility_penalty = 0;
    int coverage = 0;
    int durability = 0;
    int structurePoints;
    Arena arena;

    public Being(Arena arena) {
        this.arena = arena;
        Random stat = new Random();
        int upperBound = 97;
        this.agility = stat.nextInt(upperBound) + 1;
        upperBound -= agility - 1;
        this.perception = stat.nextInt(upperBound) + 1;
        upperBound -= perception - 1;
        this.brawn = stat.nextInt(upperBound) + 1;
        upperBound -= brawn - 1;
        this.intelligence = upperBound;

        Random technology = new Random();
        upperBound = 8;
        int equipSelector = technology.nextInt(upperBound);
        switch (equipSelector) {
            case 0:
                this.equipment = "none";
                this.damage_modifier = this.brawn / 5;
                break;
            case 1:
                this.equipment = "ancient";
                this.range = 100;
                this.attack_bonus = 5;
                this.damage_rating = 7;
                this.damage_modifier = this.brawn / 5;
                this.agility_penalty = 25;
                this.coverage = 50;
                this.durability = 2;
                this.structurePoints = 50;
                break;
            case 2:
                this.equipment = "modern defensive";
                this.range = 250;
                this.attack_bonus = 5;
                this.damage_rating = 6;
                this.damage_modifier = 15;
                this.agility_penalty = 15;
                this.coverage = 90;
                this.durability = 5;
                this.structurePoints = 150;
                break;
            case 3:
                this.equipment = "modern offensive";
                this.range = 500;
                this.attack_bonus = 10;
                this.damage_rating = 5;
                this.damage_modifier = 25;
                this.agility_penalty = 5;
                this.coverage = 50;
                this.durability = 4;
                this.structurePoints = 100;
                break;
            case 4:
                this.equipment = "modern balanced";
                this.range = 500;
                this.attack_bonus = 10;
                this.damage_rating = 5;
                this.damage_modifier = 25;
                this.agility_penalty = 15;
                this.coverage = 90;
                this.durability = 5;
                this.structurePoints = 150;
                break;
            case 5:
                this.equipment = "futuristic defensive";
                this.range = 500;
                this.attack_bonus = 10;
                this.damage_rating = 4;
                this.damage_modifier = 30;
                this.agility_penalty = 10;
                this.coverage = 100;
                this.durability = 7;
                this.structurePoints = 250;
                break;
            case 6:
                this.equipment = "futuristic offensive";
                this.range = 1000;
                this.attack_bonus = 25;
                this.damage_rating = 3;
                this.damage_modifier = 40;
                this.agility_penalty = 0;
                this.coverage = 80;
                this.durability = 6;
                this.structurePoints = 200;
                break;
            default:
                this.equipment = "futuristic balanced";
                this.range = 1000;
                this.attack_bonus = 25;
                this.damage_rating = 3;
                this.damage_modifier = 40;
                this.agility_penalty = 10;
                this.coverage = 100;
                this.durability = 7;
                this.structurePoints = 250;
                break;
        }
        upperBound = 10 - equipSelector;
        this.finesse += upperBound;
        if (this.finesse > this.agility) {
            this.finesse = this.agility;
        }
    }

    public int getVitality() {
        // TODO Auto-generated method stub
        return this.vitality;
    }

    public int getAgility() {
        // TODO Auto-generated method stub
        return this.agility;
    }

    public int getFinesse() {
        // TODO Auto-generated method stub
        return this.finesse;
    }

    public String getEquipment() {
        // TODO Auto-generated method stub
        return this.equipment;
    }

    public int getIntelligence() {
        // TODO Auto-generated method stub
        return this.intelligence;
    }

    public int getPerception() {
        return this.perception;
    }

    public int getBrawn() {
        return this.brawn;
    }
    public int getRange() {
        return this.range;
    }
    public int getAttackBonus() {
        return this.attack_bonus;
    }
    public int getDamageRating() {
        return this.damage_rating;
    }
    public int getDamageModifier() {
        return this.damage_modifier;
    }
    public int getAgilityPenalty() {
        return this.agility_penalty;
    }
    public int getCoverage() {
        return this.coverage;
    }
    public int getDurability() {
        return this.durability;
    }

    @Override
    public String toString() {
        return "BEING\nIntelligence: " + this.getIntelligence() + "\nPerception: " + this.getPerception() + "\nBrawn: " + this.getBrawn() + "\nAgility: " + this.getAgility() + "\nVitality: " + this.getVitality() + "\nFinesse: " + this.getFinesse() + "\nEquipment: " + this.getEquipment() + "\n\tRange: " + this.getRange() + "\n\tAttack Bonus: " + this.getAttackBonus() + "\n\tDamage Rating: " + this.getDamageRating() + " Mod " + this.getDamageModifier() + "\n\tAgility Penalty: " + this.getAgilityPenalty() + "\n\tCoverage: " + this.getCoverage() + "\n\tDurability: " + this.getDurability() + "\n\tStructure Points: " + this.getStructurePoints() + "\n\n\n";
    }

    public int rollDie() {
        Random die = new Random();
        return die.nextInt(10) +1;
    }

    public int rollInitiative() {
        return this.getAgility() + this.rollDie() + this.rollDie();
    }

    public int defend() {
        return this.getAgility() - this.getAgilityPenalty() + this.rollDie() + this.rollDie();
    }

    public int unarmedAttack() {
        return this.getAgility() - this.getAgilityPenalty() + this.rollDie() + this.rollDie();
    }

    public int meleeAttack() {
        return this.getAgility() - this.getAgilityPenalty() + this.getAttackBonus() + this.rollDie() + this.rollDie();
    }

    public int rangedAttack() {
        return this.getPerception() + this.getAttackBonus() + this.rollDie() + this.rollDie();
    }

    public int hide() {
        return this.getIntelligence() - this.getAgilityPenalty() + this.rollDie() + this.rollDie();
    }

    public int getStructurePoints() {
        return this.structurePoints;
    }

}
