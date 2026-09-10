package com.sense.app.core.utils;

import com.sense.app.core.user.domain.SysUser;

public class ThreadLocals{
    public static final ThreadLocal<SysUser> TokenTheadLocal = ThreadLocal.withInitial(() -> null);

}
