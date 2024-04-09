package org.nobase.nobase_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.nobase.nobase_backend.entity.Member;
import org.nobase.nobase_backend.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberService {
    @Value("${memberFile.upload-dir}")
    private String path;
    private Path rootLocation;
    private final MemberRepository memberRepository;

    public Member createMember(String mbId, String mbAddress, String mbEmail, String mbImgPath, String mbName, String mbPasswd, String mbTel, String mbUsername, MultipartFile imageFile) throws IOException {

        this.rootLocation = Paths.get(path).toAbsolutePath().normalize();
        String filename = storeFile(imageFile, mbId);
        String filePath = rootLocation.resolve(filename).toString();
        Member member = new Member();
        member.setMbId(mbId);
        member.setMbAddress(mbAddress);
        member.setMbEmail(mbEmail);
        member.setMbImgPath(mbImgPath);
        member.setMbName(mbName);
        member.setMbPasswd(mbPasswd);
        member.setMbTel(mbTel);
        member.setMbUsername(mbUsername);
        member.setMbImgPath(filePath);

        return memberRepository.save(member);
    }

    public String storeFile(MultipartFile file, String mbId) throws IOException {
        String filename = generateFileName(file, mbId);  //파일 이름 생성(userID_20240401_133340.png)
        Files.createDirectories(rootLocation);
        Files.copy(file.getInputStream(), this.rootLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        return filename;
    }

    public String generateFileName(MultipartFile file, String mbId) {
        String filename = file.getOriginalFilename();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String formattedDateTime = LocalDateTime.now().format(formatter);
        int dotIndex = filename.lastIndexOf('.');  //파일 확장자 파싱
        if (dotIndex < 0) {
            return null;
        }
        String extension = filename.substring(dotIndex);
        filename = mbId + "_" + formattedDateTime + extension;
        return filename;
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }
}