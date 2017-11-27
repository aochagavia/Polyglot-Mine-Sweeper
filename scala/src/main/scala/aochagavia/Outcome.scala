package aochagavia

sealed trait Outcome
case object Victory extends Outcome
case object Defeat extends Outcome
case object Ongoing extends Outcome
