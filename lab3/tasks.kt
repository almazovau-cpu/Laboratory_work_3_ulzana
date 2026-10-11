package com.example.mablab3.lab3

// task 1
class Sensor(val id: String, var reading: Double) {
    init {
        require(id.isNotBlank()) { "ID cannot be blank" }
    }
    fun update(newReading: Double) {
        require(newReading.isFinite()) { "Reading must be finite" }
        reading = newReading
    }
    override fun toString(): String {
        return "Sensor(id=$id, reading=$reading)"
    }
}

// task 2
class Counter(start: Int = 0) {
    private var count = start
    fun value(): Int {
        return count
    }
    fun inc(step: Int = 1) {
        require(step > 0) { "Step must be positive" }
        count += step
    }
    fun reset() {
        count = 0
    }
}

// task 3, 6, 7
open class Vehicle(val brand: String) {
    init {
        require(brand.isNotBlank()) { "Brand cannot be blank" }
    }
    open fun info(): String {
        return "Vehicle: $brand"
    }
    open fun maxSpeedKmh(): Int {
        return 100
    }
}

class Car(brand: String, val doors: Int) : Vehicle(brand) {
    init {
        require(doors >= 2) { "Car must have at least 2 doors" }
    }

    override fun info(): String {
        return "Car $brand, doors=$doors"
    }

    override fun maxSpeedKmh(): Int {
        return 180
    }
}

class Bike(brand: String, val gears: Int) : Vehicle(brand) {
    init {
        require(gears >= 1) { "Bike must have at least 1 gear" }
    }
    override fun info(): String {
        return "Bike $brand, gears=$gears"
    }
    override fun maxSpeedKmh(): Int {
        return 30
    }
}

fun printFleet(fleet: List<Vehicle>) {
    for (v in fleet) {
        println(v.info())
    }
}

fun doorsOrZero(v: Vehicle): Int {
    if (v is Car) {
        return v.doors
    }
    return 0
}

// task 4
open class Employee(val name: String, val baseSalary: Int) {
    init {
        require(name.isNotBlank()) { "Name cannot be blank" }
        require(baseSalary >= 0) { "Salary cannot be negative" }
    }

    open fun pay(): Int = baseSalary

    open fun describe(): String = "$name: ${pay()}"
}

class Accountant(name: String, baseSalary: Int, val bonusPercent: Int) : Employee(name, baseSalary) {
    init {
        require(bonusPercent in 0..100) { "Bonus must be between 0 and 100" }
    }
    override fun pay(): Int {
        return super.pay() + (super.pay() * bonusPercent / 100)
    }
    override fun describe(): String {
        return "Accountant " + super.describe()
    }
}

class Intern(name: String, baseSalary: Int, val mentor: String) : Employee(name, baseSalary) {
    init {
        require(mentor.isNotBlank()) { "Mentor name cannot be blank" }
    }
    override fun pay(): Int {
        return super.pay() / 2
    }
}

// task 5
open class Parcel(val id: String, val weightKg: Double) {
    init {
        require(id.isNotBlank()) { "ID cannot be blank" }
        require(weightKg > 0) { "Weight must be positive" }
    }

    open fun shippingCost(): Double {
        return weightKg * 2.0
    }
}

class ExpressParcel(id: String, weightKg: Double) : Parcel(id, weightKg) {
    override fun shippingCost(): Double {
        return super.shippingCost() + 15.0
    }
}

class EconomyParcel(id: String, weightKg: Double) : Parcel(id, weightKg) {
    override fun shippingCost(): Double {
        return super.shippingCost() * 0.8
    }
}

fun totalShipping(parcels: List<Parcel>): Double {
    var total = 0.0
    for (p in parcels) {
        total += p.shippingCost()
    }
    return total
}

// task 8
open class Assessment(val title: String, val maxPoints: Int) {
    init {
        require(title.isNotBlank()) { "Title cannot be blank" }
        require(maxPoints > 0) { "Max points must be > 0" }
    }
    open fun scoreTowardGrade(raw: Int): Double {
        val safeRaw = raw.coerceIn(0, maxPoints)
        return safeRaw.toDouble() / maxPoints
    }
}

class Quiz(title: String, maxPoints: Int, val penaltyIfLate: Double) : Assessment(title, maxPoints) {
    init {
        require(penaltyIfLate in 0.0..0.5) { "Penalty must be between 0.0 and 0.5" }
    }

