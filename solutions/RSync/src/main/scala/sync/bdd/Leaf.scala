package sync.bdd

class Leaf(protected var assignments: Set[Assignment], t_OwnerSubtreeForOne: Subtree, t_OwnerSubtreeForZero: Subtree, t_OwnerBDD: BDD) extends Tree(t_OwnerSubtreeForOne, t_OwnerSubtreeForZero, t_OwnerBDD) {

  def getAssignments(): Set[Assignment] = {
    assignments
  }

  def addAssignments(a: Assignment): Unit = {
    require(a != null)
    require(!assignments.contains(a))
    assignments += a
    +this syncAddAssignments (a)
  }

  def removeAssignments(a: Assignment): Unit = {
    require(a != null)
    require(assignments.contains(a))
    assignments -= a
    +this syncRemoveAssignments (a)
  }

  override def toString(): String = {
    "Leaf:"
  }

}



    