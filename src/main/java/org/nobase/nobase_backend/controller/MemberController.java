package org.nobase.nobase_backend.controller;

import org.nobase.nobase_backend.entity.Member;
import org.nobase.nobase_backend.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @PostMapping
    public ResponseEntity<Member> createMember(
            @ModelAttribute MemberDTO memberDTO
    ) throws IOException {
        try {
            String mbId = memberDTO.getMbId();
            String mbEmail = memberDTO.getMbEmail();
            String mbName = memberDTO.getMbName();
            String mbAddress = memberDTO.getMbAddress();
            String mbTel = memberDTO.getMbTel();
            String mbPasswd = memberDTO.getMbPasswd();
            String mbNickname = memberDTO.getMbNickname();
            Member member = memberService.createMember(mbId, mbAddress, mbEmail, mbName, mbPasswd, mbTel, mbNickname);
            return new ResponseEntity<>(member, HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberService.getAllMembers();
        return new ResponseEntity<>(members, HttpStatus.OK);
    }
}
