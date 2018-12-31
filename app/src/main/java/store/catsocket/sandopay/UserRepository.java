package store.catsocket.sandopay;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;

/* A Repository serves as an additional abstraction layer. With Repository,
   it is possible to mediate in between different DataSources therefore, it is
   considered as best practice. */

public class UserRepository {
    private UserDao userDao;
    private LiveData<List<User>> allUsers;
    private LiveData<List<User>> user;

    // DAO Interface & DataBase

    public UserRepository(Application application){
        BankAccountDatabase database = BankAccountDatabase.getInstance(application);
        userDao = database.userDao();
        allUsers = userDao.getAllUsers();
        user = userDao.getUser();
    }

    // Methods for inserting, updating and deleting data to/from Database through DAO using AsyncTasks

    public void insertUser(User user){
        new InsertUserAsyncTask(userDao).execute(user);
    }
    public void updateUser(User user){
        new UpdateUserAsyncTask(userDao).execute(user);
    }
    public void deleteUser(User user){
        new DeleteUserAsyncTask(userDao).execute(user);
    }

    public void deleteAllUsers(){
        new DeleteAllUsersAsyncTask(userDao).execute();
    }

    public LiveData<List<User>> getAllUsers(){
        return allUsers;
    }

    public LiveData<List<User>> getUser(){
        return user;
    }

    //  AsyncTasks to perform background operations and publish results on the UI thread without having to manipulate threads and/or handlers

    private static class InsertUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        private InsertUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.insertUser(users[0]);
            return null;
        }
    }

    private static class UpdateUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        private UpdateUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.updateUser(users[0]);
            return null;
        }
    }

    private static class DeleteUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        private DeleteUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.deleteUser(users[0]);
            return null;
        }
    }

    private static class DeleteAllUsersAsyncTask extends AsyncTask<Void, Void, Void> {
        private UserDao userDao;

        private DeleteAllUsersAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            userDao.deleteAllUsers();
            return null;
        }
    }

}



