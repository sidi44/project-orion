package geneticAlgorithm.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import ai.OrionAI;
import geneticAlgorithm.function.Function;

public class OrionAIGeneticAlgorithm extends GeneticAlgorithm<OrionAI> {
	
	public OrionAIGeneticAlgorithm(Population<Individual<OrionAI>> pop,
			EvoParams<OrionAI> evoParams, GenerationalParams genParams,
			Function<OrionAI> function) {
		super(pop, evoParams, genParams, function);
		
	}
	
	@Override
	protected void additionalActions() {
		printSummary(currentGen);
	}
	
	private void printSummary(int epoch) {
		
		System.out.println("Epoch: " + epoch);
		System.out.println("Best fitness: " + getPop().getMax().getFitness());
		
		OrionAI ai = getPop().getMax().getRepresentation();
		System.out.println("Best pill factor: " + ai.getPillFactor());
		System.out.println("Best pill dist factor: " + ai.getPillDistFactor());
		System.out.println("Best prey factor: " + ai.getPreyFactor());
		System.out.println("Best prey dist factor: " + ai.getPreyDistFactor());
		System.out.println("Best predator factor: " + ai.getPredatorFactor());
		System.out.println("Best predator dist factor: " + ai.getPredatorDistFactor());

		System.out.println(" ");
		
		writePopulation(epoch);
	}

	
	private void writePopulation(int epoch) {
		
		String nl = System.lineSeparator();
        BufferedWriter output = null;
        try {
    		Population<Individual<OrionAI>> pop = getPop();
    		
    		File file = new File("output2.txt");
            output = new BufferedWriter(new FileWriter(file, true));
            output.write("Epoch: " + epoch + nl);
            output.write("Best fitness: " + pop.getMax().getFitness() + nl);
            
    		int size = pop.size();
    		for (int i = 0; i < size; ++i) {
    			Individual<OrionAI> ind = pop.getNthMax(i);
    			OrionAI ai = ind.getRepresentation();
    			output.write("Individual No: " + i + nl);
    			output.write("Fitness: " + ind.getFitness() + nl);
    			output.write("Pill factor: " + ai.getPillFactor() + nl);
    			output.write("Pill dist factor: " + ai.getPillDistFactor() + nl);
    			output.write("Prey factor: " + ai.getPreyFactor() + nl);
    			output.write("Prey dist factor: " + ai.getPreyDistFactor() + nl);
    			output.write("Predator factor: " + ai.getPredatorFactor() + nl);
    			output.write("Predator dist factor: " + ai.getPredatorDistFactor() + nl);
    			output.write(nl);
    		}
    		output.write(nl);
    		
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
            	try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        }
		
	}
	
}
