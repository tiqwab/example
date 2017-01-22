package org.tiqwab.puyopuyo.ga;

import ch.qos.logback.classic.Level;
import org.jenetics.*;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiqwab.puyopuyo.Board;
import org.tiqwab.puyopuyo.CycleResult;
import org.tiqwab.puyopuyo.Solver;
import org.tiqwab.puyopuyo.Stage;
import org.tiqwab.puyopuyo.scanner.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by nm on 1/21/17.
 */
public class PuyopuyoGA {

    private static final Logger logger = LoggerFactory.getLogger(PuyopuyoGA.class);

    private static final List<BoardScanner> scanners = Arrays.asList(
            OneByFourScanner.newDefaultInstance(), FourByOneScanner.newDefaultInstance(),
            TwoByThreeScanner.newDefaultInstance(), ThreeByTwoScanner.newDefaultInstance(),
            TwoByTwoScanner.newDefaultInstance()
    );

    private static Integer puyopuyoEvaluator(Genotype<IntegerGene> gt) {
        int[][] colors = convertGenotypeToColors(gt);
        Stage stage = new Stage(colors, colors.length - 1);
        Solver solver = new Solver(stage, scanners);
        CycleResult cr = solver.cycles();
        return cr.chainInfo.chainCount;
    }

    private static int[][] convertGenotypeToColors(Genotype<IntegerGene> gt) {
        int[][] colors = new int[gt.length()][];
        Iterator<Chromosome<IntegerGene>> iter = gt.iterator();
        int ind = 0;
        while (iter.hasNext()) {
            Chromosome<IntegerGene> chrom = iter.next();
            colors[ind] = new int[chrom.length()];
            for (int i = 0; i < chrom.length(); i++) {
                colors[ind][i] = chrom.getGene(i).intValue();
            }
            ind++;
        }
        return colors;
    }

    private static String formatEvolutionResult(Genotype<IntegerGene> result) {
        Board board = new Board(convertGenotypeToColors(result));
        return board.toString();
    }

    public static void main(String[] args) throws Exception {
        ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.INFO/*DEBUG*/);
        Factory<Genotype<IntegerGene>> gtf = Genotype.of(
                Stream.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12).map(x -> IntegerChromosome.of(1, 3, 6)).collect(Collectors.toList())
        );

        Engine<IntegerGene, Integer> engine = Engine
                .builder(PuyopuyoGA::puyopuyoEvaluator, gtf)
                .alterers(new SinglePointCrossover<>(SinglePointCrossover.DEFAULT_ALTER_PROBABILITY), new Mutator<>(Mutator.DEFAULT_ALTER_PROBABILITY))
                .build();

        Genotype<IntegerGene> result = engine.stream()
                .limit(2000)
                .peek(gt -> {
                    double avg = gt.getPopulation().stream().map(x -> x.getFitness()).collect(Collectors.averagingDouble(x -> x));
                    logger.info("{} -> Min: {}, Max: {}, Avg: {}", gt.getGeneration(), gt.getWorstFitness(), gt.getBestFitness(), avg);
                })
                .collect(EvolutionResult.toBestGenotype());

        System.out.println("Best gene: fitness -> " + puyopuyoEvaluator(result));
        System.out.println(formatEvolutionResult(result));
    }
}
