package com.codecool.quest.store.controller;

import com.codecool.quest.store.model.Codecooler;

import java.sql.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class DbCodecoolerDAO implements CodecoolerDAO {

    private Connection connection;

    public DbCodecoolerDAO(Connection connection) {
        this.connection = connection;
    }

    private Codecooler extractCodecoolerFromResultSet(ResultSet resultSet) throws SQLException {
        Codecooler codecooler = new Codecooler();
        codecooler.setId(resultSet.getInt("id"));
        codecooler.setFirstName(resultSet.getString("first_name"));
        codecooler.setLastName(resultSet.getString("last_name"));
        codecooler.setEmail(resultSet.getString("email"));
        codecooler.setPassword(resultSet.getString("password"));
        codecooler.setClassId(resultSet.getInt("class_id"));
        codecooler.setExp(resultSet.getInt("exp"));
        codecooler.setBalance(resultSet.getInt("balance"));
        codecooler.setTeamId(resultSet.getInt("team_id"));
        return codecooler;
    }

    @Override
    public Set<Codecooler> getAllCodecoolers() {
        String sql = "SELECT * FROM codecoolers;";
        Set<Codecooler> codecoolers = new HashSet<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                codecoolers.add(extractCodecoolerFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return codecoolers;
    }

    @Override
    public Codecooler getCodecoolerById(int id) {
        String sql = "SELECT * FROM codecoolers WHERE id=?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return extractCodecoolerFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Set<Codecooler> getCodecoolersByClassId(int classId) {
        String sql = "SELECT * FROM codecoolers WHERE class_id=?;";
        Set<Codecooler> codecoolers = new HashSet<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, classId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                codecoolers.add(extractCodecoolerFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return codecoolers;
    }

    @Override
    public boolean addCodecooler(Codecooler codecooler) {
        String sql = "INSERT INTO codecoolers (first_name, last_name, email, password) VALUES (?, ?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, codecooler.getFirstName());
            statement.setString(2, codecooler.getLastName());
            statement.setString(3, codecooler.getEmail());
            statement.setString(4, codecooler.getPassword());
            int i = statement.executeUpdate();
            if (i == 1)
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateCodecooler(Codecooler codecooler) {
        String sql = "UPDATE codecoolers SET first_name = ?, last_name = ?, email = ?, password = ?, " +
                "class_id = ?, exp = ?, balance = ?, team_id = ? WHERE id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, codecooler.getFirstName());
            statement.setString(2, codecooler.getLastName());
            statement.setString(3, codecooler.getEmail());
            statement.setString(4, codecooler.getPassword());
            int classId = codecooler.getClassId();
            if (classId == 0) {
                statement.setNull(5, Types.INTEGER);
            } else {
                statement.setInt(5, classId);
            }
            statement.setInt(6, codecooler.getExp());
            statement.setInt(7, codecooler.getBalance());
            int teamId = codecooler.getTeamId();
            if (teamId == 0) {
                statement.setNull(8, Types.INTEGER);
            } else {
                statement.setInt(8, teamId);
            }
            statement.setInt(9, codecooler.getId());
            int i = statement.executeUpdate();
            if (i == 1)
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteCodecooler(Codecooler codecooler) {
        String sql = "DELETE FROM codecoolers WHERE id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, codecooler.getId());
            int i = statement.executeUpdate();
            if (i == 1)
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // main for testing
    public static void main(String[] args){
        final String URL = "jdbc:postgresql://localhost:5432/queststore";
        final String USER = "creepyguy";
        final String PASS = "magic";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DbCodecoolerDAO dao = new DbCodecoolerDAO(connection);

        boolean isRunning = true;
        Scanner scanner = new Scanner(System.in);

        while (isRunning) {
            System.out.println("1. list all codecoolers\n2. find codecooler by id\n3. find codecooler by class id"
                    + "\n4. add codecooler\n5. edit codecooler\n6. delete codecooler\n0. exit");
            System.out.print("Enter option: ");
            int option = scanner.nextInt();
            scanner.nextLine();
            System.out.println();

            switch (option) {
                case 0:
                    isRunning = false;
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    dao.getAllCodecoolers().forEach(System.out::println);
                    System.out.println("\npress enter...");
                    scanner.nextLine();
                    break;
                case 2:
                    System.out.print("Enter id: ");
                    int id = scanner.nextInt();
                    System.out.println();
                    System.out.println(dao.getCodecoolerById(id));
                    System.out.println("\npress enter...");
                    scanner.nextLine();
                    break;
                case 3:
                    System.out.print("Enter id: ");
                    int classId = scanner.nextInt();
                    System.out.println();
                    dao.getCodecoolersByClassId(classId).forEach(System.out::println);
                    System.out.println("\npress enter...");
                    scanner.nextLine();
                    break;
                case 4:
                    System.out.print("Enter first name: ");
                    String firstName = scanner.nextLine();
                    System.out.print("Enter last name: ");
                    String lastName = scanner.nextLine();
                    System.out.print("Enter email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    Codecooler codecooler = new Codecooler();
                    codecooler.setFirstName(firstName);
                    codecooler.setLastName(lastName);
                    codecooler.setEmail(email);
                    codecooler.setPassword(password);
                    if(dao.addCodecooler(codecooler)){
                        System.out.println("Codecooler added successfully");
                    } else
                        System.out.println("Could not add codecooler");
                    System.out.println("\npress enter...");
                    scanner.nextLine();
                    break;
                case 5:
                    System.out.print("Enter id: ");
                    int id1 = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println();
                    Codecooler codecooler1 = dao.getCodecoolerById(id1);
                    System.out.print("Enter new first name: ");
                    String newFirstName = scanner.nextLine();
                    codecooler1.setFirstName(newFirstName);
                    if (dao.updateCodecooler(codecooler1)) {
                        System.out.println("Codecooler updated successfully");
                    } else
                        System.out.println("Could not update codecooler");
                    System.out.println("\npress enter...");
                    scanner.nextLine();
                    break;
                case 6:
                    System.out.print("Enter id: ");
                    int id2 = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println();
                    Codecooler codecooler2 = dao.getCodecoolerById(id2);
                    if (dao.deleteCodecooler(codecooler2)) {
                        System.out.println("Codecooler deleted successfully");
                    } else
                        System.out.println("Could not delete codecooler");
                    System.out.println("\npress enter...");
                    scanner.nextLine();
                    break;

            }
        }
    }
}
