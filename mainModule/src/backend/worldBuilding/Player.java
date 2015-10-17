package backend.worldBuilding;

import java.util.Objects;

public class Player {
    private String name;
    private Integer gold;

    public Player(String name){
        this.name = name;
        gold = 100;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name);
    }

    @Override
    public int hashCode(){
        return name.hashCode();
    }

    @Override
    public String toString(){
        return name;
    }


}
