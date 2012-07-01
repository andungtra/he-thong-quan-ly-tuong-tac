package org.hcmus.tis.controller;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.hcmus.tis.dto.DtReply;
import org.hcmus.tis.dto.datatables.MemberDTO;
import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.AccountStatus;
import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.MemberRole;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.repository.AccountRepository;
import org.hcmus.tis.repository.MemberInformationRepository;
import org.hcmus.tis.repository.MemberRoleRepository;
import org.hcmus.tis.repository.ProjectRepository;
import org.hcmus.tis.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	private MemberInformationRepository memberInformationRepository;
	@Autowired
	private AccountService accountService;
	@Autowired
	private MemberRoleRepository memberRoleRepository;

	@RequestMapping(params = { "form", "redirectUrl" }, produces = "text/html")
	public String createForm(Model uiModel,
			@PathVariable("projectId") Long projectId, String redirectUrl) {
		List<MemberRole> memberRoles = memberRoleRepository.findAll();
		uiModel.addAttribute("memberRoles", memberRoles);
		uiModel.addAttribute("projectId", projectId);
		uiModel.addAttribute("redirectUrl", redirectUrl);
		return "memberinformations/createfromproject";
	}
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
    	return null;
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
			account = accountRepository.getByEmail(email);
		} catch (Exception e) {
			List<MemberRole> memberRoles = memberRoleRepository.findAll();
			uiModel.addAttribute("memberRoles", memberRoles);
			uiModel.addAttribute("projectId", projectId);
			uiModel.addAttribute("redirectUrl", redirectUrl);
			return "memberinformations/createfromproject";
		}

		MemberRole memberRole = memberRoleRepository.findOne(memberRoleId);
		Project project = projectRepository.findOne(projectId);

		MemberInformation exist = memberInformationRepository
				.findByAccountAndProjectAndDeleted(account, project, false);
		MemberInformation deletedMember = memberInformationRepository
				.findByAccountAndProjectAndDeleted(account, project, true);
		if (account != null && account.getStatus().equals(AccountStatus.ACTIVE)
				&& exist == null && deletedMember == null) {

			MemberInformation memberInformation = new MemberInformation();
			memberInformation.setAccount(account);
			memberInformation.setMemberRole(memberRole);
			memberInformation.setProject(project);
			memberInformationRepository.save(memberInformation);

			/*
			 * if (redirectUrl != null) { return "redirect:" + redirectUrl; }
			 * return "redirect:/memberinformations/" + encodeUrlPathSegment(
			 * memberInformation.getId().toString(), httpServletRequest);
			 */

			Set<MemberInformation> memberInformations = projectRepository.findOne(
					projectId).getMemberInformations();
			uiModel.addAttribute("memberinformations", memberInformations);
			uiModel.addAttribute("projectId", projectId);
			return "projects/memberinformations";

		}

		if (account != null && account.getStatus().equals(AccountStatus.ACTIVE)
				&& deletedMember != null) {
			deletedMember.setDeleted(false);
			memberInformationRepository.save(deletedMember);
			Set<MemberInformation> memberInformations = projectRepository.findOne(
					projectId).getMemberInformations();
			uiModel.addAttribute("memberinformations", memberInformations);
			uiModel.addAttribute("projectId", projectId);
			return "projects/member";
		}

		List<MemberRole> memberRoles = memberRoleRepository.findAll();
		uiModel.addAttribute("memberRoles", memberRoles);
		uiModel.addAttribute("projectId", projectId);
		uiModel.addAttribute("redirectUrl", redirectUrl);
		return "memberinformations/createfromproject";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public Long delete(@PathVariable("id") Long id) {
		MemberInformation memberInformation = memberInformationRepository.findOne(id);
		memberInformation.setDeleted(true);
		memberInformationRepository.save(memberInformation);
		memberInformationRepository.flush();
		return id;
	}

	@RequestMapping(value = "/{id}", params = { "redirectUrl" }, produces = "text/html")
	public String updateFromProjectForm(@PathVariable("id") Long id,
			Model uiModel, String redirectUrl) {
		MemberInformation memberInformation = memberInformationRepository.findOne(id);
		uiModel.addAttribute("memberInformation", memberInformation);
		uiModel.addAttribute("memberRoles", memberRoleRepository.findAll());
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
		memberInformationRepository.save(memberInformation);
		if (redirectUrl != null) {
			return "redirect:" + redirectUrl;
		}
		return "redirect:/memberinformations/"
				+ encodeUrlPathSegment(memberInformation.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(params = { "iDisplayStart", "iDisplayLength", "sEcho",
			"sSearch", "sSearch_0", "sSearch_1", "sSearch_2" })
	@ResponseBody
	public DtReply listByProject(@PathVariable("projectId") Long projectId,
			int iDisplayStart, int iDisplayLength, String sEcho, String sSearch) {
		DtReply reply = new DtReply();
		reply.setsEcho(sEcho);
		Project project = projectRepository.findOne(projectId);
		Pageable pageRequest = new PageRequest(iDisplayStart / iDisplayLength, iDisplayLength);
		Page<MemberInformation> page = memberInformationRepository.findByProjectAndAccountLikeAndDeleted(project, "%" + sSearch + "%", false, pageRequest);
		List<MemberInformation> list = page.getContent();
		for (MemberInformation item : list) {
			if (!item.getDeleted()) {
				MemberDTO dto = new MemberDTO();
				dto.DT_RowId = item.getId();
				String redirectUrl = "/projects/" + projectId + "/members";
				dto.setFirstName("<a href='../projects/" + projectId
						+ "/memberinformations/" + item.getId()
						+ "?fromProjectForm&redirectUrl=" + redirectUrl + "'>"
						+ item.getAccount().getFirstName() + "</a>");
				dto.setLastName(item.getAccount().getLastName());
				dto.setMemberRole(item.getMemberRole().getName());
				reply.getAaData().add(dto);
			}
		}
		reply.setiTotalDisplayRecords((int) page.getTotalElements());
		reply.setiTotalRecords((int)memberInformationRepository.countByProjectAndDeleted(project, false));
		return reply;
	}
    void populateEditForm(Model uiModel, MemberInformation memberInformation) {
        uiModel.addAttribute("memberInformation", memberInformation);
        uiModel.addAttribute("accounts", accountService.findAllAccounts());
        uiModel.addAttribute("memberroles", memberRoleRepository.findAll());
        uiModel.addAttribute("projects", projectRepository.findAll());
    }
}
