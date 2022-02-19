package lesson1;

import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;


class Person implements Comparable<Person> {
    public final String name;
    public final String surname;

    Person(String surname, String name) {
        this.name = name;
        this.surname = surname;
    }

    @Override
    public int compareTo(@NotNull Person other) {
        int surnameSize = Integer.min(surname.length(), other.surname.length());
        String surnameToCompareOfThis = (surname.substring(0, surnameSize)).toLowerCase(Locale.ROOT);
        String surnameToCompareOfOther = (other.surname.substring(0, surnameSize)).toLowerCase(Locale.ROOT);
        int surnameCompareResult = surnameToCompareOfThis.compareTo(surnameToCompareOfOther);
        if (surnameCompareResult == 0 && surname.length() == other.surname.length()) {
            int nameSize = Integer.min(name.length(), other.name.length());
            String nameToCompareOfThis = (name.substring(0, nameSize)).toLowerCase(Locale.ROOT);
            String nameToCompareOfOther = (other.name.substring(0, nameSize)).toLowerCase(Locale.ROOT);
            int nameCompareResult = nameToCompareOfThis.compareTo(nameToCompareOfOther);
            if (nameCompareResult == 0) {
                return Integer.compare(name.length(), other.name.length());
            } else {
                return nameCompareResult;
            }
        } else if (surnameCompareResult == 0) {
            return Integer.compare(surname.length(), other.surname.length());
        }
        return surnameCompareResult;
    }
}

class Home {

    private final List<Person> members = new ArrayList<>();

    public void addMember(Person member) {
        members.add(member);
    }

    public Person[] getSortedMembers() {
        Person[] sortedMembers = members.toArray(new Person[0]);
        Arrays.sort(sortedMembers);
        return sortedMembers;
    }
}


class Street {
    private final Map<Integer, Home> homes = new HashMap<>();
    private int[] sortedHomeNumbers = null;

    public int[] getSortedHomeNumbers() {
        return sortedHomeNumbers;
    }

    public Map<Integer, Home> getHomes() {
        return homes;
    }

    public void performSorting() {
        int[] arrayOfNumbers = this.homes.keySet().stream().mapToInt(it -> it).toArray();
        Arrays.sort(arrayOfNumbers);
        sortedHomeNumbers = arrayOfNumbers;
    }

}

@SuppressWarnings("unused")
public class JavaTasks {
    private static int getPseudoTime(String line) {
        String[] timeAndPostfix = line.split(" ");
        if (timeAndPostfix.length != 2) throw new IllegalArgumentException("Wrong time format!");
        int pseudoTime = Integer.parseInt(timeAndPostfix[0].replaceAll(":", ""));
        if (pseudoTime / 1_00_00 == 12) pseudoTime %= 1_00_00;
        if (timeAndPostfix[1].equals("PM")) pseudoTime += 12_00_00;
        return pseudoTime;
    }

    private static String getTimeString(int pseudoTime) {
        int hours = pseudoTime / 1_00_00;
        int minutes = (pseudoTime % 1_00_00) / 1_00;
        int seconds = pseudoTime % 1_00;
        return String.format(
                "%02d:%02d:%02d%s",
                (hours == 0) ? 12 : (hours > 12) ? hours - 12 : hours,
                minutes, seconds,
                ((hours < 12) ? " AM" : " PM")
        );
    }

    /**
     * Сортировка времён
     * <p>
     * Простая
     * (Модифицированная задача с сайта acmp.ru)
     * <p>
     * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС AM/PM,
     * каждый на отдельной строке. См. статью википедии "12-часовой формат времени".
     * <p>
     * Пример:
     * <p>
     * 01:15:19 PM
     * 07:26:57 AM
     * 10:00:03 AM
     * 07:56:14 PM
     * 01:15:19 PM
     * 12:40:31 AM
     * <p>
     * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
     * сохраняя формат ЧЧ:ММ:СС AM/PM. Одинаковые моменты времени выводить друг за другом. Пример:
     * <p>
     * 12:40:31 AM
     * 07:26:57 AM
     * 10:00:03 AM
     * 01:15:19 PM
     * 01:15:19 PM
     * 07:56:14 PM
     * <p>
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    /*
     * T=O(n*log(n)) (т.к. используется стандартная сортировка для Java -- mergeSort)
     * R=O(n)
     * */
    static public void sortTimes(String inputName, String outputName) throws IOException {
        FileReader reader = new FileReader(inputName);
        String[] lines = new BufferedReader(reader).lines().toArray(String[]::new);
        reader.close();
        int[] values = new int[lines.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = getPseudoTime(lines[i]);
        }
        Arrays.sort(values);
        FileWriter writer = new FileWriter(outputName);
        for (int value : values) {
            writer.write(getTimeString(value) + '\n');
        }
        writer.close();
    }


    /**
     * Сортировка адресов
     * <p>
     * Средняя
     * <p>
     * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
     * где они прописаны. Пример:
     * <p>
     * Петров Иван - Железнодорожная 3
     * Сидоров Петр - Садовая 5
     * Иванов Алексей - Железнодорожная 7
     * Сидорова Мария - Садовая 5
     * Иванов Михаил - Железнодорожная 7
     * <p>
     * Людей в городе может быть до миллиона.
     * <p>
     * Вывести записи в выходной файл outputName,
     * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
     * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
     * <p>
     * Железнодорожная 3 - Петров Иван
     * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
     * Садовая 5 - Сидоров Петр, Сидорова Мария
     * <p>
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */

