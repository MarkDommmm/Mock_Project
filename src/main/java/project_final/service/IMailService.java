package project_final.model.service.impl.mail;

public interface IMailService {
    void sendMail(String to,String subject, String text);
    void sendSimpleMessage(String to, String subject, String text);
}
