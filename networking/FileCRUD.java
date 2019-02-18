package il.co.ilrd.networking;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileCRUD<T> implements CRUD<String, Integer> {
	private static final long serialVersionUID = 1L;
	private Path inputPath;
	private Path outputPath;
	private List<String> list = new ArrayList<>(); // gets input file data

	public FileCRUD(String path) {
		outputPath = Paths.get(path);
		try {
			Files.write(outputPath, list, Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// gets data and appends to file, returns new line number
	@Override
	public Integer create(String data) {
		long num = 0;
		try (OutputStreamWriter outWriter = new FileWriter(outputPath.toString(), true)) {
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

	// returns data in line id (from log-file)
	@Override
	public String read(Integer id) {
		try {
			list = Files.readAllLines(outputPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (id > list.size() || id < 0) ? null : list.get(id - 1);
	}

	// deletes line id, copies new content to the output path
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