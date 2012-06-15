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
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.hcmus.tis.dto.FileUploaderResponse;
import org.hcmus.tis.model.Attachment;
import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.repository.AttachmentRepository;
import org.hcmus.tis.service.FileService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.springframework.ui.Model;

@RunWith(PowerMockRunner.class)
public class AttachmentControllerTest extends AbstractShiroTest{
	private AttachmentController aut;
	@Mock
	private Model uiModel;
	@Mock
	private FileService fileService;
	@Mock
	private ServletContext servletContext;
	@Mock
	private Subject mockedSubject;
	@Mock
	AttachmentRepository attachmentRepository;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		aut = new AttachmentController();
		aut.setContext(servletContext);
		aut.setFileService(fileService);
		aut.setAttachmentRepository(attachmentRepository);
		doReturn(true).when(mockedSubject).isAuthenticated();
		doThrow(UnauthorizedException.class).when(mockedSubject).checkPermission(anyString());
		setSubject(mockedSubject);
	}
	@After
	public void tearDown(){
		clearSubject();
	}
	@Test
	@PrepareForTest({AttachmentController.class,
			IOUtils.class })
	public void testCreate() throws Exception {
		HttpServletRequest httpServletRequest =mock(HttpServletRequest.class);
		Attachment mockedAttachment = mock(Attachment.class);
		doReturn((long) 1).when(mockedAttachment).getId();
		PowerMockito.whenNew(Attachment.class).withNoArguments()
				.thenReturn(mockedAttachment);
		String fileName = "fileName";
		doReturn(fileName).when(httpServletRequest)
				.getHeader("X-File-Name");
		PowerMockito.mockStatic(IOUtils.class);

		FileUploaderResponse result = aut.create(uiModel, httpServletRequest);
		PowerMockito.verifyStatic();
		IOUtils.copy(any(InputStream.class),
				any(FileOutputStream.class));
		verify(attachmentRepository).save(mockedAttachment);
		verify(mockedAttachment).setDisplayFileName(fileName);
		verify(fileService).getFileOutPutStream(
				contains(mockedAttachment.getId().toString()));
		Assert.assertEquals(true, result.isSuccess());
		assertEquals(mockedAttachment.getId(), result.getAttachmentId());
		verify(mockedAttachment).setRealFileName(anyString());
	}

	@Test
	@PrepareForTest({AttachmentController.class,
			IOUtils.class, WorkItem.class })
	public void testCreateFromWorkItem() throws Exception {
		HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
		Long workItemId = (long)1;
		Attachment mockedAttachment = mock(Attachment.class);
		doReturn((long) 1).when(mockedAttachment).getId();
		PowerMockito.whenNew(Attachment.class).withNoArguments()
				.thenReturn(mockedAttachment);
		String fileName = "fileName";
		doReturn(fileName).when(httpServletRequest)
				.getHeader("X-File-Name");
		PowerMockito.mockStatic(IOUtils.class);
		PowerMockito.mockStatic(WorkItem.class);
		WorkItem mockedWorkItem = mock(WorkItem.class);
		PowerMockito.when(WorkItem.findWorkItem(workItemId)).thenReturn(mockedWorkItem);

		FileUploaderResponse result = aut.createFromWorkItem(uiModel, workItemId,httpServletRequest);
		PowerMockito.verifyStatic();
		IOUtils.copy(any(InputStream.class),
				any(FileOutputStream.class));
		verify(mockedAttachment).setWorkItem(mockedWorkItem);
		verify(attachmentRepository).save(mockedAttachment);
		verify(mockedAttachment).setDisplayFileName(fileName);
		verify(fileService).getFileOutPutStream(
				contains(mockedAttachment.getId().toString()));
		Assert.assertEquals(true, result.isSuccess());
		assertEquals(mockedAttachment.getId(), result.getAttachmentId());
		verify(mockedAttachment).setRealFileName(anyString());
	}
	@Test
	public void testDelete(){
		Long id = (long)1;
		Attachment mockedAttachment = mock(Attachment.class);
		doReturn(mockedAttachment).when(attachmentRepository).findOne(id);
		
		FileUploaderResponse result = aut.delete(id);
		
		verify(attachmentRepository).delete(mockedAttachment);
		assertEquals(true, result.isSuccess());
		assertEquals(id, result.getAttachmentId());
		
	}
	@Test
	@PrepareForTest({IOUtils.class})
	public void testShow() throws IOException{
		Long attachmentId  = (long)1;
		HttpServletResponse mockedResponse = mock(HttpServletResponse.class);
		ServletOutputStream mockedOutputStream = mock(ServletOutputStream.class);
		doReturn(mockedOutputStream).when(mockedResponse).getOutputStream();
		
		Attachment mockedAttachment = mock(Attachment.class);
		doReturn((long)1).when(mockedAttachment).getId();
		doReturn(mockedAttachment).when(attachmentRepository).findOne(attachmentId);
		PowerMockito.mockStatic(IOUtils.class);
		
		doReturn("realpath").when(servletContext).getRealPath(anyString());
		
		
		aut.show(attachmentId, mockedResponse);
		
		verify(mockedResponse).getOutputStream();
		PowerMockito.verifyStatic();
		IOUtils.copy(any(FileInputStream.class), any(ServletOutputStream.class));
		verify(fileService).getFileInPutStream("realpath" + File.separatorChar + "1");
		
	}

}
