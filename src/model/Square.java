package model;
public class Square {

    private boolean isFixed;
    private int playerNumber;
    private int realNumber;

    public Square(boolean isFixed, int number) {
        this.isFixed = isFixed;
        this.playerNumber = number;
        this.realNumber = number;
    }
    
    public boolean isFixed() {
        return isFixed;
    }

    public void setFixed(boolean isFixed) {
        this.isFixed = isFixed;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public int getRealNumber() {
        return realNumber;
    }

    public void setRealNumber(int realNumber) {
        this.realNumber = realNumber;
    }

    public boolean isCorrect() {
        return playerNumber == realNumber;
    }

    @Override
    public String toString() {
        return Integer.toString(realNumber);
    }

}
