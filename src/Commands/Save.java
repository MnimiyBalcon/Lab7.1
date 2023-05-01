package Commands;

import Programm.Main;
import java.sql.*;

/**
 * Cодержит в себе метод, вызывающий команду save <br>
 * save : сохранить коллекцию в файл
 */
public class Save extends Command {
    CommandManager commandManager = new CommandManager();
    String name;
    public void setName(String name) {
        this.name = name;
    }
    Connection connection=null;
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    @Override
    public void execute() {
        try{
            if (!Main.username.equals("")) commandManager.save(connection);
            else System.out.println("Ошибка! Незарегистрированным пользователям недоступно изменение коллекции");
        } catch (Exception e) {
            System.out.println("Что-то пошло не так при сохранении коллекции...");
        }
    }
    public String toString(){
        return "> save : сохранить коллекцию в файл";
    }
}