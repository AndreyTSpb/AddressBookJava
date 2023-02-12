package personaddress;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author Andrey Tynyanyi
 * @version 1.0
 */
public class Person {
    public static ArrayList<String> persons = new ArrayList<>();
    public String name; //имя
    public String family; //фамилия
    public int age; //возраст
    public String birthDay; //день рождения

    public Person(String family, String name, String birthDay){
        this.name     = name;
        this.family   = family;
        this.birthDay = birthDay;
        this.age      = getAge(birthDay);
    }

    /**
     * Получаем возраст человека
     * @param birthDay - день рождения
     * @return возвращает возраст
     */
    private int getAge(String birthDay){
        String[] items = birthDay.split("-");
        int year = Integer.parseInt(items[0]);
        Calendar calendar = new GregorianCalendar();
        int nowYear = calendar.get(Calendar.YEAR);
        return nowYear-year;
    }
}
