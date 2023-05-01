package Commands;

/**
 * Cодержит метод, вызывающий команду help <br>
 * help : вывести справку по доступным командам
 */
public class Help extends Command {
    CommandManager commandManager = new CommandManager();
    @Override
     public void execute(){
        commandManager.help();
    }
    public String toString(){
         return " > help : вывести справку по доступным командам";
    }
}