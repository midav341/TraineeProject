package util;

public class MultiUploadUtil {
	public static String[] filesParentPathList(String[] filesParentPathList){
		if(filesParentPathList!=null){
			filesParentPathList[0] = filesParentPathList[0].replaceAll("\\[","");
			filesParentPathList[filesParentPathList.length-1] = filesParentPathList[filesParentPathList.length-1].replaceAll("]","");	
		}
		return filesParentPathList;
	}
	public static String[] folderParentPathList(String[] folderParentPathList){
		if(folderParentPathList!=null){
			folderParentPathList[0] = folderParentPathList[0].replaceAll("\\[","");;
			folderParentPathList[folderParentPathList.length-1] = folderParentPathList[folderParentPathList.length-1].replaceAll("]","");	
		}
		return folderParentPathList;
	}
	public static String[] folderNameList(String[] folderNameList){
		if(folderNameList!=null){
			folderNameList[0] = folderNameList[0].replaceAll("\\[","");
			folderNameList[folderNameList.length-1] = folderNameList[folderNameList.length-1].replaceAll("]","");
		}
		return folderNameList;
	}
}
