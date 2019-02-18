package il.co.ilrd.tree;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tree {
	private Directory directory;

	public Tree(String path) throws FileNotFoundException {
		if (!new File(path).isDirectory()) {
			throw new FileNotFoundException();
		}
		directory = new Directory(path);
	}

	public void print() {
		directory.print(0);
	}

	interface ComponentTree {
		public void print(int level);
	}

	private static String getIndentation(int level) {
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
			System.out.println(getIndentation(level) + fileName);
		}
		
	}

	private class Directory implements ComponentTree {
		private String directoryName;
		private List<ComponentTree> clist = new ArrayList<>();

		private Directory(String name) {
			directoryName = name;
			File[] directoryItems = new File(directoryName).listFiles();

			for (File f : directoryItems) {
				if (f.isDirectory()) {
					clist.add(new Directory(f.getAbsolutePath()));
				} else if (f.isFile()){
					clist.add(new FileLeaf(f.getName()));
				}
			}
		}

		@Override
		public void print(int level) {
			System.out.println(getIndentation(level) + "/" + new File(directoryName).getName());

			for (ComponentTree e : clist) {
				e.print(level + 1);
			}
		}
	}

	// Test
	public static void main(String[] args) throws FileNotFoundException {
		Tree tree = new Tree("/home/bob45/yuval-workspace");
		tree.print();

		Tree tree2 = new Tree("/home/bob45/invalidName");
		tree2.print();
	}
}