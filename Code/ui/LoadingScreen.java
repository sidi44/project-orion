package ui;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import progress.ProgressTask;

class LoadingScreen extends MenuScreen {

	private ProgressBar bar;
	
	private final static long PAUSE = 250;
	
	public LoadingScreen(ScreenManager manager) {
		super(manager);
	}
	
	@Override
	protected void initialise() {
		super.initialise();
	}
	
	@Override
	protected void addActors() {
		
		// Add our background image
		FileHandle file = Gdx.files.internal("data/ui/loading_screen.png");
		Image screenImage = new Image(new Texture(file));
		getStage().addActor(screenImage);
		
		bar = new ProgressBar(0, 100, 1, false, getSkin());
		bar.setValue(0);
		
		Table table = new Table();
		table.add(bar).bottom();
		
		table.setFillParent(true);
		table.setDebug(true);
		
		getStage().addActor(table);
	}

	@Override
	protected void doShow() {
		
		List<ProgressTask> tasks = getManager().getGame().getLoadingTasks();
		
		// Pause briefly at the start
		try {
			Thread.sleep(PAUSE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		bar.setValue(0);
		
		for (ProgressTask task : tasks) {
			Thread thread = new Thread(task);
			thread.start();
		}
		
	}
	
	protected void doRender(float delta) {
		
		List<ProgressTask> tasks = getManager().getGame().getLoadingTasks();
		
		if (tasks.isEmpty()) {
			finished();
		} else {
		
			// Work out how much progress has been made. Each of the tasks is 
			// weighted equally at the moment.
			float total = 0.0f;	
			for (ProgressTask task : tasks) {
				total += task.getProgress();
			}
			total /= tasks.size();
			
			// Update progress bar
			bar.setValue(total);
			
			if (total >= 100) {
				finished();
			}
		}
	}
	
	private void finished() {
		
		
		bar.setValue(100);
		
		// Pause briefly at the end
		try {
			Thread.sleep(PAUSE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		getManager().changeScreen(ScreenName.Game);
	}
	
}
