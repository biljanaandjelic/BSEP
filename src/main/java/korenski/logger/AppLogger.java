package korenski.logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class AppLogger {
	
	private static AppLogger appLogger;
	private static FileHandler fileHandler;
	private static Logger logger;
	
	
	public AppLogger() {
		super();
		logger=Logger.getLogger("AppLogger");
	
		try {
			fileHandler=new FileHandler("./files/Logger/appLoggerFile.log",true);
			logger.addHandler(fileHandler);
			SimpleFormatter formatter = new SimpleFormatter();
			fileHandler.setFormatter(formatter);
		} catch (SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated constructor stub
	}


	public static AppLogger getInstance(){
		if(appLogger==null){
			appLogger=new AppLogger();
		}
		return appLogger;
	}
	
	public Logger getLogger(){
		return logger;
	}

}
