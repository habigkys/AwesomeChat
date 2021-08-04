package com.awesome.infrastructures;

public enum AwesomeExceptionType {
    WRONG_DATE ("101", "시작일은 종료일보다 뒤에 올 수 없습니다."),
    EMPTY_USER ("102", "프로젝트 참여인력을 포함해 주세요."),
    MULTI_LEADER ("103", "프로젝트 리더는 1명을 초과할 수 없습니다."),
    TODO_DATE_INVALID ("104", "설정된 프로젝트 기간은 해야 함 프로젝트로 등록할 수 없습니다."),
    ONE_WEEK_CANCEL ("105", "생성일부터 일주일 미만인 프로젝트는 취소상태로 변경할 수 없습니다."),
    ONE_WEEK_PRIORITY ("106", "종료일까지 일주일 미만인 프로젝트는 우선순위를 매우 높음으로 변경할 수 없습니다."),
    TWO_WEEK_PRIORITY ("107", "종료일까지 이주일 미만인 프로젝트는 우선순위를 높음으로 변경할 수 없습니다."),
    HIGH_PRIORITY_USER_CHANGE ("108", "매우높음, 또는 높음 우선순위의 프로젝트는 인력을 변경할 수 없습니다.")
    ;

    AwesomeExceptionType(String code, String description) {
    }
}
