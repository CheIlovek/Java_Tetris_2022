package Tetris;

import javax.swing.*;
import java.io.*;
import java.util.*;
import Tetris.Panels.Stylization;
public class ScoreTable {

    private final HashMap<String, Integer> values = new HashMap<>();
    public int len;

    private final String SPLITTER = "/";
    private final String FILEPATH;

    public ScoreTable(String filename) {
        FILEPATH = filename;
        LinkedList<String> leaders = new LinkedList<>();
        String line = "";
        try (BufferedReader in = new BufferedReader(new FileReader(FILEPATH))) {
            while ((line = in.readLine()) != null)
                leaders.push(line);
        } catch (IOException ignored) {}
        if (line != null && line.equals(""))
            return;
        len = leaders.size();
        for (int i = len-1; i >= 0; i--) {
            String[] data = leaders.pop().split(SPLITTER,2);
            values.put(data[0],Integer.parseInt(data[1]));
        }
    }

    public void addNewScore(String name, int score) {
        values.put(name,score);
        try (BufferedWriter out = new BufferedWriter(new FileWriter(FILEPATH))) {
            values.forEach((k,v) -> {
                try {
                    out.write(k + SPLITTER + v);
                    out.newLine();
                } catch (IOException ignored) {}
            });
        } catch (IOException ignored) {}
    }

    public JLabel[] getLeaders() {
        String[] keys = values.keySet().toArray(new String[0]);
        Integer[] vals = values.values().toArray(new Integer[0]);

        int j;
        for (int i = 1; i < vals.length; i++) {
            int buffInt = vals[i];
            String buffStr = keys[i];
            for (j = i; j > 0 && buffInt < vals[j - 1]; j--) {
                vals[j] = vals[j - 1];
                keys[j] = keys[j - 1];
            }
            vals[j] = buffInt;
            keys[j] = buffStr;
        }
        JLabel[] leaders = new JLabel[vals.length];
        for (int i = 0; i < vals.length; i++) {
            leaders[vals.length - i - 1] = Stylization.getLabel(keys[i] + " - " + vals[i]);
        }
        return leaders;
    }
}
