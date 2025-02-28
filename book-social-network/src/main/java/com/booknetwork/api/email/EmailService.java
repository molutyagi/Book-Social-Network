package com.booknetwork.api.email;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;

import java.util.HashMap;
import java.util.Map;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine springTemplateEngine;

    @Async
    public void sendEmail(String to, String subject, String username, EmailTemplateName emailTemplate,
            String confirmationUrl, String activationCode) throws MessagingException {

        String templateName;
        if (emailTemplate == null) {
            templateName = "confirm_email";
        } else {
            templateName = emailTemplate.name();
        }

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper mimeHelper = new MimeMessageHelper(mimeMessage, MULTIPART_MODE_MIXED,
                UTF_8.name());

        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("username", username);
        properties.put("confirmationUrl", confirmationUrl);
        properties.put("activationCode", activationCode);

        Context context = new Context();
        context.setVariables(properties);

        mimeHelper.setFrom("molutyagi2301@gmail.com");
        mimeHelper.setTo(to.toString());
        mimeHelper.setSubject(subject);

        String template = springTemplateEngine.process(templateName, context);
        mimeHelper.setText(template, true);
        mimeMessage.setHeader("Content-Type", "text/html; charset=UTF-8");

        // Send email
        mailSender.send(mimeMessage);

    }
}
