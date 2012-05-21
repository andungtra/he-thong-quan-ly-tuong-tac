package org.hcmus.tis.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import org.hcmus.tis.dto.DtReply;
import org.hcmus.tis.dto.ProjectDTO;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.ProjectProcess;
import org.hcmus.tis.model.xml.XProjectProcess;
import org.hcmus.tis.service.ProjectProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/projectprocesses")
@Controller
@RooWebScaffold(path = "projectprocesses", formBackingObject = ProjectProcess.class)
public class ProjectProcessController {
	@RequestMapping(value = "/ID/{id}", produces = "text/html")
	public String showProcess(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("projectprocess",
				ProjectProcess.findProjectProcess(id));
		uiModel.addAttribute("itemId", id);
		return "projectprocesses/show";
	}

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(Model uiModel, MultipartFile multipartFile,
			HttpServletRequest httpServletRequest) throws IOException,
			JAXBException {
		if (multipartFile == null) {
			return null;
		}
		byte[] templateFile = multipartFile.getBytes();
		InputStream inputStream = multipartFile.getInputStream();
		JAXBContext context = JAXBContext
				.newInstance("org.hcmus.tis.model.xml");
		/*
		 * Unmarshaller umMarshaller = context.createUnmarshaller();
		 * StreamSource source = new StreamSource(inputStream);
		 * JAXBElement<XProjectProcess> element = umMarshaller.unmarshal(source,
		 * XProjectProcess.class); XProjectProcess xProjectProcess =
		 * element.getValue();
		 */

		ProjectProcess projectProcess = projectProcessService
				.createProjectProcess(templateFile);
		uiModel.addAttribute("projectprocess", projectProcess);
		uiModel.addAttribute("itemId", projectProcess.getId());
		/*
		 * return "redirect:/projectprocesses/"+
		 * encodeUrlPathSegment(projectProcess.getId().toString(),
		 * httpServletRequest);
		 */
		//return "projectprocesses/show";
		return "redirect:/projectprocesses";
	}

	@RequestMapping(value = "mList", params = { "iDisplayStart",
			"iDisplayLength", "sEcho", "sSearch" })
	@ResponseBody
	public DtReply mList(int iDisplayStart, int iDisplayLength, String sEcho, String sSearch) {
		DtReply reply = new DtReply();
		reply.setsEcho(sEcho);
		
		List<ProjectProcess> list = ProjectProcess.findProjectProcessEntries(
				iDisplayStart, iDisplayLength, sSearch);
		for (ProjectProcess item : list) {
			if (item.isIsDeleted() != true) {
				ProjectDTO dto = new ProjectDTO();
				dto.DT_RowId = item.getId();
				dto.setName(item.getName());
				dto.setDescription(item.getDescription());
				reply.getAaData().add(dto);
			}
		}
		reply.setiTotalRecords(reply.getAaData().size());
		return reply;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Long id,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		ProjectProcess projectProcess = projectProcessService
				.findProjectProcess(id);
		// projectProcessService.deleteProjectProcess(projectProcess);
		projectProcess.setIsDeleted(true);
		projectProcess.merge();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/projectprocesses";
	}

	@RequestMapping(produces = "text/html")
	public String list(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		List<ProjectProcess> list = null;
		if (page != null || size != null) {
			int sizeNo = size == null ? 10 : size.intValue();
			final int firstResult = page == null ? 0 : (page.intValue() - 1)
					* sizeNo;
			list = projectProcessService.findProjectProcessEntries(firstResult,
					sizeNo);
			float nrOfPages = (float) projectProcessService
					.countAllProjectProcesses() / sizeNo;
			uiModel.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		} else {
			list = projectProcessService.findAllProjectProcesses();
		}

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).isIsDeleted() == true)
				list.remove(i);
		}
		uiModel.addAttribute("projectprocesses", list);
		return "projectprocesses/list";
	}
}