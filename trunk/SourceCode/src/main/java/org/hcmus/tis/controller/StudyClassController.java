package org.hcmus.tis.controller;

import java.util.List;

import org.hcmus.tis.dto.DtReply;
import org.hcmus.tis.dto.StudyClassDTO;
import org.hcmus.tis.model.StudyClass;
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

	@RequestMapping(params = "find=quickFind", method = RequestMethod.GET)
	public String findStudyClassesQuickly(@RequestParam("query") String query,
			Model uiModel) {
		if (query.isEmpty()) {
			uiModel.addAttribute("studyclasses",
					StudyClass.findAllStudyClasses());
		} else {
			uiModel.addAttribute("studyclasses", StudyClass
					.findStudyClassesByNameLike(query).getResultList());
		}
		uiModel.addAttribute("query", query);
		return "studyclasses/list";
	}

	@RequestMapping(value = "/ID/{id}", produces = "text/html")
	public String showClass(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("studyclass", StudyClass.findStudyClass(id));
		uiModel.addAttribute("itemId", id);
		return "studyclasses/show";
	}

	@RequestMapping(value = "mList", params = { "iDisplayStart",
			"iDisplayLength", "sEcho", "sSearch", "sSearch_0", "sSearch_1" })
	@ResponseBody
	public DtReply mList(int iDisplayStart, int iDisplayLength, String sEcho, String sSearch, String sSearch_0, String sSearch_1) {
		DtReply reply = new DtReply();
		reply.setsEcho(sEcho);

		List<StudyClass> list = StudyClass.findStudyClassEntries(iDisplayStart,
				iDisplayLength, sSearch, sSearch_0, sSearch_1 );
		for (StudyClass item : list) {
			if (item.isIsDeleted() != true) {
				StudyClassDTO dto = new StudyClassDTO();
				dto.DT_RowId = item.getId();
				dto.setName("<a href='../studyclasses/"+item.getId()+"?form'>"+item.getName()+"</a>");
				dto.setDescription(item.getDescription());

				reply.getAaData().add(dto);
			}
		}
		reply.setiTotalRecords(reply.getAaData().size());
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
				StudyClass studyClass = StudyClass.findStudyClass(Long.valueOf(string));
				studyClass.setIsDeleted(true);
				studyClass.merge();
			}
		} else {
			StudyClass studyClass = StudyClass.findStudyClass(id);
			studyClass.setIsDeleted(true);
			studyClass.merge();
		}
		// studyClass.remove();
		//uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/studyclasses";

	}

	@RequestMapping(produces = "text/html")
	public String list(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {

		List<StudyClass> list = null;
		if (page != null || size != null) {
			int sizeNo = size == null ? 10 : size.intValue();
			final int firstResult = page == null ? 0 : (page.intValue() - 1)
					* sizeNo;
			list = StudyClass.findStudyClassEntries(firstResult, sizeNo);

			float nrOfPages = (float) StudyClass.countStudyClasses() / sizeNo;
			uiModel.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		} else {
			list = StudyClass.findAllStudyClasses();
		}

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).isIsDeleted() == true)
				list.remove(i);
		}
		uiModel.addAttribute("studyclasses", list);
		return "studyclasses/list";
	}
}
