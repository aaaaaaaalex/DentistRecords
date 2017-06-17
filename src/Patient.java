import java.io.Serializable;
import java.util.ArrayList;

public class Patient extends Person implements Serializable{
    //Serializable treats static values as transient, so they must be moved.
    public static int patientCount;

    private int patientNo;
    private int phoneNo;
    private int moneyOwed;
    private ArrayList <Invoice> p_invoiceList;

    //Constructor
    public Patient(String name, String address){
        super(name, address);
        this.p_invoiceList = new ArrayList<>();
        this.patientNo = ++patientCount;
    }

    public Patient(int patientNo){
        this.patientNo = patientNo;
    }
    public Patient(){
        super();
    }

    //-----
    //other
    //-----

    //this method is used to restore static values after deserialization.
    //the dataManager calls countInvoices on each Patient instance, and
    //the call cascades down the hierarchy, counting each class instance,
    //thereby restoring usable static 'count' values.
    public void countInvoices(){
        for(int i = 0; i < this.p_invoiceList.size(); i++){
            Invoice.invCount++;
            //payments are counted through patients, procedures, which are shared, are counted by the DataManager manually.
            this.p_invoiceList.get(i).countPayments();
        }
    }

    public String toString(){
        String s = "";

        s += "\nPatient Name: " + this.name;
        s += "\nPatient Number: " + this.patientNo;
        s += "\n\nInvoices\n-----------\n";

        if(this.p_invoiceList != null){
            for (Invoice i : this.p_invoiceList){
                s+= i.toString();
            }
        }

        return s;
    }

    public void print(){
        System.out.println(this.toString());
    }

    //---------------
    // gets and sets
    //---------------
    public int getPatientNo() {
        return patientNo;
    }
    public void setPatientNo(int patientNo) {
        this.patientNo = patientNo;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getAddress(){
        return this.address;
    }
    public void setAddress(String address){
        this.address = address;
    }

    public void setPhoneNo(int phoneNo){
        this.phoneNo = phoneNo;
    }
    public int getPhoneNo(){
        return this.phoneNo;
    }


    //--------------------------------------------INVOICE-----------------
    public ArrayList<Invoice> getP_invoiceList() {
        return p_invoiceList;
    }
    public void setP_invoiceList(ArrayList<Invoice> p_invoiceList) {
        this.p_invoiceList = p_invoiceList;
    }

    public void addInvoice(Invoice inv){
        this.p_invoiceList.add(inv);
    }
    public Invoice getInvoice(int invoiceID){
        for(int i = 0; i < p_invoiceList.size(); i++){
            if(this.p_invoiceList.get(i).getInvoiceNo() == invoiceID){
                return this.p_invoiceList.get(i);
            }
        }
        return null;
    }
    public void removeInvoice(int invoiceID){
        for(int i = 0; i < this.p_invoiceList.size(); i++){
            if(this.p_invoiceList.get(i).getInvoiceNo() == invoiceID){
                this.p_invoiceList.remove(i);
            }
        }
    }

    //--------------------------------------------PAYMENT-----------------
    public ArrayList<Payment> getPayments(int invoiceID){
        Invoice i = this.getInvoice(invoiceID);
        if (i != null) {
            return i.getIn_paymentList();
        }
        return null;
    }

    public boolean makePayment(Payment p, int invoiceNo){
        boolean successful = false;
        if(p_invoiceList != null){
            for(Invoice i : p_invoiceList){
                if(i.getInvoiceNo() == invoiceNo){
                    i.addPayment(p);
                    successful = true;
                }
            }
        }
        return successful;
    }



    //--------------------------------------------PROCEDURE---------------
    public ArrayList<Procedure> getProcedures(int invoiceID){
        Invoice i = this.getInvoice(invoiceID);
        if(i != null) return i.getIn_procList();

        return null;
    }

    public void addProcedure(Procedure p, int invoiceID){
        Invoice i = this.getInvoice(invoiceID);
        if(i != null) i.addProcedure(p);
    }

    public void removeProcedure(int invoiceID, int procedureID){
        Invoice i = this.getInvoice(invoiceID);
        if(i != null){
            i.removeProcedure(procedureID);
        }
    }

    public int getMoneyOwed(){
        return moneyOwed;
    }
    public void updateMoneyOwed(){
        int total= 0;
        for(int i = 0; i < this.p_invoiceList.size(); i++){
            total += this.p_invoiceList.get(i).getInvoiceAmnt();
        }
        this.moneyOwed = total;
    }

}