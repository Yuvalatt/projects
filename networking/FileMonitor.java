package il.co.ilrd.networking;

import il.co.ilrd.observer.CallBack;
import il.co.ilrd.observer.Dispatcher;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class FileMonitor<T> {
	private File watchFile;
	private AtomicBoolean enabled = new AtomicBoolean(true);
	private Map<String, Dispatcher<T>> monitorMap = new HashMap<>();
	private WatchService watchService;
	private WatchKey key;

	public FileMonitor(Path monitorPath) throws IOException{
		watchFile = new File(monitorPath.toString());
		mapInit();
		watchServiceInit(monitorPath);
	}

	private void mapInit() {
		monitorMap.put("ENTRY_CREATE", new Dispatcher<>());
		monitorMap.put("ENTRY_MODIFY", new Dispatcher<>());
		monitorMap.put("ENTRY_DELETE", new Dispatcher<>());
	}

	private void watchServiceInit(Path monitorPath) throws IOException{
			watchService = FileSystems.getDefault().newWatchService();
			Path path = (monitorPath).getParent();
			key = path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY,
					StandardWatchEventKinds.ENTRY_DELETE);
	}

	private Thread t = new Thread(new Runnable() {
		@Override
		public void run() {
			while (enabled.get()) {
				try {
					while ((key = watchService.take()) != null) {
						if (key.isValid()) {
							for (WatchEvent<?> event : key.pollEvents()) {
								if (((Path)event.context()).endsWith(watchFile.getName())) {
										 System.out.println("Time: " + LocalDateTime.now() + "  Event kind: " + event.kind()
										 + "  Event context: " + event.context());	
										 monitorMap.get(event.kind().name()).notifyAllCallBacks();
								}
							}
							key.reset();
						}
					}
				} catch (InterruptedException | java.nio.file.ClosedWatchServiceException e) {
				}
			}
		}
	});

	public void startMonitor() {
		System.out.println("============ Watch File: " + watchFile.getName() + " ==============");
		t.start();
	}
	
	public void stopMonitor() {
		enabled.set(false);
		try {
			watchService.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public File getInputFile() {
		return watchFile;
	}

	public void registerUpdate(CallBack<T> updteCallback) {
		monitorMap.get("ENTRY_MODIFY").register(updteCallback);		
	}

	public void registerCreate(CallBack<T> createCallback) {
		monitorMap.get("ENTRY_CREATE").register(createCallback);	
	}

	public void registerDelete(CallBack<T> deleteCallback) {
		monitorMap.get("ENTRY_DELETE").register(deleteCallback);	
	}
}