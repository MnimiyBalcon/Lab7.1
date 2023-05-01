package Programm;

import Collection.*;
import Commands.Save;
import database.User;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

//psql -h pg studs
/**
 * Main-класс <br>
 * Осуществляет считывание команд с клавиатуры пользователя и обрабатывает их.
 * @version 1.0
 */

public class Main {
    /**
     * Переменная, содержащая информацию о дате создания коллекции
     */
    public static String timeStamp;
    /**
     * Содержит коллекцию
     */
    public static Hashtable<Integer, HumanBeing> collection = new Hashtable<>();

    /**Константа для парсинга ( = кол-во полей, требующихся для заполнения одного объекта) <br>
     * используется в методе printElements класса ElementsReader
     * @see ElementsReader*/
    public static final int CONST = 12+1;
    public static String username = "";
    public final static String salt = "5H'k@%!((n]";
    /**
     * Точка входа в программу.
     */

    private static Connection begin() throws SQLException {
        /*String user = "postgres";
        String password = "13131";
        String url = "jdbc:postgresql://localhost:5432/test_db";*/

        String user = "s367541";
        String password = "GSh8Yf9e2nAwBiYK";
        String url = "jdbc:postgresql://db:5432/studs";
        Connection connection = DriverManager.getConnection(url, user, password);
        try {
            Scanner pathReader = new Scanner(System.in);
            List<String[]> list = new ArrayList<>();
            Statement stat = connection.createStatement();
            User user1 = new User();


            try {
                username = user1.auth(connection); //Авторизация
            }
            catch (NoSuchAlgorithmException e){
                e.printStackTrace();
            }



            //Начинается чтение из базы данных

            ResultSet res1 = stat.executeQuery
                    ("SELECT * from HumanBeing;");
            while (res1.next()) {
                String[] stringList = new String[CONST];
                for (int i = 2; i <= CONST+1; i++) {
                    if (res1.getString(i) == null) {
                        stringList[i-2] = "";
                    } else {
                        stringList[i-2] = res1.getString(i);
                    }
                }
                list.add(stringList);
            }
            res1.close();

            //Инициализация коллекции

            if (list.size() > 0) {
                //Initialization.printList(list);
                collection = Initialization.initi(list);
            }

            timeStamp = new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss").format(Calendar.getInstance().getTime()); //дата создания коллекции
        }
        catch (SQLException exception){
            exception.printStackTrace();
        }
        return connection;
    }

    public static void main(String[] args) {
        try {
            Connection connection = begin();
            Scanner pathReader = new Scanner(System.in);
            System.out.println("Интерактивный режим включён. Введите \"help\" для получения информации по командам");

            while (pathReader.hasNextLine()) {
                String[] str = pathReader.nextLine().trim().split(" ");
                try {
                    if (str.length == 1) {
                        if (str[0].toLowerCase().equals("logout")){
                            collection.clear();
                            Initialization.idCounter.clear();
                            Initialization.keyCounter.clear();
                            begin();
                        }
                        else if (str[0].toLowerCase().equals("save")){
                            Save save = new Save();
                            save.setConnection(connection);
                            save.execute();
                        }
                        else Structure.commandList.get(str[0].toLowerCase()).execute();
                    } else if (str.length > 1) {
                        try {
                            Structure.commandList.get(str[0].toLowerCase()).setId(Integer.parseInt(str[1]));
                            Structure.commandList.get(str[0].toLowerCase()).setKey(Integer.parseInt(str[1]));
                        } catch (NumberFormatException e0) {
                            //e0.printStackTrace();
                        }
                        Structure.commandList.get(str[0].toLowerCase()).setName(str[1]);
                        if (str[0].equals("execute_script")) Structure.script_rec = 0;
                        Structure.commandList.get(str[0].toLowerCase()).execute();
                    } else
                        System.out.println("Команда введена неверно или такой команды не существует, попробуйте еще раз.");

                } catch (NullPointerException e) {
                    System.out.println("Ошибка! Проверьте правильность ввода.");
                } catch (NumberFormatException e1) {
                    System.out.println("Некорректно введены числовые данные. Попробуйте еще раз.");
                } finally {
                    System.out.print("Введите команду: ");
                }
            }
            pathReader.close(); //закрываем сканер
            connection.close(); //закрываем соединение
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}