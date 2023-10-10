package project_final.service;

public interface IMailService {
    void sendMail(String to,String subject, String text);
    void sendSimpleMessage(String to, String subject, String text);
}
