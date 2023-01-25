package practica01;

import java.util.ArrayList;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.DifferentialEvolutionBuilder;
import org.uma.jmetal.example.AlgorithmRunner;
import org.uma.jmetal.operator.crossover.impl.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.selection.impl.DifferentialEvolutionSelection;
import org.uma.jmetal.problem.doubleproblem.DoubleProblem;
import org.uma.jmetal.problem.singleobjective.Sphere;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.MultiThreadedSolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

/**
 * Class to configure and run a differential evolution algorithm. The algorithm
 * can be configured to use threads. The number of cores is specified as an
 * optional parameter. The target problem is Sphere.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class DifferentialEvolutionRunner {

  private static final int DEFAULT_NUMBER_OF_CORES = 1;

  public static void main(String[] args) throws Exception {

    Algorithm<DoubleSolution> algorithm;
    DifferentialEvolutionSelection selection;
    DifferentialEvolutionCrossover crossover;
    SolutionListEvaluator<DoubleSolution> evaluator;
    int size = 10;
    System.out.println("DE/rand/1/bin (" + size + ")");
    DoubleProblem problem = new Sphere(size);
    int numberOfCores;
    if (args.length == 1) {
      numberOfCores = Integer.valueOf(args[0]);
    } else {
      numberOfCores = DEFAULT_NUMBER_OF_CORES;
    }

    if (numberOfCores == 1) {
      evaluator = new SequentialSolutionListEvaluator();
    } else {
      evaluator = new MultiThreadedSolutionListEvaluator(numberOfCores);
    }

    crossover = new DifferentialEvolutionCrossover(
        0.5, 0.5, DifferentialEvolutionCrossover.DE_VARIANT.RAND_1_BIN);
    selection = new DifferentialEvolutionSelection();

    double minimo = Double.MAX_VALUE;
    double suma = 0;
    int rep = 1;
    for (int i = 0; i < rep; i++) {
      algorithm
        = new DifferentialEvolutionBuilder(problem)
          .setCrossover(crossover)
          .setSelection(selection)
          .setSolutionListEvaluator(evaluator)
          .setMaxEvaluations(10000 * size)
          .setPopulationSize(100)
          .build();

      new AlgorithmRunner.Executor(algorithm).execute();
      double objetivo = algorithm.getResult().objectives()[0]; // - problem.getOptimalSolution();
      
     List<Double> variables= algorithm.getResult().variables();
        for (int j = 0; j < variables.size() ; j++) {
            System.out.println(variables.get(j));
        }
      System.out.println("DE/rand/1/bin = " + objetivo);
      suma += objetivo;
      if (objetivo < minimo) {
        minimo = objetivo;
      }
    }
    System.out.println("MINIMO  =" + minimo);
    System.out.println("PROMEDIO=" + suma / rep);

    evaluator.shutdown();
  }
}
