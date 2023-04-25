package com.devinsight.lab4.databaseelab4solution.services;

import com.devinsight.lab4.databaseelab4solution.DatabaseCredentials;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Service
public class QueryFunctionsOperations {
    private final DatabaseCredentials databaseCredentials;

    public QueryFunctionsOperations(DatabaseCredentials databaseCredentials) throws ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        this.databaseCredentials = databaseCredentials;
    }

    public String getResultForActions(int actions) {
        switch (actions) {
            case 1: {
                return getAction1Result();
            }
            case 2: {
                return getAction2Result();
            }
            case 3: {
                return getAction3Result();
            }
            case 4: {
                return getAction4Result();
            }
            case 5: {
                return getAction5Result();
            }
            case 6: {
                return getAction6Result();
            }
            case 7: {
                return getAction7Result();
            }
            default: return null;
        }
    }

    public String getAction1Result() {

        StringBuilder stringBuilder = new StringBuilder();

        String server = databaseCredentials.getUrl();
        String password = databaseCredentials.getPassword();
        String username = databaseCredentials.getUsername();

        try(Connection con = DriverManager.getConnection(server, username, password)){

            String query = "SELECT d.DOC_NAME, h.HOS_NAME, ht.HOS_TYPE_NAME " +
                    "FROM DOCTORS d " +
                    "INNER JOIN HOSPITALS h ON d.HOS_ID = h.HOS_ID " +
                    "INNER JOIN HOSPITALTYPE ht ON h.HOS_TYPE = ht.HOS_TYPE_ID ";

            PreparedStatement st=con.prepareStatement(query);

            ResultSet result=st.executeQuery();

            while(result.next())
            {
                String DoctorName = result.getString("DOC_NAME");
                String HospitalName = result.getString("hos_name");
                String HospitalType = result.getString("HOS_TYPE_NAME");

                stringBuilder.append(String.format("DoctorName: %s\n", DoctorName));
                stringBuilder.append(String.format("HospitalName: %s\n", HospitalName));
                stringBuilder.append(String.format("HOS_TYPE_NAME: %s\n", HospitalType));
                stringBuilder.append("\n");
            }


            result.close();
            st.close();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return stringBuilder.length() == 0 ? null : stringBuilder.toString();
    }

    public String getAction2Result() {

        StringBuilder stringBuilder = new StringBuilder();

        String server = databaseCredentials.getUrl();
        String password = databaseCredentials.getPassword();
        String username = databaseCredentials.getUsername();

        try(Connection con = DriverManager.getConnection(server, username, password)){

            String query = "SELECT d.DOC_NAME, h.hos_name " +
                    "FROM DOCTORS d " +
                    "INNER JOIN treatmentschedules t ON d.DOC_ID = t.doctor_id " +
                    "INNER JOIN Hospitals h on d.hos_id = h.hos_id " +
                    "GROUP BY d.DOC_NAME, h.hos_name " +
                    "HAVING COUNT(DISTINCT t.PAT_ID) > 1";

            PreparedStatement st=con.prepareStatement(query);

            ResultSet result=st.executeQuery();

            while(result.next())
            {
                String DoctorName = result.getString("DOC_NAME");
                String HospitalName = result.getString("hos_name");

                stringBuilder.append(String.format("DoctorName: %s\n", DoctorName));
                stringBuilder.append(String.format("HospitalName: %s\n", HospitalName));
                stringBuilder.append("\n");
            }


            result.close();
            st.close();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return stringBuilder.length() == 0 ? null : stringBuilder.toString();
    }

    public String getAction3Result() {

        StringBuilder stringBuilder = new StringBuilder();

        String server = databaseCredentials.getUrl();
        String password = databaseCredentials.getPassword();
        String username = databaseCredentials.getUsername();

        try(Connection con = DriverManager.getConnection(server, username, password)){

            String query = "SELECT d.DOC_NAME, COUNT(*) AS TREATMENT_COUNT " +
                    "FROM DOCTORS d " +
                    "INNER JOIN treatmentschedules t ON d.DOC_ID = t.doctor_id " +
                    "GROUP BY d.DOC_NAME " +
                    "ORDER BY TREATMENT_COUNT DESC";

            PreparedStatement st=con.prepareStatement(query);

            ResultSet result=st.executeQuery();
            while(result.next())
            {
                String DoctorName = result.getString("DOC_NAME");
                int TREATMENT_COUNT = result.getInt("TREATMENT_COUNT");

                stringBuilder.append(String.format("DoctorName: %s\n", DoctorName));
                stringBuilder.append(String.format("TREATMENT_COUNT: %s\n", TREATMENT_COUNT));
                stringBuilder.append("\n");
            }

            result.close();
            st.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return stringBuilder.length() == 0 ? null : stringBuilder.toString();
    }

    public String getAction4Result() {

        StringBuilder stringBuilder = new StringBuilder();

        String server = databaseCredentials.getUrl();
        String password = databaseCredentials.getPassword();
        String username = databaseCredentials.getUsername();

        try(Connection con = DriverManager.getConnection(server, username, password)){
            String query = "SELECT h.HOS_NAME, COUNT(*) AS PATIENT_COUNT " +
                    "FROM HOSPITALS h " +
                    "INNER JOIN TREATMENTSCHEDULES ts ON h.HOS_ID = ts.DOCTOR_ID " +
                    "INNER JOIN PATIENTS p ON ts.PAT_ID = p.PAT_ID " +
                    "GROUP BY h.HOS_NAME " +
                    "ORDER BY PATIENT_COUNT DESC ";

            PreparedStatement st=con.prepareStatement(query);

            ResultSet result=st.executeQuery();
            while(result.next())
            {
                String HOS_NAME = result.getString("HOS_NAME");
                int PATIENT_COUNT = result.getInt("PATIENT_COUNT");

                stringBuilder.append(String.format("Hospital Name: %s\n", HOS_NAME));
                stringBuilder.append(String.format("Patient COUNT: %s\n", PATIENT_COUNT));
                stringBuilder.append("\n");
            }

            result.close();
            st.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return stringBuilder.length() == 0 ? null : stringBuilder.toString();
    }

    public String getAction5Result() {

        StringBuilder stringBuilder = new StringBuilder();

        String server = databaseCredentials.getUrl();
        String password = databaseCredentials.getPassword();
        String username = databaseCredentials.getUsername();

        try(Connection con = DriverManager.getConnection(server, username, password)){
            String query = "SELECT HOS_NAME, COUNT(DOC_ID) AS NUM_DOCTORS " +
                    "FROM HOSPITALS NATURAL JOIN DOCTORS " +
                    "GROUP BY HOS_NAME " +
                    "HAVING COUNT(DOC_ID) > 3";

            PreparedStatement st=con.prepareStatement(query);

            ResultSet result=st.executeQuery();
            while(result.next())
            {
                String HOS_NAME = result.getString("HOS_NAME");
                int NUM_DOCTORS = result.getInt("NUM_DOCTORS");

                stringBuilder.append(String.format("Hospital Name: %s\n", HOS_NAME));
                stringBuilder.append(String.format("Doctor COUNT: %s\n", NUM_DOCTORS));
                stringBuilder.append("\n");
            }

            result.close();
            st.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return stringBuilder.length() == 0 ? null : stringBuilder.toString();
    }

    public String getAction6Result() {

        StringBuilder stringBuilder = new StringBuilder();

        String server = databaseCredentials.getUrl();
        String password = databaseCredentials.getPassword();
        String username = databaseCredentials.getUsername();

        try(Connection con = DriverManager.getConnection(server, username, password)){
            String query = "SELECT PAT_NAME, COUNT(*) AS NUM_TREATMENTS " +
                    "FROM PATIENTS " +
                    "JOIN TREATMENTSCHEDULES ON PATIENTS.PAT_ID = TREATMENTSCHEDULES.PAT_ID " +
                    "GROUP BY PAT_NAME " +
                    "ORDER BY NUM_TREATMENTS DESC ";

            PreparedStatement st=con.prepareStatement(query);

            ResultSet result=st.executeQuery();
            while(result.next())
            {
                String PAT_NAME = result.getString("PAT_NAME");
                int NUM_TREATMENTS = result.getInt("NUM_TREATMENTS");

                stringBuilder.append(String.format("Patient Name: %s\n", PAT_NAME));
                stringBuilder.append(String.format("Treatment COUNT: %s\n", NUM_TREATMENTS));
                stringBuilder.append("\n");
            }

            result.close();
            st.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return stringBuilder.length() == 0 ? null : stringBuilder.toString();
    }

    public String getAction7Result() {

        StringBuilder stringBuilder = new StringBuilder();

        String server = databaseCredentials.getUrl();
        String password = databaseCredentials.getPassword();
        String username = databaseCredentials.getUsername();

        try(Connection con = DriverManager.getConnection(server, username, password)){
            String query = "SELECT PAT_ID, PAT_NAME\n" +
                    "FROM PATIENTS\n" +
                    "WHERE PAT_ID IN (\n" +
                    "  SELECT PAT_ID\n" +
                    "  FROM TREATMENTSCHEDULES\n" +
                    "  WHERE DOCTOR_ID IN (\n" +
                    "    SELECT DOC_ID\n" +
                    "    FROM DOCTORS\n" +
                    "    WHERE HOS_ID IN (\n" +
                    "      SELECT HOS_ID\n" +
                    "      FROM HOSPITALS\n" +
                    "      WHERE HOS_TYPE = (\n" +
                    "        SELECT HOS_TYPE_ID\n" +
                    "        FROM HOSPITALTYPE\n" +
                    "        WHERE HOS_TYPE_NAME = 'General'\n" +
                    "      )\n" +
                    "    )\n" +
                    "  )\n" +
                    ")";

            PreparedStatement st=con.prepareStatement(query);

            ResultSet result=st.executeQuery();
            while(result.next())
            {
                String PAT_ID = result.getString("PAT_ID");
                String PAT_NAME = result.getString("PAT_NAME");

                stringBuilder.append(String.format("Patient Id: %s\n", PAT_ID));
                stringBuilder.append(String.format("Patient Name: %s\n", PAT_NAME));
                stringBuilder.append("\n");
            }

            result.close();
            st.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return stringBuilder.length() == 0 ? null : stringBuilder.toString();
    }

    public static List<QueryInform> getQueries(int action) {
        return List.of(
                new QueryInform(1, "Retrieve the names of all doctors along with their hospitals <br> and the hospital types they belong to",
                        "SELECT d.DOC_NAME, h.HOS_NAME, ht.HOS_TYPE_NAME <br>" +
                        "FROM DOCTORS d <br>" +
                        "INNER JOIN HOSPITALS h ON d.HOS_ID = h.HOS_ID <br>" +
                        "INNER JOIN HOSPITALTYPE ht ON h.HOS_TYPE = ht.HOS_TYPE_ID; <br>", 1 == action),

                new QueryInform(2, "Retrieve the names of doctors who have treated more <br> than one patient with their hospital name",
                        "SELECT d.DOC_NAME, h.hos_name <br>" +
                                "FROM DOCTORS d <br>" +
                                "INNER JOIN treatmentschedules t ON d.DOC_ID = t.doctor_id <br>" +
                                "INNER JOIN Hospitals h on d.hos_id = h.hos_id <br>" +
                                "GROUP BY d.DOC_NAME, h.hos_name <br>" +
                                "HAVING COUNT(DISTINCT t.PAT_ID) > 1;", 2 == action),

                new QueryInform(3, " Retrieve the number of treatments performed by each doctor",
                        "SELECT d.DOC_NAME, COUNT(*) AS TREATMENT_COUNT <br>" +
                                "FROM DOCTORS d <br>" +
                                "INNER JOIN TREATMENTS t ON d.DOC_ID = t.DOC_ID <br>" +
                                "GROUP BY d.DOC_NAME <br>" +
                                "ORDER BY TREATMENT_COUNT DESC", 3 == action),

                new QueryInform(4, "Retrieve the number of patients per hospital",
                        "SELECT h.HOS_NAME, COUNT(*) AS PATIENT_COUNT <br>" +
                                "FROM HOSPITALS h <br>" +
                                "INNER JOIN TREATMENTSCHEDULES ts ON h.HOS_ID = ts.DOCTOR_ID <br>" +
                                "INNER JOIN PATIENTS p ON ts.PAT_ID = p.PAT_ID <br>" +
                                "GROUP BY h.HOS_NAME <br>" +
                                "ORDER BY PATIENT_COUNT DESC;", 4 == action),

                new QueryInform(5, "Retrieves the names of hospitals that have more than 3 doctors,<br> along with the number of doctors in each hospital",
                        "SELECT HOS_NAME, COUNT(DOC_ID) AS NUM_DOCTORS <br>" +
                                "FROM HOSPITALS NATURAL JOIN DOCTORS <br>" +
                                "GROUP BY HOS_NAME <br>" +
                                "HAVING COUNT(DOC_ID) > 3", 5 == action),

                new QueryInform(6, "Query lists the number of treatments scheduled for each patient, <br> sorted by the number of treatments in descending order",
                        "SELECT PAT_NAME, COUNT(*) AS NUM_TREATMENTS <br>" +
                                "FROM PATIENTS <br>" +
                                "JOIN TREATMENTSCHEDULES ON PATIENTS.PAT_ID = TREATMENTSCHEDULES.PAT_ID <br>" +
                                "GROUP BY PAT_NAME <br>" +
                                "ORDER BY NUM_TREATMENTS DESC;", 6 == action),

                new QueryInform(7, "Query lists the number of treatments scheduled for each patient, <br> sorted by the number of treatments in descending order",
                        "SELECT PAT_ID, PAT_NAME <br>" +
                                "FROM PATIENTS <br>" +
                                "WHERE PAT_ID IN ( <br>" +
                                "  SELECT PAT_ID <br>" +
                                "  FROM TREATMENTSCHEDULES <br>" +
                                "  WHERE DOCTOR_ID IN ( <br>" +
                                "    SELECT DOC_ID <br>" +
                                "    FROM DOCTORS <br>" +
                                "    WHERE HOS_ID IN ( <br>" +
                                "      SELECT HOS_ID <br>" +
                                "      FROM HOSPITALS <br>" +
                                "      WHERE HOS_TYPE = ( <br>" +
                                "        SELECT HOS_TYPE_ID <br>" +
                                "        FROM HOSPITALTYPE <br>" +
                                "        WHERE HOS_TYPE_NAME = 'General'" +
                                "      ) <br>" +
                                "    ) <br>" +
                                "  ) <br>" +
                                ")", 7 == action)
        );
    }

    @Data
    @AllArgsConstructor
    static
    class QueryInform {
        private int id;
        private String title;
        private String query;
        private boolean selected;
    }
}
