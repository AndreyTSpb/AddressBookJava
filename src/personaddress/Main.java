package personaddress;

import java.util.*;
/**
 * @author Andrey Tynyanyi
 * @version 1.0
 */
public class Main{
    private static Scanner scanner = new Scanner(System.in); //Для ввода с клавиатуры
    private static Person[] persons = new Person[10]; //Массив обьектов людей
    private static Address[] addresses = new Address[4]; //Массив объектов адресов
    //Двуменый массив хранит идентификатор пользователя и адреса
    private static int[][] addressBook = new int[persons.length][2];

    public static void main(String[] args) {
        setAddresses();
        setPersons();
        setAddressBook();
        menu();
    }

    /**
     * Меню для выбора действий
     */
    private static void menu(){
            int exit = 0;
        while (exit != 1){
            System.out.println("Какие действия вы хотите совершить ?");
            System.out.println("осуществить поиск Человека по фамилии - 1");
            System.out.println("вывести людей, родившихся между определенными датами - 2");
            System.out.println("найти самого старого - 3");
            System.out.println("найти самого молодого - 4");
            System.out.println("найти людей, проживающих на одной улице - 5");
            System.out.println("Показать полностью адресную книгу - 6");
            System.out.println("Показать список людей - 7");
            System.out.println("Показать список адресов - 8");
            System.out.println("Закончить программу - 0");
            System.out.print("Число от 0 до 8:");
            int select = Main.scanner.nextInt();
            switch (select){
                case 0:
                    System.out.println("Exit");
                    exit = 1;
                    break;
                case 1:
                    searchAddressBookFIO();
                    break;
                case 2:
                    viewSelectByDateOfBirth();
                    break;
                case 3:
                    findOldestPerson();
                    break;
                case 4:
                    findYoungestPerson();
                    break;
                case 5:
                    peopleLivingOnSameStreet();
                    break;
                case 6:
                    viewAddressBook();
                    break;
                case 7:
                    viewAllPersons();
                    break;
                case 8:
                    viewAllAddress();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     *  вывести людей, родившихся между определенными датами
     */
    private static void viewSelectByDateOfBirth(){
        System.out.print("укажите дату от (ГГГГ-ММ-ДД): ");
        String dateFromStr   = Main.scanner.next();
        System.out.print("укажите дату до (ГГГГ-ММ-ДД): ");
        String dateBeforeStr = Main.scanner.next();

        //от какой даты
        Calendar dateFrom = Main.convertDate(dateFromStr);
        //по какую дату
        Calendar dateBefore = Main.convertDate(dateBeforeStr);

        System.out.println("Люди родившиеся между "+dateFromStr+" и "+dateBeforeStr+": ");
        for (int i=0; i < Main.persons.length; i++){
            //искомая дата
            Calendar dateSearch = Main.convertDate(Main.persons[i].birthDay);
            //проверка
            if(dateFrom.before(dateSearch) && dateBefore.after(dateSearch)){
                getAddressBookPerson(i); //вывод из адресной книги найденого человека со всеми данными
            }
        }
    }

    /**
     * Найти самого старого человека
     */
    private static void findOldestPerson(){
        System.out.println("Самый старый человек в адресной книге:");
        int max = Main.persons[0].age;
        int idUser = 0;
        for(int i=1; i<Main.persons.length; i++){
            if(max < Main.persons[i].age){
                max = Main.persons[i].age;
                System.out.println(max);
                idUser = i;
            }
        }
        getAddressBookPerson(idUser); //вывод из адресной книги найденого человека со всеми данными
    }

    /**
     * найти самого молодого
     */
    private static void findYoungestPerson(){
        System.out.println("Самый молодой человек в адресной книге:");
        int max = Main.persons[0].age;
        int idUser = 0;
        for(int i=1; i<Main.persons.length; i++){
            if(max > Main.persons[i].age){
                max = Main.persons[i].age;
                idUser = i;
            }
        }
        getAddressBookPerson(idUser); //вывод из адресной книги найденого человека со всеми данными
    }

    /**
     * найти людей, проживающих на одной улице
     */
    private static void peopleLivingOnSameStreet(){
        int[] address = new int[Main.addresses.length]; //суммирую частоту повторений адресов
        //подсчитывае количество совпадающих адресов в адресной книге
        for(int j = 0; j<Main.addresses.length; j++) {
            for (int i = 0; i < Main.addressBook.length; i++) {
                if(Main.addressBook[i][1] == j){
                    address[j] += 1;
                }
            }
        }

        //перебираем полученный массив
        for(int i = 0; i<address.length; i++){
           //если было найдено больше одного выводим
            if(address[i]>1){
                for(int k = 0; k < Main.addressBook.length; k++){
                    if(Main.addressBook[k][1] == i){
                        int idUser = Main.addressBook[k][0];
                        getAddressBookPerson(idUser);
                    }
                }
            }
        }
    }

    /**
     * Конвертируем строковое значение в дату
     * @param date - принимает датту в виде строки ГГГГ-ММ-ДД
     * @return возвращает объект типа Calendar
     */
    private static Calendar convertDate(String date){
        String[] dateArr = date.split("-");
        int year    = Integer.parseInt(dateArr[0]);
        int month   = Integer.parseInt(dateArr[1]);
        int day     = Integer.parseInt(dateArr[2]);
        return new GregorianCalendar(year, month-1, day);
    }

    /**
     * Поиск по фамилии
     */
    private static void searchAddressBookFIO(){
        System.out.print("Укажите фаммилию для поиска: ");
        String family = Main.scanner.next();
        int idUser = searchPerson(family);
        if (idUser > -1){
            System.out.println("Такой человек найден!");
            getAddressBookPerson(idUser);
        } else {
            System.out.println("В базе нет человека с фамилией" + family + "!!!");
        }
    }

    /**
     * Вывод из адресной книги одного человека
     * @param idUser - идентификатор человека
     */
    private static void getAddressBookPerson(int idUser){
        boolean find = false;
        for (int i=0; i< Main.addressBook.length; i++){
            if(Main.addressBook[i][0] == idUser){
                int idAddress = Main.addressBook[i][1];
                onDisplay(idUser, idAddress);
                find = true;
                break;
            }
        }
        if(!find) System.out.println("В адресной книге нет такого человека");
    }

    /**
     * Вывод списка всех людей
     */
    private static void viewAllPersons(){
        System.out.println("Список людей");
        for(int i = 0; i < persons.length; i++){
            System.out.println("№" + i
                    + "- Имя: " + Main.persons[i].name
                    + ", Фамилия: " + Main.persons[i].family
                    + ", Дата рожденяи: " + Main.persons[i].birthDay
                    + ", Возраст: " + Main.persons[i].age
            );
        }
    }

    /**
     * Вывод списка адресов
     */
    private static void viewAllAddress(){
        System.out.println("Список городов и улиц");
        for(int i = 0; i < addresses.length; i++){
            System.out.println("# " +i
                    + "- Город: " + Main.addresses[i].city
                    + ", Улица: " +Main.addresses[i].street
            );
        }
    }

    /**
     * Вывод всего содержание адресной книги
     */
    private static void viewAddressBook(){
        for (int i = 0; i<Main.addressBook.length; i++){
            System.out.println("Адрес №" +i+ ": ");
            int idUser   = Main.addressBook[i][0];
            int idAddress = Main.addressBook[i][1];
            onDisplay(idUser, idAddress);
        }
    }

    /**
     * Вывод полных данных о человеке
     * @param idUser - идентификатор пользователя в масиве персон
     * @param idAddress - идентификатор адреса в массиве адресов
     */
    private static void onDisplay(int idUser, int idAddress){
        System.out.print(" Фамилия: " + Main.persons[idUser].family
                + ", Имя: " + Main.persons[idUser].name
                + ", Дата рожденья: " + Main.persons[idUser].birthDay
                + ", Возраст: " + Main.persons[idUser].age
        );
        System.out.println(", Город: " + Main.addresses[idAddress].city
                + ", Улица: " +Main.addresses[idAddress].street
        );
    }

    /**
     * Наполнение массива людьми
     */
    private static void setPersons(){
        Main.persons[0] = new Person("Петров","Петр","2000-04-05");
        Main.persons[1] = new Person("Иванов","Иван","2002-09-05");
        Main.persons[2] = new Person("Семенов","Семен","2010-10-05");
        Main.persons[3] = new Person("Павлов","Павел","2001-05-05");
        Main.persons[4] = new Person("Степанов","Степан","1980-01-05");
        Main.persons[5] = new Person("Андреев","Андрей","2005-02-05");
        Main.persons[6] = new Person("Алексеев","Алексей","1998-11-05");
        Main.persons[7] = new Person("Арсеньев","Арсений","1990-10-05");
        Main.persons[8] = new Person("Васильев","Василий","1992-08-05");
        Main.persons[9] = new Person("Ленина","Елена","2000-11-22");
    }

    /**
     * Заполнение массива адресами
     */
    private static void setAddresses(){
        Main.addresses[0] = new Address("Москва", "ул. Арбат");
        Main.addresses[1] = new Address("Волгоград", "ул. Ленина");
        Main.addresses[2] = new Address("Рязань", "ул. Красная");
        Main.addresses[3] = new Address("Новгород", "ул. Рабочая");
    }

    /**
     * Заполняем адресную книгу автоматически:
     * Человека берем попорядку добавления,
     * Адрес присваивается рандомно из имеющегося списка
     */
    private static void setAddressBook(){
        Random rand = new Random();
        for (int i = 0; i < Main.addressBook.length; i++) {
            Main.addressBook[i][0] = i;
            Main.addressBook[i][1] = rand.nextInt(4);
        }
    }

    /**
     * Поиск по фамилии: осуществить поиск Человека по фамилии
     * @param family  фамилия которую будем искать
     * @return вернется ключ пользователя
     */
    private static int searchPerson(String family){
        for(int i=0; i< Main.persons.length; i++){
            //проверка есть ли нужная фамилмя
            if(Objects.equals(Main.persons[i].family, family)){
                /*Возвращаем ключ пользователя*/
                return i;
            }
        }
        /*массив начинается с 0,
         поэтому если ничего нет вернем отрицательное число*/
        return -1;
    }
}
