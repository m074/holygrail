package backend.terrain;

import java.io.Serializable;

/**
 * //TODO: javadoc
 */
public class Water extends Terrain implements Serializable {
    public Water(){
        super(2.0,2.2,1.9,1,5);

    }
    @Override
    public boolean canReceiveItem(){
        return false;
    }
}
