package com.aliens_vs_humans.src;
import java.util.Random;
import java.util.ArrayList;

public class Human extends Being {
    String name;

    public Human(String name, Arena arena) {
        super(arena);
        this.name = name.toUpperCase();
    }

    @Override
    public String toString() {
        return this.getName() + "\nSpecies: Human\nIntelligence: " + this.getIntelligence() + "\nPerception: " + this.getPerception() + "\nBrawn: " + this.getBrawn() + "\nAgility: " + this.getAgility() + "\nVitality: " + this.getVitality() + "\nFinesse: " + this.getFinesse() + "\nEquipment: " + this.getEquipment() + "\n\tRange: " + this.getRange() + "\n\tAttack Bonus: " + this.getAttackBonus() + "\n\tDamage Rating: " + this.getDamageRating() + " Mod " + this.getDamageModifier() + "\n\tAgility Penalty: " + this.getAgilityPenalty() + "\n\tCoverage: " + this.getCoverage() + "\n\tDurability: " + this.getDurability() + "\n\tStructure Points: " + this.getStructurePoints() + "\n\n\n";
    }

    public String getName() {
        // TODO Auto-generated method stub
        return this.name;
    }
}
