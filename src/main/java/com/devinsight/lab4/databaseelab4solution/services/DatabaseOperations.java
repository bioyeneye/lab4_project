package com.devinsight.lab4.databaseelab4solution.services;

import com.devinsight.lab4.databaseelab4solution.DatabaseCredentials;
import com.devinsight.lab4.databaseelab4solution.models.Doctor;
import com.devinsight.lab4.databaseelab4solution.models.Hospital;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseOperations {

    private final DatabaseCredentials databaseCredentials;

    public DatabaseOperations(DatabaseCredentials databaseCredentials) throws ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        this.databaseCredentials = databaseCredentials;
    }

    public List<Doctor> getDoctors() {
        List<Doctor> doctors = new ArrayList<>();

        String server = databaseCredentials.getUrl();
        String username = databaseCredentials.getUsername();
        String password = databaseCredentials.getPassword();

        try(Connection con = DriverManager.getConnection(server, username, password)){

            String query = "SELECT d.*, h.hos_name " +
                    "FROM ORA_BIO101.DOCTORS d " +
                    "JOIN ORA_BIO101.HOSPITALS h ON d.hos_id = h.hos_id " +
                    "order by d.doc_id desc";

            PreparedStatement st=con.prepareStatement(query);

            ResultSet rs=st.executeQuery();

            while (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setId(rs.getInt("doc_id"));
                doctor.setName(rs.getString("doc_name"));
                doctor.setAddress(rs.getString("doc_address"));
                doctor.setContactNo(rs.getString("doc_contact_no"));
                doctor.setEmail(rs.getString("doc_email"));

                Hospital hospital = new Hospital();
                hospital.setId(rs.getInt("hos_id"));
                hospital.setName(rs.getString("hos_name"));

                doctor.setHospitalId(hospital.getId());
                doctor.setHospital(hospital);

                doctors.add(doctor);
            }

            rs.close();
            st.close();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return doctors;
    }

    public Doctor getDoctorById(int id) {
        Doctor doctor = null;

        String server = databaseCredentials.getUrl();
        String username = databaseCredentials.getUsername();
        String password = databaseCredentials.getPassword();

        try(Connection con = DriverManager.getConnection(server, username, password)){

            String query = "SELECT d.*, h.hos_name " +
                    "FROM ORA_BIO101.DOCTORS d " +
                    "JOIN ORA_BIO101.HOSPITALS h ON d.hos_id = h.hos_id " +
                    "WHERE d.DOC_ID = ?";

            PreparedStatement st=con.prepareStatement(query);
            st.setInt(1, id);

            ResultSet rs=st.executeQuery();

            if (rs.next()) {
                doctor = new Doctor();
                doctor.setId(rs.getInt("doc_id"));
                doctor.setName(rs.getString("doc_name"));
                doctor.setAddress(rs.getString("doc_address"));
                doctor.setContactNo(rs.getString("doc_contact_no"));
                doctor.setEmail(rs.getString("doc_email"));

                Hospital hospital = new Hospital();
                hospital.setId(rs.getInt("hos_id"));
                hospital.setName(rs.getString("hos_name"));

                doctor.setHospitalId(hospital.getId());
                doctor.setHospital(hospital);

            }

            rs.close();
            st.close();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return doctor;
    }

    public void deleteDoctor(int id) {

        String server = databaseCredentials.getUrl();
        String username = databaseCredentials.getUsername();
        String password = databaseCredentials.getPassword();

        try(Connection con = DriverManager.getConnection(server, username, password)){
            String sql = "DELETE FROM ORA_BIO101.DOCTORS WHERE DOC_ID = ?";

            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, id);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Doctor record with ID = " + id + " has been deleted.");
            } else {
                System.out.println("No doctor record found with ID = " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Hospital> getHospitals() {
        List<Hospital> hospitals = new ArrayList<>();

        String server = databaseCredentials.getUrl();
        String username = databaseCredentials.getUsername();
        String password = databaseCredentials.getPassword();

        try(Connection con = DriverManager.getConnection(server, username, password)){

            String query = "select hos_id, hos_name from Hospitals";

            PreparedStatement st=con.prepareStatement(query);

            ResultSet rs=st.executeQuery();

            while (rs.next()) {
                Hospital hospital = new Hospital();
                hospital.setId(rs.getInt("hos_id"));
                hospital.setName(rs.getString("hos_name"));
                hospitals.add(hospital);
            }

            rs.close();
            st.close();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return hospitals;
    }

    public void saveDoctor(Doctor doctor) {
        String server = databaseCredentials.getUrl();
        String username = databaseCredentials.getUsername();
        String password = databaseCredentials.getPassword();

        try(Connection con = DriverManager.getConnection(server, username, password)){
            String sql = "INSERT INTO ORA_BIO101.DOCTORS (hos_id, doc_name, doc_address, doc_contact_no, doc_email) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, doctor.getHospitalId());
            stmt.setString(2, doctor.getName());
            stmt.setString(3, doctor.getAddress());
            stmt.setString(4, doctor.getContactNo());
            stmt.setString(5, doctor.getEmail());

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new doctor record has been inserted.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDoctor(Doctor doctor) {
        String server = databaseCredentials.getUrl();
        String username = databaseCredentials.getUsername();
        String password = databaseCredentials.getPassword();

        try(Connection con = DriverManager.getConnection(server, username, password)){
            String sql = "UPDATE ORA_BIO101.DOCTORS SET HOS_ID=?, DOC_NAME=?, DOC_ADDRESS=?, DOC_CONTACT_NO=?, DOC_EMAIL=? " +
                    "WHERE DOC_ID=?";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, doctor.getHospitalId());
            stmt.setString(2, doctor.getName());
            stmt.setString(3, doctor.getAddress());
            stmt.setString(4, doctor.getContactNo());
            stmt.setString(5, doctor.getEmail());
            stmt.setInt(6, doctor.getId());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Doctor record with ID = " + doctor.getId() + " has been updated.");
            } else {
                System.out.println("No doctor record found with ID = " + doctor.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}