package cliq.com.cliqgram.helper;

import cliq.com.cliqgram.model.User;

/**
 * Created by ilkan on 14/10/2015.
 */
public class FollowerCounter implements Comparable<FollowerCounter>{



    User user;
    int counter;

    public FollowerCounter(User user, int counter){
        this.user = user;
        this.counter = counter;
    }

    @Override
    public int compareTo(FollowerCounter otherCounter) {
        return this.counter - otherCounter.counter;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
