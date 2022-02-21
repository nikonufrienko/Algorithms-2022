package lesson2;

import kotlin.NotImplementedError;

import java.util.*;
import java.util.stream.Collectors;

class Pair<T,E>{
    private final T first;
    private final E second;

    public T getFirst() {
        return first;
    }

    public E getSecond() {
        return second;
    }



    public Pair(T firt, E second) {
        this.first = firt;
        this.second = second;
    }
}

class StringAsTableOfIndexes {
    private final Map<Character, Set<Integer>> characterIndexesMap;
    private final Map<Character, Set<Character>> characterSequenceMap;

    StringAsTableOfIndexes(String value) {
        characterIndexesMap = new HashMap<>();
        characterSequenceMap = new HashMap<>();
        char[] charArrayOfValue = value.toCharArray();
        for (int i = 0; i < charArrayOfValue.length; i++) {
            char currChar = charArrayOfValue[i];
            Set<Integer> indexesOfCurr = characterIndexesMap.computeIfAbsent(currChar, k -> new HashSet<>());
            indexesOfCurr.add(i);
            if (i < charArrayOfValue.length - 1) {
                char nextChar = charArrayOfValue[i + 1];
                Set<Character> nextChars =
                        characterSequenceMap.computeIfAbsent(currChar, k -> new LinkedHashSet<>());
                nextChars.add(nextChar);
            } else {
                characterSequenceMap.computeIfAbsent(currChar, k -> new LinkedHashSet<>());
            }
        }
    }


    public Map<Character, Set<Integer>> getCharacterIndexesMap() {
        return characterIndexesMap;
    }

    public Map<Character, Set<Character>> getCharacterSequenceMap() {
        return characterSequenceMap;
    }

    public String recursiveSearchingLongestCommonSubstring(StringAsTableOfIndexes other){
        List<Character> maxChain = new ArrayList<>();
        int minEndInd = 0;
        for (Character startCharacter : this.getCharacterIndexesMap().keySet()) {
            if(!other.getCharacterIndexesMap().containsKey(startCharacter)) continue;
            Set<Integer> currentIndexesOfThis = this.getCharacterIndexesMap().get(startCharacter);
            Set<Integer> currentIndexesOfOther = other.getCharacterIndexesMap().get(startCharacter);
            List<Character> currChain = new ArrayList<>();
            currChain.add(startCharacter);
            Pair<List<Character>, Integer> resultChain = this.getLongestCommonChain(
                    other, startCharacter,
                    currentIndexesOfThis,currentIndexesOfOther, currChain
            );
            if(resultChain.getFirst().size() > maxChain.size() || (resultChain.getFirst().size() == maxChain.size()  && minEndInd > resultChain.getSecond())) {
                maxChain = resultChain.getFirst();
                minEndInd = resultChain.getSecond();
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Character character : maxChain) {
            stringBuilder.append(character);
        }
        return stringBuilder.toString();
    }

    private Pair<List<Character>, Integer> getLongestCommonChain(
            StringAsTableOfIndexes other, Character currCharacter,
            Set<Integer> currIndexesOfThisChain, Set<Integer> currIndexesOfOtherChain, List<Character> currChain
    ) {
        int endIndex = Integer.MAX_VALUE;
        Set<Character> nextCharacterSet =
                new HashSet<>(this.getCharacterSequenceMap().get(currCharacter).stream().toList());
        nextCharacterSet.retainAll(other.getCharacterSequenceMap().get(currCharacter));
        List<Character> maxChain = currChain;
        int counter = 0;
        for (Character nextCharacter : nextCharacterSet) {
            counter++;
            Set<Integer> nextCharacterIndexesOfThis
                    = currIndexesOfThisChain.stream().map(it -> it + 1).collect(Collectors.toSet());
            Set<Integer> nextCharacterIndexesOfOther
                    = currIndexesOfOtherChain.stream().map(it -> it + 1).collect(Collectors.toSet());
            nextCharacterIndexesOfThis.retainAll(this.getCharacterIndexesMap().get(nextCharacter));
            nextCharacterIndexesOfOther.retainAll(other.getCharacterIndexesMap().get(nextCharacter));
            if (nextCharacterIndexesOfThis.isEmpty() || nextCharacterIndexesOfOther.isEmpty()) continue;
            List<Character> nextChain = new ArrayList<>(currChain);
            nextChain.add(nextCharacter);
            Pair<List<Character>, Integer> resultChain =
                    getLongestCommonChain(
                            other, nextCharacter, nextCharacterIndexesOfThis,
                            nextCharacterIndexesOfOther, nextChain
                    );
            if (resultChain.getFirst().size() > maxChain.size() || (resultChain.getFirst().size() == maxChain.size() && resultChain.getSecond() < endIndex)) {
                maxChain = resultChain.getFirst();
                endIndex = resultChain.getSecond();

            }
        }
        if(counter == nextCharacterSet.size())//обрабатывается случай, когда это конечный элемент цепи
            //noinspection OptionalGetWithoutIsPresent (Этого не может быть)
            endIndex = currIndexesOfThisChain.stream().mapToInt(it -> it).min().getAsInt();

        return new Pair<>(maxChain, endIndex);
    }
}



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

    /*
        Здесь мы каждому уникальному символу в строке сопоставляем множество индексов. Далее эта таблица рекурсивно
        обходится как граф в глубину. При этом нужно проверять чтобы индексы следующего символа были ровно на 1 больше
        предыдущего, все индексы не удовлетворяющие этому условию исключаются из множества индексов для последнего узла.
        Условием окончания обхода является невозможность найти следующий символ с непустым множеством возможных индексов.
        Трудоёмкость:
            T = unknown
            сложно оценить
        Ресурсы:
            R=O(С*n) -- так как размер map-ов и размер их содержимого линейно зависит размера входных данных.
            + цепочки накапливающиеся в процессе рекурсии всегда соответствуют цепочкам в исходных данных
     */

    static public String longestCommonSubstring(String firs, String second) {
        StringAsTableOfIndexes representedSecond = new StringAsTableOfIndexes(second);
        StringAsTableOfIndexes representedFirst = new StringAsTableOfIndexes(firs);
        return representedFirst.recursiveSearchingLongestCommonSubstring(representedSecond);

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

    /*Реализация Решета Эратосфена https://en.wikipedia.org/wiki/Sieve_of_Eratosthenes
        T = O(n*ln(ln(n)))
        R = O(n)
        где n = limit
    */

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
