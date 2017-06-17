import javax.xml.transform.Result;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

// This data manager uses the mySQL connector and the mySQL DBMS to store data.
public class DataManager_db implements DataManager{

    // record the username, password and URL for the designated database
    private final String loginName;
    private final String password;
    private final String URL;

    private Connection connection;

    public DataManager_db() throws SQLException{
        loginName = "root";
        password = "";
        URL = "jdbc:mysql://localhost:3306/dentalRecords";

            //get connection to the database
            connection = DriverManager.getConnection(URL, loginName, password);


            String createDentist =
                    "create table if not exists dentist (" +
                            "dentist_id int unsigned not null, " +
                            "name varchar(255), " +
                            "password varchar(255), " +
                            "primary key (dentist_id) );";

            String createPatient =
                    "create table if not exists patient ( " +
                            "patient_id int unsigned not null, " +
                            "dentist_id int unsigned not null, " +
                            "full_name varchar(255), " +
                            "home_address varchar(255), " +
                            "phone_number int unsigned, " +
                            "money_owed decimal(8,2), " +
                            "primary key (patient_id), " +
                            "foreign key (dentist_id) references dentist(dentist_id) );";

            String createInvoice =
                    "create table if not exists invoice (" +
                            "invoice_id int unsigned not null, " +
                            "patient_id int unsigned not null, " +
                            "amount decimal(8,2), " +
                            "invoice_date date, " +
                            "payed boolean, " +
                            "primary key (invoice_id), " +
                            "foreign key (patient_id) references patient(patient_id));";


            String createPayment =
                    "create table if not exists payment (" +
                            "payment_id int unsigned not null, " +
                            "invoice_id int unsigned not null," +
                            "amount decimal(8, 2), " +
                            "pay_date date, " +
                            "primary key (payment_id)," +
                            "foreign key (invoice_id) references invoice(invoice_id)" +
                            ");";

            String createProc =
                    "create table if not exists proc (" +
                            "proc_id int unsigned not null, " +
                            "name varchar(255), " +
                            "cost decimal(8,2), " +
                            "primary key (proc_id));";

            String createSettings =
                    "create table if not exists settings (" +
                            "user_language varchar(2)," +
                            "primary key (user_language) );";

            String inv_proc_list =
                    "create table if not exists inv_proc_list (" +
                            "invoice_id int unsigned not null, " +
                            "proc_id int unsigned not null, " +
                            "foreign key (invoice_id) references invoice(invoice_id), " +
                            "foreign key (proc_id) references proc(proc_id));";


            connection.createStatement().execute(createDentist);
            connection.createStatement().execute(createPatient);
            connection.createStatement().execute(createInvoice);
            connection.createStatement().execute(createPayment);
            connection.createStatement().execute(createProc);
            connection.createStatement().execute(inv_proc_list);
            connection.createStatement().execute(createSettings);
    }

