package it.polimi.ingsw;

import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Thread inputThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                if (scanner.hasNextLine()) {
                    String input = scanner.nextLine();
                    System.out.println("1- You typed: " + input);
                }
            }
        });

        Thread inputThread2 = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                if (scanner.hasNextLine()) {
                    String input = scanner.nextLine();
                    System.out.println("2- You typed: " + input);
                }
            }
        });

        inputThread.start();
        inputThread2.start();

    }
}
