package it.polimi.ingsw.TUI_tests;

import java.io.IOException;

public class windowSize {

    public static void main(String[] args) throws IOException {
        int count = 0;
        System.out.println("Loading...");
        
        while (true) {
            //System.out.print("\033[H\033[2J");
            System.out.print("\u001b[1000D" + count + "%");
            System.out.flush();
            count++;
            count %= 100;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
