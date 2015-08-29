package geneticAlgorithm.function;

import java.util.ArrayList;
import java.util.List;

import ai.OrionAI;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import game.GameResult;
import game.PredatorPreyGame;
import game.ResultLogger;
import geneticAlgorithm.core.Individual;

public class OrionAIFunction implements Function<OrionAI>, LifecycleListener {

	private int numIterations;
	private int numGames;
	private boolean testFinished;
	
	private LwjglApplication lwjgl;
	private PredatorPreyGame ppg;
	
	public OrionAIFunction(int numIterations, int numGames) {
		this.numIterations = numIterations;
		this.numGames = numGames;
		this.testFinished = false;
		
		ppg = new PredatorPreyGame();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.forceExit = false;
		
		lwjgl = new LwjglApplication(ppg, config);
		//lwjgl.addLifecycleListener(this);
		
		while (ppg.isLoading()) {
			wait(2000);
		}
		
		ppg.setDoRender(false);
	}
	
	@Override
	public void evaluate(Individual<OrionAI> ind) {
		
		testFinished = false;
		ppg.setAI(ind.getRepresentation());
		ppg.startGame();
		
		List<ResultLogger> allResults = new ArrayList<ResultLogger>();
		
		for (int i = 0; i < numGames; ++i) {
			
			System.out.println("Starting new game... ");
			
			while (!ppg.isStopped()) {
				wait(2000);
			}
			
			testFinished = false;
			
			ApplicationListener al = lwjgl.getApplicationListener();
			PredatorPreyGame ppg2 = (PredatorPreyGame) al;
			
			ResultLogger logger = ppg2.getLogger();
			allResults.add(logger);
			
			ppg2.resetGame();
			al.resume();
		}
		
		double averageResult = processResults(allResults);
		System.out.println("Average result: " + averageResult);
		ind.setFitness(averageResult);
	}

	private double processResults(List<ResultLogger> allResults) {
		
		int count = 0;
		int result = 0;
		for (ResultLogger logger : allResults) {
			List<GameResult> gameResults = logger.getResults();
			for (GameResult gr : gameResults) {
				result += gr.getNumSimSteps() - 5 * gr.getNumPillsRemaining();
				++count;
			}
		}
		System.out.println("Number of results = " + count);
		if (count > 0) {
			return result * 1.0 /count;
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
	
	@Override
	public void pause() {
		// We do nothing
	}

	@Override
	public void resume() {
		// We do nothing
	}

	@Override
	public void dispose() {
	}

}
