package com.example.calencon.business.geneticAlgorithm

import com.example.calencon.data.Event
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit
import kotlin.time.days

class FitnessCalc {
    // Random generator
    var random = Random()

    companion object {
        // Number of specimens in the population
        var populationSize = 0

        // Number of specimens in the selection
        var selectionSize = 0

        // Power of mutations
        // 0 - no mutations
        // 100 - mutations up to whole gene value
        // 200 - mutations up to twice gene value
        var MaxMutationPercent = 0.0

        // Likelyhood of mutation
        // 0 - no mutations, 100 - mutations for all cases
        var mutationProbability = 0

        // Maximal affinity that is considered
        // for the solution found
        var epsilon = 0.0

        // Number of crossover points of parent's class tables
        var numberOfCrossoverPoints: Int = 0

        // Probability that crossover will occur
        var crossoverProbability = 0

        // Environment
        //list of previous events
        var adaptationEnvironment = mutableListOf<Event>()
    }

    fun setStaticObjects(
        population: Int,
        CrossoverPoints: Int,
        maxMu: Double,
        muSize: Int,
        crossProbability: Int,
        mProbability: Int,
        selection: Int,
        environment: List<Event>,
        maxAffinity: Double
    ) {

        populationSize = population
        numberOfCrossoverPoints = CrossoverPoints
        mutationProbability = muSize
        crossoverProbability = crossProbability
        mutationProbability = mProbability
        MaxMutationPercent = maxMu
        selectionSize = selection
        adaptationEnvironment = environment.toMutableList()
        epsilon = maxAffinity
    }

    fun fitnessScore(event: Event): Float {
        return 0f
    }

    // Select the best specimens from the population
    fun selectBest() {
        //Method Select is implemented by performing two main actions.
        //First of all, the method sorts the population in way the array contains the best specimens
        // at the beginning and the worst specimens at the end. At the second step, the best
        // specimens are being copied to selection for future calculations
    }

    // Sort the population by affinity
    fun sort() {}

    //generate initial base
    //gerar apenas datas futuras, com horarios entre 8-21h
    private fun generateInitialPopulation() {
        val population = mutableListOf<Specimen>()

        for (i in 0..populationSize) {
            val dtStart = createEventDatabase()

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

    private fun createEventDatabase(): Long {
        val aDay = TimeUnit.DAYS.toMillis(1)
        val now = Date().time
        val threeMonthsInFuture = Date(now + aDay * 92)
        return between(Date(), threeMonthsInFuture)
    }

    //three hours default
    private fun createEndEvent(startDate: Long): Long {
        val aHour = TimeUnit.HOURS.toMillis(3)
        return startDate + aHour
    }

    private fun between(startInclusive: Date, endExclusive: Date): Long {
        val rand = kotlin.random.Random(System.nanoTime())
        val newDate = Calendar.getInstance()

        do {
            val randNumber = (0..1).random(rand)
            val hour = (8..21).random(rand)
            val minute = if (randNumber == 0) 0 else 30
            val day = (1..28).random(rand)
            val month = (startInclusive.month..endExclusive.month).random(rand)
            newDate.set(startInclusive.year, month, day, hour, minute)
        } while (newDate.timeInMillis < startInclusive.time)

        return newDate.timeInMillis
    }

    // Reproduce new specimen on base of two parents
    fun reproduceNew(a: Specimen, b: Specimen): Specimen? {
        val s = Specimen("", a.getSpecimen().calendar_id)

        // Iherit genes as the average oh the parents' genes
//        s.Genes.get(0) = (a.Genes.get(0) + b.Genes.get(0)) / 2

        // Mutate if likelyhoo allows
//        val ml: Int = rnd.nextInt(101)
//        if (ml <= MutationLikelyhoodPercent) {
//            Mutate(s)
//        }

        // Calculate Affinity for new specimen
        s.setFitnessScore()
        return s

        //After creating, the gene mutates (or not) according to the values of MutationLikelyhoodPercent and MaxMutationPercent.
    }

    // Mutate the specimen
    fun mutate() {}

    // Generate population by reproduction of selection
    fun generate() {}
}