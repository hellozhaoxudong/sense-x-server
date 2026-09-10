package com.sense.app.core.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.sense.app.core.utils.CurrentUser;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @ClassName FieldObjectHandler
 * @description 审计字段自动填充
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@Component
public class FieldObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        if (null != getFieldValByName("createUser", metaObject)){
            Long userId = CurrentUser.getUserIdNoException();
            this.strictInsertFill(metaObject, "createUser", () -> userId, Long.class);
            this.strictUpdateFill(metaObject, "updateUser", () -> userId, Long.class);
        }

        this.strictInsertFill(metaObject, "createDate", Date::new, Date.class);
        this.strictUpdateFill(metaObject, "updateDate", Date::new, Date.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (null != getFieldValByName("updateUser", metaObject)){
            Long userId = CurrentUser.getUserIdNoException();
            this.strictUpdateFill(metaObject, "updateUser", () -> userId, Long.class);
        }

        //若是为空才会填充，若是不为空，则不会覆盖
        this.strictUpdateFill(metaObject, "updateDate", Date::new, Date.class);
    }
}