    public DataManager_db (String loginName, String password, String URL) throws SQLException {
        this.loginName = loginName;
        this.password = password;
        this.URL = URL;

            //get connection to the database
            connection = DriverManager.getConnection(URL, loginName, password);

            String createDentist =
                    "create table if not exists dentist (" +
                            "dentist_id int unsigned not null, " +
                            "name varchar(255), " +
                            "password varchar(255), " +
                            "primary key (dentist_id) );";

            String createPatient =
                    "create table if not exists patient (" +
                            "patient_id int unsigned not null, " +
                            "dentist_id int unsigned not null, " +
                            "full_name varchar(255), " +
                            "home_address varchar(255), " +
                            "phone_number int unsigned, " +
                            "money_owed decimal(8,2), " +
                            "primary key (patient_id), " +
                            "foreign key (dentist_id) );";

            String createInvoice =
                    "create table if not exists invoice (" +
                            "invoice_id int unsigned not null, " +
                            "patient_id int unsigned not null, " +
                            "amount decimal(8,2), " +
                            "invoice_date date, " +
                            "payed boolean, " +
                            "primary key (invoice_id), " +
                            "foreign key (patient_id) );";


            String createPayment =
                    "create table if not exists payment (" +
                    "payment_id int unsigned not null, " +
                    "invoice_id int unsigned not null," +
                    "amount decimal(8, 2), " +
                    "pay_date date " +
                    "primary key (payment_id)," +
                    "foreign key (invoice_id) " +
                    ");";

            String createProc =
                    "create table if not exists proc (" +
                            "proc_id int unsigned not null, " +
                            "name varchar(255), " +
                            "cost decimal(8,2), " +
                            "primary key (proc_id) ); ";

            String createSettings =
                    "create table if not exists settings (" +
                            "user_language varchar(2)," +
                            "primary key (user_language));";


            String inv_proc_list =
                    "create table if not exists inv_proc_list (" +
                            "invoice_id int unsigned not null, " +
                            "proc_id int unsigned not null, " +
                            "foreign key (invoice_id), " +
                            "foriegn key (proc_id) );";


            connection.createStatement().execute(createDentist);
            connection.createStatement().execute(createPatient);
            connection.createStatement().execute(createInvoice);
            connection.createStatement().execute(createPayment);
            connection.createStatement().execute(createProc);
            connection.createStatement().execute(inv_proc_list);
            connection.createStatement().execute(createSettings);

    }


    @Override
    public void saveLocale(Locale l) {
        String query = "update settings " +
                "set user_language = \"" + l.getLanguage() + "\";";
        try{
            connection.createStatement().executeUpdate(query);
        }catch(SQLException e){
            System.out.println("Error saving locale");
        }
    }

    @Override
    public Locale loadLocale() {
        String query = "select user_language from settings;";
        Locale l;

        try {
            ResultSet rs = connection.createStatement().executeQuery(query);
            rs.next();
            l = new Locale(rs.getString("user_language"));
        } catch (SQLException e) {
            e.printStackTrace();
            l = new Locale("en");
        }

        return l;
    }

    @Override
    public void writeToFile() {
        //obsolete
    }
    @Override
    public void readFromFile() {
        //obsolete
    }

