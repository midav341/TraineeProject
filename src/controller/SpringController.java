package controller;

import java.io.IOException;

import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import util.MessageUtil;
import util.MultiUploadUtil;

@Controller
public class SpringController {
	@Autowired
	private logic.MainPageLogic mainPageLogic;

	@Autowired
	private logic.FoldersAndFilesTreeJSON foldersAndFilesTreeJSON;

	@Autowired
	private logic.CopyFolderOrFile copyFolderOrFile;

	@Autowired
	private logic.CreateFolder createFolder;

	@Autowired
	private logic.DeleteFolderOrFile deleteFolderOrFile;

	@Autowired
	private logic.MoveFolderOrFile moveFolderOrFile;

	@Autowired
	private logic.RenameFolderOrFile renameFolderOrFile;

	@Autowired
	private logic.UploadFile uploadFile;

	@Autowired
	private logic.DownloadFile downloadFile;

	@Autowired
	private logic.GetUserIdByEmail getUserIdByEmail;

	@Autowired
	private MessageUtil messageUtil;

	public static final Logger LOG = Logger.getLogger(SpringController.class);

	@RequestMapping("/")
	public String home(@RequestParam(value = "error", required = false) final String error, final ModelMap modelMap) {
		if (error != null) {
			String message = messageUtil.getMessage("homePage.message.error.login", null);
			modelMap.addAttribute("answerText", message);
		}
		String page = "Home";
		if (!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")) {
			page = "redirect:main";
		}
		return page;
	}

	@RequestMapping("/registration")
	public String registration(HttpSession session, WebRequest request, ModelMap modelMap,
			HttpServletRequest httpServletRequest) {
		String success, message;
		String forwardPageName = "Home";
		String[] answerArray;
		String regEmail = request.getParameter("regEmail");
		String regPass = request.getParameter("regPass");

		String contextPath = session.getServletContext().getRealPath("/") + "/";

		if (regEmail != null) {
			answerArray = mainPageLogic.doLogic(regEmail, regPass, contextPath);
			success = answerArray[0];
			session.setAttribute("email", regEmail);
			if (success.equals("0")) {
				message = messageUtil.getMessage(answerArray[1], null);
				modelMap.addAttribute("answerText", message);
			} else {
				forwardPageName = "redirect:main";
				Authentication authentication = new UsernamePasswordAuthenticationToken(regEmail, regPass,
						AuthorityUtils.createAuthorityList("ROLE_USER"));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		return forwardPageName;
	}

	@RequestMapping("/main")
	public String login(HttpSession session, WebRequest request, ModelMap modelMap) {
		String loginEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		session.setAttribute("email", loginEmail);
		Object[] args = new Object[] { loginEmail };

		String message = messageUtil.getMessage("mainPage.message.sucsess.login", args);
		modelMap.addAttribute("answerText", message);
		return "main";
	}

	@RequestMapping("/CopyFolderOrFile")
	public String copyFolderOrFile(HttpSession session, WebRequest request, ModelMap modelMap) {

		String fileData = (String) request.getParameter("fileCopyData");
		String folderData = (String) request.getParameter("folderCopyData");
		String newName = (String) request.getParameter("newName");
		String email = (String) session.getAttribute("email");
		String contextPath = session.getServletContext().getRealPath("/") + "/";

		String code = copyFolderOrFile.doLogic(fileData, folderData, newName, email, contextPath);
		String message = messageUtil.getMessage(code, null);

		modelMap.addAttribute("answerText", message);
		return "main";
	}

	@RequestMapping("/CreateFolder")
	public String createFolder(HttpSession session, WebRequest request, ModelMap modelMap) {
		String folderName = request.getParameter("folderName");
		String parentPath = request.getParameter("parentPath");
		String email = (String) session.getAttribute("email");
		String contextPath = session.getServletContext().getRealPath("/") + "/";

		String code = createFolder.doLogic(folderName, parentPath, email, contextPath);
		String message = messageUtil.getMessage(code, null);

		modelMap.addAttribute("answerText", message);
		return "main";
	}

	@RequestMapping("/DeleteFolderOrFile")
	public String deleteFolderOrFile(HttpSession session, WebRequest request, ModelMap modelMap) {
		String fileData = (String) request.getParameter("fileDeleteData");
		String folderData = (String) request.getParameter("folderDeleteData");
		String contextPath = session.getServletContext().getRealPath("/") + "/";

		String code = deleteFolderOrFile.doLogic(fileData, folderData, contextPath);
		String message = messageUtil.getMessage(code, null);

		modelMap.addAttribute("answerText", message);
		return "main";
	}

	@RequestMapping("/FoldersAndFilesTreeJSON")
	public @ResponseBody JsonObject foldersAndFilesTreeJSON(
			@RequestParam(value = "email", required = false) String email, HttpSession session) {
		if (email == null) {
			email = (String) session.getAttribute("email");
		}
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) {
			userId = getUserIdByEmail.doLogic(email);
			session.setAttribute("userId", userId);
		}

		JsonObject json = foldersAndFilesTreeJSON.doLogic(userId, email);
		return json;
	}

	@RequestMapping("/MoveFolderOrFile")
	public String moveFolderOrFile(HttpSession session, WebRequest request, ModelMap modelMap) {
		String fileData = (String) request.getParameter("fileMoveData");
		String folderData = (String) request.getParameter("folderMoveData");
		String parentPath = (String) request.getParameter("parentPath");
		String contextPath = session.getServletContext().getRealPath("/") + "/";

		String code = moveFolderOrFile.doLogic(fileData, folderData, parentPath, contextPath);
		String message = messageUtil.getMessage(code, null);

		modelMap.addAttribute("answerText", message);

		return "main";
	}

	@RequestMapping("/RenameFolderOrFile")
	public String renameFolderOrFile(HttpSession session, WebRequest request, ModelMap modelMap) {
		String fileData = (String) request.getParameter("fileRenameData");
		String folderData = (String) request.getParameter("folderRenameData");
		String newName = (String) request.getParameter("newName");
		String email = (String) session.getAttribute("email");
		String contextPath = session.getServletContext().getRealPath("/") + "/";

		String code = renameFolderOrFile.doLogic(fileData, folderData, newName, email, contextPath);
		String message = messageUtil.getMessage(code, null);

		modelMap.addAttribute("answerText", message);

		return "main";
	}

	@RequestMapping("/UploadFile")
	public String uploadFile(HttpServletRequest request, HttpSession session, ModelMap modelMap,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "parentPath", required = false) String parentPath) {

		if (email == null) {
			email = (String) session.getAttribute("email");
		}
		String contextPath = session.getServletContext().getRealPath("/") + "/";
		Part part = null;

		try {
			part = request.getPart("file");
		} catch (IOException | ServletException e1) {
			e1.printStackTrace();
		}

		String code = uploadFile.doLogic(email, parentPath, part, contextPath);
		String message = messageUtil.getMessage(code, null);

		modelMap.addAttribute("answerText", message);

		return "main";
	}

