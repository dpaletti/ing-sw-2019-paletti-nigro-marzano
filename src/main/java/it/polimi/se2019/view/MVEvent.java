package it.polimi.se2019.view;

import it.polimi.se2019.network.Connection;

public abstract class MVEvent extends Event {

    private Connection connection = null;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        if(this.connection == (null))
            this.connection = connection;
    }

    public void handle(View view){
        view.update(this);
    }


}
