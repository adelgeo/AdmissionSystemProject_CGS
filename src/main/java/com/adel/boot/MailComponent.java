package com.adel.boot;


//import com.adel.model.Contact;

import com.adel.model.Student;
import com.adel.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class MailComponent {

    @Autowired
    MailSender mailSender;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    TemplateEngine templateEngine;
    @Autowired
    MailService mailService;

    @Value("${spring.mail.username}")
    String defaultFrom;

    public boolean sendSimpleMail(Student student) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(defaultFrom);
        mailMessage.setTo(student.getStudentEmail());
        mailMessage.setSubject("School Registration");
        mailMessage.setText(student.getAddress());

        try {
            mailSender.send(mailMessage);
            return true;
        } catch (MailException e) {
            return false;
        }

    }

    public boolean sendHtmlMail(Student student) {

        Context context = new Context();
        context.setVariable("student", student);
        final String content = templateEngine.process("email/contact", context);


        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mailMessage = new MimeMessageHelper(mimeMessage);
        try {
            mailMessage.setFrom(defaultFrom);
            mailMessage.setTo(student.getStudentEmail());
            mailMessage.setSubject("School Administration");
            mailMessage.setText(content, true); //content replaced contact.getMessage()= Replaced simple text mail

            javaMailSender.send(mimeMessage);
            return true;
        } catch (MessagingException | MailException e) {
            return false;
        }

    }

    public boolean sendConfirmationMail(Student student) {

        Context contextConf = new Context();
        contextConf.setVariable("student", student);
        final String content = templateEngine.process("email/confirm", contextConf);


        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mailMessage = new MimeMessageHelper(mimeMessage);
        try {
            mailMessage.setFrom(defaultFrom);
            mailMessage.setTo(student.getStudentEmail());
            mailMessage.setSubject("School Registration Infos");
            mailMessage.setText(content, true);

            javaMailSender.send(mimeMessage);
            return true;
        } catch (MessagingException | MailException e) {
            return false;
        }

    }


}
