package Collection;

import Programm.Main;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Содержит метод, осуществляющий проверку структуры файла (например порядок или количество тегов)
 */

public class FileReader {

    final int Const = Main.CONST;
    boolean bool = true;
    private List<String[]> list = new ArrayList<>(); //список массивов, который в итоге отправляется на проверку
    private String[] stringList = new String[Const];    //массив значений (будущих полей коллекций)

    /** Метод осуществляет проверку структуры файла и записывает полученные поля в список.
     * @param nodeList получается в процессе парсинга xml файла при помощи DOM, содержит в себе всю информацию об элементах коллекции.
     */
    public void printElements(NodeList nodeList) {


            for (int i = 0; i < nodeList.getLength(); i++) {
                if (nodeList.item(i) instanceof Element) {
                    try {
                    if(nodeList.item(i).getTextContent().trim().equals("") || !(nodeList.item(i).getFirstChild().getTextContent().trim().equals(""))) {
                        if (!((Element) nodeList.item(i)).getTagName().equals("coordinates") && !((Element) nodeList.item(i)).getTagName().equals("car")) {
                            switch (((Element) nodeList.item(i)).getTagName()) {
                                case "name":
                                    if (bool) {
                                        bool = false;
                                    } else{
                                        list.add(stringList);
                                        stringList = new String[Const];
                                    }
                                    stringList[0] = nodeList.item(i).getFirstChild().getTextContent();
                                case "x":
                                    stringList[1] = nodeList.item(i).getTextContent();
                                case "y":
                                    stringList[2] = nodeList.item(i).getTextContent();
                                case "creationDate":
                                    stringList[3] = nodeList.item(i).getTextContent();
                                case "realHero":
                                    stringList[4] = nodeList.item(i).getTextContent();
                                case "hasToothpick":
                                    stringList[5] = nodeList.item(i).getTextContent();
                                case "impactSpeed":
                                    stringList[6] = nodeList.item(i).getTextContent();
                                case "soundtrackName":
                                    stringList[7] = nodeList.item(i).getTextContent();
                                case "minutesOfWaiting":
                                    stringList[8] = nodeList.item(i).getTextContent();
                                case "mood":
                                    stringList[9] = nodeList.item(i).getTextContent();
                                case "car_name":
                                    stringList[10] = nodeList.item(i).getTextContent();
                                case "cool":
                                    stringList[11] = nodeList.item(i).getTextContent();
                            }
                            //System.out.println("value = " + stringList[e] + " (e = " + e + ")");
                        }
                    }
                    if (nodeList.item(i).hasChildNodes()) {
                        printElements(nodeList.item(i).getChildNodes());
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Ошибка в объекте!\nОбъект не был добавлен в коллекцию");
                }
            }
        }
    }
    /**
     * @return Возвращает прошедшие первый этап проверки массивы полей элементов коллекции
     */
    public List<String[]> getList() {
        return list;
    }
}