    @Override
    public ArrayList<Patient> getPatients() {
        ArrayList<Patient> patients = new ArrayList<>();
        try {
            ResultSet rs = connection.createStatement().executeQuery("select * from patient;");

            //rs.next() functions both as a boolean indicator of more data, and also moves to the next db entry
            while(rs.next()){
                // column names which will be used to retrive patient data
                String patientNo = "patient_id";
                String name = "full_name";
                String address = "home_address";
                String phoneNo = "phone_number";

                Patient p = parsePatient(
                        rs.getInt(patientNo),
                        rs.getString(name),
                        rs.getString(address),
                        rs.getInt(phoneNo));

                patients.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    @Override
    public ArrayList<Invoice> getInvoices(int patientID) {

        ArrayList<Invoice> invoices = new ArrayList<>();
        try {
            ResultSet rs = connection.createStatement().executeQuery(
                    "select * from invoice " +
                    "where patient_id = " + String.valueOf(patientID)
            );

            String inv_id = "invoice_id";
            String inv_date = "invoice_date";


            while(rs.next()){
                Invoice i = parseInvoice(
                        rs.getInt(inv_id),
                        rs.getDate(inv_date)
                );
                invoices.add(i);
            }

        }catch(SQLException e){
            System.out.println("sql query error: getInvoices()");
        }

        return invoices;
    }

    @Override
    public ArrayList<Procedure> getProcedures(int patientID, int invoiceID) {
        ArrayList<Procedure> procs = new ArrayList<>();

        String query =
                "select * from proc " +
                "where proc_id in (" +
                        "select proc_id from inv_proc_list " +
                        "where invoice_id = " + String.valueOf(invoiceID)
                +");";

        try {
            ResultSet rs = connection.createStatement().executeQuery(query);
            String procNo = "proc_id";
            String name = "name";
            String cost = "cost";

            while(rs.next()){
                Procedure p = parseProcedure(
                        rs.getInt(procNo),
                        rs.getString(name),
                        rs.getDouble(cost)
                );

                // the simplest way (that i can think of) to find how many duplicate procedures
                // have been done on any one patient
                ResultSet noOfDuplicates = connection.createStatement().executeQuery(
                        "select count(invoice_id) as dup_no from inv_proc_list " +
                        "where proc_id = " + String.valueOf(p.getProcNo()) +
                                " AND invoice_id = " + String.valueOf(invoiceID) + ";"
                );

                noOfDuplicates.next();
                int count = noOfDuplicates.getInt("dup_no");
                for(int i = 0; i < count; i++)procs.add(p);
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }

        return procs;
    }

    @Override
    public ArrayList<Procedure> getProcedures() {
        ArrayList<Procedure> procs = new ArrayList<>();

        String query = "select * from proc;";
        String name = "name";
        String id = "proc_id";
        String cost = "cost";

        try{
            ResultSet rs = connection.createStatement().executeQuery(query);
            while(rs.next()){
                Procedure p = parseProcedure(
                        rs.getInt(id),
                        rs.getString(name),
                        rs.getDouble(cost)
                );
                procs.add(p);
            }
        }catch(SQLException e){
            System.out.println("query error : getProcedures()");
        }

        return procs;
    }

    @Override
    public ArrayList<Payment> getPayments(int patientID, int invoiceID) {
        ArrayList<Payment> payments = new ArrayList<>();

        String query =
                "select * from payment " +
                "where invoice_id = " + String.valueOf(invoiceID);

        String pay_id = "payment_id";
        String amount = "amount";
        String date = "pay_date";

        try{
            ResultSet rs = connection.createStatement().executeQuery(query);

            while(rs.next()){
                Payment p = parsePayment(
                        rs.getInt(pay_id),
                        rs.getDouble(amount),
                        rs.getDate(date)
                );
                payments.add(p);
            }
        }catch(SQLException e){System.out.println("query error: getPayments(int, int)");}

        return payments;
    }

    @Override
    public void addPatient(Patient p) {
        int patient_id = p.getPatientNo();
        int dentist_id = 1;
        String full_name = p.getName();
        String home_address = p.getAddress();
        int phone_number = p.getPhoneNo();
        double money_owed = p.getMoneyOwed();

        String query = "insert into patient values (" +
                String.valueOf(patient_id) + ", " +
                String.valueOf(dentist_id) + ", " +
                "\"" + full_name + "\", " +
                "\"" + home_address + "\", " +
                String.valueOf(phone_number) + ", " +
                String.valueOf(money_owed) + " " +
                ");";

        try {connection.createStatement().execute(query);}
        catch (SQLException e) {System.out.println("query error: addPatient(p)");}

        ArrayList<Invoice> invoices = p.getP_invoiceList();
        for(int i = 0; i < invoices.size(); i++) addInvoice(p.getPatientNo(), invoices.get(i));
    }

    @Override
    public void addInvoice(int patientID, Invoice inv) {
        int invoice_id = inv.getInvoiceNo();
        double amount = inv.getInvoiceAmnt();

        Date invoice_date = inv.getInvoiceDate();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(invoice_date);

        //converting a boolean value into an integer 1 or 0, to accommodate the
        // mysql method of storing boolean values, using conditional assignment.
        int payed = inv.isPayed() ? 1 : 0 ;

        String query = "insert into invoice values (" +
                String.valueOf(invoice_id) + ", " +
                String.valueOf(patientID) + ", " +
                String.valueOf(amount) + ", " +
                "\"" + formattedDate + "\", " +
                String.valueOf(payed) + ");";

        try{
            connection.createStatement().execute(query);
        }catch(SQLException e){
            System.out.println("query error: addInvoice(int, invoice)");
        }

        ArrayList<Payment> payments = inv.getIn_paymentList();
        ArrayList<Procedure> procs = inv.getIn_procList();
        for(int i = 0; i < payments.size(); i++) {
            addPayment(
                    payments.get(i),
                    inv.getInvoiceNo(),
                    patientID);
        }
        for(int i = 0; i < procs.size(); i++){
            addProcedure(
                    procs.get(i),
                    inv.getInvoiceNo(),
                    patientID);
        }

    }

    @Override
    public void addPayment(Payment p, int invoiceID, int patientID) {
        int payment_id = p.getPaymentNo();
        double amount = p.getPaymentAmnt();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String pay_date = df.format(p.getPaymentDate());

        String query = "insert into payment values (" +
                String.valueOf(payment_id) + ", " +
                String.valueOf(invoiceID) + ", " +
                String.valueOf(amount) + ", " +
                "\"" + pay_date + "\"" +
                ");";

        String updateInv = "update invoice " +
                "set amount = ( amount - " + String.valueOf(p.getPaymentAmnt()) + ")" +
                "where invoice_id = " + String.valueOf(invoiceID) + ";";
        try {
            connection.createStatement().execute(query);
        } catch (SQLException e) {System.out.println("query error: addPayment(p, int, int)");
        e.printStackTrace();}
    }

    @Override
    public void addProcedure(Procedure p, int invoiceID, int patientID) {
        String proc_id = String.valueOf(p.getProcNo());
        String proc_cost = String.valueOf(p.getProcCost());
        String invoice_id = String.valueOf(invoiceID);

        String query = "insert into inv_proc_list values (" +
                "\"" + invoice_id + "\", " +
                "\"" + proc_id + "\");";

        String updateInv = "update invoice " +
                "set amount = (amount + " + proc_cost + ") " +
                " where invoice_id = " + String.valueOf(invoice_id) + ";";
        try{
            connection.createStatement().execute(query);
            connection.createStatement().execute(updateInv);
        }catch(SQLException e){
            System.out.println("query error: addProcedure(p, int, int)");
            e.printStackTrace();
        }
    }
    @Override
    public void addProcedure(Procedure p) {
        int id = this.nextProcNo();
        String proc_id = String.valueOf(id);
        String name = p.getProcName();
        String cost = String.valueOf(p.getProcCost());
        String query = "insert into proc values (" +
                "\"" + proc_id + "\"," +
                "\"" + name + "\"," +
                "\"" + cost + "\");";

        try{
            connection.createStatement().execute(query);
        }catch(SQLException e){
            System.out.println("query error: addProcedure(p)");
        }
    }



    @Override
    public void removeInvoice(int patientID, int invoiceID) {
        try {
            String id = String.valueOf(invoiceID);

            connection.createStatement().execute(
                    "delete from inv_proc_list " +
                    "where invoice_id = " + id + ";");
            connection.createStatement().execute(
                    "delete from payment " +
                    "where invoice_id = " + id + ";");
            connection.createStatement().execute(
                    "delete from invoice " +
                    "where invoice_id = " + id + ";");

        } catch (SQLException e) {
            System.out.println("query error: removeInvoice");
        }
    }

    @Override
    public void removeProcedure(int patientID, int invoiceID, int procedureID) {
        String invID = String.valueOf(invoiceID);
        String procID = String.valueOf(procedureID);

        try {
            connection.createStatement().execute(
                    "delete from inv_proc_list " +
                    "where proc_id = " + procID + " AND " +
                    "invoice_id = " + invoiceID + ";");
        } catch (SQLException e) {
            System.out.println("query error: removeProcedure(int, int, int)");
        }
    }

    @Override
    public void removeProcedure(int procedureID) {
        String proc_id = String.valueOf(procedureID);

        try {
            connection.createStatement().execute(
                    "delete from inv_proc_list " +
                            "where proc_id = " + proc_id + ";");

            connection.createStatement().execute(
                    "delete from proc " +
                            "where proc_id = " + proc_id + ";");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removePatient(int patientID) {
        String p_id = String.valueOf(patientID);

        try{

            //first remove all invoices belonging to the patient using pre-existing methods
                ResultSet rs = connection.createStatement().executeQuery(
                        "select invoice_id from invoice " +
                                "where patient_id = " + p_id + ";");
                while(rs.next()) removeInvoice(patientID, rs.getInt("invoice_id"));

                connection.createStatement().execute(
                        "delete from patient " +
                                "where patient_id = " + p_id + ";");
        }catch(SQLException e){}
    }

    @Override
    public void removePayment(int paymentID){
        String p_id = String.valueOf(paymentID);
        Payment p = null;
        int invoice_id = -1;

        try{
            ResultSet rs = connection.createStatement().executeQuery(
                    "select distinct invoice_id, amount, pay_date from payment where payment_id = " + p_id
            );
            while(rs.next()) {
                p = new Payment(rs.getDouble("amount"), rs.getDate("pay_date"));
                invoice_id = rs.getInt("invoice_id");
            }

            if(p != null && (invoice_id != -1)) {

                connection.createStatement().execute(
                        "delete from payment " +
                                "where payment_id = " + p_id + ";");

                connection.createStatement().execute(
                        "update invoice " +
                                "set amount = (amount + " + String.valueOf(p.getPaymentAmnt()) + ")" +
                                "where invoice_id = " + String.valueOf(invoice_id)+ ";");
            }
        }catch(SQLException e){
            System.out.println("query error: removePayment(p)");
        }
    }


    //obsolete
    @Override
    public int whoOwns_inv(int invoiceID) {
        return 0;
    }

    @Override
    public Patient findPatient(int patientID) {
        Patient p = null;
        String query = "select * from patient where patient_id = " + String.valueOf(patientID) + ";";
        ResultSet rs;
        try {
            rs = connection.createStatement().executeQuery(query);
            if(rs.next()) {
                p = parsePatient(patientID,
                        rs.getString("full_name"),
                        rs.getString("home_address"),
                        rs.getInt("phone_number"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return p;
    }

    @Override
    public Invoice findInvoice(int patientID, int invoiceID) {
        Invoice i = null;
        String query = "" +
                "select * from invoice where invoice_id = " + String.valueOf(invoiceID);

        try{
            ResultSet rs = connection.createStatement().executeQuery(query);
            rs.next();
            i = parseInvoice(invoiceID, rs.getDate("invoice_date"));
        }catch(SQLException e){}

        return i;
    }



    // -------------------Parse-------------------
    // Take strings returned from the DB and
    // use them to create usable objects

    private Dentist parseDentist(String name, String address, String password){
        Dentist d = new Dentist(name, address, password);
        return d;
    }

    private Patient parsePatient(int patientNo, String fullname, String address, int phoneNo){

        Patient p = new Patient(fullname, address);
        p.setPatientNo(patientNo);
        p.setPhoneNo(phoneNo);
        try {
            ResultSet rs = connection.createStatement().executeQuery(
                    "select * " +
                    "from invoice " +
                    "where patient_id = " + String.valueOf(patientNo)
            );

            String inv_id = "invoice_id";
            String inv_date = "invoice_date";
            while(rs.next()){
                Invoice i = parseInvoice(
                        rs.getInt(inv_id),
                        rs.getDate(inv_date)
                );

                p.addInvoice(i);
            }

        }catch(SQLException e){
            System.out.println("query error: parsePatient");
        }

        return p;
    }


    private Invoice parseInvoice(int invoiceNo, Date invDate){
        Invoice inv = new Invoice();
        inv.setInvoiceNo(invoiceNo);
        inv.setInvoiceDate(invDate);

        try{
            // query for procedures relating to the given invoice.
            // a nested select statement is used to get the desired procs...
            // from the many-to-many table 'inv_proc_list'.
            ResultSet procRS = connection.createStatement().executeQuery(
                    "select * from proc " +
                            "where proc_id in (" +
                            "select proc_id from inv_proc_list " +
                            "where invoice_id = " + String.valueOf(invoiceNo) +
                            ");"
            );

            String proc_id = "proc_id";
            String proc_name = "name";
            String proc_cost = "cost";
            while(procRS.next()){
                Procedure p = parseProcedure(
                        procRS.getInt(proc_id),
                        procRS.getString(proc_name),
                        procRS.getDouble(proc_cost)
                );
                inv.addProcedure(p);
            }


            ResultSet paymentRS = connection.createStatement().executeQuery(
                    "select * from payment " +
                    "where invoice_id = " + String.valueOf(invoiceNo)
            );

            String payment_id = "payment_id";
            String payment_amount = "amount";
            String payment_date = "pay_date";
            while(paymentRS.next()){
                Payment payment = parsePayment(
                        paymentRS.getInt(payment_id),
                        paymentRS.getDouble(payment_amount),
                        paymentRS.getDate(payment_date)
                );
                inv.addPayment(payment);
            }

        }catch(SQLException e){
            System.out.println("query error: parseInvoice");
            e.printStackTrace();
        }

        return inv;
    }


    private Procedure parseProcedure(int procNo, String name, double cost){
        Procedure p = new Procedure(name, cost);
        p.setProcNo(procNo);
        return p;
    }

    private Payment parsePayment(int paymentNo, double paymentAmount, Date payDate){
        Payment p = new Payment(paymentAmount, payDate);
        p.setPaymentNo(paymentNo);
        return p;
    }

    private int nextProcNo(){

        // The static integer used to count procedures increments
        // unintentionally due to parsing the same procedures for
        // different invoices.
        // To maintain full modularity, I will implement this fix
        // rather than a simpler fix that would involve altering Procedure.java

        int count = 0;

        try {
            //find the maximum value in the list of procedures.
            ResultSet rs = connection.createStatement().executeQuery("select max(proc_id) as count from proc;");
            rs.next();
            //the next id will be one larger than the last biggest.
            count = rs.getInt("count") + 1;
            Procedure.procCount = count;
        } catch (SQLException e) {
            System.out.println("query error: nextProcNo");
        }


        return count;
    }

    @Override
    public void generateReport(String dirPath, String fileName) {
        ArrayList<Patient> patients = this.getPatients();
        writePatientReport(patients, dirPath, fileName);
    }

    @Override
    public void generateReport(String dirPath, String fileName, int timeLimit_months) {
        ArrayList<Patient> patients = new ArrayList<>();

        try {

            // this huge query handles the filtering of all criteria.
            // it returns the set of patients for whom
            // there is no payments and the invoice is overdue,
            // or there is a payment but it is too old.
            ResultSet rs = connection.createStatement().executeQuery(
                        "select * from patient " +
                            "where patient_id in (" +
                            "    select patient_id from invoice i , payment p " +
                            "    where (" +
                            "       ( i.invoice_id = p.invoice_id " +
                            "       AND TIMESTAMPDIFF(month, p.pay_date, curdate()) > " + String.valueOf(timeLimit_months) +
                            "       ) " +
                            "    OR " +
                            "       ( not exists (select * from patient p where p.invoice_id = i.invoice_id)" +
                            "       AND TIMESTAMPDIFF(month, i.invoice_date, curdate()) > " + String.valueOf(timeLimit_months) +
                            "       )" +
                            "   )" +
                            ") AND money_owed > 0 " +
                            "order by money_owed desc;"
            );

            while(rs.next()){
                int patientNo = rs.getInt("patient_id");
                String fullname = rs.getString("full_name");
                String address = rs.getString("home_address");
                int phoneNo = (rs.getInt("phone_number"));

                patients.add(parsePatient(patientNo, fullname, address, phoneNo));
            }
            writePatientReport(patients, dirPath, fileName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writePatientReport(ArrayList<Patient> patientsToWrite, String dirPath, String fileName) {
        String report = "";
        System.out.println("writing to file... " + dirPath + fileName);
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

    @Override
    public void sortPatientHighestFirst(ArrayList<Patient> patientsUnsorted) {

    }
}
