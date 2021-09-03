package com.awesome.infrastructures.exceptions;

public enum AwesomeExceptionType {
    EMPTY_TASK ("98", "존재하지 않는 타스크입니다."),
    EMPTY_PROJECT ("99", "존재하지 않는 프로젝트입니다."),
    EMPTY_USER ("100", "존재하지 않는 인력입니다."),
    WRONG_DATE ("101", "시작일은 종료일보다 뒤에 올 수 없습니다."),
    EMPTY_PROJECT_USER ("102", "프로젝트 참여인력을 포함해 주세요."),
    MULTI_LEADER ("103", "프로젝트 리더는 1명을 초과할 수 없습니다."),
    TODO_DATE_INVALID ("104", "설정된 프로젝트 기간은 해야 함 프로젝트로 등록할 수 없습니다."),
    ONE_WEEK_CANCEL ("105", "생성일부터 일주일 미만인 프로젝트는 취소상태로 변경할 수 없습니다."),
    WRONG_PRIORITY ("106", "종료일까지 일주일 또는 이주일 미만인 프로젝트는 특정 우선순위로 변경할 수 없습니다."),
    HIGH_PRIORITY_USER_CHANGE ("108", "매우높음, 또는 높음 우선순위의 프로젝트는 인력을 변경할 수 없습니다."),
    EMPTY_TASK_USER ("109", "타스크 참여인력을 포함해 주세요."),
    EMPTY_TASK_PARENT ("110", "타스크 상위 프로젝트 및 타스크가 지정되어야 합니다."),
    EMPTY_DOCUMENT_USER("111", "산출물 담당인력을 포함해 주세요.")
    ;

    AwesomeExceptionType(String code, String description) {
    }
}
