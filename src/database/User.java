package database;

import Programm.Main;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class User {
    Scanner scanner = new Scanner(System.in);
    public String auth(Connection connection) throws SQLException, NoSuchAlgorithmException {

        Statement stat = connection.createStatement();
        String salt = Main.salt;
        System.out.println("Авторизация пользователя:");
        String message = "Введите 'login' для входа в учётную запись или 'register' для регистрации нового пользователя.\n" +
                "Для просмотра базы данных в качестве гостя введите 'guest'";
        System.out.println(message);
        label:
        while (scanner.hasNext()) {                                                                                      //авторизация
            String line = scanner.nextLine();
            switch (line.trim().toLowerCase(Locale.ROOT)) {
                case "login": {                                                 //login
                    System.out.println("Введите логин учётной записи");         //ввод логина login
                    String line1 = scanner.nextLine();
                    ResultSet resultSet = stat.executeQuery("SELECT * FROM users where name = '" + line1 + "';");
                    if (resultSet.next()){
                        String username = resultSet.getString(1);
                        System.out.println("Введите пароль учётной записи"); //ввод пароля login

                        line1 = scanner.nextLine();
                        MessageDigest md = MessageDigest.getInstance("SHA-512");
                        String password = Arrays.toString(md.digest((line1 + salt).getBytes(StandardCharsets.UTF_8)));

                        if (password.equals(resultSet.getString(2))) {
                            return username;
                        } else
                            System.out.println("Неверный пароль!\n" + message);
                    }
                     else System.out.println("Неверный логин!\n" + message);
                    break;
                }

                case "register": {                                                  //register
                    System.out.println("Введите логин новой учётной записи");

                    String login = scanner.nextLine();                              //ввод логина register

                    ResultSet resultSet = stat.executeQuery("SELECT * from users where name = '" + login + "';");
                    if (resultSet.next()){
                        System.out.println("Такой логин уже зарегистрирован\n" + message);
                    }
                    else {
                    if (!login.equals("")) {
                        System.out.println("Введите пароль новой учётной записи (1)");

                        String password1 = scanner.nextLine();                          //ввод пароля register (1)
                        if (!password1.equals("")) {
                            System.out.println("Введите пароль новой учётной записи (2)");

                            String password2 = scanner.nextLine();                      //ввод пароля register (2)

                            if (password1.equals(password2)) {
                                MessageDigest md = MessageDigest.getInstance("SHA-512");
                                String hash = Arrays.toString(md.digest(
                                        (password1 + salt).getBytes(StandardCharsets.UTF_8)));
                                String sqlRequest = "INSERT INTO users (name, password) VALUES (?, ?)";
                                executePrepareStatement(connection, sqlRequest, login, hash);
                                return login;
                            } else
                                System.out.println("Ошибка! Пароли не совпадают!\n" + message);
                        } else
                            System.out.println("Ошибка! Поле пароля не может быть пустым!\n" + message);
                    } else
                        System.out.println("Ошибка! Поле логина не может быть пустым!\n" + message);
                    }
                    break;
                }
                case "guest":                                                  //guest
                    break label;
                case "exit":                                                   //exit
                    System.exit(0);
            }
        }
        return "";
    }
    public ResultSet executePrepareStatement(Connection conn,String sqlRequest, String... values){
        try {
            PreparedStatement psmt = conn.prepareStatement(sqlRequest);
            for(int i = 0; i < values.length; i++){
                psmt.setString(i+1, values[i]);
            }
            return psmt.executeQuery();
        }catch (SQLException e) {
            //System.err.println("Ошибка выполнения запроса: " + e.getMessage());
            return null;
        }
    }
}
