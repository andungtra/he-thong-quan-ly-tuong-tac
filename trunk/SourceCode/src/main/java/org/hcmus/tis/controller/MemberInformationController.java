package org.hcmus.tis.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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

@RequestMapping("projects/{projectId}/memberinformations")
@Controller
@RooWebScaffold(path = "memberinformations", formBackingObject = MemberInformation.class)
public class MemberInformationController {

	@RequestMapping(params = { "form", "redirectUrl" }, produces = "text/html")
	public String createForm(Model uiModel,@PathVariable("projectId") Long projectId, String redirectUrl) {
		Collection<MemberRole> memberRoles = MemberRole.findAllMemberRoles();
		uiModel.addAttribute("memberRoles", memberRoles);
		uiModel.addAttribute("projectId", projectId);
		uiModel.addAttribute("redirectUrl", redirectUrl);
		return "memberinformations/createfromproject";
	}

	@RequestMapping(params = { "email", "memberRoleId", "projectId" }, method = RequestMethod.POST, produces = "text/html")
	public String create(
			Model uiModel,
			String email,
			Long memberRoleId,
			Long projectId,
			@RequestParam(value = "redirectUrl", required = false) String redirectUrl,
			HttpServletRequest httpServletRequest) {
		Account account = null;
		try {
			account = Account.getAccountbyEmail(email);
		} catch (Exception e) {
			Collection<MemberRole> memberRoles = MemberRole
					.findAllMemberRoles();
			uiModel.addAttribute("memberRoles", memberRoles);
			uiModel.addAttribute("projectId", projectId);
			uiModel.addAttribute("redirectUrl", redirectUrl);
			return "memberinformations/createfromproject";
		}

		MemberRole memberRole = MemberRole.findMemberRole(memberRoleId);
		Project project = Project.findProject(projectId);

		List<MemberInformation> exist = MemberInformation
				.findMemberInformationsByAccountAndProject(account, project)
				.getResultList();

		if (account != null && account.getStatus().equals(AccountStatus.ACTIVE)
				&& exist.size() == 0) {

			MemberInformation memberInformation = new MemberInformation();
			memberInformation.setAccount(account);
			memberInformation.setMemberRole(memberRole);
			memberInformation.setProject(project);
			memberInformation.persist();

			/*
			 * if (redirectUrl != null) { return "redirect:" + redirectUrl; }
			 * return "redirect:/memberinformations/" + encodeUrlPathSegment(
			 * memberInformation.getId().toString(), httpServletRequest);
			 */

			Set<MemberInformation> memberInformations = Project.findProject(
					projectId).getMemberInformations();
			uiModel.addAttribute("memberinformations", memberInformations);
			uiModel.addAttribute("projectId", projectId);
			return "projects/member";

		} else if (account != null
				&& account.getStatus().equals(AccountStatus.ACTIVE)
				&& exist.size() > 0 && exist.get(0).getDeleted() == true) {
			MemberInformation memberInformation = exist.get(0);
			memberInformation.setDeleted(false);
			memberInformation.merge();

			/*
			 * if (redirectUrl != null) { return "redirect:" + redirectUrl; }
			 * return "redirect:/memberinformations/" + encodeUrlPathSegment(
			 * memberInformation.getId().toString(), httpServletRequest);
			 */
			Set<MemberInformation> memberInformations = Project.findProject(
					projectId).getMemberInformations();
			uiModel.addAttribute("memberinformations", memberInformations);
			uiModel.addAttribute("projectId", projectId);
			return "projects/member";
		}

		else {
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
		memberInformation.setDeleted(true);
		memberInformation.merge();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		if (redirectUrl != null) {
			return "redirect:" + redirectUrl;
		}
		return "redirect:/memberinformations";
	}

	@RequestMapping(value = "/{id}", params = {"redirectUrl" }, produces = "text/html")
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

	@RequestMapping(params = { "iDisplayStart",
			"iDisplayLength", "sEcho", "sSearch", "sSearch_0", "sSearch_1", "sSearch_2" })
	@ResponseBody
	public DtReply listByProject(@PathVariable("projectId") Long projectId, int iDisplayStart,
			int iDisplayLength, String sEcho, String sSearch, String sSearch_0, String sSearch_1, String sSearch_2) {
		DtReply reply = new DtReply();
		reply.setsEcho(sEcho);

		List<MemberInformation> list = MemberInformation
				.findMemberInformationsByProjectBaseAccount(Project.findProject(projectId),iDisplayStart, iDisplayLength, sSearch, sSearch_0, sSearch_1, sSearch_2);
		for (MemberInformation item : list) {
			if (!item.getDeleted()) {
				MemberDTO dto = new MemberDTO();
				dto.DT_RowId = item.getId();
				dto.setFirstName(item.getAccount().getFirstName());
				dto.setLastName(item.getAccount().getLastName());
				dto.setMemberRole(item.getMemberRole().getName());
				reply.getAaData().add(dto);
			}
		}
		reply.setiTotalRecords(reply.getAaData().size());
		return reply;
	}
}
