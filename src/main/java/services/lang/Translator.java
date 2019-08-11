package services.lang;

import org.springframework.util.StringUtils;

import java.util.HashMap;

public class Translator {
    public static String toRussian(String original) {
        String translate = original.toLowerCase();

        HashMap<String, String> dictionary = new HashMap<>();
        dictionary.put("action", "экшен");
        dictionary.put("sci-fi", "фантастика");
        dictionary.put("drama", "драма");
        dictionary.put("adventure", "приключения");

        for (String word : dictionary.keySet()) {
            translate = translate.replace(word, dictionary.get(word));
        }

        return translate;
    }

    public static String translitToRussian(String original) {
        HashMap<String, String> pairsBefore = new HashMap<>();
        pairsBefore.put("ch", "к");

        HashMap<String, String> pairs = new HashMap<>();
        pairs.put("ld", "льд");
        pairs.put("sh", "ш");
        pairs.put("lt", "льт");
        pairs.put("sch", "ш");
        pairs.put("ll", "ль");
        pairs.put("hn", "н");

        HashMap<String, String> consonants = new HashMap<>();
        consonants.put("p", "п");
        consonants.put("s", "с");
        consonants.put("r", "р");
        consonants.put("n", "н");
        consonants.put("j", "дж");
        consonants.put("w", "в");
        consonants.put("g", "г");
        consonants.put("d", "д");
        consonants.put("l", "л");
        consonants.put("z", "ц");
        consonants.put("m", "м");
        consonants.put("x", "кс");
        consonants.put("f", "ф");
        consonants.put("h", "х");
        consonants.put("b", "б");

        HashMap<String, String> vowelsOpen = new HashMap<>();
        vowelsOpen.put("a", "э");
        vowelsOpen.put("o", "о");
        vowelsOpen.put("e", "э");
        vowelsOpen.put("i", "и");

        HashMap<String, String> vowelsClosed = new HashMap<>();
        vowelsClosed.put("a", "а");
        vowelsClosed.put("o", "о");
        vowelsClosed.put("e", "е");
        vowelsClosed.put("i", "ай");

        HashMap<String, String> vowelsClosedPairs = new HashMap<>();
        vowelsClosedPairs.put("ae", "а");

        String[] words = original.toLowerCase().replace(",", "").split(" ");
        String translate = "";
        int i = 1;
        for (String word : words) {
            String lastLetter = word.substring(word.length() - 1);
            if (vowelsOpen.keySet().contains(lastLetter)) {
                if (lastLetter.equals("e")) {
                    word = word.substring(0, word.length() - 1);
                }
                for (String letter : vowelsOpen.keySet()) {
                    word = word.replace(letter, vowelsOpen.get(letter));
                }
            } else {
                for (String letter : vowelsClosedPairs.keySet()) {
                    word = word.replace(letter, vowelsClosedPairs.get(letter));
                }
                for (String letter : vowelsClosed.keySet()) {
                    word = word.replace(letter, vowelsClosed.get(letter));
                }
            }
            for (String letter : pairsBefore.keySet()) {
                word = word.replace(letter, pairsBefore.get(letter));
            }
            for (String letter : pairs.keySet()) {
                word = word.replace(letter, pairs.get(letter));
            }
            for (String letter : consonants.keySet()) {
                word = word.replace(letter, consonants.get(letter));
            }
            String str = StringUtils.capitalize(word.trim());
            if (i % 2 == 0) {
                str += ",";
            }
            translate = translate.concat(str + " ");
            i++;
        }

        return translate.substring(0, translate.length() - 1);
    }
    public static boolean isCyrillic(String s) {
        boolean result = false;
        for (char a : s.toCharArray()) {
            if (Character.UnicodeBlock.of(a) == Character.UnicodeBlock.CYRILLIC) {
                result = !result;
                break;
            }
        }
        return result;
    }
}
