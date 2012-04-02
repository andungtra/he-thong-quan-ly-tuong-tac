package org.hcmus.tis.controller;

import java.util.List;
import org.hcmus.tis.dto.JqgridResponse;
import org.hcmus.tis.dto.Page;
import org.hcmus.tis.dto.PageRequest;
import org.hcmus.tis.dto.StudyClassDTO;
import org.hcmus.tis.model.StudyClass;
import org.hcmus.tis.util.StudyClassMapper;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    public JqgridResponse<org.hcmus.tis.dto.StudyClassDTO> getFilteredRecords(String filters, PageRequest pageRequest) {
        return null;
    }

    @RequestMapping(value = "/records", produces = "application/json")
    @ResponseBody
    public JqgridResponse<org.hcmus.tis.dto.StudyClassDTO> records(@RequestParam("_search") Boolean search, @RequestParam(value = "filters", required = false) String filters, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "rows", required = false) Integer rows, @RequestParam(value = "sidx", required = false) String sidx, @RequestParam(value = "sord", required = false) String sord) {
        PageRequest pageRequest = new PageRequest(page - 1, rows);
        if (search == true) {
            return getFilteredRecords(filters, pageRequest);
        }
        Page<StudyClass> users = StudyClass.findAll(pageRequest);
        List<StudyClassDTO> userDtos = StudyClassMapper.map(users);
        JqgridResponse<StudyClassDTO> response = new JqgridResponse<StudyClassDTO>();
        response.setRows(userDtos);
        response.setRecords(Long.valueOf(users.getTotalElements()).toString());
        response.setTotal(Integer.valueOf((int) users.getTotalPages()).toString());
        response.setPage(Integer.valueOf(users.getNumber() + 1).toString());
        return response;
    }
}
