package com.banking.core.api.response;

import java.util.List;

public record OperationHistoryResponse(
    List<OperationResponse> items,
    int limit,
    int offset,
    int nextOffset
) {
}
