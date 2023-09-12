package com.yt.ytojcodesandbox.security;

import java.security.Permission;

public class DefaultSecurityManage extends SecurityManager {

    @Override
    public void checkPermission(Permission perm) {
        throw new SecurityException("权限不足: " + perm.toString());
    }
}
