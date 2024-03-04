package com.example.hotsix_be.member.dto.request;


import static lombok.AccessLevel.PRIVATE;

import com.example.hotsix_be.member.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class MemberRoleRequest {

    private Role role;
}
