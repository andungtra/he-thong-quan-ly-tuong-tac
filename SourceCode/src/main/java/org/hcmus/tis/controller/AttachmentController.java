package org.hcmus.tis.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.hcmus.tis.dto.FileUploaderResponse;
import org.hcmus.tis.model.Attachment;
import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.repository.AttachmentRepository;
import org.hcmus.tis.repository.WorkItemRepository;
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


@RequestMapping("/attachments")
@Controller
@RooWebScaffold(path = "attachments", formBackingObject = Attachment.class)
public class AttachmentController {
	private static final String DESTINATION_DIR_PATH = "uploadedfiles";
	@Autowired
	private ServletContext context;
	@Autowired
	private FileService fileService;
	@Autowired
	private AttachmentRepository attachmentRepository;
	@Autowired
	private WorkItemRepository workItemRepository;

	public AttachmentRepository getAttachmentRepository() {
		return attachmentRepository;
	}

	public void setAttachmentRepository(AttachmentRepository attachmentRepository) {
		this.attachmentRepository = attachmentRepository;
	}

	@RequestMapping(value = "/{id}/{fileName}", method = RequestMethod.GET)
	public void show(@PathVariable("id") Long id, HttpServletResponse response)
			throws IOException {
		Attachment attachment = attachmentRepository.findOne(id);
		ServletOutputStream outPutSteam = response.getOutputStream();
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ attachment.getDisplayFileName() + "\"");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		String realPath = getContext().getRealPath(DESTINATION_DIR_PATH);
		String realFileName = realPath + File.separatorChar
				+ attachment.getId();
		FileInputStream fileInputStream = fileService
				.getFileInPutStream(realFileName);
		IOUtils.copy(fileInputStream, outPutSteam);
	}

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
			attachmentRepository.save(attachment);
			String realPath = getContext().getRealPath(DESTINATION_DIR_PATH);
			String realFileName = realPath + File.separatorChar
					+ attachment.getId();
			fos = fileService.getFileOutPutStream(realFileName);
			IOUtils.copy(is, fos);
			attachment.setRealFileName(realFileName);
			attachmentRepository.flush();
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
		WorkItem workItem = workItemRepository.findOne(workItemId);
		String filename = httpServletRequest.getHeader("X-File-Name");
		Attachment attachment = new Attachment();
		try {
			is = httpServletRequest.getInputStream();
			attachment.setDisplayFileName(filename);
			attachmentRepository.save(attachment);
			String realPath = getContext().getRealPath(DESTINATION_DIR_PATH);
			String realFileName = realPath + File.separatorChar
					+ attachment.getId();
			fos = fileService.getFileOutPutStream(realFileName);
			IOUtils.copy(is, fos);
			attachment.setRealFileName(realFileName);
			attachment.setWorkItem(workItem);
			attachmentRepository.flush();
		} 
		catch (Exception e) {
			Logger.getLogger(getClass()).log(Priority.FATAL, e);
		}
		finally {
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

	public WorkItemRepository getWorkItemRepository() {
		return workItemRepository;
	}

	public void setWorkItemRepository(WorkItemRepository workItemRepository) {
		this.workItemRepository = workItemRepository;
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
		Attachment attachment = attachmentRepository.findOne(id);
		attachmentRepository.delete(attachment);
		FileUploaderResponse response = new FileUploaderResponse();
		response.setSuccess(true);
		response.setAttachmentId(id);
		return response;
	}
}
