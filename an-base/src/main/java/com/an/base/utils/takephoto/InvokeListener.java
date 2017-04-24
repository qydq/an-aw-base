package com.an.base.utils.takephoto;


import com.an.base.model.entity.InvokeParam;
import com.an.base.utils.ytips.PermissionManager;

/**
 * 授权管理回调
 */
public interface InvokeListener {
    PermissionManager.TPermissionType invoke(InvokeParam invokeParam);
}
