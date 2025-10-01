package NerdTech.DR_Fashion.RoleItems;

public class RoleItem {

    private int id;
    private String position;

    public RoleItem(int id, String position) {
        this.id = id;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public String getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return position;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RoleItem) {
            RoleItem other = (RoleItem) obj;
            return this.id == other.id;
        }
        return false;
    }
}
