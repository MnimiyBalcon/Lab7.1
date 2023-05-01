package Commands;
import Programm.Main;
/**
 * Cодержит в себе метод, вызывающий команду clear <br>
 * clear : очистить коллекцию
 */
public class Clear extends Command {
    CommandManager commandManager = new CommandManager();
    @Override
    public void execute(){
        if (!Main.username.equals("")) commandManager.clear();
        else System.out.println("Ошибка! Незарегистрированным пользователям недоступно изменение коллекции");
    }
    public String toString(){
        return " > clear : очистить коллекцию";
    }
}