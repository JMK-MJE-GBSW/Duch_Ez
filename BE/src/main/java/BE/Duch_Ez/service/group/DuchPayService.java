package BE.Duch_Ez.service.group;

import BE.Duch_Ez.dto.group.DuchPayRequest;
import BE.Duch_Ez.dto.group.DuchPayResponse;
import BE.Duch_Ez.dto.group.GroupDto;
import BE.Duch_Ez.dto.group.ParticipantDto;
import BE.Duch_Ez.entity.group.*;
import BE.Duch_Ez.repository.group.DuchPayRepository;
import BE.Duch_Ez.repository.group.GroupRepository;
import BE.Duch_Ez.repository.group.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DuchPayService {

    private final GroupRepository groupRepository;
    private final ParticipantRepository participantRepository;
    private final DuchPayRepository duchPayRepository;

    public DuchPayResponse createDuchPay(String groupName, DuchPayRequest request) {
        // 그룹과 돈 낸 사람 조회
        GroupEntity group = groupRepository.findByName(groupName).orElseThrow(() -> new IllegalArgumentException("그룹을 찾을 수 없습니다."));
        ParticipantEntity payer = participantRepository.findById(request.getPayerId()).orElseThrow(() -> new IllegalArgumentException("돈 낸 사람을 찾을 수 없습니다."));

        // 참여자 목록 조회
        List<ParticipantEntity> participants = participantRepository.findAllById(request.getParticipantIds());
        if (participants.isEmpty()) {
            throw new IllegalArgumentException("참여자 목록이 비어있습니다.");
        }

        // 금액 나누기
        int splitAmount = request.getTotalAmount() / participants.size();

        // DuchPayEntity 생성
        DuchPayEntity duchPay = new DuchPayEntity();
        duchPay.setTitle(request.getTitle());
        duchPay.setTotalAmount(request.getTotalAmount());
        duchPay.setPayer(payer);
        duchPay.setGroup(group);

        // 참여자에 대한 부채 정보 저장
        List<DuchPayParticipantEntity> debts = participants.stream().map(participant -> {
            DuchPayParticipantEntity debt = new DuchPayParticipantEntity();
            debt.setDuchPay(duchPay);
            debt.setParticipant(participant);
            debt.setAmountOwed(splitAmount);
            return debt;
        }).collect(Collectors.toList());

        duchPay.setParticipants(debts);
        duchPayRepository.save(duchPay);

        // 응답 생성
        DuchPayResponse response = new DuchPayResponse();
        response.setId(duchPay.getId());
        response.setTitle(duchPay.getTitle());
        response.setTotalAmount(duchPay.getTotalAmount());
        response.setPayerId(payer.getId());
        response.setParticipantDebts(debts.stream().map(debt -> {
            DuchPayResponse.ParticipantDebt participantDebt = new DuchPayResponse.ParticipantDebt();
            participantDebt.setParticipantId(debt.getParticipant().getId());
            participantDebt.setParticipantName(debt.getParticipant().getName());
            participantDebt.setAmountOwed(debt.getAmountOwed());
            return participantDebt;
        }).collect(Collectors.toList()));

        return response;
    }


    public Map<String, Map<String, Integer>> calculateDebts(String groupName) {
        GroupEntity group = groupRepository.findByName(groupName)
                .orElseThrow(() -> new IllegalArgumentException("그룹이 존재하지 않습니다."));

        Map<String, Map<String, Integer>> debts = new HashMap<>();
        for (DuchPayEntity duchPay : group.getDuchPays()) {
            String payerName = duchPay.getPayer().getName();
            int perPersonShare = duchPay.getTotalAmount() / duchPay.getParticipants().size();

            for (DuchPayParticipantEntity participant : duchPay.getParticipants()) {
                String participantName = participant.getParticipant().getName();

                if (!participantName.equals(payerName)) {
                    debts.computeIfAbsent(participantName, k -> new HashMap<>())
                            .merge(payerName, perPersonShare, Integer::sum);
                }

            }
        }
        return debts;
    }



    public List<DuchPayResponse> getAllDuchPay(String groupName) {
        return duchPayRepository.findByGroupName(groupName).stream()
                .map(duchPay -> {
                    DuchPayResponse duchPayResponse = new DuchPayResponse();
                    duchPayResponse.setId(duchPay.getId());
                    duchPayResponse.setTitle(duchPay.getTitle());
                    duchPayResponse.setTotalAmount(duchPay.getTotalAmount());
                    duchPayResponse.setPayerId(duchPay.getPayer().getId());
                    // 참가자 리스트를 DTO로 변환
                    List<DuchPayResponse.ParticipantDebt> participantDebts = duchPay.getParticipants().stream()
                            .map(ParticipantDebts -> {
                                DuchPayResponse.ParticipantDebt Debt = new DuchPayResponse.ParticipantDebt();
                                Debt.setParticipantId(ParticipantDebts.getParticipant().getId());
                                Debt.setParticipantName(ParticipantDebts.getParticipant().getName());
                                Debt.setAmountOwed(ParticipantDebts.getAmountOwed());
                                return Debt;
                            })
                            .collect(Collectors.toList());

                    duchPayResponse.setParticipantDebts(participantDebts);
                    return duchPayResponse;
                })
                .collect(Collectors.toList());
    }
}
