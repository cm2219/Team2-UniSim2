package Testing;

//package io.github.unisim;

import java.util.Random;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

/**
 * Generates data to be displayed at each new event.
 */
public class Event {
    private int money;
    private double points;
    private String title;
    private String description;

    /**
     * Create a new event, which attributes are based on the event number.
     *
     * Event zero is triggered at the start of gameplay, which has no money or
     * points and
     * title and description provide the user information on how to play.
     *
     * Even-numbered events are triggered at the start of each new year. These
     * events
     * have positive money, based on the user's score, no points and a title and
     * description
     * about the increase in money.
     *
     * Odd-numbered events are triggered halfway through each year. The attributes
     * of these
     * events are randomly selected from a csv file: randomEvents.csv
     * 
     * @param eventNumber - The current event number
     * @param score       - The user's current score
     */
    public Event(int eventNumber, double score) {

        if (eventNumber == 0) {
            money = 0;
            points = 0;
            title = "Tutorial title";
            description = "Tutorial description";
        }

        else if (eventNumber % 2 == 0) {
            money = (int) (10_000 * score);
            points = 0;
            title = "Funding for year " + Integer.toString(eventNumber / 2 + 1);
            description = "Currently your students are " + Integer.toString((int) (score * 100))
                    + "% satisfied and your annual funding reflects this. You have recieved £" + Integer.toString(money)
                    + " this year. Increase your student satisfaction to get more funding next year.";
        }

        else {
            Random rand = new Random();
            int randInt = rand.nextInt(10);

            File file = new File("data/randomEvents.csv");

            if (!file.exists()) {
                System.out.println("CSV file not found: randomEvents.csv");
                return;
            }

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;

                // Read each line from the file
                int lineNumber = 0;
                while ((line = br.readLine()) != null) {
                    if (lineNumber == randInt) {
                        String[] parts = line.split(",");
                        if (parts.length == 4) {
                            title = parts[0].trim();
                            description = parts[1].trim();
                            money = Integer.parseInt(parts[2].trim());
                            points = Double.parseDouble(parts[3].trim());
                        }
                    }
                    lineNumber += 1;
                }
            } catch (IOException e) {
                System.out.println("An error occurred while reading the file: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Invalid format in the file. Please check the file content.");
            }
        }
    }

    /**
     * Return the title of the event.
     * 
     * @return - a string.
     */
    public String getEventTitle() {
        return title;
    }

    /**
     * Return the description of the event.
     * 
     * @return - a string.
     */
    public String getEventDescription() {
        return description;
    }

    /**
     * Return the money value of the event.
     * 
     * @return - an integer.
     */
    public int getEventMoney() {
        return money;
    }

    /**
     * Return the points value of the event.
     * 
     * @return - a double between -1 and 1.
     */
    public double getEventPoints() {
        return points;
    }

}