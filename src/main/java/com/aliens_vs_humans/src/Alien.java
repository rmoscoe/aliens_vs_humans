package com.aliens_vs_humans.src;
import java.util.Random;


public class Alien extends Being {
    public String name;
    public Power power;
    public String powerName = "None";

    Power flight = new Power(arena, this) {
        @Override
        public void activate(int dieRoll) {
            this.active = true;
            arena.alienLocation[2] = Math.min(arena.getHeight(), 300);
            this.roundsRemaining = (dieRoll + 1) / 2;
        }

        @Override
        public void deactivate() {
            this.active = false;
            arena.alienLocation[2] = 0;
            this.roundsRemaining = 0;
        }

        @Override
        public String toString() {
            return "Flight";
        }
    };
    Power invisibility = new Power(arena, this) {
        @Override
        public void activate(int dieRoll) {
            this.active = true;
            this.roundsRemaining = (dieRoll + 1) / 2;
            alien.hidden = true;
        }

        @Override
        public String toString() {
            return "Invisibility";
        }
    };
    Power shapeshifting = new Power(arena, this) {
        @Override
        public void activate(int dieRoll) {
            this.active = true;
            this.roundsRemaining = (dieRoll + 1) / 2;
            alien.hidden = true;
        }

        @Override
        public String toString() {
            return "Shapeshifting";
        }
    };
    Power mindControl = new Power(arena, this) {
        @Override
        public void activate(int dieRoll) {
            this.active = true;
            this.roundsRemaining = (dieRoll + 1) / 2;
            arena.human.hidden = false;
            arena.humanActions = 0;
        }

        @Override
        public String toString() {
            return "Mind Control";
        }
    };
    Power thermalVision = new Power(arena, this) {
        @Override
        public void activate(int dieRoll) {
            this.active = true;
            this.roundsRemaining = (dieRoll + 1) / 2;
            arena.human.hidden = false;
        }

        @Override
        public String toString() {
            return "Thermal Vision";
        }
    };
    Power regeneration = new Power(arena, this) {
        @Override
        public void activate(int dieRoll) {
            alien.vitality += alien.rollDie() + alien.rollDie();
            if (alien.getVitality() > 100) {
                alien.vitality = 100;
            }
        }

        @Override
        public String toString() {
            return "Regeneration";
        }
    };

    Power[] powers = {flight, shapeshifting, thermalVision, invisibility, regeneration, mindControl};

    public Alien(String name, Arena arena) {
        super(arena);
        this.name = name.toUpperCase();
        Random stat = new Random();
        int upperBound = 97;
        this.intelligence = stat.nextInt(upperBound) + 1;
        upperBound -= intelligence - 1;
        this.perception = stat.nextInt(upperBound) + 1;
        upperBound -= perception - 1;
        this.brawn = stat.nextInt(upperBound) + 1;
        upperBound -= brawn - 1;
        this.agility = upperBound;

        Random technology = new Random();
        upperBound = 8;
        int equipSelector = technology.nextInt(upperBound);
        //System.out.println(equipSelector);
        switch (equipSelector) {
            case 0 -> {
                this.equipment = "none";
                this.range = 20;
                this.attack_bonus = 0;
                this.damage_rating = 8;
                this.damage_modifier = this.brawn / 5;
                this.agility_penalty = 0;
                this.coverage = 0;
                this.durability = 0;
                this.structurePoints = 0;
            }
            case 1 -> {
                this.equipment = "ancient";
                this.range = 100;
                this.attack_bonus = 5;
                this.damage_rating = 7;
                this.damage_modifier = this.brawn / 5;
                this.agility_penalty = 25;
                this.coverage = 50;
                this.durability = 2;
                this.structurePoints = 50;
            }
            case 2 -> {
                this.equipment = "modern defensive";
                this.range = 250;
                this.attack_bonus = 5;
                this.damage_rating = 7;
                this.damage_modifier = 15;
                this.agility_penalty = 15;
                this.coverage = 90;
                this.durability = 5;
                this.structurePoints = 150;
            }
            case 3 -> {
                this.equipment = "modern offensive";
                this.range = 500;
                this.attack_bonus = 10;
                this.damage_rating = 6;
                this.damage_modifier = 25;
                this.agility_penalty = 5;
                this.coverage = 50;
                this.durability = 4;
                this.structurePoints = 100;
            }
            case 4 -> {
                this.equipment = "modern balanced";
                this.range = 500;
                this.attack_bonus = 10;
                this.damage_rating = 6;
                this.damage_modifier = 25;
                this.agility_penalty = 15;
                this.coverage = 90;
                this.durability = 5;
                this.structurePoints = 150;
            }
            case 5 -> {
                this.equipment = "futuristic defensive";
                this.range = 500;
                this.attack_bonus = 10;
                this.damage_rating = 5;
                this.damage_modifier = 30;
                this.agility_penalty = 10;
                this.coverage = 100;
                this.durability = 7;
                this.structurePoints = 250;
            }
            case 6 -> {
                this.equipment = "futuristic offensive";
                this.range = 1000;
                this.attack_bonus = 25;
                this.damage_rating = 4;
                this.damage_modifier = 40;
                this.agility_penalty = 0;
                this.coverage = 80;
                this.durability = 6;
                this.structurePoints = 200;
            }
            default -> {
                this.equipment = "futuristic balanced";
                this.range = 1000;
                this.attack_bonus = 25;
                this.damage_rating = 4;
                this.damage_modifier = 40;
                this.agility_penalty = 10;
                this.coverage = 100;
                this.durability = 7;
                this.structurePoints = 250;
            }
        }
        upperBound = 10 - equipSelector;
        int powerPicker = technology.nextInt(upperBound);
        //System.out.println(powerPicker);
        if (powerPicker > 0) {
            if (powerPicker < 7) {
                this.power = powers[powerPicker - 1];
            } else {
                this.power = powers[5];
                powerPicker = 6;
            }
        }

        upperBound -= powerPicker;
        this.finesse += upperBound;
        if (this.finesse > this.agility) {
            this.finesse = this.agility;
        }

        if (this.power != null) {
            this.powerName = this.power.toString();
        }
    }

    @Override
    public String toString() {
        return this.getName() + "\nSpecies: Alien\nIntelligence: " + this.getIntelligence() + "\nPerception: " + this.getPerception() + "\nBrawn: " + this.getBrawn() + "\nAgility: " + this.getAgility() + "\nVitality: " + this.getVitality() + "\nFinesse: " + this.getFinesse() + "\nPower: " + this.powerName + "\nEquipment: " + this.getEquipment() + "\n\tRange: " + this.getRange() + "\n\tAttack Bonus: " + this.getAttackBonus() + "\n\tDamage Rating: " + this.getDamageRating() + " Mod " + this.getDamageModifier() + "\n\tAgility Penalty: " + this.getAgilityPenalty() + "\n\tCoverage: " + this.getCoverage() + "\n\tDurability: " + this.getDurability() + "\n\tStructure Points: " + this.getStructurePoints() + "\n\n\n";
    }

    public String getName() {
        // TODO Auto-generated method stub
        return this.name;
    }
}