    override fun scoreTowardGrade(raw: Int): Double {
        val baseScore = super.scoreTowardGrade(raw)
        return baseScore * (1.0 - penaltyIfLate)
    }
}

class Project(title: String, maxPoints: Int, val bonusCap: Double) : Assessment(title, maxPoints) {
    init {
        require(bonusCap >= 0.0) { "Bonus cap must be >= 0" }
    }

    override fun scoreTowardGrade(raw: Int): Double {
        val baseScore = super.scoreTowardGrade(raw)
        if (raw > maxPoints) {
            val extraPoints = raw - maxPoints
            val bonusRatio = extraPoints.toDouble() / maxPoints
            val allowedBonus = bonusRatio.coerceAtMost(bonusCap)
            return baseScore + allowedBonus
        }
        return baseScore
    }
}

// task 9
open class Notification(private val payload: String) {
    init {
        require(payload.isNotBlank()) { "Payload cannot be blank" }
    }

    protected fun body(): String = payload

    open fun render(): String = body()
}

class EmailNotification(payload: String, val to: String) : Notification(payload) {
    override fun render(): String {
        return "Email to $to: ${body()}"
    }
}

class SmsNotification(payload: String, val phone: String) : Notification(payload) {
    override fun render(): String {
        return "SMS to $phone: ${body()}"
    }
}

fun sendAll(items: List<Notification>) {
    for (item in items) {
        println(item.render())
    }
}

// task 10
open class Ticket(val code: String, val basePrice: Int) {
    init {
        require(code.isNotBlank()) { "Code cannot be blank" }
        require(basePrice >= 0) { "Price cannot be negative" }
    }

    open fun price(): Int = basePrice
}

class AdultTicket(code: String, basePrice: Int) : Ticket(code, basePrice)

class ChildTicket(code: String, basePrice: Int, val guardianCode: String) : Ticket(code, basePrice) {
    init {
        require(guardianCode.isNotBlank()) { "Guardian code required" }
    }

    override fun price(): Int {
        return super.price() / 2
    }
}

class VipTicket(code: String, basePrice: Int, val loungeAccess: Boolean) : Ticket(code, basePrice) {
    override fun price(): Int {
        val extra = if (loungeAccess) 50 else 20
        return super.price() + extra
    }
}

fun checkout(tickets: List<Ticket>): Int {
    var sum = 0
    for (t in tickets) {
        sum += t.price()
    }
    return sum
}

fun childGuardianCodes(tickets: List<Ticket>): List<String> {
    val result = mutableListOf<String>()
    for (t in tickets) {
        if (t is ChildTicket) {
            result.add(t.guardianCode)
        }
    }
    return result
}

fun main() {
    // 1
    val s1 = Sensor("S-101", 22.5)
    val s2 = Sensor("S-102", 18.0)
    s1.update(25.0)
    println(s1)

    // 2
    val counter = Counter()
    counter.inc(2)
    println(counter.value())

    // 3
    val car = Car("Toyota", 4)
    val bike = Bike("Trek", 18)
    println(car.info())
    println(bike.info())

    // 4
    val list = listOf(
        Employee("Alice", 3000),
        Accountant("Bob", 4000, 10),
        Intern("Charlie", 2000, "Alice")
    )
    for (e in list) {
        println(e.describe())
    }

    // 5
    val parcels = listOf(
        Parcel("P1", 2.0),
        ExpressParcel("P2", 2.0),
        EconomyParcel("P3", 2.0)
    )
    println(totalShipping(parcels))

    // 6, 7
    val v: Vehicle = Car("Volvo", 4)
    printFleet(listOf(v, bike))
    println(doorsOrZero(v))

    // 8
    val q = Quiz("Kotlin", 100, 0.1)
    println(q.scoreTowardGrade(90))

    // 9
    val notes = listOf(
        EmailNotification("Hi", "mail@test.com"),
        SmsNotification("Code 123", "+12345")
    )
    sendAll(notes)

    // 10
    val tickets = listOf(
        AdultTicket("A-1", 100),
        ChildTicket("C-1", 100, "G-1")
    )
    println(checkout(tickets))
    println(childGuardianCodes(tickets))
}