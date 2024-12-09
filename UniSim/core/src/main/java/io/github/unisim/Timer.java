package io.github.unisim;

/**
 * A simple timer utility that can be updated on each render call.
 */
public class Timer {
  private float remainingTime;
  private float initialTime;
  private boolean hasFinished;
  private int eventNumber;
  private int initialEvents;
  private int remainingEvents;

  /**
   * Create a new timer set to count down from an initial number of milliseconds
   * for an initial number
   * of events.
   * 
   * @param initialTime   - The number of milliseconds before the next event
   * @param initialEvents - The number of events before the timer ends
   */
  public Timer(float initialTime, int initialEvents) {
    this.initialTime = initialTime;
    remainingTime = initialTime;
    hasFinished = false;
    this.initialEvents = initialEvents;
    remainingEvents = initialEvents;
  }

  /**
   * Removes a provided timestep from the counter and returns whether the timer
   * has stopped.
   * Decrements the remaining number of events and resets the remaining time until
   * the next event.
   * 
   * @param deltaTime - the time in milliseconds to remove from the counter
   * @return - true if the timer is running, the time has been decremented and
   *         there are remaining events,
   *         false otherwise.
   */
  public boolean tick(float deltaTime) {
    remainingTime -= deltaTime;
    if (remainingTime > 0) {
      return true;
    } else {
      if (remainingEvents > 0) {
        eventNumber += 1;
        remainingEvents -= 1;
        remainingTime = initialTime;
        return true;
      } else {
        hasFinished = true;
        return false;
      }
    }
  }

  /**
   * Reset the timer to its' initial time value and the initial number of events.
   */
  public void reset() {
    remainingTime = initialTime;
    remainingEvents = initialEvents;
    hasFinished = false;
  }

  /**
   * Return the total remaining time until the timer ends in a String
   * representation.
   * 
   * @return - time before timer ends in the form MM:SS
   */
  public String getRemainingTime() {
    // get the number of minutes and seconds from the total remaining time in
    // milliseconds.
    int totalRemainingTime = (int) (initialTime * (remainingEvents - 1) + remainingTime);
    int remainingMinutes = (int) ((totalRemainingTime + 1000) / 60_000);
    int remainingSeconds = (int) Math.ceil(totalRemainingTime / 1000 - 60 * remainingMinutes);

    return formatNum(remainingMinutes) + ":" + formatNum(remainingSeconds);
  }

  /**
   * Format a number of minutes or seconds to always have a length of two digits.
   * This is done by prepending a zero if the number has only one digit.
   * 
   * @param num - the number to convert to a formatted string
   * @return - a formatted string with length at least two.
   */
  private String formatNum(int num) {
    if (num < 10) {
      return "0" + num;
    }
    return String.valueOf(num);
  }

  /**
   * Return whether the timer is still running or has reached zero.
   * 
   * @return - true if the timer is running, false if the remaining time has
   *         reached zero
   */
  public boolean isRunning() {
    return !hasFinished;
  }

  /**
   * Return the number of events that are due to have happened at this time.
   * 
   * @return - an integer between 0 and 9.
   */
  public int getEventNumber() {
    return eventNumber;
  }
}
