package com.nri.ems.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {
    @Autowired
    private JavaMailSender mailSender;

    // @Autowired
    // private MailStructure mailStructure;

    @Value("$(HR NEXUS)")
    private String fromMail;

    @Async
    public void sendMail(String mail, String name, String username, String password) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage(); // Fully qualified class name

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        String htmlContent = "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>Welcome to HR Nexus</title>" +
                "    <style>" +
                "        body {" +
                "            font-family: Arial, sans-serif;" +
                "            line-height: 1.6;" +
                "            margin: 0;" +
                "            padding: 0;" +
                "            background-color: #f5f5f5;" +
                "            color: #333;" +
                "        }" +
                "        .container {" +
                "            max-width: 600px;" +
                "            margin: 20px auto;" +
                "            padding: 20px;" +
                "            background-color: #fff;" +
                "            border-radius: 5px;" +
                "            box-shadow: 0 0 10px rgba(0,0,0,0.1);" +
                "        }" +
                "        h1 {" +
                "            color: #9b3855;" +
                "            margin-top: 0;" +
                "        }" +
                "        p {" +
                "            margin: 10px 0;" +
                "        }" +
                "        .footer {" +
                "            margin-top: 20px;" +
                "            font-size: 14px;" +
                "            color: #777;" +
                "        }" +
                "        .button {" +
                "            display: inline-block;" +
                "            padding: 10px 20px;" +
                "            background-color: #9b3855;" +
                "            font-weight: 600;" +
                "            text-decoration: none;" +
                "            border-radius: 5px;" +
                "            transition: background-color 0.3s;" +
                "            color: #FFFFFF;" +
                "            border: none;" +
                "            cursor: pointer;" +
                "        }" +
                "        .button a {" +
                "            color: #FFFFFF;" + // Change button text color to white
                "            text-decoration: none;" + // Remove underline
                "        }" +
                "        .button:hover {" +
                "            background-color: #9b3855;" +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"container\">" +
                "        <h1>Welcome to HR Nexus</h1>" +
                "        <p>Dear " + name + ",</p>" +
                "        <p>Welcome to HR Nexus! We're thrilled to have you on board.</p>" +
                "        <p>Your account has been successfully created, and here are your login credentials:</p>" +
                "        <ul>" +
                "            <li><strong>Username:</strong> " + username + "</li>" +
                "            <li><strong>Password:</strong> " + password + "</li>" +
                "        </ul>" +
                "        <p>Please keep these credentials secure. You can use them to access your profile on HR Nexus</p>"
                +
                "        <p>Once again, welcome aboard!</p>" +
                "        <button class=\"button\"><a href=\"http://localhost:5555/login\">Take me to HR Nexus</a></button>"
                +
                "    </div>" +
                "</body>" +
                "</html>";

        mimeMessageHelper.setTo(mail);
        mimeMessageHelper.setFrom(fromMail);
        mimeMessageHelper.setSubject("Account Created");
        mimeMessageHelper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
    }

    @Async
    public void sendApproveMail(String mail, String name) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage(); // Fully qualified class name

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        String htmlContent = "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>Profile Update Notification</title>" +
                "    <style>" +
                "        body {" +
                "            font-family: Arial, sans-serif;" +
                "            line-height: 1.6;" +
                "            margin: 0;" +
                "            padding: 0;" +
                "            background-color: #f5f5f5;" +
                "            color: #333;" +
                "        }" +
                "        .container {" +
                "            max-width: 600px;" +
                "            margin: 20px auto;" +
                "            padding: 20px;" +
                "            background-color: #fff;" +
                "            border-radius: 5px;" +
                "            box-shadow: 0 0 10px rgba(0,0,0,0.1);" +
                "        }" +
                "        h1 {" +
                "            color: #9b3855;" +
                "            margin-top: 0;" +
                "        }" +
                "        p {" +
                "            margin: 10px 0;" +
                "        }" +
                "        .button {" +
                "            display: inline-block;" +
                "            padding: 10px 20px;" +
                "            background-color: #9b3855;" +
                "            font-weight: 600;" +
                "            text-decoration: none;" +
                "            border-radius: 5px;" +
                "            transition: background-color 0.3s;" +
                "            color: #FFFFFF;" +
                "            border: none;" +
                "            cursor: pointer;" +
                "        }" +
                "        .button a {" +
                "            color: #FFFFFF;" +
                "            text-decoration: none;" +
                "        }" +
                "        .button:hover {" +
                "            background-color: #9b3855;" +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"container\">" +
                "        <h1>Profile Update Notification</h1>" +
                "        <p>Dear " + name + ",</p>" +
                "        <p>We are pleased to inform you that your profile update request has been accepted.</p>" +
                "        <p>You can now view the updated information in your profile.</p>" +
                "        <p>Thank you.</p>" +
                "        <button class=\"button\"><a href=\"http://localhost:5555/login\">Login to HR Nexus</a></button>"
                +
                "    </div>" +
                "</body>" +
                "</html>";

        mimeMessageHelper.setTo(mail);
        mimeMessageHelper.setFrom(fromMail);
        mimeMessageHelper.setSubject("PROFILE UPDATE NOTIFICATION");
        mimeMessageHelper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
    }

    @Async
    public void sendRejectMail(String mail, String name) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage(); // Fully qualified class name

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        String htmlContent = "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>Profile Update Notification</title>" +
                "    <style>" +
                "        body {" +
                "            font-family: Arial, sans-serif;" +
                "            line-height: 1.6;" +
                "            margin: 0;" +
                "            padding: 0;" +
                "            background-color: #f5f5f5;" +
                "            color: #333;" +
                "        }" +
                "        .container {" +
                "            max-width: 600px;" +
                "            margin: 20px auto;" +
                "            padding: 20px;" +
                "            background-color: #fff;" +
                "            border-radius: 5px;" +
                "            box-shadow: 0 0 10px rgba(0,0,0,0.1);" +
                "        }" +
                "        h1 {" +
                "            color: #9b3855;" +
                "            margin-top: 0;" +
                "        }" +
                "        p {" +
                "            margin: 10px 0;" +
                "        }" +
                "        .button {" +
                "            display: inline-block;" +
                "            padding: 10px 20px;" +
                "            background-color: #9b3855;" +
                "            font-weight: 600;" +
                "            text-decoration: none;" +
                "            border-radius: 5px;" +
                "            transition: background-color 0.3s;" +
                "            color: #FFFFFF;" +
                "            border: none;" +
                "            cursor: pointer;" +
                "        }" +
                "        .button a {" +
                "            color: #FFFFFF;" +
                "            text-decoration: none;" +
                "        }" +
                "        .button:hover {" +
                "            background-color: #9b3855;" +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"container\">" +
                "        <h1>Profile Update Notification</h1>" +
                "        <p>Dear " + name + ",</p>" +
                "        <p>We regret to inform you that your profile update request has been rejected.</p>" +
                "        <p>Please review the reason for rejection and resubmit your request if necessary.</p>" +
                "        <p>Thank you.</p>" +
                "        <button class=\"button\"><a href=\"http://localhost:5555/login\">Login to HR Nexus</a></button>"
                +
                "    </div>" +
                "</body>" +
                "</html>";

        mimeMessageHelper.setTo(mail);
        mimeMessageHelper.setFrom(fromMail);
        mimeMessageHelper.setSubject("PROFILE UPDATE NOTIFICATION");
        mimeMessageHelper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
    }
}