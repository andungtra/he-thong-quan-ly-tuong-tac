package org.hcmus.tis.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.hcmus.tis.dto.FileUploaderResponse;
import org.hcmus.tis.model.Attachment;
import org.hcmus.tis.model.ProjectProcess;
import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/attachments")
@Controller
@RooWebScaffold(path = "attachments", formBackingObject = Attachment.class)
public class AttachmentController {
	private static final String DESTINATION_DIR_PATH = "uploadedfiles";
	@Autowired
	private ServletContext context;
	@Autowired
	private FileService fileService;

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public FileUploaderResponse create(Model uiModel,
			HttpServletRequest httpServletRequest) throws IOException,
			JAXBException {
		InputStream is = null;
		FileOutputStream fos = null;
		String filename = httpServletRequest.getHeader("X-File-Name");
		Attachment attachment = new Attachment();
		try {
			is = httpServletRequest.getInputStream();
			attachment.setDisplayFileName(filename);
			attachment.persist();
			String realPath = getContext().getRealPath(DESTINATION_DIR_PATH);
			String realFileName = realPath + File.separatorChar
					+ attachment.getId();
			fos = fileService.getFileOutPutStream(realFileName);
			IOUtils.copy(is, fos);
			attachment.setRealFileName(realFileName);
			attachment.flush();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (IOException ignored) {
			}
		}
		FileUploaderResponse response = new FileUploaderResponse();
		response.setAttachmentId(attachment.getId());
		response.setSuccess(true);
		return response;
	}

	@RequestMapping(method = RequestMethod.POST, params = { "workitemid" })
	@ResponseBody
	public FileUploaderResponse createFromWorkItem(Model uiModel,
			@RequestParam(value = "workitemid") Long workItemId,
			HttpServletRequest httpServletRequest) throws IOException,
			JAXBException {
		InputStream is = null;
		FileOutputStream fos = null;
		WorkItem workItem = WorkItem.findWorkItem(workItemId);
		String filename = httpServletRequest.getHeader("X-File-Name");
		Attachment attachment = new Attachment();
		try {
			is = httpServletRequest.getInputStream();
			attachment.setDisplayFileName(filename);
			attachment.persist();
			String realPath = getContext().getRealPath(DESTINATION_DIR_PATH);
			String realFileName = realPath + File.separatorChar
					+ attachment.getId();
			fos = fileService.getFileOutPutStream(realFileName);
			IOUtils.copy(is, fos);
			attachment.setRealFileName(realFileName);
			attachment.setWorkItem(workItem);
			attachment.flush();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (IOException ignored) {
			}
		}
		FileUploaderResponse response = new FileUploaderResponse();
		response.setAttachmentId(attachment.getId());
		response.setSuccess(true);
		return response;
	}

	@RequestMapping(value = "/kkk")
	@ResponseBody
	public FileUploaderResponse createxxxx(Model uiModel,
			HttpServletRequest httpServletRequest) throws IOException,
			JAXBException {
		int x = 5;
		x++;
		FileUploaderResponse response = new FileUploaderResponse();
		response.setSuccess(true);
		return response;
	}

	public ServletContext getContext() {
		return context;
	}

	public void setContext(ServletContext context) {
		this.context = context;
	}

	public FileService getFileService() {
		return fileService;
	}

	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}
	 @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	 @ResponseBody
	    public FileUploaderResponse delete(@PathVariable("id") Long id) {
	        Attachment attachment = Attachment.findAttachment(id);
	        attachment.remove();
	        FileUploaderResponse response = new FileUploaderResponse();
	        response.setSuccess(true);
	        response.setAttachmentId(id);
	        return response;
	    }
}
