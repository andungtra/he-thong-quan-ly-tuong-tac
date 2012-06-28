package org.hcmus.tis.controller;

import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.hcmus.tis.dto.DtReply;
import org.hcmus.tis.dto.datatables.ProjectDTO;
import org.hcmus.tis.dto.datatables.StudyClassDTO;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.ProjectStatus;
import org.hcmus.tis.model.StudyClass;
import org.hcmus.tis.repository.StudyClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/studyclasses")
@Controller
@RooWebScaffold(path = "studyclasses", formBackingObject = StudyClass.class)
@RooWebFinder
public class StudyClassController {
	@RequestMapping(value = "/ID/{id}", produces = "text/html")
	public String showClass(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("studyclass", studyClassRepository.findOne(id));
		uiModel.addAttribute("itemId", id);
		return "studyclasses/show";
	}
	 @RequestMapping(method = RequestMethod.POST, produces = "text/html")
	    public String create(@Valid StudyClass studyClass, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
	        if (bindingResult.hasErrors()) {
	            populateEditForm(uiModel, studyClass);
	            return "studyclasses/create";
	        }
	        uiModel.asMap().clear();
	        studyClassRepository.save(studyClass);
	        return "redirect:/studyclasses";
	    }
	 @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	    public String update(@Valid StudyClass studyClass, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
	        if (bindingResult.hasErrors()) {
	            populateEditForm(uiModel, studyClass);
	            return "studyclasses/update";
	        }
	        uiModel.asMap().clear();
	        studyClassRepository.save(studyClass);
	        return "redirect:/studyclasses";
	    }
	    
	@RequestMapping(value = "mList", params = { "iDisplayStart",
			"iDisplayLength", "sEcho", "sSearch"})
	@ResponseBody
	public DtReply mList(int iDisplayStart, int iDisplayLength, String sEcho, String sSearch, HttpServletRequest request) {
		DtReply reply = new DtReply();
		reply.setsEcho(sEcho);
		Pageable pageable = new PageRequest(iDisplayStart / iDisplayLength, iDisplayLength);
		Page<StudyClass> page = null;
		if(sSearch == null || sSearch.length() == 0){
			page = studyClassRepository.findByDeleted(false, pageable);
		}else{
			sSearch   = "%" + sSearch + "%";
			page = studyClassRepository.findByNameLikeAndDeleted(sSearch, false, pageable);
		}
		List<StudyClass> list = page.getContent();
		for (StudyClass item : list) {
			if (item.isDeleted() != true) {
				StudyClassDTO dto = new StudyClassDTO();
				dto.DT_RowId = item.getId();
				dto.setName("<a href='" + request.getContextPath() + "/studyclasses/"+item.getId()+"?form'>"+item.getName()+"</a>");
				dto.setDescription(item.getDescription());

				reply.getAaData().add(dto);
			}
		}
		reply.setiTotalDisplayRecords((int)page.getTotalElements());
		reply.setiTotalRecords((int)studyClassRepository.countStudyClassesNotDeleted());
		return reply;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Long id,
			@RequestParam(value = "listId", required = false) String listId,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		if (listId != null && listId.length() > 0) {
			String [] lst = listId.split(",");
			for (String string : lst) {
				StudyClass studyClass = studyClassRepository.findOne(Long.valueOf(string));
				studyClass.setDeleted(true);
				studyClassRepository.save(studyClass);
			}
		} else {
			StudyClass studyClass = studyClassRepository.findOne(id);
			studyClass.setDeleted(true);
			studyClassRepository.save(studyClass);
		}
		// studyClass.remove();
		//uiModel.asMap().clear();
		studyClassRepository.flush();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/studyclasses";

	}
	@RequestMapping(value = "/{classId}/projects", params = { "iDisplayStart",
			"iDisplayLength", "sEcho", "sSearch" })
	@ResponseBody
	public DtReply listProjects(@PathVariable("classId") Long classId, int iDisplayStart, int iDisplayLength, String sEcho,
			String sSearch, HttpServletRequest request) {
		DtReply reply = new DtReply();
		reply.setsEcho(sEcho);
		StudyClass studyClass = studyClassRepository.findOne(classId);
		studyClass.setId(classId);
		Pageable pageable = new PageRequest(iDisplayStart / iDisplayLength,
				iDisplayLength);
		Page<Project> page = projectRepository.findByNameLikeAndStudyClassAndStatusNot("%"
				+ sSearch + "%",studyClass, ProjectStatus.DELETED, pageable);
		List<Project> list = page.getContent();
		for (Project item : list) {
			ProjectDTO dto = new ProjectDTO();
			dto.DT_RowId = item.getId();
			dto.setName("<a href='" + request.getContextPath() + "/projects/" + item.getId() + "?goto=true'>"
					+ item.getName() + "</a>");

			if (item.getParentContainer() != null)
				dto.setParentContainer(item.getParentContainer().getName());
			String s;
			if (item.getDescription() != null
					&& item.getDescription().length() > 50)
				s = item.getDescription().substring(0, 49) + " ...";
			else
				s = item.getDescription();
			dto.setDescription(s);

			reply.getAaData().add(dto);
		}
		reply.setiTotalDisplayRecords((int) page.getTotalElements());
		reply.setiTotalRecords((int) projectRepository
				.countByStudyClassAndStatusNot(studyClass,ProjectStatus.DELETED));
		return reply;
	}
}
