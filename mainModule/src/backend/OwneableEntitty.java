package backend;

import backend.worldBuilding.Player;
import backend.worldBuilding.Location;
import jdk.nashorn.internal.objects.NativeUint16Array;

import java.io.Serializable;


public abstract class OwneableEntitty extends Entity implements Serializable {
    private Player owner=null;

    public OwneableEntitty(Location inLocation,Player inOwner){
        super(inLocation);
        this.owner=inOwner;
    }

    public void setOwner(Player owner){
        this.owner=owner;
    }

    public Player getOwner() {
        return owner;
    }

    public boolean hasOwner(){
        return owner != null;
    }
}
