package org.dromio.client001.models.data;

import java.time.LocalDateTime;

public class AuditLog {
    String logId;
    String userId;
    String action;
    String tableName;
    String recordId;
    LocalDateTime timestamp;
}
