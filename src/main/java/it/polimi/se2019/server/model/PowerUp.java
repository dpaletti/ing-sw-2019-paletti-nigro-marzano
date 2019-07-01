package it.polimi.se2019.server.model;


public class PowerUp extends Card implements Drawable,Jsonable{
    public PowerUp(String path){
        super(path);
    }

    public PowerUp(PowerUp powerUp){
        super(powerUp);
    }

    public String getColour(){
        String colour="";
        if (name.contains("Red"))
            colour="RED";
        else if (name.contains("Blue"))
            colour="BLUE";
        else if(name.contains("Yellow"))
            colour="YELLOW";
        return colour;
    }
}
