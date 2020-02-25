package Exceptions

case object NotValidUserInputForAttackCoordinate extends Exception {
  val message: String = "Improper input for attack coordinate ! Try again"
}
