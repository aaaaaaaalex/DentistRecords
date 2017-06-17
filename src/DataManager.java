/*
*  A new interface between other classes and the DataManager.
*  This will make it easier to swap out DataBase / File-Based
*  modules in the future.
*  In short, higher modularity.
* */

import java.util.ArrayList;
import java.util.Locale;

public interface DataManager {


    // Load and Saves
    void saveLocale(Locale l);

    Locale loadLocale();

    void writeToFile();

    void readFromFile();


    // Gets
    ArrayList<Patient> getPatients();

    ArrayList<Invoice> getInvoices(int patientID);

    ArrayList<Procedure> getProcedures(int patientID, int invoiceID);

    //overloaded to allow for an unfiltered list of all procedures in existence
    ArrayList<Procedure> getProcedures();

    ArrayList<Payment> getPayments(int patientID, int invoiceID);


    // Sets
    void addPatient(Patient p);

    void addInvoice(int patientID, Invoice inv);

    void addPayment(Payment p, int invoiceID, int patientID);

    void addProcedure(Procedure p , int invoiceID, int patientID);

    void addProcedure(Procedure p);

    void removeInvoice(int patientID, int invoiceID);

    void removeProcedure(int patientID, int invoiceID, int procedureID);

    void removeProcedure(int procedureID);

    void removePayment(int paymentID);
    void removePatient(int patientID);


    // Utility
    int whoOwns_inv(int invoiceID);

    Patient findPatient(int patientID);

    Invoice findInvoice(int patientID, int invoiceID);


    // Reports
    void generateReport(String dirPath, String fileName);

    void generateReport(String dirPath, String fileName, int timeLimit_days);

    void writePatientReport(ArrayList<Patient> patientsToWrite, String dirPath, String fileName);

    void sortPatientHighestFirst(ArrayList<Patient> patientsUnsorted);
}
