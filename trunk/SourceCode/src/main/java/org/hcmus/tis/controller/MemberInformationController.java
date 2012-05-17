package org.hcmus.tis.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.hcmus.tis.dto.DtReply;
import org.hcmus.tis.dto.MemberDTO;
import org.hcmus.tis.dto.ProjectDTO;
import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.AccountStatus;
import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.MemberRole;
import org.hcmus.tis.model.Project;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/memberinformations")
@Controller
@RooWebScaffold(path = "memberinformations", formBackingObject = MemberInformation.class)
public class MemberInformationController {

	@RequestMapping(params = { "form", "projectId", "redirectUrl" }, produces = "text/html")
	public String createForm(Model uiModel, Long projectId, String redirectUrl) {
		Collection<MemberRole> memberRoles = MemberRole.findAllMemberRoles();
		uiModel.addAttribute("memberRoles", memberRoles);
		uiModel.addAttribute("projectId", projectId);
		uiModel.addAttribute("redirectUrl", redirectUrl);
		return "memberinformations/createfromproject";
	}

	@RequestMapping(params = { "email", "memberRoleId", "projectId" }, method = RequestMethod.POST, produces = "text/html")
	public String create(Model uiModel,	String email,Long memberRoleId,	Long projectId,	@RequestParam(value = "redirectUrl", required = false) String redirectUrl,
			HttpServletRequest httpServletRequest) {
		MemberRole memberRole = MemberRole.findMemberRole(memberRoleId);
		Project project = Project.findProject(projectId);
		Account account = Account.findAccountsByEmailEquals(email)
				.getResultList().get(0);

		List<MemberInformation> exist =  MemberInformation.findMemberInformationsByAccountAndProject(account, project).getResultList();
				
		
		if (account != null && account.getStatus().equals(AccountStatus.ACTIVE)
				&& exist.size() == 0) {

			MemberInformation memberInformation = new MemberInformation();
			memberInformation.setAccount(account);
			memberInformation.setMemberRole(memberRole);
			memberInformation.setProject(project);
			memberInformation.persist();
			if (redirectUrl != null) {
				return "redirect:" + redirectUrl;
			}
			return "redirect:/memberinformations/"
					+ encodeUrlPathSegment(
							memberInformation.getId().toString(),
							httpServletRequest);
		} else {
			Collection<MemberRole> memberRoles = MemberRole
					.findAllMemberRoles();
			uiModel.addAttribute("memberRoles", memberRoles);
			uiModel.addAttribute("projectId", projectId);
			uiModel.addAttribute("redirectUrl", redirectUrl);
			return "memberinformations/createfromproject";
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(
			@RequestParam(value = "redirectUrl", required = false) String redirectUrl,
			@PathVariable("id") Long id,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		MemberInformation memberInformation = MemberInformation
				.findMemberInformation(id);
		memberInformation.remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		if (redirectUrl != null) {
			return "redirect:" + redirectUrl;
		}
		return "redirect:/memberinformations";
	}

	@RequestMapping(value = "/{id}", params = { "fromProjectForm",
			"redirectUrl" }, produces = "text/html")
	public String updateFromProjectForm(@PathVariable("id") Long id,
			Model uiModel, String redirectUrl) {
		MemberInformation memberInformation = MemberInformation
				.findMemberInformation(id);
		uiModel.addAttribute("memberInformation", memberInformation);
		uiModel.addAttribute("memberRoles", MemberRole.findAllMemberRoles());
		uiModel.addAttribute("redirectUrl", redirectUrl);
		return "memberinformations/updatefromproject";
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(
			@RequestParam(value = "redirectUrl", required = false) String redirectUrl,
			@Valid MemberInformation memberInformation,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, memberInformation);
			return "memberinformations/update";
		}
		uiModel.asMap().clear();
		memberInformation.merge();
		if (redirectUrl != null) {
			return "redirect:" + redirectUrl;
		}
		return "redirect:/memberinformations/"
				+ encodeUrlPathSegment(memberInformation.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(value = "mList/{id}", params = { "iDisplayStart",
			"iDisplayLength", "sEcho" })
	@ResponseBody
	public DtReply mList(@PathVariable("id") Long id, int iDisplayStart,
			int iDisplayLength, String sEcho) {
		DtReply reply = new DtReply();
		reply.setsEcho(sEcho);
		reply.setiTotalRecords((int) MemberInformation
				.countMemberInformations());
		reply.setiTotalDisplayRecords((int) MemberInformation
				.countMemberInformations());
		List<MemberInformation> list = MemberInformation
				.findMemberInformationsByProject(Project.findProject(id));
		for (MemberInformation item : list) {
			MemberDTO dto = new MemberDTO();
			dto.DT_RowId = item.getId();
			dto.setName(item.getAccount().getFirstName() + " "
					+ item.getAccount().getLastName());
			dto.setMemberRole(item.getMemberRole().getName());
			reply.getAaData().add(dto);
		}
		return reply;
	}
}