    /*
        T = O(n*ln(n)) -- в худшем случае, при котором 1 млн жителей живет в одном доме. (Т.к. используется mergeSort)
        R = O(alpha*C*n),alpha зависит от содержания входных данных,
         alpha < 1 -- поскольку каждому дому и улице будет соответсвовать только одна запись.
     */
    static public void sortAddresses(String inputName, String outputName) throws IOException {
        Map<String, Street> streets = new HashMap<>();
        FileReader reader = new FileReader(inputName);
        new BufferedReader(reader).lines().forEach(line -> {
            String[] personWithAddress = line.split(" ");
            if (personWithAddress.length != 5) throw new IllegalArgumentException("Wrong file data:" + line);
            String personSurname = personWithAddress[0];
            String personName = personWithAddress[1];
            String streetName = personWithAddress[3];
            int homeNumber = Integer.parseInt(personWithAddress[4]);
            if (!streets.containsKey(streetName)) {
                Street street = new Street();
                street.getHomes().put(homeNumber, new Home());
                streets.put(streetName, street);
            } else if (!streets.get(streetName).getHomes().containsKey(homeNumber)) {
                streets.get(streetName).getHomes().put(homeNumber, new Home());
            }
            streets.get(streetName).getHomes().get(homeNumber).addMember(new Person(personSurname, personName));
        });
        reader.close();
        String[] streetNames = streets.keySet().toArray(new String[0]);
        Arrays.parallelSort(streetNames, String::compareTo);
        FileWriter fileWriter = new FileWriter(outputName);
        for (String streetName : streetNames) {
            Street street = streets.get(streetName);
            street.performSorting();
            int[] sortedHomeNumbers = street.getSortedHomeNumbers();
            for (int homeNumber : sortedHomeNumbers) {
                Home home = street.getHomes().get(homeNumber);
                Person[] sortedMembers = home.getSortedMembers();
                fileWriter.write(streetName + " " + homeNumber + " - ");
                for (int i = 0; i < sortedMembers.length; i++) {
                    fileWriter.write(sortedMembers[i].surname + " " +
                            sortedMembers[i].name +
                            ((i != sortedMembers.length - 1) ? ", " : "\n")
                    );
                }
            }
        }
        fileWriter.close();
    }

    /**
     * Сортировка температур
     * <p>
     * Средняя
     * (Модифицированная задача с сайта acmp.ru)
     * <p>
     * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
     * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
     * Например:
     * <p>
     * 24.7
     * -12.6
     * 121.3
     * -98.4
     * 99.5
     * -12.6
     * 11.0
     * <p>
     * Количество строк в файле может достигать ста миллионов.
     * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
     * Повторяющиеся строки сохранить. Например:
     * <p>
     * -98.4
     * -12.6
     * -12.6
     * 11.0
     * 24.7
     * 99.5
     * 121.3
     */

    /*
        T=O(C*n) ... т.к. один раз осуществляется проход по входным данным, а затем один раз, проходя по массиву
        фиксированного размера, делаем n записей в выходной файл.
        R=C, т.к. используется массив фиксированного размера
     */
    static public void sortTemperatures(String inputName, String outputName) throws IOException {
        int[] tempNumbers = new int[7731];
        FileReader reader = new FileReader(inputName);
        new BufferedReader(reader).lines()
                .forEach(line -> tempNumbers[(int) ((Float.parseFloat(line) * 10) + 2730)] += 1);
        reader.close();
        FileWriter writer = new FileWriter(outputName);
        for (int i = 0; i < tempNumbers.length; i++) {
            for (int j = 0; j < tempNumbers[i]; j++) {
                writer.write(((((float) (i - 2730)) / 10)) + "\n");
            }
        }
        writer.close();
    }

    /**
     * Сортировка последовательности
     * <p>
     * Средняя
     * (Задача взята с сайта acmp.ru)
     * <p>
     * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
     * <p>
     * 1
     * 2
     * 3
     * 2
     * 3
     * 1
     * 2
     * <p>
     * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
     * а если таких чисел несколько, то найти минимальное из них,
     * и после этого переместить все такие числа в конец заданной последовательности.
     * Порядок расположения остальных чисел должен остаться без изменения.
     * <p>
     * 1
     * 3
     * 3
     * 1
     * 2
     * 2
     * 2
     */
    static public void sortSequence(String inputName, String outputName) {
        throw new NotImplementedError();
    }

    /**
     * Соединить два отсортированных массива в один
     * <p>
     * Простая
     * <p>
     * Задан отсортированный массив first и второй массив second,
     * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
     * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
     * <p>
     * first = [4 9 15 20 28]
     * second = [null null null null null 1 3 9 13 18 23]
     * <p>
     * Результат: second = [1 3 4 9 9 13 15 20 23 28]
     */
    static <T extends Comparable<T>> void mergeArrays(T[] first, T[] second) {
        throw new NotImplementedError();
    }
}
