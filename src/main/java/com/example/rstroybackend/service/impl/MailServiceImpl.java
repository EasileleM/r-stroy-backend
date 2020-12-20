package com.example.rstroybackend.service.impl;

import com.example.rstroybackend.dto.MailMessageDto;
import com.example.rstroybackend.entity.User;
import com.example.rstroybackend.repo.UserRepo;
import com.example.rstroybackend.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepo userRepo;

    @Value("${spring.mail.username}")
    private String username;

    @Override
    public void sendToOne(String emailTo, MailMessageDto mailMessageDto) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(username);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(mailMessageDto.getSubject());
        mailMessage.setText(mailMessageDto.getMessage());

        mailSender.send(mailMessage);
    }

    @Override
    public void sendToAllSubscribers(MailMessageDto mailMessageDto) throws MessagingException {
        List<User> users = userRepo.findByIsSubscribedTrue();
        String[] userEmails = new String[users.size()];
        users.stream()
                .map(User::getEmail)
                .collect(Collectors.toList())
                .toArray(userEmails);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setText(mailMessageDto.getMessage(), true);
        helper.setSubject(mailMessageDto.getSubject());

        helper.setFrom(username);
        helper.setTo(userEmails);

        mailSender.send(mimeMessage);
    }
}
