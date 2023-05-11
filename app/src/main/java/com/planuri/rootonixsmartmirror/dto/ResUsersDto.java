package com.planuri.rootonixsmartmirror.dto;

import com.planuri.rootonixsmartmirror.model.kioskuser.KioskUser;

import java.util.List;

/**
 * 전화번호 검색 요청 후 응답 받을 객체
 */
public class ResUsersDto {

    public boolean success;
    public int code;
    public String msg;

    public List<KioskUser> list;
}
