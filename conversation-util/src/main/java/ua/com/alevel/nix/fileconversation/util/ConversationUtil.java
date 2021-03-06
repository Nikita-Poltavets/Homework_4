package ua.com.alevel.nix.fileconversation.util;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class ConversationUtil {

    public static String splitConversation(String file) {
        return Arrays.stream(file.split(" ")).map(s -> s.concat("\n")).collect(Collectors.joining());
    }

    public static String replaceConversation(String file) {
        return file.replaceAll("\\?", "X");
    }


    public static String countSymbolsConversation(String file){
        String result = "";
        Map<Character, Integer> map = new HashMap<>();
        int occurrence;
        for (int i = 0; i < file.length(); i++) {
            if(!map.containsKey(file.charAt(i))) {
                occurrence = StringUtils.countMatches(file, file.charAt(i));
                map.put(file.charAt(i), occurrence);
            }
        }

        Map <Character, Integer> mapResult = sortMapByValueForSymbol(map);

        for (Map.Entry<Character, Integer> pair: mapResult.entrySet()) {
            result += pair.getValue() + " ---> " + pair.getKey() + "\r\n";
        }

        return result;
    }

    public static String countWordsConversation(String file){
        String result = "", newFile;

        Map<String, Integer> map = new HashMap<>();

        ArrayList<String> arrayWords = new ArrayList<>();

        int occurrence;


        newFile = file.replaceAll("\\pP|\\s{2,}|\r\n|\n", "");

        for (String word : newFile.split(" ")){
            arrayWords.add(word);
        }

        for (int i = 0; i < arrayWords.size(); i++) {
            if(!map.containsKey(arrayWords.get(i))) {
                occurrence = StringUtils.countMatches(file, arrayWords.get(i));
                map.put(arrayWords.get(i), occurrence);
            }
        }

        Map <String, Integer> mapResult = sortMapByValueForWord(map);

        for (Map.Entry<String, Integer> pair: mapResult.entrySet()) {
            result += pair.getValue() + " ---> " + pair.getKey() + "\r\n";
        }

        return result;
    }

    public static String reverseConversation(String file){
        String result = "";
        ArrayList<String> arrayWords = new ArrayList<>();
        List<Integer> indexSlashN = new ArrayList<>();

        for (int pos = file.indexOf("\r\n"); pos != -1; pos = file.indexOf("\r\n", pos + 1)) {
            indexSlashN.add(pos);
        }

        for (String word : file.split("\\s")){
            arrayWords.add(word);
        }

        for (int i = arrayWords.size() - 1; i >= 0; i--) {
            result += arrayWords.get(i) + " ";
        }

        for (int i = 0; i < result.length(); i++) {
            for (int j = 0; j < indexSlashN.size(); j++) {
                if(indexSlashN.get(j) == i){
                    result = result.substring(0, i) + "\r\n" + result.substring(i);
                }
            }
        }

        return result;
    }

    public static String findRootsConversation(String file){
        String result = "";
        ArrayList<String> arrayWords = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        int occurrence;

        String[] roots = {"ama", "astr", "aud",  "auto", "belli", "bio", "brev", "cap", "centri", "chrono", "cide", "cor", "cardi", "cred", "deca", "derm", "dorm", "duct", "fac", "fic", "fid", "fin", "flex", "fort", "fract", "frag", "gam", "gen", "geo", "gress", "grad", "graph", "gram",
                          "greg", "hema", "hemo", "hydra", "hydro", "ject", "loqu", "magna", "mania", "mar", "mega", "meter", "micro", "migra", "mit", "mis", "multi", "nov", "omni", "onym", "ortho", "pan", "ped", "pod", "pel", "puls", "phil", "phobia", "port", "proto", "psych", "rupt",
                          "scope", "scrib", "script", "spir", "struct", "tele", "temp", "terra", "therm", "tox", "tract", "uni", "vent", "vera", "veri", "vict", "vinc", "voc", "vor", "zo"};

        for (String word : roots){
            arrayWords.add(word);
        }

        for (int i = 0; i < arrayWords.size(); i++) {
            if(!map.containsKey(arrayWords.get(i))) {
                occurrence = StringUtils.countMatches(file, arrayWords.get(i));
                if(occurrence != 0) {
                    map.put(arrayWords.get(i), occurrence);
                }
            }
        }

        Map <String, Integer> mapResult = sortMapByValueForWord(map);

        for (Map.Entry<String, Integer> pair: mapResult.entrySet()) {
            result += pair.getValue() + " ---> " + pair.getKey() + "\r\n";
        }

        return result;
    }


    public static Map<Character, Integer> sortMapByValueForSymbol(final Map<Character, Integer> charCounts) {

        return charCounts.entrySet()

                .stream()

                .sorted((Map.Entry.<Character, Integer>comparingByValue().reversed()))

                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }


    public static Map<String, Integer> sortMapByValueForWord(final Map<String, Integer> charCounts) {

        return charCounts.entrySet()

                .stream()

                .sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))

                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
