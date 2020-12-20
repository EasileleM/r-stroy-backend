package com.example.rstroybackend.service;

import com.example.rstroybackend.dto.MailMessageDto;

import javax.mail.MessagingException;

public interface MailService {
    public void sendToOne(String emailTo, MailMessageDto mailMessageDto);
    public void sendToAllSubscribers(MailMessageDto mailMessageDto) throws MessagingException;
}
