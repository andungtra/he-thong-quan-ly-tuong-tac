package org.hcmus.tis.controller;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.hcmus.tis.dto.FileUploaderResponse;
import org.hcmus.tis.model.Attachment;
import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.service.FileService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.springframework.ui.Model;

@RunWith(PowerMockRunner.class)
public class AttachmentControllerTest {
	private AttachmentController aut;
	private Model uiModel;
	private FileService fileService;
	private ServletContext servletContext;

	@Before
	public void setUp() {
		aut = new AttachmentController();
		uiModel = Mockito.mock(Model.class);
		fileService = Mockito.mock(FileService.class);
		servletContext = Mockito.mock(ServletContext.class);
		aut.setContext(servletContext);
		aut.setFileService(fileService);
	}

	@Test
	@PrepareForTest({ Attachment.class, AttachmentController.class,
			IOUtils.class })
	public void testCreate() throws Exception {
		HttpServletRequest httpServletRequest = Mockito
				.mock(HttpServletRequest.class);
		Attachment mockedAttachment = Mockito.mock(Attachment.class);
		Mockito.doReturn((long) 1).when(mockedAttachment).getId();
		PowerMockito.whenNew(Attachment.class).withNoArguments()
				.thenReturn(mockedAttachment);
		String fileName = "fileName";
		Mockito.doReturn(fileName).when(httpServletRequest)
				.getHeader("X-File-Name");
		PowerMockito.mockStatic(IOUtils.class);

		FileUploaderResponse result = aut.create(uiModel, httpServletRequest);
		PowerMockito.verifyStatic();
		IOUtils.copy(Mockito.any(InputStream.class),
				Mockito.any(FileOutputStream.class));
		Mockito.verify(mockedAttachment).persist();
		Mockito.verify(mockedAttachment).setDisplayFileName(fileName);
		Mockito.verify(fileService).getFileOutPutStream(
				Mockito.contains(mockedAttachment.getId().toString()));
		Assert.assertEquals(true, result.isSuccess());
		assertEquals(mockedAttachment.getId(), result.getAttachmentId());
		Mockito.verify(mockedAttachment).setRealFileName(Mockito.anyString());
	}

	@Test
	@PrepareForTest({ Attachment.class, AttachmentController.class,
			IOUtils.class, WorkItem.class })
	public void testCreateFromWorkItem() throws Exception {
		HttpServletRequest httpServletRequest = Mockito
				.mock(HttpServletRequest.class);
		Long workItemId = (long)1;
		Attachment mockedAttachment = Mockito.mock(Attachment.class);
		Mockito.doReturn((long) 1).when(mockedAttachment).getId();
		PowerMockito.whenNew(Attachment.class).withNoArguments()
				.thenReturn(mockedAttachment);
		String fileName = "fileName";
		Mockito.doReturn(fileName).when(httpServletRequest)
				.getHeader("X-File-Name");
		PowerMockito.mockStatic(IOUtils.class);
		PowerMockito.mockStatic(WorkItem.class);
		WorkItem mockedWorkItem = Mockito.mock(WorkItem.class);
		PowerMockito.when(WorkItem.findWorkItem(workItemId)).thenReturn(mockedWorkItem);

		FileUploaderResponse result = aut.createFromWorkItem(uiModel, workItemId,httpServletRequest);
		PowerMockito.verifyStatic();
		IOUtils.copy(Mockito.any(InputStream.class),
				Mockito.any(FileOutputStream.class));
		Mockito.verify(mockedAttachment).setWorkItem(mockedWorkItem);
		Mockito.verify(mockedAttachment).persist();
		Mockito.verify(mockedAttachment).setDisplayFileName(fileName);
		Mockito.verify(fileService).getFileOutPutStream(
				Mockito.contains(mockedAttachment.getId().toString()));
		Assert.assertEquals(true, result.isSuccess());
		assertEquals(mockedAttachment.getId(), result.getAttachmentId());
		Mockito.verify(mockedAttachment).setRealFileName(Mockito.anyString());
	}
	@Test
	@PrepareForTest({Attachment.class})
	public void testDelete(){
		Long id = (long)1;
		PowerMockito.mockStatic(Attachment.class);
		Attachment mockedAttachment = Mockito.mock(Attachment.class);
		PowerMockito.when(Attachment.findAttachment(id)).thenReturn(mockedAttachment);
		
		FileUploaderResponse result = aut.delete(id);
		
		Mockito.verify(mockedAttachment).remove();
		assertEquals(true, result.isSuccess());
		assertEquals(id, result.getAttachmentId());
		
	}
	@Test
	@PrepareForTest({Attachment.class, IOUtils.class})
	public void testShow() throws IOException{
		Long attachmentId  = (long)1;
		HttpServletResponse mockedResponse = Mockito.mock(HttpServletResponse.class);
		ServletOutputStream mockedOutputStream = Mockito.mock(ServletOutputStream.class);
		Mockito.doReturn(mockedOutputStream).when(mockedResponse).getOutputStream();
		
		Attachment mockedAttachment = Mockito.mock(Attachment.class);
		Mockito.doReturn((long)1).when(mockedAttachment).getId();
		PowerMockito.mockStatic(Attachment.class);
		PowerMockito.when(Attachment.findAttachment(attachmentId)).thenReturn(mockedAttachment);
		PowerMockito.mockStatic(IOUtils.class);
		
		Mockito.doReturn("realpath").when(servletContext).getRealPath(Mockito.anyString());
		
		
		aut.show(attachmentId, mockedResponse);
		
		Mockito.verify(mockedResponse).getOutputStream();
		PowerMockito.verifyStatic();
		IOUtils.copy(Mockito.any(FileInputStream.class), Mockito.any(ServletOutputStream.class));
		Attachment.findAttachment(attachmentId);
		Mockito.verify(fileService).getFileInPutStream("realpath" + File.separatorChar + "1");
		
	}

}
