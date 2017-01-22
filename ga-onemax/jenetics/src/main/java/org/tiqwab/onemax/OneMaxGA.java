package org.tiqwab.onemax;

import org.jenetics.*;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.util.Factory;

import java.util.stream.Collectors;

public class OneMaxGA {

    // Fitness function.
    private static Integer calcFitness(Genotype<BitGene> gt) {
        return (int) gt.getChromosome().stream().filter(BitGene::getAllele).count();
    }

    public static void main(String[] args) {
        // Specify what kind of genes and chromosomes are used.
        Factory<Genotype<BitGene>> gtf = Genotype.of(BitChromosome.of(/*length:*/ 20, /*prob. of 1:*/ 0.2));

        Engine<BitGene, Integer> engine = Engine
                .builder(OneMaxGA::calcFitness, gtf)
                .populationSize(50) // The size of population.
                .selector(new TournamentSelector<>(3)) // The methodology for selection
                .alterers( // The methodology to perform crossover and mutation.
                        new SinglePointCrossover<>(0.2),
                        new Mutator<>(0.15))
                .build();

        Genotype<BitGene> result = engine.stream()
                .limit(100) // The number of generation. In this case, the selection will be performed 100 times.
                .peek(er -> {
                    double averageFitness = er.getPopulation().stream()
                            .collect(Collectors.averagingDouble(Phenotype::getFitness));
                    System.out.println(String.format("%d -> Min: %d, Max: %d, Avg: %f",
                            er.getGeneration(),
                            er.getWorstFitness(),
                            er.getBestFitness(),
                            averageFitness));
                })
                .collect(EvolutionResult.toBestGenotype());

        System.out.println("Best gene: " + result.toString());
    }

}
