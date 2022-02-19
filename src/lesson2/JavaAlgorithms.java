package lesson2;

import kotlin.NotImplementedError;
import kotlin.Pair;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class JavaAlgorithms {
    /**
     * Получение наибольшей прибыли (она же -- поиск максимального подмассива)
     * Простая
     *
     * Во входном файле с именем inputName перечислены цены на акции компании в различные (возрастающие) моменты времени
     * (каждая цена идёт с новой строки). Цена -- это целое положительное число. Пример:
     *
     * 201
     * 196
     * 190
     * 198
     * 187
     * 194
     * 193
     * 185
     *
     * Выбрать два момента времени, первый из них для покупки акций, а второй для продажи, с тем, чтобы разница
     * между ценой продажи и ценой покупки была максимально большой. Второй момент должен быть раньше первого.
     * Вернуть пару из двух моментов.
     * Каждый момент обозначается целым числом -- номер строки во входном файле, нумерация с единицы.
     * Например, для приведённого выше файла результат должен быть Pair(3, 4)
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    static public Pair<Integer, Integer> optimizeBuyAndSell(String inputName) {
        throw new NotImplementedError();
    }

    /**
     * Задача Иосифа Флафия.
     * Простая
     *
     * Образовав круг, стоят menNumber человек, пронумерованных от 1 до menNumber.
     *
     * 1 2 3
     * 8   4
     * 7 6 5
     *
     * Мы считаем от 1 до choiceInterval (например, до 5), начиная с 1-го человека по кругу.
     * Человек, на котором остановился счёт, выбывает.
     *
     * 1 2 3
     * 8   4
     * 7 6 х
     *
     * Далее счёт продолжается со следующего человека, также от 1 до choiceInterval.
     * Выбывшие при счёте пропускаются, и человек, на котором остановился счёт, выбывает.
     *
     * 1 х 3
     * 8   4
     * 7 6 Х
     *
     * Процедура повторяется, пока не останется один человек. Требуется вернуть его номер (в данном случае 3).
     *
     * 1 Х 3
     * х   4
     * 7 6 Х
     *
     * 1 Х 3
     * Х   4
     * х 6 Х
     *
     * х Х 3
     * Х   4
     * Х 6 Х
     *
     * Х Х 3
     * Х   х
     * Х 6 Х
     *
     * Х Х 3
     * Х   Х
     * Х х Х
     *
     * Общий комментарий: решение из Википедии для этой задачи принимается,
     * но приветствуется попытка решить её самостоятельно.
     */
    static public int josephTask(int menNumber, int choiceInterval) {
        throw new NotImplementedError();
    }

    /**
     * Наибольшая общая подстрока.
     * Средняя
     *
     * Дано две строки, например ОБСЕРВАТОРИЯ и КОНСЕРВАТОРЫ.
     * Найти их самую длинную общую подстроку -- в примере это СЕРВАТОР.
     * Если общих подстрок нет, вернуть пустую строку.
     * При сравнении подстрок, регистр символов *имеет* значение.
     * Если имеется несколько самых длинных общих подстрок одной длины,
     * вернуть ту из них, которая встречается раньше в строке first.
     */

    static public String longestCommonSubstring(String firs, String second) {
        Map<Character, Set<Integer>> characterIndexesMap = new HashMap<>();
        Map<Character, Set<Character>> characterSequenceMap = new HashMap<>();
        if (second.length() == 0 || firs.length() == 0) return "";
        char[] charArrayOfSecond = second.toCharArray();
        for (int i = 0; i < charArrayOfSecond.length; i++) {
            char currChar = charArrayOfSecond[i];
            Set<Integer> indexesOfCurr = characterIndexesMap.computeIfAbsent(currChar, k -> new HashSet<>());
            indexesOfCurr.add(i);
            if (i < charArrayOfSecond.length - 1) {
                char nextChar = charArrayOfSecond[i + 1];
                Set<Character> nextChars = characterSequenceMap.computeIfAbsent(currChar, k -> new HashSet<>());
                nextChars.add(nextChar);
            } else {
                characterSequenceMap.computeIfAbsent(currChar, k -> new HashSet<>());
            }
        }
        char[] charArrayOfFirst = firs.toCharArray();
        int lengthSubstring = 0;
        int indexOfBiggerAtFirst = -1;
        for (int charInd = 0; charInd < charArrayOfFirst.length; charInd++) {
            Set<Integer> indexesOfFirstCharacter = characterIndexesMap.get(charArrayOfFirst[charInd]);
            if (indexesOfFirstCharacter == null)
                continue;
            Set<Integer> currentIndexes = new HashSet<>(indexesOfFirstCharacter);
            for (int indexOfLastChar = charInd + 1; indexOfLastChar < charArrayOfFirst.length; indexOfLastChar++) {
                char lastChar = charArrayOfFirst[indexOfLastChar];
                currentIndexes = currentIndexes.stream().map(it -> it + 1).collect(Collectors.toSet());
                Set<Integer> currentCharIndexes = characterIndexesMap.get(lastChar);
                currentCharIndexes = (currentCharIndexes == null) ? new HashSet<>() : currentCharIndexes;
                currentIndexes.retainAll(currentCharIndexes);
                if (currentIndexes.isEmpty()) {
                    if (lengthSubstring < indexOfLastChar - charInd) {
                        indexOfBiggerAtFirst = charInd;
                        lengthSubstring = indexOfLastChar - charInd;
                    }
                    break;
                }
            }
        }
        if (indexOfBiggerAtFirst != -1) {
            return firs.substring(indexOfBiggerAtFirst, indexOfBiggerAtFirst + lengthSubstring);
        }
        return "";
    }

    /**
     * Число простых чисел в интервале
     * Простая
     *
     * Рассчитать количество простых чисел в интервале от 1 до limit (включительно).
     * Если limit <= 1, вернуть результат 0.
     *
     * Справка: простым считается число, которое делится нацело только на 1 и на себя.
     * Единица простым числом не считается.
     */

    //Реализация Решета Эратосфена https://en.wikipedia.org/wiki/Sieve_of_Eratosthenes
    static public int calcPrimesNumber(int limit) {
        if (limit <= 1) return 0;
        byte[] bitwiseSieve = new byte[limit/8 + 1];
        int primesCounter = 0;
        for (int i = 2; i <= limit; i++) {
            if ((bitwiseSieve[(i-2)/8] & 1<<((i-2)%8)) == 0) {
                primesCounter++;
                for (int j = i * 2; j <= limit; j += i) {
                    bitwiseSieve[(j-2)/8] |= 1<<((j-2)%8);
                }
            }
        }
        return primesCounter;
    }
}
