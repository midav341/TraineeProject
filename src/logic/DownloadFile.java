package logic;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class DownloadFile {
	public static final Logger LOG=Logger.getLogger(DownloadFile.class);
	
	public void doLogic(HttpServletResponse response, String filepath, String contextPath){
		LOG.info("filepath "+filepath);
		LOG.info("contextPath "+contextPath);
		
		int filePathLenght = filepath.split("/").length;
		String fileName = filepath.split("/")[filePathLenght-1];
		
		response.setContentType("APPLICATION/OCTET-STREAM");   
		response.setHeader("Content-Disposition","attachment; filename=\"" + fileName + "\"");   
		try {
			FileUtils.copyFile(
				      FileUtils.getFile(contextPath +"/"+ filepath), 
				      response.getOutputStream()
				      );
			response.flushBuffer();
		} catch (IOException e) {
			LOG.error(e);;
		}
	}
}
