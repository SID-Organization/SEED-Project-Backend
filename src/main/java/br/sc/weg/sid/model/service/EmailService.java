package br.sc.weg.sid.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String supportMail;

    public void sendEmail(String subject, String email, String content) throws MessagingException {

        MimeMessage mail = mailSender.createMimeMessage();

        MimeMessageHelper message = new MimeMessageHelper(mail);
        message.setSubject(subject);
        message.setText(content, true);
        message.setFrom(supportMail);
        message.setTo(email);

        mailSender.send(mail);
    }


    public String getContentMail(String nome, String data){
        return "<p>Ol&aacute; " +nome+ ",</p>" +
            "<p>Espero que esteja bem!</p>" +
            "<p>Gostaria de informar que uma reuni&atilde;o foi marcada para discutirmos uma pauta importante.</p>\n" +
                "<p><strong>Data: " +data+ "</strong></p>" +
            " <p>Fico no aguardo da sua presen&ccedil;a e participa&ccedil;&atilde;o nessa reuni&atilde;o. Se tiver alguma d&uacute;vida ou precisar de mais " +
                "informa&ccedil;&otilde;es, estou &agrave; disposi&ccedil;&atilde;o.</p> " +
            "<p>Atenciosamente, Kobe bryant </p>";
    }

}
