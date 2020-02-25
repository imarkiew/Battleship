package Exceptions

case object NotValidUserInputForShipPlacement extends Exception {
  val message: String = "Improper input for ship placement ! Try again."
}