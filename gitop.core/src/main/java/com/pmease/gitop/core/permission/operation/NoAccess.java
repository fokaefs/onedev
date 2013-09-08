package com.pmease.gitop.core.permission.operation;

@SuppressWarnings("serial")
public class NoAccess implements PrivilegedOperation {

	@Override
	public boolean can(PrivilegedOperation operation) {
		return false;
	}

}
