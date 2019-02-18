package il.co.ilrd.factory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class TreeFactory {
	private ComponentTree directory;
	private static Factory<Boolean, String, ComponentTree> factory = new Factory<>();
	
	public TreeFactory(String path) throws FileNotFoundException { 
		if (!(new File(path).isDirectory())) {
			throw new FileNotFoundException();
		}
		
		Function<String, ComponentTree> createFile = (name)->{
			return new FileLeaf(name);
		};
		
		Function<String, ComponentTree> createDirectory = (name)->{
			return new Directory(name);
		};
		
		factory.add(false, createFile);
		factory.add(true, createDirectory);
		directory = factory.create(true, path);		
	}
		
	public void print() {
		directory.print(0);
	}

	interface ComponentTree {
		public void print(int level);
	}

	private String getIndentation(int level) {
		char[] charArray = new char[level];
		Arrays.fill(charArray, '_');
		return new String(charArray);
	}

	private class FileLeaf implements ComponentTree {
		private String fileName;
		
		private FileLeaf(String name) {
			fileName = name;
		}

		@Override
		public void print(int level) {
			String [] arr = fileName.split("/");
			System.out.println(getIndentation(level) + "/" + arr[arr.length -1]);
		}
	}

	private class Directory implements ComponentTree {
		private String directoryName;
		private List<ComponentTree> clist = new ArrayList<>();
		
		private Directory(String name) {
			directoryName = name;
			File[] directoryItems = new File(directoryName).listFiles();

			for (File f : directoryItems) {		
				clist.add(factory.create(f.isDirectory(), f.getAbsolutePath()));
			}
		}

		@Override
		public void print(int level) {
			String [] arr = directoryName.split("/");
			System.out.println(getIndentation(level) + "/" + arr[arr.length -1]);
			
			for (ComponentTree e : clist) {
				e.print(level + 1);				
			}
		}
	}

	// Test
	public static void main(String[] args) throws FileNotFoundException {
		TreeFactory tree = new TreeFactory("/home/bob45/yuval-workspace");
		tree.print();
		
		TreeFactory tree2 = new TreeFactory("/home/bob45/inValidName");
		tree2.print();	
	}
}