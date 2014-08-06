package ru.xakep.inspector;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.stream.Collectors;

public class Inspector {

    private ScriptEngine engine;

    public static void main(String... args) {
        Inspector inspector = new Inspector();
        try {
            inspector.initEngine("script.js");
            inspector.loadLogs("logs/");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void initEngine(String fileName) throws Exception {
        ScriptEngineManager engineManager = new ScriptEngineManager();
        engine = engineManager.getEngineByName("nashorn");
        engine.eval(new FileReader(fileName));
    }

    public void loadLogs(String dir) throws Exception {
        try (FileReader fr = new FileReader(dir + "log.txt");
             BufferedReader br = new BufferedReader(fr)) {
            br.lines().map(s -> s.split(" ")).filter(strings -> strings[2].contains("vkontakte"))
                    .collect(Collectors.groupingBy(strings -> strings[1], Collectors.counting()))
                    .forEach(this::processBadUsers);
        }
    }

    public void executeScript(String userName) throws Exception {
        Invocable invocable = (Invocable) engine;
        invocable.invokeFunction("punish", userName);
    }

    public void processBadUsers(String userName, Long visitCount) {
        if (visitCount > 3) {
            try {
                executeScript(userName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
