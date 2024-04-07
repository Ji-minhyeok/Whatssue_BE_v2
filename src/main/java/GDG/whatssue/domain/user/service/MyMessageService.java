package GDG.whatssue.domain.user.service;

import GDG.whatssue.domain.user.entity.PhoneCertNum;
import GDG.whatssue.domain.user.repository.PhoneCertNumRepository;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;


@Service
public class MyMessageService {
    private final DefaultMessageService defalutMessageService;
    @Autowired
    private final PhoneCertNumRepository phoneCertNumRepository;
    private String apiKey;
    private String apiSecret;
    private String fromNumber;
    public MyMessageService(PhoneCertNumRepository certificationNumRepository, @Value("${coolsms.api.key}") String apiKey, @Value("${coolsms.api.secret}") String apiSecret, @Value("${coolsms.api.number}") String fromNumber) {
        this.phoneCertNumRepository = certificationNumRepository;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.fromNumber = fromNumber;
        this.defalutMessageService = NurigoApp.INSTANCE.initialize(apiKey,apiSecret,"https://api.coolsms.co.kr");
        System.out.println("messageService : " + defalutMessageService);
    }
    /*
      단일 메시지 발송 예제
     */
    public SingleMessageSentResponse sendOne(String toNumber, Long userId) {
        Message message = new Message();
        PhoneCertNum phoneCertNum = new PhoneCertNum();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        int randomNum = randomNumber();
        message.setFrom(fromNumber);
        message.setTo(toNumber);
        message.setText("인증번호:" + randomNum + "입니다. 정확히 입력해주세요. 그리고 방구를 드십시오");

        phoneCertNum = PhoneCertNum.builder()
                .Id(toNumber +":"+ userId)
                .certificationNum(randomNum)
                .createdAt(java.time.LocalDateTime.now())
                .build();

        System.out.println("PhoneCertNum 확인:" + phoneCertNum.getId());
        phoneCertNumRepository.save(phoneCertNum);
        if (phoneCertNumRepository.findById(phoneCertNum.getId()).isEmpty())
            throw new RuntimeException("인증 번호 저장에 실패했습니다.");
            SingleMessageSentResponse response = this.defalutMessageService.sendOne(new SingleMessageSendingRequest(message));
            System.out.println(response);
            return response;
        }
        public String checkCertNum(String toNumber, int certNum,Long userId) {
            PhoneCertNum phoneCertNum = phoneCertNumRepository.findById(toNumber + ":" + userId).orElseThrow(() -> new RuntimeException("인증번호가 존재하지 않습니다."));
            if (phoneCertNum.getCertificationNum() != certNum) {
                throw new RuntimeException("인증번호가 일치하지 않습니다.");
            }
            return "인증번호가 일치합니다.";
        }
        public int randomNumber() {
            Random random = new Random();
            int randomNum = random.nextInt(899) + 100;
            return randomNum;
        }
}

