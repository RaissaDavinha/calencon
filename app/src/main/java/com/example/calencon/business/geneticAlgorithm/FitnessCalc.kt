package com.example.calencon.business.geneticAlgorithm

import com.example.calencon.data.WeekScore
import java.time.DayOfWeek
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random
import kotlin.random.nextInt

class FitnessCalc {
    companion object {
        var populationSize = 0
        var selectionSize = 0
        var mutationProbability = 0

        // Maximal affinity that is considered for the solution found
        var epsilon = 0.0

        // List of previous events
        var adaptationEnvironment = mutableListOf<Specimen>()
    }

    fun setStaticObjects(
        population: Int,
        muSize: Int,
        mProbability: Int,
        selection: Int,
        environment: List<Specimen>,
        maxAffinity: Double
    ) {
        populationSize = population
        mutationProbability = muSize
        mutationProbability = mProbability
        selectionSize = selection
        adaptationEnvironment = environment.toMutableList()
        epsilon = maxAffinity
    }

    fun fitnessScore(event: Specimen): Double {
        //receber adaptation environment .orderBy("dtstart", Query.Direction.ASCENDING)
        //ignora todos os eventos antes da data atual
        //verificar todos os eventos proximos que comecam antes do specime, se este esta entre o dtstart e o dtend
        //verificar evento logo apos o specime, se comeca antes do specime acabar
        val weekDayScore: Double
        var eventOverlapScore = 1.0
        val environment = mutableListOf<Specimen>()
        val day = Calendar.getInstance()

        if(!adaptationEnvironment.isNullOrEmpty()) {
            environment.addAll(adaptationEnvironment.takeLastWhile { it.getDtStart() > Calendar.getInstance().timeInMillis })
        }

        if(!environment.isNullOrEmpty()) {
            environment.find { it.getDtStart() < event.getDtStart()}?.let {
                if (it.getDtEnd() > event.getDtStart() && eventOverlapScore > 0) {
                    eventOverlapScore -= 0.5
                }
            }
        }

        //verificar dia da semana
        day.timeInMillis = event.getDtStart()

        weekDayScore = when(day.get(Calendar.DAY_OF_WEEK)) {
            DayOfWeek.MONDAY.value -> WeekScore.MONDAY.dayScore
            DayOfWeek.TUESDAY.value -> WeekScore.TUESDAY.dayScore
            DayOfWeek.WEDNESDAY.value -> WeekScore.WEDNESDAY.dayScore
            DayOfWeek.THURSDAY.value -> WeekScore.THURSDAY.dayScore
            DayOfWeek.FRIDAY.value -> WeekScore.FRIDAY.dayScore
            DayOfWeek.SATURDAY.value -> WeekScore.SATURDAY.dayScore
            DayOfWeek.SUNDAY.value -> WeekScore.SUNDAY.dayScore
            else -> 0.0
        }

        return weekDayScore + eventOverlapScore
    }

    // Select the best specimens from the population
    private fun selectBest(population: MutableList<Specimen>): List<Specimen> {
        sort(population)
        //adicionar proximmidade do evento ao calculo

        return population.take(selectionSize)
    }

    // Sort population by score
    private fun sort(population: MutableList<Specimen>) {
        var temp: Specimen

        for (i in 0 until populationSize) {
            for (j in 0 until populationSize) {
                if (population[i].getFitnessScore() < population[j].getFitnessScore()) {
                    temp = population[i]
                    population[i] = population[j]
                    population[j] = temp
                }
            }
        }
    }

    // Generate initial base
    fun generateInitialPopulation() {
        val population = mutableListOf<Specimen>()

        for (i in 0..populationSize) {
            val dtStart = createStartEvent()

            val item = Specimen(
                userId = "",
                calendarId = 1,
                dtStart = dtStart,
                dtEnd = createEndEvent(dtStart),
                eventTitle = "specimen $i",
                eventDuration = null,
                allDay = false,
                rRule = "",
                rDate = "",
                userAvailability = 0
            )

            //calcular afinidade
            item.setFitnessScore()
            population.add(item)
        }
    }

