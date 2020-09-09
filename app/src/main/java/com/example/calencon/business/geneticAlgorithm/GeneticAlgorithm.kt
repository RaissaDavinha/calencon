package com.example.calencon.business.geneticAlgorithm

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
    // Current Population
    private var currentPopulation = mutableListOf<Specimen>()

    // Current Selection
    private var currentSelection = mutableListOf<Specimen>()

    // Current generation
    private var currentProximity = 0.0

    // Current Iteration
    private var currentGeneration = 0

    // Initializes genetic algorithm
    fun GeneticAlgorithm(numberOfChromosomes: Int, replaceByGeneration: Int, trackBest: Int) {
        // Set up options for the algorithm
        // Generate initial population
    }

    // Frees used resources
    fun ClearGeneticAlgorithm() {

    }

    // Starts and executes algorithm
    fun run() {

    }

    // Stops execution of algorithm
    fun Stop() {

    }

    // Returns pointer to best chromosomes in population
    fun GetBestChromosome() {

    }

    // Returns current generation
    fun GetCurrentGeneration() = currentGeneration
}