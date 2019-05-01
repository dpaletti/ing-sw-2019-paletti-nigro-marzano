package it.polimi.se2019.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.*;

public class LockRifle extends Weapon {
      private HashSet<Effect> basicEffect;
      private HashSet<Effect> withSecondLock;

      public LockRifle(){
          cardColour= new Ammo(AmmoColour.BLUE);
          price=new HashSet<>();
          price.add(new Ammo(AmmoColour.BLUE));
          price.add(new Ammo(AmmoColour.BLUE));
          basicEffect=new HashSet<>();
          Effect tempBasic= new Effect();
          basicEffect.add(tempBasic);

          TargetSpecification tempTargetBasic= new TargetSpecification();
          tempTargetBasic.setVisible(1);
          tempBasic.setTargetSpecification(tempTargetBasic);

          tempBasic.setPriority(3);

          tempBasic.setName("B");

          ArrayList<Action> tempList=new ArrayList<>();
          Action tempDamage= new Action();
          tempDamage.setValue(2);
          tempDamage.setActionType(ActionType.DAMAGE);
          tempList.add(tempDamage);
          Action tempMark= new Action();
          tempMark.setValue(1);
          tempMark.setActionType(ActionType.MARK);
          tempList.add(tempMark);
          tempBasic.setActions(tempList);

          withSecondLock= new HashSet();
          Effect tempSecond= new Effect();
          withSecondLock.add(tempSecond);
          TargetSpecification tempTargetSecond= new TargetSpecification();
          Pair tempPair= new Pair<>();
          tempPair.setFirst(1);
          tempPair.setSecond(basicEffect);
          tempTargetSecond.setDifferent(tempPair);
          tempSecond.setTargetSpecification(tempTargetSecond);

          tempSecond.setName("E");

          tempSecond.setPriority(2);

          ArrayList<Action> tempList2=new ArrayList<>();
          tempList2.add(tempMark);
          tempSecond.setActions(tempList2);

          ArrayList<Ammo> tempAmmo= new ArrayList<>();
          tempAmmo.add(new Ammo(AmmoColour.RED));
          tempSecond.setPrice(tempAmmo);


      }

      public static void main(String[] args){
        LockRifle instance= new LockRifle();
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        System.out.println(gson.toJson(instance));


      }
   }
