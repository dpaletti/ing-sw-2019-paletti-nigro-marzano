package it.polimi.se2019.model;

import it.polimi.se2019.utility.Factory;
import org.junit.Test;

import java.nio.file.Paths;

public class TestCard {
    private Weapon cyberblade= Factory.createWeapon(Paths.get("files/weapons/Cyberblade.json").toString());
    private Weapon electroschyte= Factory.createWeapon(Paths.get("files/weapons/Electroschyte.json").toString());
    private Weapon flamethrower= Factory.createWeapon(Paths.get("files/weapons/Flamethrower.json").toString());
    private Weapon furnace= Factory.createWeapon(Paths.get("files/weapons/Furnace.json").toString());
    private Weapon grenadeLauncher= Factory.createWeapon(Paths.get("files/weapons/GrenadeLauncher.json").toString());
    private Weapon heatseeker= Factory.createWeapon(Paths.get("files/weapons/Heatseeker.json").toString());
    private Weapon hellion= Factory.createWeapon(Paths.get("files/weapons/Hellion.json").toString());
    private Weapon lockRifle= Factory.createWeapon(Paths.get("files/weapons/LockRifle.json").toString());
    private Weapon plasmaGun= Factory.createWeapon(Paths.get("files/weapons/PlasmaGun.json").toString());
    private Weapon railgun= Factory.createWeapon(Paths.get("files/weapons/Railgun.json").toString());
    //TODO Check todo in rocketlauncher
    private Weapon rocketLauncher= Factory.createWeapon(Paths.get("files/weapons/RocketLauncher.json").toString());
    private Weapon shockwave= Factory.createWeapon(Paths.get("files/weapons/Shockwave.json").toString());
    private Weapon shotgun= Factory.createWeapon(Paths.get("files/weapons/Shotgun.json").toString());
    private Weapon sledgehammer= Factory.createWeapon(Paths.get("files/weapons/Sledgehammer.json").toString());
    private Weapon thor= Factory.createWeapon(Paths.get("files/weapons/Thor.json").toString());
    private Weapon tractorBeam= Factory.createWeapon(Paths.get("files/weapons/TractorBeam.json").toString());
    private Weapon whisper= Factory.createWeapon(Paths.get("files/weapons/Whisper.json").toString());
    private Weapon zx2= Factory.createWeapon(Paths.get("files/weapons/Zx2.json").toString());
    private Weapon machineGun= Factory.createWeapon(Paths.get("files/weapons/machineGun.json").toString());



    /*@Test
    public void testGetStaticDefinition(){
        Set<GraphNode<PartialWeaponEffect>> children=cyberblade.getStaticDefinition().getChildren();
        Set<String> stringSet= new HashSet<>();
        stringSet.add("B1");
        Set<String> effectNames= new HashSet<>();
        for(GraphNode<PartialWeaponEffect> node: children){
            assertEquals(1, node.getNode().size());
            effectNames.add(node.getNode().iterator().next().getName());
        }
        assertEquals(stringSet,effectNames);
    }*/

    @Test
    public void testPrintGraph(){
        for (GraphNode<GraphWeaponEffect> graphWeaponEffectGraphNode: rocketLauncher.getStaticDefinition()){
           System.out.println(graphWeaponEffectGraphNode);
            for (GraphNode<PartialWeaponEffect> p: graphWeaponEffectGraphNode.getKey().getEffectGraph()){
                System.out.print(p+"|");
            }
            System.out.println();
        }


    }


}
