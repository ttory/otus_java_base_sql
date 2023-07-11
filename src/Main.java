/*
 * Copyright 2023.
 * Student's project.
 * Author: Polyanskaya Victory.
 */

import db.DBConnector;
import tables.CuratorsTable;
import tables.GroupsTable;
import tables.StudentsTable;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    static final Path CURATORS_PATH = Paths.get("src/resources/Curators.txt");
    static final Path STUDENTS_PATH = Paths.get("src/resources/Students.txt");
    static final Path GROUPS_PATH = Paths.get("src/resources/Groups.txt");
    static Scanner scanner;

    public static void main(String[] args) {

        // Создано подключение к бд
        DBConnector connector = DBConnector.getInstance();
        connector.connect();

        //Создание и заполнение таблицы Curator(
        CuratorsTable curatorTable = new CuratorsTable();
        curatorTable.create();
        fillCurators(curatorTable);

        //Создание и заполнение таблицы Group
        GroupsTable groupsTable = new GroupsTable();
        groupsTable.create();
        fillGroupTable(groupsTable);

        //Создание и заполнение таблицы Student
        StudentsTable studentsTable = new StudentsTable();
        studentsTable.create();
        fillStudentsTable(studentsTable);

        //Вывод инфоримации о всех студентах включая название группы и имя куратора
        System.out.println("All students list");
        studentsTable.selectStudents();
        System.out.println();

        //Вывести на экран количество студентов
        studentsTable.printCountStudents();

        //Вывести студенток
        System.out.println("Female students");
        studentsTable.printFemaleStudents();

        //Обновить данные по группе сменив куратора
        System.out.println("Choose curator and group id:");
        curatorTable.print();
        groupsTable.print();
        scanner = new Scanner(System.in);
        System.out.println("Insert id group and new curator id:");
        try {
            int groupId = scanner.nextInt();
            scanner.nextLine();
            int curatorId = scanner.nextInt();
            groupsTable.updateCurator(groupId, curatorId);
            System.out.println("Groups table updated:");
            groupsTable.print();
        } catch (InputMismatchException e) {
            e.printStackTrace();
        } finally {
            scanner.nextLine();
        }

        //Вывести список групп с их кураторами
        System.out.println("Groups with curators:");
        groupsTable.selectGroups();

        //Используя вложенные запросы вывести на экран студентов из определенной группы(поиск по имени группы)
        System.out.println("Insert group name:");
        String groupName = scanner.nextLine();
        groupsTable.selectGroupByName(groupName);

        connector.close();
    }

    private static void fillGroupTable(GroupsTable groupsTable) {
        int rowsCounter = 0;
        try {
            scanner = new Scanner(GROUPS_PATH);

            System.out.println("Fill in Group table");
            while (scanner.hasNextLine()) {
                String[] groupsIn = scanner.nextLine().split(",");
                int groupId;
                int curatorId;
                try {
                    groupId = Integer.parseInt(groupsIn[0].trim());
                } catch (NumberFormatException ex) {
                    System.out.println("First parameter isn't int");
                    continue;
                }
                try {
                    curatorId = Integer.parseInt(groupsIn[2].trim());
                } catch (NumberFormatException ex) {
                    System.out.println("Last parameter isn't int");
                    continue;
                }
                if (groupsTable.fill(groupId, groupsIn[1].trim(), curatorId)) {
                    rowsCounter++;
                }
            }
            System.out.println("Added " + rowsCounter + " rows");
            scanner.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void fillStudentsTable(StudentsTable studentsTable) {
        int rowsCounter = 0;
        try {
            scanner = new Scanner(STUDENTS_PATH);
            System.out.println("Fill in Students table");
            while (scanner.hasNextLine()) {
                String[] studentsIn = scanner.nextLine().split(",");
                int studentId;
                int groupId;
                try {
                    studentId = Integer.parseInt(studentsIn[0].trim());
                } catch (NumberFormatException ex) {
                    System.out.println("First parameter isn't int");
                    continue;
                }
                try {
                    groupId = Integer.parseInt(studentsIn[3].trim());
                } catch (NumberFormatException ex) {
                    System.out.println("Last parameter isn't int");
                    continue;
                }
                if (studentsTable.fill(studentId, studentsIn[1].trim(), studentsIn[2].trim(), groupId)) {
                    rowsCounter++;
                }
            }
            System.out.println("Added " + rowsCounter + " rows");
            scanner.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void fillCurators(CuratorsTable curatorTable) {
        int rowsCounter = 0;

        try {
            scanner = new Scanner(CURATORS_PATH);

            System.out.println("Fill in Curators Table");
            while (scanner.hasNextLine()) {
                String[] curatorIn = scanner.nextLine().split(",");
                int curatorId;
                try {
                    curatorId = Integer.parseInt(curatorIn[0].trim());
                } catch (NumberFormatException ex) {
                    System.out.println("First parameter isn't int");
                    continue;
                }
                if (curatorTable.fill(curatorId, curatorIn[1].trim())) {
                    rowsCounter++;
                }
            }
            System.out.println("Added " + rowsCounter + " rows");
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
