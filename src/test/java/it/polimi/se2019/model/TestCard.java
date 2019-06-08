package it.polimi.se2019.model;

import org.junit.Test;

import java.nio.file.Paths;

public class TestCard {
  /*
    private Weapon zx2= new Weapon(Paths.get("files/weapons/Zx2.json").toString());

    private Weapon cyberblade= new Weapon(Paths.get("files/weapons/Cyberblade.json").toString());
    private Weapon flamethrower= new Weapon(Paths.get("files/weapons/Flamethrower.json").toString());
    private Weapon furnace= new Weapon(Paths.get("files/weapons/Furnace.json").toString());
    private Weapon grenadeLauncher= new Weapon(Paths.get("files/weapons/GrenadeLauncher.json").toString());
    private Weapon heatseeker= new Weapon(Paths.get("files/weapons/Heatseeker.json").toString());
    private Weapon hellion= new Weapon(Paths.get("files/weapons/Hellion.json").toString());
    private Weapon railgun= new Weapon(Paths.get("files/weapons/Railgun.json").toString());
    private Weapon rocketLauncher= new Weapon(Paths.get("files/weapons/RocketLauncher.json").toString());
    private Weapon shockwave= new Weapon(Paths.get("files/weapons/Shockwave.json").toString());
    private Weapon shotgun= new Weapon(Paths.get("files/weapons/Shotgun.json").toString());
    private Weapon sledgehammer= new Weapon(Paths.get("files/weapons/Sledgehammer.json").toString());
    private Weapon lockRifle= new Weapon(Paths.get("files/weapons/LockRifle.json").toString());
    private Weapon thor= new Weapon(Paths.get("files/weapons/Thor.json").toString());
    private Weapon plasmaGun= new Weapon(Paths.get("files/weapons/PlasmaGun.json").toString());
    private Weapon whisper= new Weapon(Paths.get("files/weapons/Whisper.json").toString());
    private Weapon electroschyte= new Weapon(Paths.get("files/weapons/Electroschyte.json").toString());
    private Weapon tractorBeam= new Weapon(Paths.get("files/weapons/TractorBeam.json").toString());
    private Weapon vortexCannon= new Weapon(Paths.get("files/weapons/VortexCannon.json").toString());
 */
    private Weapon machineGun= new Weapon(Paths.get("files/weapons/MachineGun.json").toString());

    @Test
    public void testPrintGraph(){
        System.out.print(machineGun.getDefinition());
        for (GraphNode<GraphWeaponEffect> g: machineGun.getDefinition()){
            System.out.print(g.getKey().name+"\t These are my effects:"+System.lineSeparator());
            System.out.print(g.getKey().getEffectGraph());
        }
    }


}
