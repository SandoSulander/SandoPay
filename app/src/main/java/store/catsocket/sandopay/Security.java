package store.catsocket.sandopay;

/* This is class is mean for Security purposes but is still on progress. */

public class Security {

    private int counter;

    private static Security security = new Security();

    public Security(){
        counter = 5;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter =- counter;
    }

    public void resetCounter(){
        this.counter = 5;
    }

    public static Security getInstance() {

        return security;
    }
}
