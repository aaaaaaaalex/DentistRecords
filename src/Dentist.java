public class Dentist extends Person{
    private String password;

    public Dentist(String name, String address, String password){
        super(name ,address);
        this.password = password;
    }


    // Gets and Sets
    public void setPassword(String password){
        this.password = password;
    }

     public String getPassword(){
         return this.password;
     }
}
