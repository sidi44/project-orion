package ui;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import progress.ProgressTask;

class LoadingScreen extends MenuScreen {

	private ProgressBarPanel progressPanel;
	
	private boolean finished;
	
	private final static float PADDING = 50;
	
	private final static long START_PAUSE = 250;
	private final static long END_PAUSE = 500;
	
	public LoadingScreen(ScreenManager manager) {
		super(manager);
	}
	
	@Override
	protected void initialise() {
		super.initialise();
		
		finished = false;
	}
	
	@Override
	protected void addActors() {
		
		// Add our background image
		FileHandle file = Gdx.files.internal("data/ui/loading_screen.png");
		Image screenImage = new Image(new Texture(file));
		getStage().addActor(screenImage);
		
		progressPanel = new ProgressBarPanel(getSkin());
		progressPanel.setValue(0);
		
		Table table = new Table();
		table.add(progressPanel);
		
		table.pad(PADDING).bottom();
		
		table.setFillParent(true);
		table.setDebug(true);
		
		getStage().addActor(table);
	}

	@Override
	protected void doShow() {
		
		finished = false;
		
		List<ProgressTask> tasks = getManager().getGame().getLoadingTasks();
		
		// Pause briefly at the start
		try {
			Thread.sleep(START_PAUSE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		progressPanel.setValue(0);
		
		for (ProgressTask task : tasks) {
			Thread thread = new Thread(task);
			thread.start();
		}
		
	}
	
	protected void doRender(float delta) {
		
		// Once all the progress tasks are complete (we've reached 100%) we want
		// the current render cycle to be completed (so that the progress bar 
		// is updated to show 100%) before we call finish() and change screens.
		// The finished variable is used to enforce this.
		if (finished) {
			finish();
			return;
		}
		
		List<ProgressTask> tasks = getManager().getGame().getLoadingTasks();
		
		if (tasks.isEmpty()) {
			setFinished();
		} else {
		
			// Work out how much progress has been made. Each of the tasks is 
			// weighted equally at the moment.
			float total = 0.0f;	
			for (ProgressTask task : tasks) {
				total += task.getProgress();
			}
			total /= tasks.size();
			
			// Update progress bar
			progressPanel.setValue(total);
			
			if (total >= 100) {
				setFinished();
			}
		}
	}
	
	private void setFinished() {
		progressPanel.setValue(100);
		finished = true;
	}
	
	private void finish() {
		
		// Reset the finished flag
		finished = false;
		
		// Pause briefly at the end
		try {
			Thread.sleep(END_PAUSE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Change to the Game screen
		getManager().changeScreen(ScreenName.Game);
	}
	
}
