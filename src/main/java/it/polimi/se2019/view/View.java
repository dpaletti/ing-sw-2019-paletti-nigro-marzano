package it.polimi.se2019.view;
import it.polimi.se2019.network.NetworkHandler;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.utility.Observable;
import it.polimi.se2019.utility.Observer;

import java.util.ArrayList;
import java.util.Scanner;

public class View extends Observable<VCEvent> implements Observer<MVEvent>{
    NetworkHandler networkHandler;

    protected View(){
        observers = new ArrayList<>();
    }

    @Override
    public void update(MVEvent message) {
        message.handle(this);
    }

    public void update(WrongUsernameEvent message){
        Scanner in = new Scanner(System.in);
        Log.info("Username and password are wrong, please change them");
        Log.input("Input new username (old username: " + message.getConnection().getUsername() + ")");
        String user = in.nextLine();
        while(user.equals(message.getConnection().getUsername())) {
            Log.input("Change your username please");
            user = in.nextLine();
        }
        Log.input("Input new password");
        String password = in.nextLine();
        UsernameResetEven event = new UsernameResetEven();
        event.setUsername(user);
        event.setPassword(password);
        notify(event);
    }

}
