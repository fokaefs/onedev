package io.onedev.server.model.support.administration.authenticator;

import java.io.Serializable;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;

import io.onedev.server.util.Usage;
import io.onedev.server.web.editable.annotation.Editable;
import io.onedev.server.web.editable.annotation.GroupChoice;

@Editable
public abstract class Authenticator implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String defaultGroup;
	
	private int timeout = 300;

	@Editable(order=10000, description="Specify network timeout in seconds when authenticate through this system")
	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	@Editable(order=20000, description="Optionally add newly authenticated user to specified group if group "
			+ "information is not available in external authentication system")
	@GroupChoice
	public String getDefaultGroup() {
		return defaultGroup;
	}

	public void setDefaultGroup(String defaultGroup) {
		this.defaultGroup = defaultGroup;
	}

	public void onRenameGroup(String oldName, String newName) {
		if (oldName.equals(defaultGroup))
			defaultGroup = newName;
	}
	
	public Usage onDeleteGroup(String groupName) {
		Usage usage = new Usage();
		if (groupName.equals(defaultGroup))
			usage.add("default group");
		return usage.prefix("external authenticator");
	}
	
	public abstract Authenticated authenticate(UsernamePasswordToken token) throws AuthenticationException;
	
}
