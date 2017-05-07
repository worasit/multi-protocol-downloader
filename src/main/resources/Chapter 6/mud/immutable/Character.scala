package mud.immutable

trait Character {
  val name: String
  def items: List[Item]
  def currentRoom: String
}