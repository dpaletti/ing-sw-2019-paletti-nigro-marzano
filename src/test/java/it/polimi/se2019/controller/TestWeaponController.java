package it.polimi.se2019.controller;
import it.polimi.se2019.model.Weapon;

import java.nio.file.Paths;

import static org.junit.Assert.*;
public class TestWeaponController {
    private Weapon vortexCannon= new Weapon(Paths.get("files/weapons/VortexCannon.json").toString());

}
