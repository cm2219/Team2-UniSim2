package io.github.unisim;
/** A simple balance utility that can be updated on each render call.
*/

public class PlayerBalance {
  private int balance;
  private boolean isZero;

  /**Constructor
  * Create a new balance with an initial amount
  * @param initialBalance - starting balance
  */
  public PlayerBalance (int initialBalance) {
    this.balance = initialBalance;
    isZero = false;
  }

  /* Return current balance as an integer.
  * @return - balance in integer form
  */
  public int getBalance(){
    return this.balance;
  }

  /*Update balance
  */
  public void updateBalance(int changeAmt) {
    this.balance += changeAmt;
    if balance <= 0 {
      this.isZero = true;
    }
  }
