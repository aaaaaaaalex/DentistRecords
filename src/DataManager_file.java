import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DataManager_file implements DataManager{

    private ArrayList<Patient> patients;
    private ArrayList<Procedure> procedureList;

    public DataManager_file() {
        this.patients = new ArrayList<>();
        this.procedureList = new ArrayList<>();
    }

    public void saveLocale(Locale l){
        String language = l.getLanguage();
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(".//save/locale.txt"));
            bw.write(language);
            bw.close();
        } catch (IOException e) {
//            e.printStackTrace();
            System.out.println("ERROR: could not save locale");
        }
    }
    public Locale loadLocale(){
        Locale loc;
        try {
            BufferedReader br = new BufferedReader(new FileReader(".//save/locale.txt"));
            String s = br.readLine();
            loc = new Locale(s);
        } catch (IOException e) {
//            e.printStackTrace();
            System.out.println("ERROR: couldnt load locale");
            loc = new Locale("en");
        }

        return loc;
    }

    public void writeToFile(){
        try {
            FileOutputStream FOS = new FileOutputStream(".//save//patients.ser");
            FileOutputStream FOS2 = new FileOutputStream(".//save//procedures.ser");
            ObjectOutputStream OOS = new ObjectOutputStream(FOS);
            ObjectOutputStream OOS2 = new ObjectOutputStream(FOS2);
            OOS.writeObject(patients);
            OOS2.writeObject(procedureList);
        } catch (IOException e) {
            System.out.println("Output stream couldn't write a file");
//            e.printStackTrace();
        }
    }
    public void readFromFile(){

        // Load the lst of patients
        try {
            FileInputStream FIS = new FileInputStream(".//save//patients.ser");
            ObjectInputStream OIS = new ObjectInputStream(FIS);
            this.patients = (ArrayList<Patient>)OIS.readObject();
            for(int i = 0; i < this.patients.size(); i++){
                Patient.patientCount++;
                //refresh static fields by counting instances
                this.patients.get(i).countInvoices();
            }
        } catch (IOException e) {
            System.out.println("Patients not loaded");
//            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Patients not loaded");
//            e.printStackTrace();
        }

        //Load the list of procedures
        try{
            FileInputStream FIS2 = new FileInputStream(".//save//procedures.ser");
            ObjectInputStream OIS2 = new ObjectInputStream(FIS2);
            this.procedureList = (ArrayList<Procedure>)OIS2.readObject();
            for(int i = 0; i <= this.procedureList.size(); i++){
                Procedure.procCount++;
            }
        } catch (ClassNotFoundException | IOException e) {
            System.out.println("Procedures not loaded!");
//            e.printStackTrace();
        }
    }
    //-----------------------------------------------------------------------
    //                                  GETS
    //-----------------------------------------------------------------------

    public ArrayList<Patient> getPatients(){
        return this.patients;
    }
    public ArrayList<Invoice> getInvoices(int patientID){
        if(this.findPatient(patientID) != null) {
            return this.findPatient(patientID).getP_invoiceList();
        }

        return null;
    }
    // a method for finding the Patient associated with a given Invoice
    public int whoOwns_inv(int invoiceID){
        for(int i = 0; i < patients.size(); i++){
            ArrayList<Invoice> invs = patients.get(i).getP_invoiceList();
            for(int j = 0; j < invs.size(); j++){
                if(invs.get(j).getInvoiceNo() == invoiceID){
                    return patients.get(i).getPatientNo();
                }
            }
        }
        return -1;
    }

    public ArrayList<Procedure> getProcedures(int patientID, int invoiceID){
        Patient p = findPatient(patientID);
        Invoice i = p.getInvoice(invoiceID);
        if(i != null) return i.getIn_procList();
        return null;
    }
    //overloaded to allow for an unfiltered list of all procedures in existence
    public ArrayList<Procedure> getProcedures(){
        return this.procedureList;
    }

    public ArrayList<Payment> getPayments(int patientID, int invoiceID){
        Patient p = findPatient(patientID);
        ArrayList<Payment> pays = p.getPayments(invoiceID);
        if(pays != null) return pays;
        return null;
    }

    //---------------------------------------------------------------------
    //                              SETS
    //---------------------------------------------------------------------

    public void addPatient(Patient p){
        this.patients.add(p);
    }

    public void addInvoice(int patientID, Invoice inv){
        Patient patient = this.findPatient(patientID);
        if(patient != null) patient.addInvoice(inv);
    }

    public void addPayment(Payment p, int invoiceID, int patientID){
        Patient patient = this.findPatient(patientID);
        if(p != null)patient.makePayment(p, invoiceID);
        else System.out.println("couldn't find patient");

    }

    //add procedure to a specific patient.
    public void addProcedure(Procedure p , int invoiceID, int patientID){
        Patient patient = this.findPatient(patientID);
        if(patient != null) patient.addProcedure(p, invoiceID);
    }
    //add a procedure to the list of all procedures/
    public void addProcedure(Procedure p){
        this.procedureList.add(p);
    }


    //----------------------------------------------------------------------
    // UTILITY
    //----------------------------------------------------------------------
    public Patient findPatient(int patientID){
        for(int i = 0; i < this.patients.size(); i++){
            if(this.patients.get(i).getPatientNo() == patientID){
                return this.patients.get(i);
            }
        }
        return null;
    }
    public void removePatient(int patientID){
        for(int i = 0; i < this.patients.size(); i++){
            if(this.patients.get(i).getPatientNo() == patientID){
                this.patients.remove(i);
            }
        }
    }

    public Invoice findInvoice(int patientID, int invoiceID){
        Patient p = this.findPatient(patientID);
        Invoice inv = p.getInvoice(invoiceID);
        if(inv != null) return inv;

        return null;
    }
    public void removeInvoice(int patientID, int invoiceID){
        Patient p = this.findPatient(patientID);
        p.removeInvoice(invoiceID);
    }


    public void removeProcedure(int patientID, int invoiceID, int procedureID){
        Patient p = this.findPatient(patientID);
        p.removeProcedure(invoiceID, procedureID);
    }

    public void removeProcedure(int procedureID){
        for(int i = 0; i < this.procedureList.size(); i++){
            Procedure p = this.procedureList.get(i);
            if(p.getProcNo() == procedureID){
                this.procedureList.remove(i);
            }
        }
    }

    @Override
    public void removePayment(int paymentID) {

    }


    // ----------------
    // REPORTS
    // ----------------
    public void generateReport(String dirPath, String fileName){
        writePatientReport(this.patients, dirPath, fileName);
    }
    public void generateReport(String dirPath, String fileName, int timeLimit_days){
        ArrayList<Patient> patientsInDebt = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        //store the current date in miliseconds
        long nowMili = cal.getTimeInMillis();
        //get all patients in debt
        for(int i = 0; i < this.patients.size(); i++) {
            Patient p = this.patients.get(i);
            for (int inv = 0; inv < p.getP_invoiceList().size(); inv++) {
                Invoice invoice = p.getP_invoiceList().get(inv);
                if (invoice.getInvoiceAmnt() > 0) {
                    patientsInDebt.add(p);
                }
            }
        }

        // of patient in debt, get those who are overdue
        ArrayList<Patient> patientsOverdue = new ArrayList<>();
        for(int i = 0; i < patientsInDebt.size(); i++){
            Patient p = patientsInDebt.get(i);
            Date payDate;
            for(int j = 0; j < p.getP_invoiceList().size(); j++){
                Invoice inv = p.getP_invoiceList().get(j);
                for(int k = 0; k < inv.getIn_paymentList().size(); k++){
                    Payment pay = inv.getIn_paymentList().get(k);

                    // Find difference in time between last pay-date and now
                    payDate = pay.getPaymentDate();
                    long then = payDate.getTime();
                    long day = 1000 * 60 * 60 * 24;
                    long daysWithoutPayment = nowMili - then /day;

                    // if the comparison between then and now is over the limit, add patient to list
                        if(daysWithoutPayment > timeLimit_days){
                            patientsOverdue.add(p);
                        }

                }if(inv.getIn_paymentList().size() == 0){
                    //if there is no payment to check, get time between invoice and now.
                    payDate = inv.getInvoiceDate();
                    long then = payDate.getTime();
                    long day = 1000 * 60 * 60 * 24;
                    long daysWithoutPayment = nowMili - then /day;

                    if(daysWithoutPayment > timeLimit_days) patientsOverdue.add(p);
                }
            }
        }
        //sort the overduePatients
        sortPatientHighestFirst(patientsOverdue);
        //generate report of all overdue patients.
        writePatientReport(patientsOverdue, dirPath, fileName);

    }

    public void writePatientReport(ArrayList<Patient> patientsToWrite, String dirPath, String fileName) {
        String report = "";
        for (int i = 0; i < patientsToWrite.size(); i++) {
            Patient p = patientsToWrite.get(i);
            p.updateMoneyOwed();
            report += "---------------------------------------------\r\n";
            report += "|Patient " + String.valueOf(i) + "\r\n";
            report += "|Name:\t" + p.getName() + "\r\n";
            report += "|Address:\t" + p.getAddress() + "\r\n";
            report += "|Phone:\t" + String.valueOf(p.getPhoneNo()) + "\r\n";
            report += "|Amount Owed:\t" + String.valueOf(p.getMoneyOwed()) + "\r\n";
            report += "---------------------------------------------\r\n";
            report += "|INVOICES:\r\n";
            for (int j = 0; j < p.getP_invoiceList().size(); j++) {
                Invoice invoice = p.getP_invoiceList().get(j);
                report += "\t|Invoice " + String.valueOf(j) + ":\r\n";
                report += "\t|Date:\t" + String.valueOf(invoice.getInvoiceDate()) + "\r\n";
                report += "\t|Amount:\t" + String.valueOf(invoice.getInvoiceAmnt()) + "\r\n";
                report += "---------------------------------------------\r\n";
                report += "\tPROCEDURES:\r\n";
                for (int k = 0; k < invoice.getIn_procList().size(); k++) {
                    Procedure procedure = invoice.getIn_procList().get(k);
                    report += "\t\tProcedure " + String.valueOf(k) + "\r\n";
                    report += "\t\tName:\t" + procedure.getProcName() + "\r\n";
                    report += "\t\tCost:\t" + procedure.getProcCost() + "\r\n";
                    report += "---------------------------------------------\r\n";
                }
                report += "\tPAYMENTS:\r\n";
                for (int k = 0; k < invoice.getIn_paymentList().size(); k++) {
                    Payment payment = invoice.getIn_paymentList().get(k);
                    report += "\t\tPayment " + String.valueOf(k) + "\r\n";
                    report += "\t\tDate:\t" + payment.getPaymentDate() + "\r\n";
                    report += "\t\tAmount:\t" + payment.getPaymentAmnt() + "\r\n";
                    report += "---------------------------------------------\r\n";
                }
            }

            try {
                BufferedWriter out = new BufferedWriter(new FileWriter(dirPath + fileName));
                out.write(report);
                out.close();
            } catch (IOException e) {
                System.out.println("File write failed.");
//            e.printStackTrace();
            }
        }
    }

    public void sortPatientHighestFirst(ArrayList<Patient> patientsUnsorted){
        for (int i = 0; i < patientsUnsorted.size(); i++) {
            patientsUnsorted.get(i).updateMoneyOwed();
        }

        // use bubble-sort to sort patiens by debt.
        boolean sorted = false;
        while(!sorted) {
            sorted = true;
            for(int i = 1; i < patientsUnsorted.size(); i++){
                if(patientsUnsorted.get(i).getMoneyOwed() >
                        patientsUnsorted.get(i-1).getMoneyOwed())
                {
                    sorted = false;
                    Patient p2 = patientsUnsorted.get(i),
                            p1 = patientsUnsorted.get(i-1);
                    patientsUnsorted.remove(i);
                    patientsUnsorted.add(i-1, p2);
                }
            }

        }
    }
}
