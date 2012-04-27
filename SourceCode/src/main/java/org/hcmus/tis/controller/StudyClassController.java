package org.hcmus.tis.controller;

import java.util.List;

import org.hcmus.tis.dto.DtReply;
import org.hcmus.tis.dto.ProjectDTO;
import org.hcmus.tis.dto.StudyClassDTO;
import org.hcmus.tis.model.Project;
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
    public String findStudyClassesQuickly(@RequestParam("query") String query, Model uiModel) {
        if (query.isEmpty()) {
            uiModel.addAttribute("studyclasses", StudyClass.findAllStudyClasses());
        } else {
            uiModel.addAttribute("studyclasses", StudyClass.findStudyClassesByNameLike(query).getResultList());
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
    
    @RequestMapping(value = "mList", params = { "iDisplayStart", "iDisplayLength", "sEcho" })
	@ResponseBody
	public DtReply mList( int iDisplayStart,int iDisplayLength, String sEcho) {		
		DtReply reply = new DtReply();
		reply.setsEcho(sEcho);
		reply.setiTotalRecords((int) StudyClass.countStudyClasses());
		reply.setiTotalDisplayRecords((int)   StudyClass.countStudyClasses());
		List<StudyClass> list = StudyClass.findStudyClassEntries(iDisplayStart,iDisplayLength);
		for (StudyClass item : list) {
			StudyClassDTO dto = new StudyClassDTO();
			dto.DT_RowId = item.getId();
			dto.setName(item.getName());			
			dto.setDescription(item.getDescription());
			
			reply.getAaData().add(dto);
		}		
		return reply;
	}
}
