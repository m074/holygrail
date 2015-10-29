package backend.worldBuilding;


import backend.building.Building;
import backend.exceptions.CellIsEmptyException;
import backend.exceptions.CellIsOccupiedException;
import backend.items.Item;
import backend.items.ItemFactory;
import backend.items.ItemType;
import backend.terrain.Terrain;
import backend.units.Unit;
import frontend.CellUIData;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Cell {
    Location location;
    Unit localUnit;
    Queue<Item> treasures;
    private final static int MAX_ITEMS = 5;
    Terrain terrain;

    /**
     * Constructs a new Cell, on a Location, with certain Terrain and random Items.
     *
     * @param location Location of the Cell in the World.
     * @param terrain Terrain of the Cell.
     */
    public Cell(Location location, Terrain terrain) {
        this.terrain = terrain;
        this.location = location;
        this.treasures = new LinkedList<>();

        Random random = new Random();
        int numberOfItems = random.nextInt(MAX_ITEMS);

        for ( int i = 0 ; i <= numberOfItems ; i++ ){
            treasures.add(ItemFactory.buildRandomItem());
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return ((Cell) o).getLocation().equals(this.getLocation());
    }

    @Override
    public int hashCode() {
        return location.hashCode();
    }

    /**
     * Returns the Terrain of the Cell.
     *
     * @return Terrain of the Cell.
     */
    public Terrain getTerrain() {
        return terrain;
    }

    /**
     * Returns the Unit in the Cell.
     *
     * @return Unit in Cell.
     */
    public Unit getUnit() {
        return localUnit;
    }

    /**
     * Returns the Location of the Cell.
     *
     * @return location of the cell.
     */
    public Location getLocation() {
        return location;
    }


    /**
     * Returns true if there is a Unit in the Cell.
     *
     * @return true if there is a Unit, if not it returns false.
     */
    public boolean hasUnit() {
        return !(localUnit == null);
    }

    /**
     * Adds a Unit to the Cell.
     *
     * @param unit unit to be added.
     * @throws CellIsOccupiedException if the Cell is occupied by a unit.
     */
    public void addUnit(Unit unit) {
        if (hasUnit()) throw new CellIsOccupiedException("Cell at " + location.toString() + " has a unit already");
        localUnit = unit;
    }

    /**
     * Adds a Building to the Cell.
     *
     * @param building building to be added.
     * @throws CellIsOccupiedException if the Cell is occupied by a building.
     */
    public void addBuilding(Building building) {
        if (!(this.building == null))
            throw new CellIsOccupiedException("Cell at " + location.toString() + " has a building already");
        this.building = building;
    }

    /**
     * Removes the Unit of the Cell.
     *
     * @throws CellIsEmptyException if there is no Unit in the Cell.
     */
    public void removeUnit() {
        if (!hasUnit()) {
            throw new CellIsEmptyException("Cell at " + location.toString() + " is empty");
        }
        localUnit = null;
    }

    public String toString() {
        return "Cell at " + ((location == null) ? "null location" : location.toString()) +
                "terrain type: " + terrain + " unit: " + ((localUnit == null) ? "no unit" : localUnit.toString()) +
                "building: " + ((building == null) ? "no building" : building.toString());
    }

    /**
     * TODO: Un m�todo del backend que utiliza una clase del frontend??
     *
     * @return
     */
    public CellUIData getCellUIData() {
        CellUIData cellUIData;
        cellUIData = new CellUIData(location, terrain);
        if (hasBuilding()) {
            cellUIData.addBuildingData(getBuilding().getBuildingType(), getBuilding().getOwner().getId());
        }
        if (hasUnit()) {
            cellUIData.addUnitData(getUnit().getUnitType(), getUnit().getHealth(), getUnit().getMaxHealth(), getUnit().getOwner().getId());
        }
        return cellUIData;
    }

    /**
     * Adds the Holy Grail item to the current Cell.
     */
    public void addHolyGrail(){
        treasures.add(ItemFactory.buildItem("Holy Grail", ItemType.EXTRA, 0, 0, 0, 0, 0)); //TODO Check Values.
    }

    /**
     * Get the first item in the Treasures Queue.
     * @return First item in the Treasures Queue.
     */
    public Item getItem(){
        if ( treasures.isEmpty() ){
            return null;
        }else{
            return treasures.remove();
        }
    }

    /**
     * Adds an item to the Treasures Queue.
     * @param addItem Item added to the Treasures Queue
     */
    public void addItem( Item addItem ){
        treasures.add(addItem);
    }

    public boolean canRecieveItem(){
        if(treasures.size() >= MAX_ITEMS) return false;
        else return getTerrain().canRecieveItem();
    }
}
