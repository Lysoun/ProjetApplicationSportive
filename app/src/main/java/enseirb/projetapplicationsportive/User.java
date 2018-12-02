package enseirb.projetapplicationsportive;

public class User {
    private final String name;
    private final int id;

    public User(String name, int id){
        this.name = name;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return new String(name);
    }
}
