package com.quyetdw.winmall.domain;

public enum AccountStatus {
    PENDING_VERIFICATION,   // Tài khoản đang xác minh
    ACTIVE,                 // Tài khoản hoạt động
    SUSPENDED,              // Tài khoản tạm thời đình chỉ
    DEACTIVATED,            // Tài khoản bị vô hiệu hóa
    BANNED,                 // Tài khoản đã bị cấm
    CLOSED                  // Tài khoản đã đóng
}
