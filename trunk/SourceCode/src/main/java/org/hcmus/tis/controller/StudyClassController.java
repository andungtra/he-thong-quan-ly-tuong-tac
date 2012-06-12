package org.hcmus.tis.controller;

import java.util.List;

import org.hcmus.tis.dto.DtReply;
import org.hcmus.tis.dto.datatables.StudyClassDTO;
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

	@RequestMapping(value = "mList", params = { "iDisplayStart",
			"iDisplayLength", "sEcho", "sSearch"})
	@ResponseBody
	public DtReply mList(int iDisplayStart, int iDisplayLength, String sEcho, String sSearch) {
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
				dto.setName("<a href='../studyclasses/"+item.getId()+"?form'>"+item.getName()+"</a>");
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
			StudyClass studyClass = StudyClass.findStudyClass(id);
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
}
