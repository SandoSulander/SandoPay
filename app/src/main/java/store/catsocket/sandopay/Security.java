package store.catsocket.sandopay;

/* This is class is meant for Security purposes but is still on progress.
   Instead of Singleton, it is planned to add as an Entity to the Database
   and add more functionality to it. */

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
