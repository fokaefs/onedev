package com.gitplex.server.web.page.account;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.gitplex.server.security.SecurityUtils;
import com.gitplex.server.web.component.avatar.Avatar;
import com.gitplex.server.web.component.link.ViewStateAwarePageLink;
import com.gitplex.server.web.component.tabbable.PageTab;
import com.gitplex.server.web.component.tabbable.Tabbable;
import com.gitplex.server.web.page.account.collaborators.AccountCollaboratorListPage;
import com.gitplex.server.web.page.account.collaborators.CollaboratorPage;
import com.gitplex.server.web.page.account.members.MemberListPage;
import com.gitplex.server.web.page.account.members.MemberPage;
import com.gitplex.server.web.page.account.members.NewMembersPage;
import com.gitplex.server.web.page.account.overview.AccountOverviewPage;
import com.gitplex.server.web.page.account.overview.NewDepotPage;
import com.gitplex.server.web.page.account.overview.NewOrganizationPage;
import com.gitplex.server.web.page.account.setting.AvatarEditPage;
import com.gitplex.server.web.page.account.setting.PasswordEditPage;
import com.gitplex.server.web.page.account.setting.ProfileEditPage;
import com.gitplex.server.web.page.account.tasks.TaskListPage;
import com.gitplex.server.web.page.account.teams.TeamListPage;
import com.gitplex.server.web.page.account.teams.TeamPage;

@SuppressWarnings("serial")
public abstract class AccountLayoutPage extends AccountPage {
	
	public AccountLayoutPage(PageParameters params) {
		super(params);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		ViewStateAwarePageLink<Void> avatarLink = new ViewStateAwarePageLink<Void>("avatar", 
				AvatarEditPage.class, AvatarEditPage.paramsOf(getAccount())) {

			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				if (!isEnabled())
					tag.setName("span");
			}
			
		};
		if (!SecurityUtils.canManage(getAccount())) {
			avatarLink.setEnabled(false);
		}
		add(avatarLink);
		avatarLink.add(new Avatar("avatar", accountModel.getObject()));
		
		List<PageTab> tabs = new ArrayList<>();
		
		tabs.add(new AccountTab("Overview", "fa fa-fw fa-list-alt", 0, 
				AccountOverviewPage.class, NewOrganizationPage.class, NewDepotPage.class));

		if (getAccount().isOrganization()) {
			if (SecurityUtils.canAccess(getAccount())) {
				tabs.add(new AccountTab("Members", "fa fa-fw fa-user",
						getAccount().getOrganizationMembers().size(),
						MemberListPage.class, NewMembersPage.class, MemberPage.class));
				tabs.add(new AccountTab("Teams", "fa fa-fw fa-group", 
						getAccount().getDefinedTeams().size(), TeamListPage.class, TeamPage.class));
			} 
			if (SecurityUtils.canManage(getAccount())) {
				tabs.add(new AccountTab("Collaborators", "fa fa-fw fa-user",
						getAccount().getAllUserAuthorizationsInOrganization().size(),
						AccountCollaboratorListPage.class, CollaboratorPage.class));
				tabs.add(new AccountTab("Setting", "fa fa-fw fa-cog", 0, ProfileEditPage.class, AvatarEditPage.class));
			} 
		} else if (SecurityUtils.canManage(getAccount())) {
			tabs.add(new AccountTab("Collaborators", "fa fa-fw fa-user", 
					getAccount().getAllUserAuthorizationsInOrganization().size(),
					AccountCollaboratorListPage.class, CollaboratorPage.class));
			tabs.add(new AccountTab("Tasks", "fa fa-fw fa-bell-o", 
					getAccount().getRequestTasks().size(), TaskListPage.class));
			tabs.add(new AccountTab("Setting", "fa fa-fw fa-cog", 0, 
					ProfileEditPage.class, AvatarEditPage.class, PasswordEditPage.class));
		}
		add(new Tabbable("accountTabs", tabs));
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(CssHeaderItem.forReference(new AccountResourceReference()));
	}

}