    // Generate only future dates, with hour between 8-21h, up tp 3 months in the future
    private fun createStartEvent(): Long {
        val aDay = TimeUnit.DAYS.toMillis(1)
        val now = Calendar.getInstance().timeInMillis

        val threeMonthsInFuture = Calendar.getInstance()
        threeMonthsInFuture.timeInMillis = (now + aDay * 92)
        return between(Calendar.getInstance(), threeMonthsInFuture)
    }

    // One hour default
    private fun createEndEvent(startDate: Long): Long {
        val aHour = TimeUnit.HOURS.toMillis(1)
        return startDate + aHour
    }

    private fun between(startInclusive: Calendar, endExclusive: Calendar): Long {
        val newDate = Calendar.getInstance()

        do {
            val randNumber = Random.nextInt(0..1)
            val hour = Random.nextInt(8..21)
            val minute = if (randNumber == 0) 0 else 30
            val day = Random.nextInt(1..28)
            val month = Random.nextInt(
                range = startInclusive.get(Calendar.DAY_OF_MONTH)..endExclusive.get(
                    Calendar.MONTH
                )
            )
            newDate.set(startInclusive.get(Calendar.YEAR), month, day, hour, minute)
        } while (newDate.timeInMillis < startInclusive.timeInMillis)

        return newDate.timeInMillis
    }

    // Reproduce new specimen on base of two parents
    private fun produceNew(a: Specimen, b: Specimen): Specimen {
        val s = Specimen("", a.getSpecimen().calendar_id)
        val mp = Random.nextInt(101)

        when (Random.nextInt(101)) {
            // 45%
            in (0..45) -> {
                s.setStartHour((a.getHour() + b.getHour()) / 2)
            }
            // 40%
            in (46..85) -> {
                s.setDay((a.getDay() + b.getDay()) / 2)
            }
            // 15%
            in (86..100) -> {
                s.setMonth((a.getMonth() + b.getMonth()) / 2)
            }
        }
        s.setDuration(TimeUnit.HOURS.toMillis(1))           //update end event date

        // Mutate if probability allows
        if (mp <= mutationProbability) {
            mutate(s)
        }

        // Calculate Affinity for new specimen
        s.setFitnessScore()
        return s
    }

    // Mutate the specimen
    private fun mutate(a: Specimen) {

        when (Random.nextInt(101)) {
            // 45%
            in (0..45) -> {
                val hour = Random.nextInt(8..21)
                val minute = if (Random.nextInt(0..1) == 0) 0 else 30
                a.setStartHour(hour, minute)
            }
            // 40%
            in (46..85) -> {
                val day = Random.nextInt(1..28)
                a.setDay(day)
            }
            // 15%
            in (86..100) -> {
                val threeMonthsInFuture = Calendar.getInstance()
                val aDay = TimeUnit.DAYS.toMillis(1)
                val now = Calendar.getInstance().timeInMillis
                threeMonthsInFuture.timeInMillis = (now + aDay * 92)

                val month = Random.nextInt(
                    range = Calendar.getInstance()
                        .get(Calendar.DAY_OF_MONTH)..threeMonthsInFuture.get(
                        Calendar.MONTH
                    )
                )
                a.setMonth(month)
            }
        }

        a.setDuration(TimeUnit.HOURS.toMillis(1))
    }

    // Generate population by reproduction of selection
    fun generate(population: MutableList<Specimen>): MutableList<Specimen> {
        val newGeneration = mutableListOf<Specimen>( )

        // Copy best instances from the selection to keep them in new generation
        newGeneration.addAll(selectBest(population))

        // Creates new specimens by reproducing two parents
        // Parents are selected randomly from the selection.
        var childIndex = selectionSize
        var parent1Index: Int
        var parent2Index: Int

        while (childIndex < populationSize) {
            do {
                parent1Index = Random.nextInt(population.size)
                parent2Index = Random.nextInt(population.size)
            } while (parent1Index == parent2Index)

            // Creates new specimen
            newGeneration[childIndex] = produceNew(population[parent1Index], population[parent2Index])
            childIndex++
        }

        return newGeneration
    }
}