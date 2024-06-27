package it.polimi.ingsw;

import java.io.*;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {        // Set the console encoding to UTF-8
        // Set the console encoding to UTF-8
        System.out.println("To print emoji on Windows, first run: 4" +
                "chcp 65001");
        System.out.println("|🤑|");
        System.out.println("|aa|");
        System.out.println("Hello World!🔥");

        while (true) {
            // Clear the screen and move the cursor to the home position
            System.out.print("\033[H\033[2J");
            System.out.flush();

            // Request cursor position
            requestCursorPosition(System.out);

            // Read the cursor position response from the terminal
            StringBuilder sb = new StringBuilder();
            readResponse(System.in, sb);

            // Parse the cursor position response
            String size = sb.toString();
            if (size.startsWith("\u001b[") && size.endsWith("R")) {
                String[] parts = size.substring(2, size.length() - 1).split(";");
                if (parts.length == 2) {
                    int rows = Integer.parseInt(parts[0]);
                    int cols = Integer.parseInt(parts[1]);
                    System.err.printf("Rows: %d, Cols: %d%n", rows, cols);
                }
            } else {
                System.err.println("Failed to get terminal size.");
            }

            // Sleep for a while before the next iteration
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Configs.printStackTrace(e);
            }
        }
    }

    private static void requestCursorPosition(OutputStream os) throws IOException {
        // ANSI escape code to request cursor position
        final String GET_CURSOR_POS = "\u001b[6n";
        os.write(GET_CURSOR_POS.getBytes());
        os.flush();
    }

    private static void readResponse(InputStream in, StringBuilder sb) throws IOException {
        in.mark(1024); // Mark the current position in the stream
        long startTime = System.currentTimeMillis();
        while (true) {
            if (in.available() > 0) {
                int ch = in.read();
                sb.append((char) ch);
                if (ch == 'R') {
                    break;
                }
            } else {
                // Check for timeout (1 second)
                if (System.currentTimeMillis() - startTime > 1000) {
                    break;
                }
                try {
                    Thread.sleep(50); // Briefly wait for more data
                } catch (InterruptedException e) {
                    Configs.printStackTrace(e);
                }
            }
        }
        in.reset(); // Reset to the marked position in the stream
    }
}


