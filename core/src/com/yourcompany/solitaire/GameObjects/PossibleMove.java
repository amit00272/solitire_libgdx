package com.yourcompany.solitaire.GameObjects;

public class PossibleMove {
    private byte stackFrom;
    private byte stackTo;
    private byte startAtCard;

    public PossibleMove(byte stackFrom, byte stackTo, byte startAtCard) {
        this.stackFrom = stackFrom;
        this.stackTo = stackTo;
        this.startAtCard = startAtCard;
    }

    public byte getStackFrom() { return stackFrom; }

    public byte getStackTo() { return stackTo; }

    public byte getStartAtCard() { return startAtCard; }
}