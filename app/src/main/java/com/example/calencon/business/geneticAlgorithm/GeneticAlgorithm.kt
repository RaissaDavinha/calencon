package com.example.calencon.business.geneticAlgorithm

import com.example.calencon.data.Event
import java.time.LocalDate

/*
The basic process for a genetic algorithm is:
* Initialization - Create an initial population. This population is usually randomly generated and can be any desired size, from only a few individuals to thousands.

* Evaluation - Each member of the population is then evaluated and we calculate a 'fitness' for that individual. The fitness value is calculated by how well it fits with our desired requirements.
These requirements could be simple, 'faster algorithms are better', or more complex, 'stronger materials are better but they shouldn't be too heavy'. (Avalie a adequação f(x) de cada cromossoma x da população)

* Selection - We want to be constantly improving our populations overall fitness. Selection helps us to do this by discarding the bad designs and only keeping the best individuals in the population.
There are a few different selection methods but the basic idea is the same, make it more likely that fitter individuals will be selected for our next generation.

* Crossover - During crossover we create new individuals by combining aspects of our selected individuals. We can think of this as mimicking how sex works in nature.
The hope is that by combining certain traits from two or more individuals we will create an even 'fitter' offspring which will inherit the best traits from each of it's parents.

* Mutation - We need to add a little bit randomness into our populations' genetics otherwise every combination of solutions we can create would be in our initial population.
Mutation typically works by making very small changes at random to an individuals genome.

* And repeat! - Now we have our next generation we can start again from step two until we reach a termination condition.
*/

class GeneticAlgorithm {
    private val geneticAlgorithmCalc = GACalc()
    private val populationSize = 500
    private val selectionSize = 80
    private val mutationProbability = 80
    private var adaptationEnvironment = mutableListOf<Specimen>()
    private var currentPopulation = mutableListOf<Specimen>()

    // Initializes genetic algorithm
    fun initializeGeneticAlgorithm(events: MutableMap<LocalDate, List<Event>>) {
        events.forEach { (_, eventList) ->
            eventList.forEach { event ->
                val specimen = Specimen(event.user_id, event.calendar_id, event.title, event.dtstart, event.dtend, event.duration, event.all_day, event.rrule, event.rdate, event.availability)
                adaptationEnvironment.add(specimen)
            }
        }

        // Set up options for the algorithm
        // Generate initial population
        geneticAlgorithmCalc.setStaticObjects(populationSize, mutationProbability, selectionSize, adaptationEnvironment)
        currentPopulation = geneticAlgorithmCalc.generateInitialPopulation()
    }

    // Starts and executes algorithm
    fun run(): Long {
        var currentSelection = mutableListOf<Specimen>()
        var currentProximity: Double
        val maxIterations = 200
        val maxScore = 3.00

        for (i in 0..maxIterations) {
            currentSelection = geneticAlgorithmCalc.selectBest(currentPopulation).toMutableList()
            currentProximity = currentSelection[0].getFitnessScore()

            println("Current_Iteration " + i + "\t" +
                        "Current_Proximity " + currentProximity + "\t" +
                        "Current_Selection " + currentSelection[0].getHour() + ":" + currentSelection[0].getMinutes() + "\t" + currentSelection[0].getDay() + "/" + currentSelection[0].getMonth()
            )

//            if (currentProximity == maxScore) break

            currentPopulation = geneticAlgorithmCalc.generate(currentSelection)
        }

        //adicionar proximmidade do evento ao calculo
        currentSelection.sortWith(compareBy { it.getDtStart() })
        for (i in 0..5) {
            currentSelection[i].addFitnessScore(0.25)
        }
        currentProximity = currentSelection[0].getFitnessScore()

        println("Current_Proximity " + currentProximity + "\t" +
                "Current_Selection " + currentSelection[0].getHour() + ":" + currentSelection[0].getMinutes() + "\t" + currentSelection[0].getDay() + "/" + currentSelection[0].getMonth()
        )

        currentSelection.sortWith(compareByDescending { it.getFitnessScore() })

        currentProximity = currentSelection[0].getFitnessScore()

        println("Current_Proximity " + currentProximity + "\t" +
                "Current_Selection " + currentSelection[0].getHour() + ":" + currentSelection[0].getMinutes() + "\t" + currentSelection[0].getDay() + "/" + currentSelection[0].getMonth()
        )

        return currentSelection[0].getDtStart()
    }
}