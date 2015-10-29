package Tests;

import backend.building.Castle;
import backend.terrain.TerrainFactory;
import backend.units.Unit;
import backend.units.UnitType;
import backend.worldBuilding.Location;
import backend.worldBuilding.Player;
import backend.terrain.Terrain;
import backend.worldBuilding.World;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BuildingTest {
    Player p1;
    Player p2;
    World world;

    @Before
    public void initialisation() {
        p1 = new Player("Pablo");
        p1 = new Player("Sergio");
        world = new World(50, 50, p1, p2);
    }

    @Test
    public void buildUnitTest(){
        Castle castle = new Castle(p1);
        world.addBuilding(castle, new Location(0,0));

        Unit unit = castle.buildUnit(UnitType.LANCER, TerrainFactory.buildHillTerrain(), new Location(1,1), p1);

        assertTrue(unit.getUnitType().equals(UnitType.LANCER));
        assertTrue(unit.getLocation().equals(new Location(1,1)));
        assertTrue(unit.getOwner().equals(p1));
    }
}
