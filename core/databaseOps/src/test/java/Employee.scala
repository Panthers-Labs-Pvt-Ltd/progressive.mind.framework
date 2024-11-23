package test

case class Employee(
                     private var _id: Int,
                     private var _name: String,
                     private var _age: Int,
                     private var _salary: Double
                   ) {
  // Getters
  def id: Int = _id
  def name: String = _name
  def age: Int = _age
  def salary: Double = _salary

  // Setters
  def id_=(value: Int): Unit = _id = value
  def name_=(value: String): Unit = _name = value
  def age_=(value: Int): Unit = _age = value
  def salary_=(value: Double): Unit = _salary = value
}
