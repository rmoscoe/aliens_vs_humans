package com.aliens_vs_humans.src;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class Arena {
    public Human human;
    public int alienInitiative;
    public Alien alien;
    public int humanInitiative;
    public Being hasInitiative;
    public int humanActions;
    public int alienActions;
    public Being defender;
    public Being winner;
    public Being loser;
    private String name;
    private String arenaType;
    private int length;
    private int width;
    public int height;
    public int humanStartingVitality;
    public int alienStartingVitality;
    public int humanStartingSP;
    public int alienStartingSP;
    public int stalemateRounds;

    //These are the arrays for the coordinates.
    public double[] humanLocation = new double[3];
    public double[] alienLocation = new double[3];

    private double distance;

    //Constructor
    public Arena(String name, String arenaType, int length, int width, int height) {
        this.name = name;
        this.arenaType = arenaType;
        this.length = length;
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString() {
        return "This battle will take place on " + this.getArenaType() + " " + this.getName() + ".";
    }

    public String getName() {
        return name;
    }

    public int getLength() {
        return this.length;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getArenaType() {
        return arenaType;
    }

    //Creates a human character and places the character within the arena.
    public Human spawnHuman() {
        human = new Human("Rex", this);
        System.out.println(human);
        Random coordinates = new Random();
        humanLocation[0] = coordinates.nextInt(1, getLength());
        humanLocation[1] = coordinates.nextInt(1, getWidth());
        humanLocation[2] = 0;
        return human;
    }

    //Creates an alien character and places the character within the arena.
    public Object spawnAlien() {
        alien = new Alien("Kodos", this);
        System.out.println(alien);
        Random coordinates = new Random();
        alienLocation[0] = coordinates.nextInt(1, getLength());
        alienLocation[1] = coordinates.nextInt(1, getWidth());
        alienLocation[2] = 0;
        return alien;
    }

    public double getDistance() {
        return distance;
    }

    //Calculates the distance between the characters using the Pythagorean Theorem.
    public int findDistance() {
        distance = Math.sqrt(Math.pow(Math.abs(humanLocation[0] - alienLocation[0]), 2) + Math.pow(Math.abs(humanLocation[1] - alienLocation[1]), 2) + Math.pow(Math.abs(humanLocation[2] - alienLocation[2]), 2));
        return (int) distance;
    }

    //This method starts each round of the simulation by determining who goes first and how many actions each character will take.
    public void startRound() {
        System.out.println("\nRolling initiative...");
        humanStartingVitality = human.getVitality();
        alienStartingVitality = alien.getVitality();
        humanStartingSP = human.getStructurePoints();
        alienStartingSP = alien.getStructurePoints();
        if (alien.hidden && human.hidden) {
            if (alien.hide() + alien.getPerception() > human.hide() + human.getPerception()) {
                human.hidden = false;
                System.out.println(human.getName() + " has given away his position.");
            } else {
                alien.hidden = false;
                System.out.println(alien.getName() + " has given away his position.");
            }
        }
        if (alien.hidden) {
            humanInitiative = 0;
        } else {
            humanInitiative = human.rollInitiative();
        }
        if (alien.power != null) {
            if (alien.power.toString().equals("Mind Control") && alien.power.active) {
                human.hidden = false;
                humanInitiative = 0;
            }
        }
        if (human.hidden) {
            alienInitiative = 0;
        } else {
            alienInitiative = alien.rollInitiative();
        }
        if (humanInitiative >= alienInitiative) {
            hasInitiative = human;
            defender = alien;
        } else {
            hasInitiative = alien;
            defender = human;
        }
        if (humanInitiative != 0) {
            humanActions = human.getFinesse();
        } else {
            humanActions = 0;
        }
        if (alienInitiative != 0) {
            alienActions = alien.getFinesse();
        } else {
            alienActions = 0;
        }
    }

    //This method selects an action for the character whose turn it is.
    public String chooseAction(Being hasInitiative) {
        distance = findDistance();
        if (hasInitiative == alien && alien.power != null && !alien.power.active && (!alien.power.toString().equals("Regeneration") || alien.getVitality() < 70) && (!alien.power.toString().equals("Flight") || distance <= Math.min(getHeight(), 300))) {
            return "activate power";
        } else if (hasInitiative.getIntelligence() >= 5 && !hasInitiative.hidden && hasInitiative.getIntelligence() - hasInitiative.getAgilityPenalty() > defender.getPerception() - 18 && (hasInitiative != human || alien.power == null || (!alien.power.toString().equals("Thermal Vision") || !alien.power.active))) {
            return "hide";
        } else if (!hasInitiative.getEquipment().equals("None") && distance <= hasInitiative.getRange() && distance >= hasInitiative.getRange() / 20.0 && !defender.hidden) {
            return "ranged attack";
        } else if (hasInitiative.getIntelligence() >= 10 && hasInitiative.getRange() > defender.getRange() && distance <= defender.getRange()) {
            return "open distance";
        } else if (hasInitiative.getIntelligence() >= 15 && (distance > hasInitiative.getRange() && (hasInitiative == alien || alienLocation[2] <= hasInitiative.getRange()) || (hasInitiative.getRange() < defender.getRange() && distance > defender.getRange() / 20.0 && (hasInitiative == alien || alienLocation[2] <= hasInitiative.getRange())))) {
            return "close distance";
        } else if (!hasInitiative.getEquipment().equals("None") && !defender.hidden && distance <= 12 && distance >= 2) {
            return "melee attack";
        } else if (distance > hasInitiative.getRange() && (defender != alien || (alien.power != null && !alien.power.toString().equals("Flight")) || !Objects.requireNonNull(alien.power).active)) {
            return "close distance";
        } else if (distance < hasInitiative.getRange() / 20.0) {
            return "open distance";
        } else if (distance <= 6 && !defender.hidden) {
            return "unarmed attack";
        } else if (hasInitiative.getIntelligence() >= 5 && hasInitiative.getRange() > defender.getRange() || distance >= defender.getRange() / 20.0 && distance <= 12 && distance >= 2) {
            return "close distance";
        } else {
            return "open distance";
        }
    }

    //This method determines the result of a character's attempt to hide.
    public String resolveHide(Being hasInitiative) {
        if (hasInitiative.hide() > defender.getPerception() + defender.rollDie() + defender.rollDie()) {
            hasInitiative.hidden = true;
            if (hasInitiative == human) {
                System.out.println(human.getName() + " hides to prepare an ambush.");
                this.hasInitiative = alien;
                humanActions -= 1;
                defender = human;
            } else {
                System.out.println(alien.getName() + " hides to prepare an ambush.");
                this.hasInitiative = human;
                alienActions -= 1;
                defender = alien;
            }
            return "Success";
        } else {
            if (hasInitiative == human) {
                this.hasInitiative = alien;
                humanActions -= 1;
                defender = human;
            } else {
                this.hasInitiative = human;
                alienActions -= 1;
                defender = alien;
            }
            return "Failure";
        }
    }

    //This method determines the result of a character's attempt to close the distance to his opponent.
    public String closeDistance(Being hasInitiative) {
        boolean flee = false;
        int startingDistance = findDistance();
        int humanMovement;
        int alienMovement;
        double xDist;
        double xSpace;
        double yDist;
        double ySpace;
        int xMove;
        int yMove;
        int xChange;
        int yChange;

        //The alien is trying to close the distance.
        if (hasInitiative == alien) {

            //Determines whether the human will attempt to flee or stand his ground.
            if (humanActions > 0 && defender.getIntelligence() >= 5 && defender.getRange() > hasInitiative.getRange() && startingDistance <= defender.getRange()) {
                flee = true;
                humanActions -=1;
            }
            System.out.println(alien.getName() + " is attempting to close the distance to " + human.getName() + ".");

            //If the human is attempting to flee, this portion of the method determines how far the human can move, moves the human character, and subtracts 1 from the human's remaining actions for the round.
            if (flee) {
                System.out.println(human.getName() + " is attempting to flee.");
                humanMovement = human.getAgility() + human.rollDie() + human.rollDie();
                xDist = humanLocation[0] - alienLocation[0];
                xSpace = getLength() - humanLocation[0];
                yDist = humanLocation[1] - alienLocation[1];
                ySpace = getWidth() - humanLocation[1];

                //Ensures the human cannot move beyond the confines of the arena.
                if (humanMovement > Math.max(Math.max((int) (humanLocation[0] + ySpace), (int) (humanLocation[0] + humanLocation[1])), Math.max((int) (xSpace + humanLocation[1]), (int) (xSpace + ySpace)))) {
                    humanMovement = Math.max(Math.max((int) (humanLocation[0] + ySpace), (int) (humanLocation[0] + humanLocation[1])), Math.max((int) (xSpace + humanLocation[1]), (int) (xSpace + ySpace)));
                }
                if (Math.abs(xDist) < Math.abs(yDist)) {

                    //Assigns a change to the human's x coordinate.
                    if (humanLocation[0] <= alienLocation[0]) {
                        xMove = Math.min(humanMovement, (int) humanLocation[0]);
                        this.humanLocation[0] -= xMove;
                    } else {
                        xMove = (int) Math.min(humanMovement, xSpace);
                        this.humanLocation[0] += xMove;
                    }

                    //Reduces the human's remaining movement by the change in the x coordinate and assigns the change to the y coordinate.
                    humanMovement -= xMove;
                    if (humanMovement > 0) {
                        if (humanLocation[1] <= alienLocation[1]) {
                            if (humanLocation[1] > humanMovement) {
                                this.humanLocation[1] -= humanMovement;
                            } else if (ySpace > humanMovement) {
                                this.humanLocation[1] += humanMovement;
                            } else {
                                xChange = (int) Math.min(xMove + humanMovement, xSpace);
                                this.humanLocation[0] += xChange;
                                humanMovement -= xChange;
                                if (humanLocation[1] > humanMovement) {
                                    this.humanLocation[1] -= humanMovement;
                                } else {
                                    this.humanLocation[1] += humanMovement;
                                }
                            }
                        }
                    }
                } else {

                    //Assigns a change to the human's y coordinate.
                    if (humanLocation[1] <= alienLocation[1]) {
                        yMove = Math.min(humanMovement, (int) humanLocation[1]);
                        this.humanLocation[1] -= yMove;
                    } else {
                        yMove = (int) Math.min(humanMovement, ySpace);
                        this.humanLocation[1] += yMove;
                    }

                    //reduces the human's remaining movement by the change in the y coordinate and assigns the change to the x coordinate.
                    humanMovement -= yMove;
                    if (humanMovement > 0) {
                        if (humanLocation[0] <= alienLocation[0]) {
                            if (humanLocation[0] > humanMovement) {
                                this.humanLocation[0] -= humanMovement;
                            } else if (xSpace > humanMovement) {
                                this.humanLocation[0] += humanMovement;
                            } else {
                                yChange = (int) Math.min(yMove + humanMovement, ySpace);
                                this.humanLocation[1] += yChange;
                                humanMovement -= yChange;
                                if (humanLocation[0] > humanMovement) {
                                    this.humanLocation[0] -= humanMovement;
                                } else {
                                    this.humanLocation[0] += humanMovement;
                                }
                            }
                        }
                    }
                }

                //determines whether the human has given away his position by moving, if the human was previously hidden.
                if (human.hidden) {
                    if (human.rollDie() + human.rollDie() > human.getAgility()) {
                        human.hidden = false;
                        System.out.println(human.getName() + " has given away his position.");
                    }
                }
            }

            //Moves the alien in pursuit of the human.
            alienMovement = alien.getAgility() + alien.rollDie() + alien.rollDie();
            xDist = alienLocation[0] - humanLocation[0];
            xSpace = getLength() - alienLocation[0];
            yDist = alienLocation[1] - humanLocation[1];
            ySpace = getWidth() - alienLocation[1];

            //Ensures the alien cannot move beyond the confines of the arena nor get within range of enemy fire unnecessarily.
            if (alienMovement > Math.max(Math.max((int) (alienLocation[0] + ySpace), (int) (alienLocation[0] + alienLocation[1])), Math.max((int) (xSpace + alienLocation[1]), (int) (xSpace + ySpace)))) {
                alienMovement = Math.max(Math.max((int) (alienLocation[0] + ySpace), (int) (alienLocation[0] + alienLocation[1])), Math.max((int) (xSpace + alienLocation[1]), (int) (xSpace + ySpace)));
            }
            if (hasInitiative.getIntelligence() >= 5 && hasInitiative.getRange() > defender.getRange()) {
                if (Math.abs(xDist) < alienMovement) {
                    alienMovement = (int) Math.min(Math.sqrt(Math.pow(xDist, 2) + Math.pow(alienMovement - Math.abs(xDist), 2)), findDistance() - defender.getRange());
                } else if (Math.abs(yDist) < alienMovement) {
                    alienMovement = (int) Math.min(Math.sqrt(Math.pow(yDist, 2) + Math.pow(alienMovement - Math.abs(yDist), 2)), findDistance() - defender.getRange());
                } else if (Math.abs(xDist) > Math.abs(yDist)) {
                    alienMovement = (int) Math.min(alienMovement, Math.sqrt(Math.pow(findDistance() - defender.getRange(), 2) - Math.pow(yDist, 2)));
                } else {
                    alienMovement = (int) Math.min(alienMovement, Math.sqrt(Math.pow(findDistance() - defender.getRange(), 2) - Math.pow(xDist, 2)));
                }
            }
            System.out.println("Alien movement: " + alienMovement);
            if (Math.abs(xDist) > Math.abs(yDist)) {

                //Assigns a change to the alien's x coordinate.
                xMove = (int) Math.min(alienMovement, Math.abs(xDist));
                if (alienLocation[0] < humanLocation[0]) {
                    this.alienLocation[0] += xMove;
                } else {
                    this.alienLocation[0] -= xMove;
                }

                //Reduces the alien's movement by the change in the x coordinate and assigns the change to the y coordinate.
                alienMovement -= xMove;
                if (alienMovement > 0 && yDist != 0) {
                    yMove = (int) Math.min(alienMovement, Math.abs(yDist));
                    if (yDist > 0) {
                        this.alienLocation[1] -= yMove;
                    } else {
                        this.alienLocation[1] += yMove;
                    }
                }
            } else {

                //Assigns a change to the alien's y coordinate.
                yMove = (int) Math.min(alienMovement, Math.abs(yDist));
                if (alienLocation[1] < humanLocation[1]) {
                    this.alienLocation[1] += yMove;
                } else {
                    this.alienLocation[1] -= yMove;
                }

                //Reduces the alien's movement by the change in the y coordinate and assigns the change to the x coordinate.
                alienMovement -= yMove;
                if (alienMovement > 0 && xDist != 0) {
                    xMove = (int) Math.min(alienMovement, Math.abs(xDist));
                    if (xDist > 0) {
                        this.alienLocation[0] -= xMove;
                    } else {
                        this.alienLocation[0] += xMove;
                    }
                }
            }
            alienActions -=1;

            //Determines whether the alien has given away his position by moving, if the alien was previously hidden.
            if (alien.hidden && (alien.power == null || !(alien.power.toString().equals("Invisibility") && alien.power.active))) {
                if (alien.rollDie() + alien.rollDie() > alien.getAgility()) {
                    alien.hidden = false;
                    System.out.println(alien.getName() + " has given away his position.");
                }
            }
        } else { //The human is trying to close the distance to the alien.

            //Determines whether the alien will attempt to flee or stand his ground.
            if (alienActions > 0 && defender.getIntelligence() >= 10 && defender.getRange() > hasInitiative.getRange()) {
                flee = true;
                alienActions -=1;
            }
            System.out.println(human.getName() + " is attempting to close the distance to " + alien.getName() + ".");

            //If the alien is fleeing, this section of the method determines how far the alien can move and moves the alien.
            if (flee) {
                System.out.println(alien.getName() + " is attempting to flee.");
                alienMovement = alien.getAgility() + alien.rollDie() + alien.rollDie();
                xDist = alienLocation[0] - humanLocation[0];
                xSpace = getLength() - alienLocation[0];
                yDist = alienLocation[1] - humanLocation[1];
                ySpace = getWidth() - alienLocation[1];

                //Ensures the alien cannot move beyond the confines of the arena.
                if (alienMovement > Math.max(Math.max((int) (alienLocation[0] + ySpace), (int) (alienLocation[0] + alienLocation[1])), Math.max((int) (xSpace + alienLocation[1]), (int) (xSpace + ySpace)))) {
                    alienMovement = Math.max(Math.max((int) (alienLocation[0] + ySpace), (int) (alienLocation[0] + alienLocation[1])), Math.max((int) (xSpace + alienLocation[1]), (int) (xSpace + ySpace)));
                }
                if (Math.abs(xDist) < Math.abs(yDist)) {

                    //Assigns the change in the alien's x coordinate.
                    if (alienLocation[0] <= humanLocation[0]) {
                        xMove = (int) Math.min(alienMovement, alienLocation[0]);
                        this.alienLocation[0] -= xMove;
                    } else {
                        xMove = (int) Math.min(alienMovement, xSpace);
                        this.alienLocation[0] += xMove;
                    }

                    //Reduces the alien's movement by the change in the x coordinate and assigns the change in the y coordinate.
                    alienMovement -= xMove;
                    if (alienMovement > 0) {
                        if (alienLocation[1] <= humanLocation[1]) {
                            if (alienLocation[1] > alienMovement) {
                                this.alienLocation[1] -= alienMovement;
                            } else if (ySpace > alienMovement) {
                                this.alienLocation[1] += alienMovement;
                            } else {
                                xChange = (int) Math.min(xMove + alienMovement, xSpace);
                                this.alienLocation[0] += xChange;
                                alienMovement -= xChange;
                                if (alienLocation[1] > alienMovement) {
                                    this.alienLocation[1] -= alienMovement;
                                } else {
                                    this.alienLocation[1] += alienMovement;
                                }
                            }
                        }
                    }
                } else {

                    //Assigns the change in the alien's y coordinate.
                    if (alienLocation[1] <= humanLocation[1]) {
                        yMove = (int) Math.min(alienMovement, alienLocation[1]);
                        this.alienLocation[1] -= yMove;
                    } else {
                        yMove = (int) Math.min(alienMovement, ySpace);
                        this.alienLocation[1] += yMove;
                    }

                    //Reduces the alien's movement by the change in the y coordinate and assigns the change in the x coordinate.
                    alienMovement -= yMove;
                    if (alienMovement > 0) {
                        if (alienLocation[0] <= humanLocation[0]) {
                            if (alienLocation[0] > alienMovement) {
                                this.alienLocation[0] -= alienMovement;
                            } else if (xSpace > alienMovement) {
                                this.alienLocation[0] += alienMovement;
                            } else {
                                yChange = (int) Math.min(yMove + alienMovement, ySpace);
                                this.alienLocation[1] += yChange;
                                alienMovement -= yChange;
                                if (alienLocation[0] > alienMovement) {
                                    this.alienLocation[0] -= alienMovement;
                                } else {
                                    this.alienLocation[0] += alienMovement;
                                }
                            }
                        }
                    }
                }

                //Determines whether the alien has given away his position, if the alien was hidden previously.
                if (alien.hidden && !(Objects.requireNonNull(alien.power.toString().equals("Invisibility") && alien.power.active))) {
                    if (alien.rollDie() + alien.rollDie() > alien.getAgility()) {
                        alien.hidden = false;
                    }
                }
            }

            //This section of the method moves the human closer to the alien.
            humanMovement = human.getAgility() + human.rollDie() + human.rollDie();
            xDist = humanLocation[0] - alienLocation[0];
            xSpace = getLength() - humanLocation[0];
            yDist = humanLocation[1] - alienLocation[1];
            ySpace = getWidth() - humanLocation[1];

            //Ensures the human cannot move beyond the confines of the arena nor get within range of enemy fire unnecessarily.
            if (humanMovement > Math.max((Math.max((int) (humanLocation[0] + ySpace), (int) (humanLocation[0] + humanLocation[1]))), Math.max((int) (xSpace + humanLocation[1]), (int) (xSpace + ySpace)))) {
                humanMovement = Math.max((Math.max((int) (humanLocation[0] + ySpace), (int) (humanLocation[0] + humanLocation[1]))), Math.max((int) (xSpace + humanLocation[1]), (int) (xSpace + ySpace)));
            }
            if (hasInitiative.getIntelligence() >= 5 && hasInitiative.getRange() > defender.getRange()) {
                if (Math.abs(xDist) < humanMovement) {
                    humanMovement = (int) Math.min(Math.sqrt(Math.pow(xDist, 2) + Math.pow(humanMovement - Math.abs(xDist), 2)), findDistance() - defender.getRange());
                } else if (Math.abs(yDist) < humanMovement) {
                    humanMovement = (int) Math.min(Math.sqrt(Math.pow(yDist, 2) + Math.pow(humanMovement - Math.abs(yDist), 2)), findDistance() - defender.getRange());
                } else if (Math.abs(xDist) > Math.abs(yDist)) {
                    humanMovement = (int) Math.min(humanMovement, Math.sqrt(Math.pow(findDistance() - defender.getRange(), 2) - Math.pow(yDist, 2)));
                } else {
                    humanMovement = (int) Math.min(humanMovement, Math.sqrt(Math.pow(findDistance() - defender.getRange(), 2) - Math.pow(xDist, 2)));
                }
            }
            if (Math.abs(xDist) > Math.abs(yDist)) {

                //Assigns the change in the human's x coordinate.
                xMove = (int) Math.min(humanMovement, Math.abs(xDist));
                if (humanLocation[0] < alienLocation[0]) {
                    this.humanLocation[0] += xMove;
                } else {
                    this.humanLocation[0] -= xMove;
                }

                //Reduces the human's movement by the change in the x coordinate and assigns the change in the y coordinate.
                humanMovement -= xMove;
                if (humanMovement > 0 && yDist != 0) {
                    yMove = (int) Math.min(humanMovement, Math.abs(yDist));
                    if (yDist > 0) {
                        this.humanLocation[1] -= yMove;
                    } else {
                        this.humanLocation[1] += yMove;
                    }
                }
            } else {

                //Assigns the change in the human's y coordinate.
                yMove = (int) Math.min(humanMovement, Math.abs(yDist));
                if (humanLocation[1] < alienLocation[1]) {
                    this.humanLocation[1] += yMove;
                } else {
                    this.humanLocation[1] -= yMove;
                }

                //Reduces the human's movement by the change in the y coordinate and assigns the change in the x coordinate.
                humanMovement -= yMove;
                if (humanMovement > 0 && xDist != 0) {
                    xMove = (int) Math.min(humanMovement, Math.abs(xDist));
                    if (xDist > 0) {
                        this.humanLocation[0] -= xMove;
                    } else {
                        this.humanLocation[0] += xMove;
                    }
                }
            }
            humanActions -= 1;

            //Determines whether the human has given away his position by moving, if the human was previously hidden.
            if (human.hidden) {
                if (human.rollDie() + human.rollDie() > human.getAgility()) {
                    human.hidden = false;
                }
            }
        }

        //Determines which character takes the next turn.
        if (!flee) {
            if (this.hasInitiative == alien) {
                this.hasInitiative = human;
                defender = alien;
            } else {
                this.hasInitiative = alien;
                defender = human;
            }
        }

        //Prints and returns the results of the characters' efforts to close distance and/or flee.
        int changeInDistance;
        distance = findDistance();
        System.out.println("Human location: " + Arrays.toString(humanLocation) + "\nAlien location: " + Arrays.toString(alienLocation) + "\nEnding Distance: " + distance);
        if (distance < startingDistance) {
            changeInDistance = (int) (startingDistance - distance);
            System.out.println("\tThe distance has decreased by " + changeInDistance);
            return "Success";
        } else {
            changeInDistance = (int) (distance - startingDistance);
            System.out.println("\tThe distance has increased by " + changeInDistance);
            return "Failure";
        }
    }

    //This method determines the results of a character's attempt to widen the distance to his opponent.
    public String resolveOpenDistance(Being hasInitiative) {
        boolean chase = false;
        int startingDistance = findDistance();
        int humanMovement;
        int alienMovement;
        double xDist;
        double xSpace;
        double yDist;
        double ySpace;
        int xMove;
        int yMove;

        //The alien is trying to widen the distance.
        if (hasInitiative == alien) {

            //Determines whether the human will give chase.
            if (humanActions > 0 && defender.getIntelligence() >= 5 && defender.getRange() < hasInitiative.getRange() || startingDistance >= defender.getRange()) {
                chase = true;
                humanActions -=1;
            }
            System.out.println(alien.getName() + " is attempting to widen the distance to " + human.getName() + ".");

            //Moves the alien away from the human.
            alienMovement = alien.getAgility() + alien.rollDie() + alien.rollDie();
            xDist = alienLocation[0] - humanLocation[0];
            xSpace = getLength() - alienLocation[0];
            yDist = alienLocation[1] - humanLocation[1];
            ySpace = getWidth() - alienLocation[1];

            //Ensures the alien cannot move beyond the confines of the arena.
            if (alienMovement > Math.max(Math.max((int) (alienLocation[0] + ySpace), (int) (alienLocation[0] + alienLocation[1])), Math.max((int) (xSpace + alienLocation[1]), (int) (xSpace + ySpace)))) {
                alienMovement = Math.max(Math.max((int) (alienLocation[0] + ySpace), (int) (alienLocation[0] + alienLocation[1])), Math.max((int) (xSpace + alienLocation[1]), (int) (xSpace + ySpace)));
            }
            if (Math.abs(xDist) < Math.abs(yDist)) {

                //Assigns a change to the alien's x coordinate.
                if (xDist <= 0 || (alienMovement - xSpace > alienLocation[1] && alienMovement - xSpace > ySpace)) {
                    xMove = (int) Math.min(alienMovement, alienLocation[0]);
                    alienLocation[0] -= xMove;
                } else {
                    xMove = (int) Math.min(alienMovement, xSpace);
                    alienLocation[1] += xMove;
                }

                //Reduces the alien's movement by the change in the x coordinate and assigns the change to the y coordinate.
                alienMovement -= xMove;
                if (alienMovement > 0) {
                    if (yDist <= 0) {
                        yMove = (int) Math.min(alienMovement, alienLocation[1]);
                        alienLocation[1] -= yMove;
                    } else {
                        yMove = (int) Math.min(alienMovement, ySpace);
                        alienLocation[1] += yMove;
                    }
                }
            } else {

                //Assigns a change to the alien's y coordinate.
                if (yDist < alienLocation[1] || yDist > alienMovement / 2.0) {
                    yMove = (int) Math.min(alienMovement, alienLocation[1]);
                    alienLocation[1] -= yMove;
                } else {
                    yMove = (int) Math.min(alienMovement, ySpace);
                    alienLocation[1] += yMove;
                }

                //Reduces the alien's movement by the change in the y coordinate and assigns the change to the x coordinate.
                alienMovement -= yMove;
                if (alienMovement > 0) {
                    if (xDist <=0) {
                        xMove = (int) Math.min(alienMovement, alienLocation[0]);
                        alienLocation[0] -= xMove;
                    } else {
                        xMove = (int) Math.min(alienMovement, xSpace);
                        alienLocation[0] += xMove;
                    }
                }
            }
            alienActions -=1;

            //Determines whether the alien has given away his position by moving, if the alien was previously hidden.
            if (alien.hidden && (alien.power == null || !(alien.power.toString().equals("Invisibility") && alien.power.active))) {
                if (alien.rollDie() + alien.rollDie() > alien.getAgility()) {
                    alien.hidden = false;
                    System.out.println(alien.getName() + " has given away his position.");
                }
            }

            //If the human is chasing, this portion of the method determines how far the human can move and moves the human character.
            if (chase) {
                System.out.println(human.getName() + " is giving chase.");
                humanMovement = human.getAgility() + human.rollDie() + human.rollDie();
                xDist = humanLocation[0] - alienLocation[0];
                xSpace = getLength() - humanLocation[0];
                yDist = humanLocation[1] - alienLocation[1];
                ySpace = getWidth() - humanLocation[1];

                //Ensures the human cannot move beyond the confines of the arena and does not unnecessarily move within range of enemy fire.
                if (humanMovement > Math.max(Math.max((int) (humanLocation[0] + ySpace), (int) (humanLocation[0] + humanLocation[1])), Math.max((int) (xSpace + humanLocation[1]), (int) (xSpace + ySpace)))) {
                    humanMovement = Math.max(Math.max((int) (humanLocation[0] + ySpace), (int) (humanLocation[0] + humanLocation[1])), Math.max((int) (xSpace + humanLocation[1]), (int) (xSpace + ySpace)));
                }
                if (defender.getIntelligence() >= 5 && defender.getRange() > hasInitiative.getRange()) {
                    if (Math.abs(xDist) < humanMovement) {
                        humanMovement = (int) Math.min(Math.sqrt(Math.pow(xDist, 2) + Math.pow(humanMovement - Math.abs(xDist), 2)), findDistance() - hasInitiative.getRange());
                    } else if (Math.abs(yDist) < humanMovement) {
                        humanMovement = (int) Math.min(Math.sqrt(Math.pow(yDist, 2) + Math.pow(humanMovement - Math.abs(yDist), 2)), findDistance() - hasInitiative.getRange());
                    } else if (Math.abs(xDist) > Math.abs(yDist)) {
                        humanMovement = (int) Math.min(humanMovement, Math.sqrt(Math.pow(findDistance() - hasInitiative.getRange(), 2) - Math.pow(yDist, 2)));
                    } else {
                        humanMovement = (int) Math.min(humanMovement, Math.sqrt(Math.pow(findDistance() - hasInitiative.getRange(), 2) - Math.pow(xDist, 2)));
                    }
                }

                if (Math.abs(xDist) > Math.abs(yDist)) {

                    //Assigns a change to the human's x coordinate.
                    xMove = (int) Math.min(humanMovement, Math.abs(xDist));
                    if (humanLocation[0] <= alienLocation[0]) {
                        this.humanLocation[0] += xMove;
                    } else {
                        this.humanLocation[0] -= xMove;
                    }

                    //Reduces the human's remaining movement by the change in the x coordinate and assigns the change to the y coordinate.
                    humanMovement -= xMove;
                    if (humanMovement > 0) {
                        if (humanLocation[1] <= alienLocation[1]) {
                            humanLocation[1] += humanMovement;
                        } else {
                            humanLocation[1] -= humanMovement;
                        }
                    }
                } else {

                    //Assigns a change to the human's y coordinate.
                    yMove = (int) Math.min(humanMovement, Math.abs(yDist));
                    if (humanLocation[1] <= alienLocation[1]) {
                        this.humanLocation[1] += yMove;
                    } else {
                        this.humanLocation[1] -= yMove;
                    }

                    //reduces the human's remaining movement by the change in the y coordinate and assigns the change to the x coordinate.
                    humanMovement -= yMove;
                    if (humanMovement > 0) {
                        if (humanLocation[0] <= alienLocation[0]) {
                            humanLocation[0] += humanMovement;
                        } else {
                            humanLocation[1] += humanMovement;
                        }
                    }
                }

                //determines whether the human has given away his position by moving, if the human was previously hidden.
                if (human.hidden) {
                    if (human.rollDie() + human.rollDie() > human.getAgility()) {
                        human.hidden = false;
                        System.out.println(human.getName() + " has given away his position.");
                    }
                }
            }
        } else { //The human is trying to widen the distance to the alien.

            //Determines whether the alien will give chase.
            if (alienActions > 0 && defender.getIntelligence() >= 5 && defender.getRange() < hasInitiative.getRange() || startingDistance >= defender.getRange()) {
                chase = true;
                alienActions -=1;
            }
            System.out.println(human.getName() + " is attempting to widen the distance to " + alien.getName() + ".");

            //This section of the method moves the human away from the alien.
            humanMovement = human.getAgility() + human.rollDie() + human.rollDie();
            xDist = humanLocation[0] - alienLocation[0];
            xSpace = getLength() - humanLocation[0];
            yDist = humanLocation[1] - alienLocation[1];
            ySpace = getWidth() - humanLocation[1];

            //Ensures the human cannot move beyond the confines of the arena.
            if (humanMovement > Math.max((Math.max((int) (humanLocation[0] + ySpace), (int) (humanLocation[0] + humanLocation[1]))), Math.max((int) (xSpace + humanLocation[1]), (int) (xSpace + ySpace)))) {
                humanMovement = Math.max((Math.max((int) (humanLocation[0] + ySpace), (int) (humanLocation[0] + humanLocation[1]))), Math.max((int) (xSpace + humanLocation[1]), (int) (xSpace + ySpace)));
            }
            if (Math.abs(xDist) < Math.abs(yDist)) {

                //Assigns a change to the human's x coordinate.
                if (xDist <= 0 || (humanMovement - xSpace > humanLocation[1] && humanMovement - xSpace > ySpace)) {
                    xMove = (int) Math.min(humanMovement, humanLocation[0]);
                    humanLocation[0] -= xMove;
                } else {
                    xMove = (int) Math.min(humanMovement, xSpace);
                    humanLocation[1] += xMove;
                }

                //Reduces the human's movement by the change in the x coordinate and assigns the change to the y coordinate.
                humanMovement -= xMove;
                if (humanMovement > 0) {
                    if (yDist <= 0) {
                        yMove = (int) Math.min(humanMovement, humanLocation[1]);
                        humanLocation[1] -= yMove;
                    } else {
                        yMove = (int) Math.min(humanMovement, ySpace);
                        humanLocation[1] += yMove;
                    }
                }
            } else {

                //Assigns a change to the human's y coordinate.
                if (yDist < humanLocation[1] || yDist > humanMovement / 2.0) {
                    yMove = (int) Math.min(humanMovement, humanLocation[1]);
                    humanLocation[1] -= yMove;
                } else {
                    yMove = (int) Math.min(humanMovement, ySpace);
                    humanLocation[1] += yMove;
                }

                //Reduces the human's movement by the change in the y coordinate and assigns the change to the x coordinate.
                humanMovement -= yMove;
                if (humanMovement > 0) {
                    if (xDist <=0) {
                        xMove = (int) Math.min(humanMovement, humanLocation[0]);
                        humanLocation[0] -= xMove;
                    } else {
                        xMove = (int) Math.min(humanMovement, xSpace);
                        humanLocation[0] += xMove;
                    }
                }
            }
            humanActions -=1;

            //Determines whether the human has given away his position by moving, if the alien was previously hidden.
            if (human.hidden) {
                if (human.rollDie() + human.rollDie() > human.getAgility()) {
                    human.hidden = false;
                    System.out.println(human.getName() + " has given away his position.");
                }
            }

            //If the alien is chasing, this portion of the method determines how far the alien can move and moves the alien character.
            if (chase) {
                System.out.println(alien.getName() + " is giving chase.");
                alienMovement = alien.getAgility() + alien.rollDie() + alien.rollDie();
                xDist = alienLocation[0] - humanLocation[0];
                xSpace = getLength() - alienLocation[0];
                yDist = alienLocation[1] - humanLocation[1];
                ySpace = getWidth() - alienLocation[1];

                //Ensures the alien cannot move beyond the confines of the arena and does not unnecessarily move within range of enemy fire.
                if (alienMovement > Math.max(Math.max((int) (alienLocation[0] + ySpace), (int) (alienLocation[0] + alienLocation[1])), Math.max((int) (xSpace + alienLocation[1]), (int) (xSpace + ySpace)))) {
                    alienMovement = Math.max(Math.max((int) (alienLocation[0] + ySpace), (int) (alienLocation[0] + alienLocation[1])), Math.max((int) (xSpace + alienLocation[1]), (int) (xSpace + ySpace)));
                }
                if (defender.getIntelligence() >= 5 && defender.getRange() > hasInitiative.getRange()) {
                    if (Math.abs(xDist) < alienMovement) {
                        alienMovement = (int) Math.min(Math.sqrt(Math.pow(xDist, 2) + Math.pow(alienMovement - Math.abs(xDist), 2)), findDistance() - hasInitiative.getRange());
                    } else if (Math.abs(yDist) < alienMovement) {
                        alienMovement = (int) Math.min(Math.sqrt(Math.pow(yDist, 2) + Math.pow(alienMovement - Math.abs(yDist), 2)), findDistance() - hasInitiative.getRange());
                    } else if (Math.abs(xDist) > Math.abs(yDist)) {
                        alienMovement = (int) Math.min(alienMovement, Math.sqrt(Math.pow(findDistance() - hasInitiative.getRange(), 2) - Math.pow(yDist, 2)));
                    } else {
                        alienMovement = (int) Math.min(alienMovement, Math.sqrt(Math.pow(findDistance() - hasInitiative.getRange(), 2) - Math.pow(xDist, 2)));
                    }
                }

                if (Math.abs(xDist) > Math.abs(yDist)) {

                    //Assigns a change to the alien's x coordinate.
                    xMove = (int) Math.min(alienMovement, Math.abs(xDist));
                    if (alienLocation[0] <= humanLocation[0]) {
                        alienLocation[0] += xMove;
                    } else {
                        alienLocation[0] -= xMove;
                    }

                    //Reduces the alien's remaining movement by the change in the x coordinate and assigns the change to the y coordinate.
                    alienMovement -= xMove;
                    if (alienMovement > 0) {
                        if (alienLocation[1] <= humanLocation[1]) {
                            alienLocation[1] += alienMovement;
                        } else {
                            alienLocation[1] -= alienMovement;
                        }
                    }
                } else {

                    //Assigns a change to the alien's y coordinate.
                    yMove = (int) Math.min(alienMovement, Math.abs(yDist));
                    if (alienLocation[1] <= humanLocation[1]) {
                        alienLocation[1] += yMove;
                    } else {
                        alienLocation[1] -= yMove;
                    }

                    //reduces the alien's remaining movement by the change in the y coordinate and assigns the change to the x coordinate.
                    alienMovement -= yMove;
                    if (alienMovement > 0) {
                        if (alienLocation[0] <= humanLocation[0]) {
                            alienLocation[0] += alienMovement;
                        } else {
                            alienLocation[1] += alienMovement;
                        }
                    }
                }

                //determines whether the alien has given away his position by moving, if the human was previously hidden.
                if (alien.hidden && (alien.power == null || !(alien.power.toString().equals("Invisibility") && alien.power.active))) {
                    if (alien.rollDie() + alien.rollDie() > alien.getAgility()) {
                        alien.hidden = false;
                        System.out.println(alien.getName() + " has given away his position.");
                    }
                }
            }
        }

        //Determines which character takes the next turn.
        if (!chase) {
            if (this.hasInitiative == alien) {
                this.hasInitiative = human;
                defender = alien;
            } else {
                this.hasInitiative = alien;
                defender = human;
            }
        }

        //Prints and returns the results of the characters' efforts to widen the distance and/or chase.
        int changeInDistance;
        distance = findDistance();
        System.out.println("Human location: " + Arrays.toString(humanLocation) + "\nAlien location: " + Arrays.toString(alienLocation) + "\nEnding Distance: " + distance);
        if (distance > startingDistance) {
            changeInDistance = (int) (distance - startingDistance);
            System.out.println("\tThe distance has increased by " + changeInDistance);
            return "Success";
        } else {
            changeInDistance = (int) (startingDistance - distance);
            System.out.println("\tThe distance has decreased by " + changeInDistance);
            return "Failure";
        }
    }

    public String resolveRangedAttack(Being hasInitiative) {
        boolean dodge = false;
        int defenseRoll = defender.rollDie();
        String attackerName;
        String defenderName;
        if (hasInitiative == alien) {
            attackerName = alien.getName();
            defenderName = human.getName();
            switch (alien.getEquipment()) {
                case "ancient" -> System.out.println(alien.getName() + " launches a volley with his bow and arrow.");
                case "modern defensive" ->
                        System.out.println(alien.getName() + " shoots at " + human.getName() + " with his pistol.");
                case "modern offensive", "modern balanced" -> System.out.println(alien.getName() + " opens fire with his rifle.");
                case "futuristic defensive" -> System.out.println(alien.getName() + " shoots at " + human.getName() + " with his laser pistol.");
                case "futuristic offensive", "futuristic balanced" -> System.out.println(alien.getName() + " opens fire with his laser rifle.");
                default -> System.out.println(alien.getName() + " opens fire.");
            }
            alienActions -= 1;
            if (humanActions > 0 && human.getIntelligence() >= 5 && !hasInitiative.hidden) {
                dodge = true;
                System.out.println(human.getName() + " attempts to dodge.");
                humanActions -= 1;
            }
        } else {
            attackerName = human.getName();
            defenderName = alien.getName();
            switch (human.getEquipment()) {
                case "ancient" -> System.out.println(human.getName() + " launches a volley with his bow and arrow.");
                case "modern defensive" ->
                        System.out.println(human.getName() + " shoots at " + alien.getName() + " with his pistol.");
                case "modern offensive", "modern balanced" -> System.out.println(human.getName() + " opens fire with his rifle.");
                case "futuristic defensive" -> System.out.println(human.getName() + " shoots at " + alien.getName() + " with his laser pistol.");
                case "futuristic offensive", "futuristic balanced" -> System.out.println(human.getName() + " opens fire with his laser rifle.");
                default -> System.out.println(human.getName() + " opens fire.");
            }
            humanActions -= 1;
            if (alienActions > 0 && alien.getIntelligence() >= 5 && !hasInitiative.hidden) {
                dodge = true;
                System.out.println(alien.getName() + " attempts to dodge.");
                alienActions -= 1;
            }
        }
        int attackRoll = hasInitiative.rangedAttack();
        if (hasInitiative != alien || alien.power == null || !(alien.power.toString().equals("Invisibility") && alien.power.active)) {
            hasInitiative.hidden = false;
        }
        if (dodge) {
            defenseRoll = defender.defend();
        }
        if (attackRoll < defenseRoll) {
            System.out.println("\t" + attackerName + " misses his target!");
            if (!dodge) {
                if (hasInitiative == alien) {
                    this.hasInitiative = human;
                    defender = alien;
                } else {
                    this.hasInitiative = alien;
                    defender = human;
                }
            }
            return "Miss";
        } else {
            if (attackRoll <= defender.getCoverage()) {
                System.out.println("\t" + attackerName + " hits " + defenderName + "'s armor!");
                int damageDealt = (attackRoll - defenseRoll + hasInitiative.getDamageModifier()) / hasInitiative.getDamageRating();
                if (damageDealt - defender.getDurability() >= defender.getStructurePoints()) {
                    int remainingDamageDealt = (damageDealt - defender.getDurability() - defender.getStructurePoints());
                    int damageTaken = Math.max((remainingDamageDealt - defender.getBrawn() / 5), 0);
                    defender.vitality -= damageTaken;
                    defender.agility_penalty = 0;
                    defender.coverage = 0;
                    defender.durability = 0;
                    defender.structurePoints = 0;
                    System.out.println("\t" + defenderName + "'s armor is destroyed, and " + defenderName + " takes " + damageTaken + " damage.");
                } else {
                    int damageTaken = Math.max(damageDealt - defender.getDurability(), 0);
                    defender.structurePoints -= damageTaken;
                    System.out.println("\t" + defenderName + "'s armor takes " + damageTaken + " damage.");
                }
            } else {
                System.out.println("\t" + attackerName + " hits " + defenderName + " in an unarmored spot!");
                int damageTaken = Math.max((attackRoll - defenseRoll + hasInitiative.getDamageModifier()) / hasInitiative.getDamageRating() - (defender.getBrawn() / 5), 0);
                defender.vitality -= damageTaken;
                System.out.println("\t" + defenderName + " takes " + damageTaken + " damage.");
            }
            if (defender.getVitality() < 0) {
                defender.vitality = 0;
            }
            if (!dodge) {
                if (hasInitiative == alien) {
                    this.hasInitiative = human;
                    defender = alien;
                } else {
                    this.hasInitiative = alien;
                    defender = human;
                }
            }
            return "Hit";
        }
    }

    public String resolveMeleeAttack(Being hasInitiative) {
        boolean defend = false;
        int defenseRoll = defender.rollDie();
        String attackerName;
        String defenderName;
        if (hasInitiative == alien) {
            attackerName = alien.getName();
            defenderName = human.getName();
            switch (alien.getEquipment()) {
                case "ancient" -> System.out.println(alien.getName() + " slashes at " + defenderName + " with his sword.");
                case "modern defensive" ->
                        System.out.println(alien.getName() + " stabs at " + human.getName() + " with his knife.");
                case "modern offensive", "modern balanced" -> System.out.println(alien.getName() + " swings at " + defenderName + " with his side-handled baton.");
                case "futuristic defensive" -> System.out.println(alien.getName() + " stabs at " + human.getName() + " with his laser knife.");
                case "futuristic offensive", "futuristic balanced" -> System.out.println(alien.getName() + " slashes at " + defenderName + " with his laser sword.");
                default -> System.out.println(alien.getName() + " stabs at " + defenderName + ".");
            }
            alienActions -= 1;
            if (humanActions > 0 && human.getIntelligence() >= 5 && !hasInitiative.hidden) {
                defend = true;
                System.out.println(human.getName() + " attempts to defend.");
                humanActions -= 1;
            }
        } else {
            attackerName = human.getName();
            defenderName = alien.getName();
            switch (human.getEquipment()) {
                case "ancient" -> System.out.println(human.getName() + " slashes at " + defenderName + " with his sword.");
                case "modern defensive" ->
                        System.out.println(human.getName() + " stabs at " + alien.getName() + " with his knife.");
                case "modern offensive", "modern balanced" -> System.out.println(human.getName() + " swings at " + defenderName + " with his side-handled baton.");
                case "futuristic defensive" -> System.out.println(human.getName() + " stabs at " + alien.getName() + " with his laser knife.");
                case "futuristic offensive", "futuristic balanced" -> System.out.println(human.getName() + " slashes at " + defenderName + " with his laser sword.");
                default -> System.out.println(human.getName() + " stabs at " + defenderName + ".");
            }
            humanActions -= 1;
            if (alienActions > 0 && alien.getIntelligence() >= 5 && !hasInitiative.hidden) {
                defend = true;
                System.out.println(alien.getName() + " attempts to defend.");
                alienActions -= 1;
            }
        }
        int attackRoll = hasInitiative.meleeAttack();
        if (hasInitiative != alien || !(alien.power.toString().equals("Invisibility") && alien.power.active)) {
            hasInitiative.hidden = false;
        }
        if (defend) {
            defenseRoll = defender.defend();
        }
        if (attackRoll < defenseRoll) {
            System.out.println("\t" + attackerName + " misses!");
            if (!defend) {
                if (hasInitiative == alien) {
                    this.hasInitiative = human;
                    defender = alien;
                } else {
                    this.hasInitiative = alien;
                    defender = human;
                }
            }
            return "Miss";
        } else {
            if (attackRoll <= defender.getCoverage()) {
                System.out.println("\t" + attackerName + " hits " + defenderName + "'s armor!");
                int damageDealt = (attackRoll - defenseRoll + hasInitiative.getDamageModifier()) / hasInitiative.getDamageRating();
                if (damageDealt - defender.getDurability() >= defender.getStructurePoints()) {
                    int remainingDamageDealt = (damageDealt - defender.getDurability() - defender.getStructurePoints());
                    int damageTaken = Math.max((remainingDamageDealt - defender.getBrawn() / 5), 0);
                    defender.vitality -= damageTaken;
                    defender.agility_penalty = 0;
                    defender.coverage = 0;
                    defender.durability = 0;
                    defender.structurePoints = 0;
                    System.out.println("\t" + defenderName + "'s armor is destroyed, and " + defenderName + " takes " + damageTaken + " damage.");
                } else {
                    int damageTaken = Math.max(damageDealt - defender.getDurability(), 0);
                    defender.structurePoints -= damageTaken;
                    System.out.println("\t" + defenderName + "'s armor takes " + damageTaken + " damage.");
                }
            } else {
                System.out.println("\t" + attackerName + " hits " + defenderName + " in an unarmored spot!");
                int damageTaken = Math.max((attackRoll - defenseRoll + hasInitiative.getDamageModifier()) / hasInitiative.getDamageRating() - (defender.getBrawn() / 5), 0);
                defender.vitality -= damageTaken;
                System.out.println("\t" + defenderName + " takes " + damageTaken + " damage.");
            }
            if (defender.getVitality() < 0) {
                defender.vitality = 0;
            }
            if (!defend) {
                if (hasInitiative == alien) {
                    this.hasInitiative = human;
                    defender = alien;
                } else {
                    this.hasInitiative = alien;
                    defender = human;
                }
            }
            return "Hit";
        }
    }

    public String resolveUnarmedAttack(Being hasInitiative) {
        boolean defend = false;
        int defenseRoll = defender.rollDie();
        String attackerName;
        String defenderName;
        if (hasInitiative == alien) {
            attackerName = alien.getName();
            defenderName = human.getName();
            System.out.println(attackerName + " unleashes a barrage of punches and kicks at " + defenderName + ".");
            alienActions -= 1;
            if (humanActions > 0 && human.getIntelligence() >= 5 && !hasInitiative.hidden) {
                defend = true;
                System.out.println(human.getName() + " attempts to defend.");
                humanActions -= 1;
            }
        } else {
            attackerName = human.getName();
            defenderName = alien.getName();
            System.out.println(attackerName + " unleashes a barrage of punches and kicks at " + defenderName + ".");
            humanActions -= 1;
            if (alienActions > 0 && alien.getIntelligence() >= 5 && !hasInitiative.hidden) {
                defend = true;
                System.out.println(alien.getName() + " attempts to defend.");
                alienActions -= 1;
            }
        }
        int attackRoll = hasInitiative.unarmedAttack();
        if (hasInitiative != alien || !(alien.power.toString().equals("Invisibility") && alien.power.active)) {
            hasInitiative.hidden = false;
        }
        if (defend) {
            defenseRoll = defender.defend();
        }
        if (attackRoll < defenseRoll) {
            System.out.println("\t" + attackerName + " fails to land a blow!");
            if (!defend) {
                if (hasInitiative == alien) {
                    this.hasInitiative = human;
                    defender = alien;
                } else {
                    this.hasInitiative = alien;
                    defender = human;
                }
            }
            return "Failure";
        } else {
            if (attackRoll <= defender.getCoverage()) {
                System.out.println("\t" + attackerName + " hits " + defenderName + "'s armor!");
                int damageDealt = (attackRoll - defenseRoll + hasInitiative.getBrawn() / 5) / 8;
                if (damageDealt - defender.getDurability() >= defender.getStructurePoints()) {
                    int remainingDamageDealt = (damageDealt - defender.getDurability() - defender.getStructurePoints());
                    int damageTaken = Math.max((remainingDamageDealt - defender.getBrawn() / 5), 0);
                    defender.vitality -= damageTaken;
                    defender.agility_penalty = 0;
                    defender.coverage = 0;
                    defender.durability = 0;
                    defender.structurePoints = 0;
                    System.out.println("\t" + defenderName + "'s armor is destroyed, and " + defenderName + " takes " + damageTaken + " damage.");
                } else {
                    int damageTaken = Math.max(damageDealt - defender.getDurability(), 0);
                    defender.structurePoints -= damageTaken;
                    System.out.println("\t" + defenderName + "'s armor takes " + damageTaken + " damage.");
                }
            } else {
                System.out.println("\t" + attackerName + " hits " + defenderName + " in an unarmored spot!");
                int damageTaken = Math.max((attackRoll - defenseRoll + hasInitiative.getBrawn() / 5) / 8 - (defender.getBrawn() / 5), 0);
                defender.vitality -= damageTaken;
                System.out.println("\t" + defenderName + " takes " + damageTaken + " damage.");
            }
            if (defender.getVitality() < 0) {
                defender.vitality = 0;
            }
            if (!defend) {
                if (hasInitiative == alien) {
                    this.hasInitiative = human;
                    defender = alien;
                } else {
                    this.hasInitiative = alien;
                    defender = human;
                }
            }
            return "Success";
        }
    }

    public void endRound() {
        if (alien.power != null && alien.power.active) {
            alien.power.roundsRemaining -= 1;
            if (alien.power.roundsRemaining <= 0) {
                alien.power.deactivate();
            }
        }
        System.out.println(human.getName() + " has " + human.getVitality() + " vitality remaining.");
        System.out.println(alien.getName() + " has " + alien.getVitality() + " vitality remaining.");
        if ((findDistance() <= alien.getRange() || findDistance() <= human.getRange()) && alien.getVitality() == alienStartingVitality && human.getVitality() == humanStartingVitality && alien.getStructurePoints() == alienStartingSP && human.getStructurePoints() == humanStartingSP) {
            stalemateRounds += 1;
        } else {
            stalemateRounds = 0;
        }
        if (stalemateRounds >= 5) {
            System.out.println("\nThis battle has become a stalemate.");
            endBattle();
        }
    }

    public void resolveActivatePower() {
        System.out.println(alien.getName() + " activates " + alien.power.toString() + ".");
        alien.power.activate(alien.rollDie());
        alienActions -= 1;
        if (humanActions > 0) {
            hasInitiative = human;
            defender = alien;
        } else {
            hasInitiative = alien;
            defender = human;
        }
    }

    public void endBattle() {
        String winnerName;
        if (human.getVitality() == 0) {
            winner = alien;
            loser = human;
        } else if (alien.getVitality() == 0) {
            winner = human;
            loser = alien;
        } else if (stalemateRounds < 5) {
            System.out.println("Wait! Something went wrong. This battle isn't over yet!");
            return;
        }
        if (winner == alien) {
            assert alien != null;
            winnerName = alien.getName();
        } else {
            winnerName = human.getName();
        }
        if (stalemateRounds < 5) {
            System.out.println(winnerName + " has won the battle!\n\nA strange game. The only winning move is not to play. How about a nice game of chess?");
        }
    }
    
    public static void main(String[] args) {
        System.out.println("ALIENS VERSUS HUMANS\n");
        Arena arena = new Arena("Omega", "Starbase", 1200, 1200, 50);
        System.out.println(arena);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        arena.spawnHuman();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        arena.spawnAlien();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Human location: " + Arrays.toString(arena.humanLocation) + "\nAlien location: " + Arrays.toString(arena.alienLocation));
        System.out.println("\nThe battle has begun...");
        while (arena.human.getVitality() > 0 && arena.alien.getVitality() > 0 && arena.stalemateRounds < 5) {
            arena.startRound();
            while (arena.humanActions > 0 || arena.alienActions > 0) {
                String action = arena.chooseAction(arena.hasInitiative);
                switch (action) {
                    case "activate power": {
                        arena.resolveActivatePower();
                        break;
                    }
                    case "hide": {
                        arena.resolveHide(arena.hasInitiative);
                        break;
                    }
                    case "close distance": {
                        arena.closeDistance(arena.hasInitiative);
                        break;
                    }
                    case "open distance": {
                        arena.resolveOpenDistance(arena.hasInitiative);
                        break;
                    }
                    case "ranged attack": {
                        arena.resolveRangedAttack(arena.hasInitiative);
                        break;
                    }
                    case "melee attack": {
                        arena.resolveMeleeAttack(arena.hasInitiative);
                        break;
                    }
                    case "unarmed attack": {
                        arena.resolveUnarmedAttack(arena.hasInitiative);
                        break;
                    }
                    default: {
                        System.out.println("Oops! Something went wrong. The active player failed to choose an action.");
                    }
                }
                if (arena.human.getVitality() == 0 || arena.alien.getVitality() == 0) {
                    arena.endBattle();
                    break;
                }
                if (arena.hasInitiative == arena.human && arena.humanActions == 0) {
                    arena.hasInitiative = arena.alien;
                    arena.defender = arena.human;
                }
            }
            if (arena.human.getVitality() == 0 || arena.alien.getVitality() == 0) {
                break;
            }
            arena.endRound();
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}