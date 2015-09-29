package geneticAlgorithm.function;

import java.util.ArrayList;
import java.util.List;

import ai.OrionAI;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import game.GameResult;
import game.PredatorPreyGame;
import game.ResultLogger;
import geneticAlgorithm.core.Individual;

public class OrionAIFunction implements Function<OrionAI> {

	private int numGames;
	
	@SuppressWarnings("unused")
	private LwjglApplication lwjgl;
	private PredatorPreyGame ppg;
	
	public OrionAIFunction(int numGames) {
		this.numGames = numGames;
		
		ppg = new PredatorPreyGame();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.forceExit = false;
		
		lwjgl = new LwjglApplication(ppg, config);
		
		String mainMenu = "MAIN_MENU";
		while ((ppg.getScreen() != null) && (ppg.getScreenByName(mainMenu) != ppg.getScreen())) {
			wait(1000);
		}
		wait(5000);
	}
	
	@Override
	public void evaluate(Individual<OrionAI> ind) {
		
		ppg.resetLogger();
		
		List<ResultLogger> allResults = new ArrayList<ResultLogger>();
		
		for (int i = 0; i < numGames; ++i) {
			
			// We have to set the AI every new game as the game logic is reset, 
			// so it'll be the default values if we don't set the AI here.
			ppg.setAI(ind.getRepresentation());
			
			ppg.startGame();

			String gameScreen = "GAME";
			while (ppg.getScreenByName(gameScreen) != ppg.getScreen()) {
				wait(1000);
			}
			
			System.out.println("Starting new game... ");
			
			String mainMenu = "MAIN_MENU";
			while (ppg.getScreenByName(mainMenu) != ppg.getScreen()) {
				wait(1000);
			}
		}
		
		ResultLogger logger = ppg.getLogger();
		allResults.add(logger);
		
		double averageResult = processResults(allResults);
		System.out.println("Average result: " + averageResult);
		ind.setFitness(averageResult);
	}

	private double processResults(List<ResultLogger> allResults) {
		
		int count = 0;
		int averageResult = 0;
		for (ResultLogger logger : allResults) {
			List<GameResult> gameResults = logger.getResults();
			for (GameResult gr : gameResults) {
				double result = Math.pow(
						(gr.getNumSquares() - gr.getNumPillsRemaining()), 2); //gr.getNumSimSteps() - 5 * gr.getNumPillsRemaining();
				averageResult += result;
				++count;
				System.out.println("Result " + count + ": " + result);
			}
		}
		System.out.println("Number of results = " + count);
		if (count > 0) {
			return averageResult * 1.0 / count;
		} else {
			return 0;
		}
		
	}
	
	
	private void wait(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
