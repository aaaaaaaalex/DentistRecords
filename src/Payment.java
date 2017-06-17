import java.io.Serializable;
import java.util.Date;

public class Payment implements Serializable{
    public static int paymentCount = 0;

    private int paymentNo;
    private double paymentAmnt;
    private Date paymentDate;

    public Payment(double paymentAmnt, Date payDate){
        this.paymentDate = payDate;
        String formatCost = String.format("%.2f", paymentAmnt);
        this.paymentNo = ++paymentCount;
        this.paymentAmnt = Double.valueOf(formatCost);
    }

    public Payment(){
        this.paymentDate = new Date();
        this.paymentAmnt = 0;
        String formatCost = String.format("%.2f", this.paymentAmnt);
        this.paymentAmnt = Double.valueOf(formatCost);
        this.paymentNo = ++paymentCount;
        this.paymentDate = new Date();
    }

    public Payment(int paymentNo){
        this. paymentNo = paymentNo;
    }


    //other
    public String toString(){
        String s = "";

        s += "\nPayment number: " + this.paymentNo;
        s += "\nPayment Date: " + this.paymentDate;
        s += "\nPayment Amount: " + this.paymentAmnt + "\n";

        return s;
    }
    public void print(){
        System.out.println(this.toString());
    }

    //gets and sets
    public int getPaymentNo() {
        return paymentNo;
    }

    public void setPaymentNo(int paymentNo) {
        this.paymentNo = paymentNo;
    }

    public double getPaymentAmnt() {
        return paymentAmnt;
    }

    public void setPaymentAmnt(double paymentAmnt) {
        this.paymentAmnt = paymentAmnt;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
}
