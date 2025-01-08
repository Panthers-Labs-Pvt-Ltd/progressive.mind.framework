package com.progressive.minds.chimera.common.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Definition of all the messages errors
 */
@Getter
@AllArgsConstructor
public enum ExceptionMessage {
    ILLEGAL_ARGUMENT("Illegal Argument"),
    REMOTE_SERVICE_ERROR("Remote service error"),
    REMOTE_SERVICE_TIMEOUT_ERROR("Remote service timeout error"),
    UNEXPECTED_ERROR("Unexpected error");

    String description;
}
