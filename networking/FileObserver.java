package il.co.ilrd.networking;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import il.co.ilrd.observer.CallBack;

public class FileObserver<T> implements CRUD<String, Integer> {
	private static final long serialVersionUID = 1L;
	private File inputFile;
	private Path inputPath;
	private CallBack<T> updateCallback;
	private CRUD<String, Integer> crud;
	private int id;
	private static int inputLines;
	private static int outputLines = 0;

	public FileObserver(CRUD<String, Integer> crud, int id) {
		this.crud = crud;
		this.id = id;
		updateCallback = new CallBack<>(update, null);
	}

	public void register(FileMonitor<T> fm) {
		inputFile = fm.getInputFile();
		inputPath = inputFile.toPath();
		fm.registerUpdate(updateCallback);
	}

	private Runnable update = new Runnable() {
		@Override
		public void run() {
			try {
				inputLines = (int) Files.lines(inputPath).count();
				for (int i = 0; i < inputLines - outputLines; i++) {
					String str = readFromInput(outputLines + i + 1);
					create(id + ": " + str + "\n");
				}
				outputLines = (int) Files.lines(inputPath).count();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	public Integer create(String data) {
		return crud.create(data);
	}

	@Override
	public void update(Integer id, String data) {
		crud.update(id, data);
	}

	@Override
	public void delete(Integer id) {
		crud.delete(id);
	}

	@Override
	public String read(Integer id) {
		return crud.read(id);
	}

	public String readFromInput(Integer id) {
		List<String> list = new ArrayList<>();
		try {
			list = Files.readAllLines(inputPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (id > list.size() || id < 0) ? null : list.get(id - 1);
	}
}