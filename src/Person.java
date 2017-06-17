import java.io.Serializable;

public class Person implements Serializable{
    protected String name;
    protected String address;

    public Person(String name , String address){
        this.name = name;
        this.address = address;
    }
    public Person(){
    }

}
