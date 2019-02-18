package il.co.ilrd.crud;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import il.co.ilrd.observer.CallBack;

public class FileObserver<T> implements CRUD<String, Integer> {
	private static final long serialVersionUID = 1L;
	private File inputFile;	
	private Path inputPath;
	private Path outputPath;
	private List<String> list = new ArrayList<>();   //gets input file data
	private CallBack<T> updteCallback;
	private CallBack<T> createCallback;
	private CallBack<T> deleteCallback;

	public FileObserver(String path) {
		outputPath = Paths.get(path);		
		updteCallback = new CallBack<>(update, null);
		createCallback = new CallBack<>(createFile, null);
		deleteCallback = new CallBack<>(deleteFile, null);
	}

	public void register(FileMonitor<T> fm) {
		inputFile = fm.getInputFile();
		inputPath = inputFile.toPath();			
		try {
			list = Files.readAllLines(inputPath);
			Files.write(outputPath, list, Charset.defaultCharset()); //copies content of input file
		} catch (IOException e) {
			e.printStackTrace();
		}
		fm.registerUpdate(updteCallback);
		fm.registerCreate(createCallback);
		fm.registerDelete(deleteCallback);
	}

	private Runnable update = new Runnable() {
		@Override
		public void run() {
			try {
				int inLines = (int) Files.lines(inputPath).count();
				int outLines = (int) Files.lines(outputPath).count();
				
				// Handles event of new lines
					for (int i = 0; i < inLines - outLines; i++) {
						String str = read(outLines + i + 1); 
						create(str);
						System.out.println("ADDED to file: " + str);
					}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	private Runnable createFile = new Runnable() {
		@Override
		public void run() {
			System.out.println("Time: " + LocalDateTime.now() + " Event kind: ENTRY_CREATE");
		}
	};

	private Runnable deleteFile = new Runnable() {
		@Override
		public void run() {
			System.out.println("Time: " + LocalDateTime.now() + " Event kind: ENTRY_DELETE");
		}
	};

	// gets data and appends to file, returns new line number
	@Override
	public Integer create(String data) {
		long num = 0;
		try (OutputStreamWriter outWriter = new FileWriter(outputPath.toString(), true)){
			outWriter.append(data + "\n");
			num = Files.lines(outputPath).count();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Integer((int) num);
	}

	// updates line id with new data, copies new content to the output path
	@Override
	public void update(Integer id, String data) {
		try {
			list = Files.readAllLines(inputPath);
			if (id > 0 && id < list.size()) {
				list.set(id - 1, data);
				Files.write(outputPath, list, Charset.defaultCharset());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// returns data in line id
	@Override
	public String read(Integer id) {
		try {
			list = Files.readAllLines(inputPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (id > list.size() || id < 0) ? null : list.get(id - 1);
	}

	// updates line id, copies new content to the output path
	@Override
	public void delete(Integer id) {
		try {
			list = Files.readAllLines(inputPath);
			list.remove(id.intValue() - 1);
			Files.write(outputPath, list, Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}