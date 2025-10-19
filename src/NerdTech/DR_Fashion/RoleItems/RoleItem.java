package NerdTech.DR_Fashion.RoleItems;

public class RoleItem {

    private int id;
    private String name;

    public RoleItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name; // JComboBox එකේ display වෙන එක
    }
}
