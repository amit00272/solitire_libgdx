package com.yourcompany.solitaire.GameObjects;

public class Move {
    public byte[] cardIDS;
    public byte[] stack;
    public byte[] revealed;

    public byte counter;

    public Move() {
        cardIDS = new byte[52];
        stack = new byte[52];
        revealed = new byte[52];

        counter = 0;
    }

    public void add(byte cardID, byte cardStack, boolean revealed) {
        this.cardIDS[counter] = cardID;
        this.stack[counter] = cardStack;

        if(revealed == true) { this.revealed[counter] = 1; }
        else { this.revealed[counter] = 0; }

        counter++;
    }

    public boolean isRevealed(int i) {
        if(revealed[i] == 1) { return true; }

        return false;
    }
}