	@RequestMapping("/DownloadFile")
	public void downloadFile(HttpServletRequest request, HttpSession session, HttpServletResponse response,
			@RequestParam(value = "filepath", required = false) String filepath) {

		String contextPath = session.getServletContext().getRealPath("/") + "/";
		downloadFile.doLogic(response, filepath, contextPath);
	}

	@RequestMapping("/MultiUploadFile")
	public void multiUploadFile(HttpServletRequest request, HttpSession session, ModelMap modelMap,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "parentPath", required = false) String parentPath,
			@RequestParam(value = "filesParentPathList", required = false) String[] filesParentPathList,
			@RequestParam(value = "folderParentPathList", required = false) String[] folderParentPathList,
			@RequestParam(value = "folderNameList", required = false) String[] folderNameList) {

		filesParentPathList = MultiUploadUtil.filesParentPathList(filesParentPathList);
		folderNameList = MultiUploadUtil.folderNameList(folderNameList);
		folderParentPathList = MultiUploadUtil.folderParentPathList(folderParentPathList);

		String contextPath = session.getServletContext().getRealPath("/") + "/";
		try {
			for (int k = 0; k < folderParentPathList.length; k++) {
				createFolder.doLogic(folderNameList[k], folderParentPathList[k], email, contextPath);
			}
			int i = 0;
			for (String fileParentPath : filesParentPathList) {
				Part part = request.getPart("file" + i);
				uploadFile.doLogic(email, fileParentPath, part, contextPath);
				i++;
			}
		} catch (IOException | ServletException e) {
			LOG.error(e);
		}
	}
}
