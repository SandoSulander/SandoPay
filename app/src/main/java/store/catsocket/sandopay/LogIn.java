package store.catsocket.sandopay;

/* This Class is Purely used for tracking the user id after login.
   Id is needed for displaying and manipulating correct data.
   Therefore, the Class is created as Singleton for easy access. */

public class LogIn {

    private int loginId;

    private static LogIn login = new LogIn();

    public LogIn(){
    }

    public static LogIn getInstance() {

        return login;
    }

    public int getLoginId() {
        return loginId;
    }

    public void setLoginId(int loginId) {
        this.loginId = loginId;
    }
}