package com.kiet.studentmanagement;

import java.sql.*;

public class StudentJDBC {

    public static void main(String[] args) {

        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/student_management",
                    "root",
                    "root"
            );

            Statement st = con.createStatement();

            String query = "SELECT student_id, student_name, " +
                    "programming_score, mathematics_score FROM students";

            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {

                double programming = rs.getDouble("programming_score");
                double mathematics = rs.getDouble("mathematics_score");

                double average = (programming + mathematics) / 2;

                String category;

                if (average >= 85)
                    category = "Excellent";
                else if (average >= 70)
                    category = "Good";
                else if (average >= 50)
                    category = "Satisfactory";
                else
                    category = "Needs Improvement";

                String stronger;

                if (programming > mathematics)
                    stronger = "Programming";
                else if (mathematics > programming)
                    stronger = "Mathematics";
                else
                    stronger = "Both Equal";

                System.out.println("ID: " + rs.getInt("student_id"));
                System.out.println("Name: " + rs.getString("student_name"));
                System.out.println("Average: " + average);
                System.out.println("Category: " + category);
                System.out.println("Stronger Subject: " + stronger);
                System.out.println("----------------------");
            }
            String meritQuery = "SELECT student_id, student_name, " +
                    "((programming_score + mathematics_score)/2) AS average " +
                    "FROM students ORDER BY average DESC";

            ResultSet merit = st.executeQuery(meritQuery);

            System.out.println("MERIT LIST");

            while (merit.next()) {
                System.out.println(
                        merit.getInt("student_id") + " - " +
                                merit.getString("student_name") + " - " +
                                merit.getDouble("average")
                );
            }
            // Search by Student ID
            PreparedStatement ps1 = con.prepareStatement(
                    "SELECT * FROM students WHERE student_id = ?");
            ps1.setInt(1, 101);
            ResultSet r1 = ps1.executeQuery();

            while (r1.next()) {
                System.out.println("Search by ID: " + r1.getString("student_name"));
            }

// Search by Email
            PreparedStatement ps2 = con.prepareStatement(
                    "SELECT * FROM students WHERE email = ?");
            ps2.setString(1, "riya@gmail.com");
            ResultSet r2 = ps2.executeQuery();

            while (r2.next()) {
                System.out.println("Search by Email: " + r2.getString("student_name"));
            }

// Search by Course
            PreparedStatement ps3 = con.prepareStatement(
                    "SELECT * FROM students WHERE course_name = ?");
            ps3.setString(1, "CSE");
            ResultSet r3 = ps3.executeQuery();

            while (r3.next()) {
                System.out.println("Search by Course: " + r3.getString("student_name"));
            }
            String skillQuery = "SELECT skill, GROUP_CONCAT(student_id) AS students " +
                    "FROM student_skills GROUP BY skill HAVING COUNT(DISTINCT student_id) > 1";

            ResultSet skillResult = st.executeQuery(skillQuery);

            System.out.println("COMMON TECHNICAL SKILLS");

            while (skillResult.next()) {
                System.out.println(
                        skillResult.getString("skill") + " : " +
                                skillResult.getString("students")
                );
            }
            String improveQuery = "SELECT student_id, student_name, " +
                    "programming_score, mathematics_score FROM students " +
                    "WHERE programming_score < mathematics_score " +
                    "AND mathematics_score >= 75";

            ResultSet improve = st.executeQuery(improveQuery);

            System.out.println("PROGRAMMING IMPROVEMENT REQUIRED");

            while (improve.next()) {
                System.out.println(
                        improve.getInt("student_id") + " - " +
                                improve.getString("student_name")
                );
            }
            // Duplicate ID Validation
            PreparedStatement check = con.prepareStatement(
                    "SELECT COUNT(*) FROM students WHERE student_id = ?");
            check.setInt(1, 101);

            ResultSet duplicate = check.executeQuery();

            if (duplicate.next() && duplicate.getInt(1) > 0) {
                System.out.println("Duplicate Student ID Not Allowed");
            }

// Score Validation
            double score = 105;

            if (score < 0 || score > 100) {
                System.out.println("Invalid Score! Score must be between 0 and 100");
            } else {
                System.out.println("Valid Score");
            }
            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}