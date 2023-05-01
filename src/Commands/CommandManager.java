package Commands;

import Collection.*;
import Programm.*;
import database.User;

import java.sql.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * Менеджер команд <br>
 * Содержит информацию о работе всех команд и реализует их вызов.
 */

public class CommandManager {
     Hashtable<Integer, HumanBeing> collection = Main.collection; //здесь хранится коллекция
     private final int commandConst = 9; //количество команд в истории
     private HumanBeing buffer = new HumanBeing();
     private boolean updateFlag = false; //вспомогательная переменная для реализации метода update

/** Реализует команду "help" */
    public void help(){
        System.out.println("Справка по доступным командам:\n" +
                " > help : вывести справку по доступным командам\n" +
                " > info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                " > show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                " > insert null {element} : добавить новый элемент с заданным ключом\n" +
                "После вызова команды все новые поля объекта вводятся с новой строки в соответствии с получаемой инструкцией\n" +
                " > update id {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
                "После вызова команды все новые поля объекта вводятся с новой строки в соответствии с получаемой инструкцией\n" +
                " > remove_key null : удалить элемент из коллекции по его ключу\n" +
                " > clear : очистить коллекцию\n" +
                " > save {name}: сохранить коллекцию в файл\n" +
                " > execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
                " > exit : завершить программу (без сохранения в файл)\n" +
                " > remove_greater {id} : удалить из коллекции все элементы, превышающие заданный\n" +
                " > history : вывести последние 6 команд (без их аргументов)\n" +
                " > remove_lower_key null : удалить из коллекции все элементы, ключ которых меньше, чем заданный\n" +
                " > filter_contains_name name : вывести элементы, значение поля name которых содержит заданную подстроку\n" +
                " > print_unique_soundtrack_name : вывести уникальные значения поля soundtrackName всех элементов в коллекции\n" +
                " > print_field_descending_minutes_of_waiting : вывести значения поля minutesOfWaiting всех элементов в порядке убывания\n" +
                " > logout : выйти из учётной записи (без сохранения коллекции)\n");

         Structure.commandHistory[Structure.commandCounter%commandConst] = "help";
         Structure.commandCounter++;
    }
    /** Реализует команду "info" */
    public void info(){
        collection = Main.collection;
                System.out.println("В коллекции типа HashTable имеется " + collection.size() + " объектов.");
                System.out.println("Дата создания коллекции: " + Main.timeStamp);
                Structure.commandHistory[Structure.commandCounter%commandConst] = "info";
                Structure.commandCounter++;
        }
    /** Реализует команду "show" */
        public void show() {
            collection = Main.collection;
            System.out.println("В коллекции содержатся следующие объекты:");
            for (Integer keys : Initialization.keyCounter) { //сортирует элементы по key
                System.out.println("key = " + keys + ": " + collection.get(keys).toString());
            }
            Structure.commandHistory[Structure.commandCounter % commandConst] = "show";
            Structure.commandCounter++;
        }
    /** Реализует команду "insert"
     * @param key - ключ, который будет присвоен новому элементу коллекции*/
    void insert(int key) {
        collection = Main.collection;
        boolean isExist = !Initialization.keyCounter.add(key);
        HumanBeing humanBeing = new HumanBeing();
            Coordinates coordinates = new Coordinates();
            Car car = new Car();
            Scanner scanner = new Scanner(System.in);
            System.out.println("Для выхода из команды введите \"exit\"");
            String line="";
            for (int i = 0; i < Main.CONST-1; i++) {
                    if (line.equals("exit")) break;
                    switch (i) {
                        case 0: //name
                            System.out.println("Введите поле name");
                            line = scanner.nextLine();
                            if (line.equals("exit")) break;
                            try {
                                humanBeing.setName(line.trim());
                            } catch (Exception e) {
                                System.out.println("Некорректно введены данные. Поле не может быть пустым");
                                i--;
                            }
                            break;
                        case 1: //Coordinates_x
                            System.out.println("Введите поле Coordinates_x");
                            line = scanner.nextLine();
                            if (line.equals("exit")) break;
                            try {
                                coordinates.setX(Float.parseFloat(line.trim()));
                            } catch (NumberFormatException e) {
                                System.out.println("Некорректно введены данные. Поле не может быть пустым или не являться числом");
                                i--;
                            }
                            catch (NullPointerException e) {
                                System.out.println("Ошибка! Значение поля не может быть больше 546!");
                                i--;
                            }
                            break;

                        case 2: //Coordinates_y
                            System.out.println("Введите поле Coordinates_y");
                            line = scanner.nextLine();
                            if (line.equals("exit")) break;
                            try {
                                coordinates.setY(Float.parseFloat(line));
                                humanBeing.setCoordinates(coordinates);
                            } catch (NumberFormatException e) {
                                System.out.println("Некорректно введены данные. Поле не может быть пустым или не являться числом");
                                i--;
                            }
                            catch (NullPointerException e) {
                                System.out.println("Ошибка! Значение поля не может быть больше 287!");
                                i--;
                            }
                            break;
                        case 3: //realHero
                            System.out.println("Введите поле realHero");
                            line = scanner.nextLine();
                            if (line.equals("exit")) break;
                            try {
                                if (line.equals("1") || line.toLowerCase().equals("true")) {   //realHero
                                    humanBeing.setRealHero(true);
                                } else if (line.equals("0") || line.toLowerCase().equals("false")) {
                                    humanBeing.setRealHero(false);
                                }
                                else if (line.trim().equals("")){
                                }
                                else throw new IllegalArgumentException();
                            } catch (Exception e) {
                                System.out.println("Некорректно введены данные. Формат ввода логических переменных: true/false, 1/0.\n" +
                                        "Данное поле может быть пустым.");
                                i--;
                            } break;
                        case 4: //hasToothpick
                            System.out.println("Введите поле hasToothpick");
                            line = scanner.nextLine();
                            if (line.equals("exit")) break;
                            try {
                                if (line.equals("1") || line.toLowerCase().equals("true")) {   //realHero
                                    humanBeing.setHasToothpick(true);
                                } else if (line.equals("0") || line.toLowerCase().equals("false")) {
                                    humanBeing.setHasToothpick(false);
                                } else {
                                    throw new IllegalArgumentException();
                                }
                            } catch (Exception e) {
                                System.out.println("Некорректно введены данные. Формат ввода логических переменных: true/false, 1/0");
                                i--;
                            } break;
                        case 5: //impactSpeed
                            System.out.println("Введите поле impactSpeed");
                            line = scanner.nextLine();
                            if (line.equals("exit")) break;
                            try {
                                humanBeing.setImpactSpeed(Double.parseDouble(line.trim()));
                            } catch (NumberFormatException e) {
                                System.out.println("Некорректно введены данные. Поле не может быть пустым или не являться числом");
                                i--;
                            }
                            catch (NullPointerException e) {
                                System.out.println("Ошибка! Значение поля не может быть больше 995!");
                                i--;
                            } break;
                        case 6: //soundtrackName
                            System.out.println("Введите поле soundtrackName");
                            line = scanner.nextLine();
                            if (line.equals("exit")) break;
                            try {
                                humanBeing.setSoundtrackName(line.trim());
                            } catch (Exception e) {
                                System.out.println("Некорректно введены данные. Поле не может быть пустым");
                                i--;
                            } break;
                        case 7: //minutesOfWaiting
                            System.out.println("Введите поле minutesOfWaiting");
                            line = scanner.nextLine();
                            if (line.equals("exit")) break;
                            try {
                                if (!(line.trim().equals("")))
                                humanBeing.setMinutesOfWaiting(Long.parseLong(line.trim()));
                            } catch (Exception e) {
                                System.out.println("Некорректно введены данные. Формат поля должен быть числовой." +
                                        "Поле может быть пустым.");
                                i--;
                            }
                            break;
                        case 8: //mood
                            System.out.println("Введите поле mood. Для ввода доступны:\n -SORROW\n -CALM\n -LONGING\n -RAGE\n" +
                                    "Поле может быть пустым");
                            line = scanner.nextLine();
                            if (line.equals("exit")) break;
                            try {
                                Mood mood1 = null;                                              //mood
                                boolean flag=false;
                                switch (line.toUpperCase()) {
                                    case "LONGING":
                                        mood1 = Mood.LONGING;
                                        flag=true;
                                    case "SORROW":
                                        mood1 = Mood.SORROW;
                                        flag=true;
                                        break;
                                    case "CALM":
                                        mood1 = Mood.CALM;
                                        flag=true;
                                        break;
                                    case "RAGE":
                                        mood1 = Mood.RAGE;
                                        flag=true;
                                        break;
                                }
                                if (flag){
                                    humanBeing.setMood(mood1);
                                }
                                else if (line.toUpperCase().equals("")){}
                                else {
                                    throw new IllegalArgumentException();
                                }

                            } catch (IllegalArgumentException e) {
                                System.out.println("Некорректный ввод!");
                                i--;
                            } break;
                        case 9: //car_name
                            System.out.println("Введите поле car_name");
                            line = scanner.nextLine();
                            if (line.equals("exit")) break;
                            try {
                                car.setName(line.trim());
                            } catch (Exception e) {
                                System.out.println("Некорректно введены данные. Поле не может быть пустым");
                                i--;
                            }break;
                        case 10: //car_cool
                            System.out.println("Введите поле car_cool");
                            line = scanner.nextLine();
                            if (line.equals("exit")) break;
                            try {
                                if (line.equals("1") || line.toLowerCase().equals("true")) {   //realHero
                                    car.setCool(true);
                                } else if (line.equals("0") || line.toLowerCase().equals("false")) {
                                    car.setCool(false);
                                } else {
                                    throw new IllegalArgumentException();
                                }
                                humanBeing.setCar(car);
                            } catch (Exception e) {
                                System.out.println("Некорректно введены данные. Формат ввода логических переменных: true/false, 1/0");
                                i--;
                            }
                            break;
                    }
                }
            try {
                humanBeing.setCreationDate(ZonedDateTime.now()); //дата создания коллекции
            }
            catch (Exception e1){
                e1.printStackTrace();
            }

            if (line.equals("exit")) {
                System.out.println("Выполнение команды было прервано");
                if (updateFlag){
                    updateFlag = false;
                    collection.put(key, buffer);
                    Initialization.idCounter.add(collection.get(key).getId());
                }
                else if(!isExist) Initialization.keyCounter.remove(key);
            }
            else {
                int id=1;
                if (!Initialization.idCounter.add(id)){
                    id = Initialization.idCounter.last() + 1;
                    Initialization.idCounter.add(id);
                }
                if(isExist){
                    Integer[] arrayOfKeys = Initialization.keyCounter.toArray(new Integer[0]);
                    for(int i=arrayOfKeys.length-1; i>=0; i--){
                        if (arrayOfKeys[i]>=key){
                                if (i!=arrayOfKeys.length-1){
                                    collection.replace((arrayOfKeys[i+1]), collection.get(arrayOfKeys[i]));
                                }
                                else{
                                    collection.put(arrayOfKeys[i]+1,collection.get(arrayOfKeys[i]));
                                    Initialization.keyCounter.add(arrayOfKeys[i]+1);
                                }
                        }
                    }
                    collection.remove(key);
                }
                humanBeing.setId(id);
                humanBeing.setOwner(Main.username);
                Initialization.keyCounter.remove(key);
                if (Initialization.keyCounter.isEmpty()){
                    key = 1;
                    Initialization.keyCounter.add(key);
                    collection.put(key, humanBeing);
                }
                else {
                    if (key>Initialization.keyCounter.last()) key = Initialization.keyCounter.last()+1;
                    Initialization.keyCounter.add(key);
                    collection.put(key, humanBeing);
                }
            }
        Structure.commandHistory[Structure.commandCounter%commandConst] = "insert";
        Structure.commandCounter++;
    }
    /** Реализует команду "update"
     * @param id - id элемента коллекции, который требуется обновить*/
    public void update(int id){
        collection = Main.collection;
        int key = 0;
        for (Integer keys: Initialization.keyCounter){
            if (collection.get(keys).getId() == id){
                key = keys;
                break;
            }
        }
        if (key!=0){
            if (collection.get(key).getOwner().equals(Main.username)){
                updateFlag = true;
                buffer = collection.get(key);
                Initialization.idCounter.remove(collection.get(key).getId());
                collection.remove(key);
                Initialization.keyCounter.remove(key);
                insert(key);

                Structure.commandHistory[Structure.commandCounter%commandConst] = "update";
                Structure.commandCounter++;
            }
            else System.out.println("Ошибка! Этот объект принадлежит другому пользователю");
        }
        else System.out.println("В коллекции нет элемента с указанным id");
    }
    /** Реализует команду "remove_key"
     * @param key - ключ элемента коллекции, по которому ведётся поиск*/
    public void remove_key(int key){
        collection = Main.collection;
        Integer[] arrayOfKeys = Initialization.keyCounter.toArray(new Integer[0]);
        int j = 0;
        try{
            if (collection.get(key).getOwner().equals(Main.username)) {
                Initialization.idCounter.remove(collection.get(key).getId());
                for (Integer KEY : arrayOfKeys) {
                    if (KEY > key) {
                        collection.replace((arrayOfKeys[j - 1]), collection.get(KEY));
                    }
                    j++;
                }
                collection.remove(Initialization.keyCounter.last());
                Initialization.keyCounter.remove(Initialization.keyCounter.last());
                Structure.commandHistory[Structure.commandCounter % commandConst] = "remove_key";
                Structure.commandCounter++;
            }
            else System.out.println("Ошибка! Этот объект принадлежит другому пользователю");
        }
        catch (NullPointerException e) {
            System.out.println("В коллекции нет элемента с таким ключом");
        }
    }
    /** Реализует команду "clear" */
    public void clear() {
        collection = Main.collection;
        List<HumanBeing> newCollection = new ArrayList<>();
        List<Integer> keysToDelete = new ArrayList<>();
        for (Integer key : Initialization.keyCounter) {
            if (collection.get(key).getOwner().equals(Main.username)) {
                keysToDelete.add(key);
            }
        }
        for (Integer integer : keysToDelete) {
            Initialization.idCounter.remove(collection.remove(integer).getId());
            collection.remove(integer);
            Initialization.keyCounter.remove(integer);
        }
        for (Integer key : Initialization.keyCounter) {
            newCollection.add(collection.get(key));
        }
        int size = Initialization.keyCounter.size();
        Initialization.keyCounter.clear();
        collection.clear();
        for (int i = 1; i <= size; i++) {
            collection.put(i,newCollection.get(i-1));
            Initialization.keyCounter.add(i);
        }

        Structure.commandHistory[Structure.commandCounter % commandConst] = "clear";
        Structure.commandCounter++;
    }
    /** Реализует команду "exit" */
    public void exit(){
        collection = Main.collection;
        Structure.commandHistory[Structure.commandCounter%commandConst] = "exit";
        Structure.commandCounter++;
        System.exit(0);
    }
    /** Реализует команду "remove_greater"
     * @param ID - id элемента коллекции, по которому ведётся поиск*/
    public void remove_greater(int ID){
        List<HumanBeing> newCollection = new ArrayList<>();
        List<Integer> keys = new ArrayList<>();
        collection = Main.collection;
        for (Integer currentKey: Initialization.keyCounter){
            if (Main.collection.get(currentKey).getId() > ID && collection.get(currentKey).getOwner().equals(Main.username)){
                Initialization.idCounter.remove(collection.get(currentKey).getId());
                Main.collection.remove(currentKey);
                keys.add(currentKey);
            }
        }
        for (Integer key:
                keys) {
            Initialization.keyCounter.remove(key);
        }
        for (Integer key : Initialization.keyCounter) {
            newCollection.add(collection.get(key));
        }
        int size = Initialization.keyCounter.size();
        Initialization.keyCounter.clear();
        collection.clear();
        for (int i = 1; i <= size; i++) {
            collection.put(i,newCollection.get(i-1));
            Initialization.keyCounter.add(i);
        }
        Structure.commandHistory[Structure.commandCounter%commandConst] = "remove_greater";
        Structure.commandCounter++;
    }
    /** Реализует команду "history" */
    public void history(){
        System.out.println("Список последних 9 команд: ");
        for (int i=0; i<commandConst; i++){
            System.out.println(Structure.commandHistory[i]);
        }
        Structure.commandHistory[Structure.commandCounter%commandConst] = "history";
        Structure.commandCounter++;
    }
    /** Реализует команду "remove_lower_key"
     * @param key - ключ элемента коллекции, по которому ведётся поиск*/
    void remove_lower_key(int key){
        collection = Main.collection;
        List<Integer> keysToDelete = new ArrayList<>();
        List<HumanBeing> newCollection = new ArrayList<>();
        for (Integer currentKey : Initialization.keyCounter) {
            if (currentKey < key && collection.get(currentKey).getOwner().equals(Main.username)){
                keysToDelete.add(currentKey);
            }
        }
        for (Integer currentKey : keysToDelete) {
            Initialization.idCounter.remove(collection.remove(currentKey).getId());
            collection.remove(currentKey);
            Initialization.keyCounter.remove(currentKey);
        }
        for (Integer key1 : Initialization.keyCounter) {
            newCollection.add(collection.get(key1));
        }
        int size = Initialization.keyCounter.size();
        Initialization.keyCounter.clear();
        collection.clear();
        for (int i = 1; i <= size; i++) {
            collection.put(i,newCollection.get(i-1));
            Initialization.keyCounter.add(i);
        }

        Structure.commandHistory[Structure.commandCounter%commandConst] = "remove_lower_key";
        Structure.commandCounter++;
    }
    /** Реализует команду "filter_contains_name"
     * @param name - имя элемента коллекции, по которому ведётся поиск*/
    void filter_contains_name(String name){
        collection = Main.collection;
        for (Integer keys: Initialization.keyCounter){
            if (collection.get(keys).getName().contains(name)){
                System.out.println(collection.get(keys));
            }
        }
        Structure.commandHistory[Structure.commandCounter%commandConst] = "filter_contains_name";
        Structure.commandCounter++;
    }
    /** Реализует команду "print_unique_soundtrack_name" */
    void print_unique_soundtrack_name(){
        collection = Main.collection;
        TreeSet<String> uniqueSoundtrackNames = new TreeSet<>();
        System.out.println("uniqueSoundtrackNames: ");
        for (Integer keys: Initialization.keyCounter){
            if (uniqueSoundtrackNames.add(collection.get(keys).getSoundtrackName())){
                System.out.println(collection.get(keys).getSoundtrackName());
            }
        }
        Structure.commandHistory[Structure.commandCounter%commandConst] = "print_unique_soundtrack_name";
        Structure.commandCounter++;
    }
    /** Реализует команду "print_field_descending_minutes_of_waiting" */
    public void print_field_descending_minutes_of_waiting(){
        collection = Main.collection;
        System.out.println("Descending minutes of waiting: ");
        SortedSet<Long> waitings = new TreeSet<>();
        for (Integer keys: Initialization.keyCounter){
            try{
                waitings.add(collection.get(keys).getMinutesOfWaiting());
            }
            catch (NullPointerException e){}
        }
           while (waitings.size()>0){
               System.out.println(waitings.last());
               waitings.remove(waitings.last());
           }

        Structure.commandHistory[Structure.commandCounter%commandConst] = "print_field_descending_minutes_of_waiting";
        Structure.commandCounter++;
    }
    /** Реализует команду "save"*/
    public void save(Connection connection) {
        collection = Main.collection;
        try {
            Statement stat = connection.createStatement();
            User user = new User();
            stat.executeUpdate("TRUNCATE TABLE HUMANBEING;");
            stat.executeUpdate("drop sequence HumanBeingIdSeq;");
            stat.executeUpdate("CREATE SEQUENCE HumanBeingIdSeq START WITH 1 INCREMENT BY 1");
            for (Integer key : Initialization.keyCounter) {

                String name = collection.get(key).getName();
                float x = collection.get(key).getCoordinates().getX();
                float y = collection.get(key).getCoordinates().getY();
                String creationDate = String.valueOf(collection.get(key).getCreationDate());
                Boolean realHero;
                try {
                    realHero = collection.get(key).isRealHero();
                } catch (NullPointerException e1) {
                    realHero = null;
                }
                Boolean hasToothpick = collection.get(key).getHasToothpick();
                double impactSpeed = collection.get(key).getImpactSpeed();
                String soundtrackName = collection.get(key).getSoundtrackName();
                Long minutesOfWaiting;
                try {
                    minutesOfWaiting = collection.get(key).getMinutesOfWaiting();
                } catch (NullPointerException e2) {
                    minutesOfWaiting = null;
                }
                String mood;
                try {
                    mood = String.valueOf(collection.get(key).getMood());
                    if (mood.equals("null")){
                        mood = null;
                    }
                } catch (NullPointerException e3) {
                    mood = null;
                }
                String car_name = collection.get(key).getCar().getName();
                Boolean car_cool = collection.get(key).getCar().getCool();
                String owner = collection.get(key).getOwner();

                String sqlRequest = "INSERT INTO HumanBeing VALUES (" + "nextval('HumanBeingIdSeq'), '" + name + "', " + x + ", " + y +
                        ", '" + creationDate + "', '" + realHero + "', '" + hasToothpick + "', " + impactSpeed + ", '" + soundtrackName + "', "
                        + minutesOfWaiting + ", '" + mood + "', '" + car_name + "', '" + car_cool + "', '" + owner + "')";
                stat.executeUpdate(sqlRequest);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        Structure.commandHistory[Structure.commandCounter%commandConst] = "save";
        Structure.commandCounter++;
    }
    /** Реализует команду "execute_script"
     * @param filePath - имя файла, в котором находятся команды*/
    public void execute_script(String filePath) {
        collection = Main.collection;
        Structure.commandHistory[Structure.commandCounter%commandConst] = "execute_script";
        Structure.commandCounter++;
        //int recursionCount = Structure.script_rec;
        if (Structure.script_rec < 3){
            Structure.script_rec++;
            try {
                File file = new File(filePath);
                Scanner sc = new Scanner(file);
                Path path = Paths.get(file.toString());
                if (!(Files.isReadable(path))) {
                    throw new SecurityException();
                }
                while (sc.hasNext()) {
                    String[] str = sc.nextLine().trim().split(" ");
                    try {
                        if (str.length == 1){
                                Structure.commandList.get(str[0].toLowerCase()).execute();
                        }
                        else {
                            try{
                                Structure.commandList.get(str[0].toLowerCase()).setId(Integer.parseInt(str[1]));
                                Structure.commandList.get(str[0].toLowerCase()).setKey(Integer.parseInt(str[1]));
                            }
                            catch (NumberFormatException e0){
                                //e0.printStackTrace();
                            }
                            Structure.commandList.get(str[0].toLowerCase()).setName(str[1]);
                            Structure.commandList.get(str[0].toLowerCase()).execute();
                            }

                    } catch (StringIndexOutOfBoundsException e) {
                        System.out.println("Команда введена неверно или такой команды не существует, попробуйте еще раз.");
                    } catch (NumberFormatException e1) {
                        System.out.println("Некорректно введены числовые данные.");
                    }
                    catch (NullPointerException e2){
                        //e2.printStackTrace();
                        System.out.println("Неверный формат команды. Пожалуйста, проверьте правильность написания скриптов.");
                    }
                }
            } catch (FileNotFoundException | NullPointerException  e) {
                //e.printStackTrace();
                System.out.println("Файл не найден!");
            }
            catch (NoSuchElementException e1){
                System.out.println("Файл пуст!");
            }
            catch (SecurityException e2){
                //e2.printStackTrace();
                System.out.println("Ошибка прав доступа!");
            }
            catch (StackOverflowError e666){
                System.out.println("Воу воу! Вы сумели обойти ограничение на глубину рекурсии, и вот к чему это привело! Не стыдно?");
            }
        }
        else {
            System.out.println("Превышена максимальная глубина рекурсии (3)! Выполнение команды запрещено.");
        }
    }
}
