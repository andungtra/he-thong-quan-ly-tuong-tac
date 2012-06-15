package org.hcmus.tis.controller;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.Comment;
import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.repository.AccountRepository;
import org.hcmus.tis.repository.CommentRepository;
import org.hcmus.tis.repository.MemberInformationRepository;
import org.hcmus.tis.service.EmailService;
import org.hcmus.tis.util.NotifyAboutWorkItemTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
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

@RequestMapping("/projects/{projectId}/workitems/{workitemid}/comments")
@Controller
@RooWebScaffold(path = "comments", formBackingObject = Comment.class)
public class CommentController {
	@Autowired
	private EmailService emailService;
	@Autowired
	private TaskExecutor taskExecutor;
	@Autowired
	private CommentRepository commentRepository;
	public CommentRepository getCommentRepository() {
		return commentRepository;
	}

	public void setCommentRepository(CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
	}

	public AccountRepository getAccountRepository() {
		return accountRepository;
	}

	public void setAccountRepository(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private MemberInformationRepository memberInformationRepository;
	public MemberInformationRepository getMemberInformationRepository() {
		return memberInformationRepository;
	}

	public void setMemberInformationRepository(
			MemberInformationRepository memberInformationRepository) {
		this.memberInformationRepository = memberInformationRepository;
	}

	@RequestMapping(produces = "text/html")
	public String listCommentsByWorkItem(
			@PathVariable("workitemid") Long workItemId,
			Integer firstResult, Integer maxResult, Model uiModel) {
		if (firstResult == null) {
			firstResult = 0;
		}
		if (maxResult == null) {
			maxResult = 100;
		}
		Pageable pageable = new PageRequest(firstResult / maxResult, maxResult);
		WorkItem workItem = WorkItem.findWorkItem(workItemId);
		Page<Comment> page = commentRepository.findByWorkItem(workItem, pageable);
		List<Comment> comments = page.getContent();
		uiModel.addAttribute("comments", comments);
		uiModel.addAttribute("workitem", workItem);
		return "comments/listByWorkItem";
	}
    void populateEditForm(Model uiModel, Comment comment) {
        uiModel.addAttribute("comment", comment);
        addDateTimeFormatPatterns(uiModel);
/*        uiModel.addAttribute("memberinformations", MemberInformation.findAllMemberInformations());
        uiModel.addAttribute("workitems", WorkItem.findAllWorkItems());*/
    }
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid Comment comment, BindingResult bindingResult,
			Model uiModel) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, comment);
			return "comments/create";
		}
		uiModel.asMap().clear();
		String name = (String) SecurityUtils.getSubject().getPrincipal();
		WorkItem workItem = comment.getWorkItem();
		Project project = workItem.getWorkItemContainer().getParentProjectOrMyself();
		Account loginAccount = accountRepository.getByEmail(name);
		MemberInformation memberInformation = memberInformationRepository.findByAccountAndProjectAndDeleted(loginAccount,
						project, false);
		comment.setProjectMember(memberInformation);
		commentRepository.save(comment);
		workItem.getSubcribers().toArray();
		taskExecutor.execute(new NotifyAboutWorkItemTask(workItem, "commented", emailService));
		return "redirect:/projects/" + project.getId() + "/workitems/" + workItem.getId() + "/comments";
	}

	/*public String list(Integer page, Integer size, Model uiModel) {
		if (page != null || size != null) {
			int sizeNo = size == null ? 10 : size.intValue();
			final int firstResult = page == null ? 0 : (page.intValue() - 1)
					* sizeNo;
			uiModel.addAttribute("comments",
					Comment.findCommentEntries(firstResult, sizeNo));
			float nrOfPages = (float) Comment.countComments() / sizeNo;
			uiModel.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		} else {
			uiModel.addAttribute("comments", Comment.findAllComments());
		}
		addDateTimeFormatPatterns(uiModel);
		return "comments/list";
	}*/

	public TaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

}
