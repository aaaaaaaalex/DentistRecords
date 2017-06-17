import java.io.Serializable;

public class Procedure implements Serializable{
    public static int procCount = 0;

    private int procNo;
    private String procName;
    private double procCost;


    public Procedure(String procName, double procCost){
        String formatCost = String.format("%.2f", procCost);
        this.procName = procName;
        this.procCost = Double.valueOf(formatCost);
        this.procNo = ++procCount;
    }

    public Procedure(int procNo){
        this.procNo = procNo;
    }

    //other

    public String toString(){
        String s = "";

        s+= "\nProcedure Name: " + this.procName;
        s+= "\nProcedure Number: " + this.procNo;
        s+= "\nProcedure Cost: " + this.procCost + "\n";

        return s;
    }

    public void print(){
        System.out.println(this.toString());
    }


    // gets and sets
    public int getProcNo() {
        return procNo;
    }

    public void setProcNo(int procNo) {
        this.procNo = procNo;
    }

    public String getProcName() {
        return procName;
    }

    public void setProcName(String procName) {
        this.procName = procName;
    }

    public double getProcCost() {
        return procCost;
    }

    public void setProcCost(double procCost) {
        this.procCost = procCost;
    }
}
