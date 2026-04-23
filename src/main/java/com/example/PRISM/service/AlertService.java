package com.example.PRISM.service;
import com.example.PRISM.entity.AlertEntity;
import com.example.PRISM.entity.MachineEntity;
import com.example.PRISM.repository.AlertRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.type.PhoneNumber;
import com.twilio.type.Twiml;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
class AlertService {

    @Value("${twilio.account.sid}") private String twilioSid;
    @Value("${twilio.auth.token}") private String twilioToken;
    @Value("${twilio.phone.number}") private String twilioFrom;
    @Value("${emergency.contact.number}") private String emergencyContact;

    private final JavaMailSender mailSender;
    private final AlertRepository alertRepo;

    public void triggerEmergency(MachineEntity machine, String details) {

        alertRepo.save(new AlertEntity(null, "SABOTAGE: " + machine.getName(), "CRITICAL", LocalDateTime.now()));

//        try {
//            SimpleMailMessage mail = new SimpleMailMessage();
//            mail.setTo("vinithselvaraj123@gmail.com");
//            mail.setSubject(" CRITICAL ALERT: " + machine.getName());
//            mail.setText("Emergency declared. " + details);
//            mailSender.send(mail);
//            log.info("Emergency Email Sent!");
//        } catch (Exception e) {
//            log.error("Email failed: {}", e.getMessage());
//        }

//        try {
//            Twilio.init(twilioSid, twilioToken);
//            Call.creator(
//                    new PhoneNumber(emergencyContact),
//                    new PhoneNumber(twilioFrom),
//                    new Twiml("<Response><Say>Emergency alert. Sabotage detected at " + machine.getName() + ". Please evacuate.</Say></Response>")
//            ).create();
//
//            System.out.println("Emergency Call Initiated!");
//        } catch (Exception e) {
//            System.err.println("Twilio failed: " + e.getMessage());
//        }
        log.info("Alert Activated");
    }
}
