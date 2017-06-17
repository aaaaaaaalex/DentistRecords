import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;

public class Invoice implements Serializable{
    public static int invCount = 0;

    private int invoiceNo;
    private double invoiceAmnt;
    private Date invoiceDate;
    private boolean isPayed;

    private ArrayList <Procedure> in_procList;
    private ArrayList <Payment> in_paymentList;

    //CONSTRUCTOR
    public Invoice(){
        in_paymentList = new ArrayList<>();
        in_procList = new ArrayList<>();
        this.updateTotal();
        this.invoiceDate = new Date();
        this.invoiceNo = ++invCount;
    }

    public Invoice(int invoiceNo){
        this.invoiceNo = invoiceNo;
    }


    // Gets the total outstanding amount due for procedures,
    // then updates invoiceAmnt. This is a private function
    // that will be called when procedures or payments are altered.
    private void updateTotal(){
        double total = 0.0;

        if(this.in_procList != null) {
            for (Procedure p : in_procList) {
                total += p.getProcCost();
            }
        }

        if(this.in_paymentList != null) {
            for (Payment p : in_paymentList) {
                total -= p.getPaymentAmnt();
            }
        }

        if(total <= 0.0){
            isPayed = true;
        }
        this.invoiceAmnt = total;
    }

    //------
    //other
    //------

    public void countPayments(){
        for(int i = 0; i < this.in_procList.size(); i++){
            Payment.paymentCount++;
        }
    }

    public void print(){
        System.out.println(this.toString());
    }

    public String toString(){
        String s = "---------------";

        s += "\nInvoice Number: " + this.invoiceNo;
        s += "\nInvoice Date: " + this.invoiceDate;
        s += "\nPayed: ";
        if(this.isPayed)s+= "yes";
        else s += "no";

        s+= "\n\n PROCEDURES\n-----------";
        if(this.in_procList != null) {
            for (Procedure p : this.in_procList) {
                s += p.toString();
            }
        }else s += "\nnone\n";

        s += "\nPayments\n-----------";
        if(this.in_paymentList != null) {
            for (Payment p : this.in_paymentList) {
                s += p.toString();
            }
        }else s+= "\nnone\n";

        s += "\nTOTAL\n-----------\n" + String.format("%.2f", this.invoiceAmnt) + "\n";

        return s;
    }


    //--------------
    //gets and sets
    //--------------
    public int getInvoiceNo() {
        return invoiceNo;
    }

    //is setting the invoice no. necessary if it uniquely identifies the invoice?
    public void setInvoiceNo(int invoiceNo) {
        this.invoiceNo = invoiceNo;
    }


    public double getInvoiceAmnt() {
        this.updateTotal();
        if(this.invoiceAmnt >= 0) {
            return invoiceAmnt;
        }
        // preventative measure in case an invoice amount has not been set.
        else return 0;
    }

    // setting the invoice amount will probably not be done by another class, rather
    // it will be indirectly set by setting procedures and payments.
    public void setInvoiceAmnt(double invoiceAmnt) {
        this.invoiceAmnt = invoiceAmnt;
    }


    public Date getInvoiceDate() {
        return invoiceDate;
    }
    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }


    public boolean isPayed() {
        return isPayed;
    }
    public void setPayed(boolean payed) {
        isPayed = payed;
    }


    //---------------------------
    // Procedure List Operations
    //---------------------------
    public ArrayList<Procedure> getIn_procList() {
        return in_procList;
    }

    //updates the total amount due after new list is added.
    public void setIn_procList(ArrayList<Procedure> in_procList) {
        this.in_procList = in_procList;
        this.updateTotal();
    }

    //-----------------------------
    // Add procedures individually
    //-----------------------------
    public boolean addProcedure(Procedure p){
        boolean success = false;

        try{
            this.in_procList.add(p);
            success = true;
        }catch(Exception e){}

        this.updateTotal();
        return success;
    }

    public boolean addProcedure(String procName, double procCost){
        boolean success = false;

        Procedure p = new Procedure(procName, procCost);
        try{
            this.in_procList.add(p);
            success = true;
        }catch(Exception e){}

        this.updateTotal();
        return success;
    }


    //--------------------------------
    // Remove procedures individually
    //--------------------------------
    public boolean removeProcedure(int procID){
        boolean success = false;

        for(int i = 0; i < this.in_procList.size(); i++) {
            if (procID == this.in_procList.get(i).getProcNo()) {
                try {
                    this.in_procList.remove(i);
                    success = true;
                    break;
                } catch (NullPointerException e) {}
            }
        }
        this.updateTotal();
        return success;
    }


    //-------------------------
    // Payment List Operations
    //-------------------------
    public ArrayList<Payment> getIn_paymentList() {
        return in_paymentList;
    }

    //updates the total amount due after new list is added.
    public void setIn_paymentList(ArrayList<Payment> in_paymentList) {
        this.in_paymentList = in_paymentList;
        this.updateTotal();
    }

    //---------------------------
    // Add payments individually
    //---------------------------
    public boolean addPayment( double payAmount, Date payDate ){
        boolean success = false;
        Payment p = new Payment (payAmount, payDate);
        try{
            this.in_paymentList.add(p);
            success = true;
        }catch(Exception e){}

        this.updateTotal();
        return success;
    }

    public boolean addPayment(Payment p){
        boolean success = false;
        try{
            this.in_paymentList.add(p);
            success = true;
        }catch(Exception e){}

        this.updateTotal();
        return success;
    }
    //-----------------------------
    // Remove payments individually
    //-----------------------------

    public boolean removePayment(int paymentID){
        boolean success = false;

        for(int i = 0; i < this.in_paymentList.size(); i++) {
            if (paymentID == this.in_paymentList.get(i).getPaymentNo()) {
                try {
                    this.in_paymentList.remove(i);
                    success = true;
                    break;
                } catch (NullPointerException e) {}
            }
        }
        this.updateTotal();
        return success;
    }
}
