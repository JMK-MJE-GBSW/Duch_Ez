package BE.Duch_Ez.service.group;

import BE.Duch_Ez.dto.group.GroupCreateRequest;
import BE.Duch_Ez.dto.group.GroupDto;
import BE.Duch_Ez.dto.group.ParticipantDto;
import BE.Duch_Ez.entity.group.DuchPayEntity;
import BE.Duch_Ez.entity.group.GroupEntity;
import BE.Duch_Ez.entity.group.ParticipantEntity;
import BE.Duch_Ez.entity.user.UserEntity;
import BE.Duch_Ez.repository.group.GroupRepository;
import BE.Duch_Ez.repository.group.ParticipantRepository;
import BE.Duch_Ez.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;

    // 그룹 생성
    public void createGroup(GroupCreateRequest request) throws SQLIntegrityConstraintViolationException {
        // 그룹 이름 중복 체크
        if (groupRepository.existsByname(request.getName())) {
            throw new SQLIntegrityConstraintViolationException("그룹 이름이 중복됩니다.");
        }

        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setName(request.getName());
        UserEntity owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid owner ID"));
        groupEntity.setOwner(owner);

        // 기존 참가자 중복 확인 (같은 그룹 내에서만 중복 체크)
        List<ParticipantEntity> participants = new ArrayList<>();

        for (String name : request.getParticipants()) {
            // 해당 그룹 내에 이미 같은 이름의 참가자가 있는지 확인
            boolean isDuplicate = groupEntity.getParticipants().stream()
                    .anyMatch(existingParticipant -> existingParticipant.getName().equals(name));

            if (isDuplicate) {
                throw new IllegalArgumentException("그룹 내에 중복된 참가자가 존재합니다: " + name);
            }

            // 참가자 추가
            ParticipantEntity participant = new ParticipantEntity();
            participant.setName(name);
            participant.setGroup(groupEntity);  // 그룹 설정
            participants.add(participant);
        }

        groupEntity.setParticipants(participants);

        // 그룹 저장 (Cascade 옵션에 따라 참가자도 저장됨)
        groupRepository.save(groupEntity);
    }


    public List<GroupDto> getGroupsByUser(UserEntity user) {
        // 사용자 ID를 기반으로 그룹 조회
        return groupRepository.findByOwner(user).stream()
                .map(group -> {
                    GroupDto groupDto = new GroupDto();
                    groupDto.setName(group.getName());
                    groupDto.setOwnerId(group.getOwner().getId());
                    groupDto.setId(group.getId());

                    // 참가자 리스트를 DTO로 변환
                    List<ParticipantDto> participantDtos = group.getParticipants().stream()
                            .map(participant -> {
                                ParticipantDto dto = new ParticipantDto();
                                dto.setId(participant.getId());
                                dto.setName(participant.getName());
                                dto.setGroupId(group.getId());
                                dto.setGroupName(group.getName());
                                return dto;
                            })
                            .collect(Collectors.toList());

                    groupDto.setParticipants(participantDtos);
                    return groupDto;
                })
                .collect(Collectors.toList());
    }


    public GroupDto getGroup(String groupName) {
        GroupEntity group = groupRepository.findByName(groupName)
                .orElseThrow(() -> new IllegalArgumentException("그룹이 존재하지 않습니다."));

        GroupDto groupDto = new GroupDto();
        groupDto.setId(group.getId());
        groupDto.setName(group.getName());

        // 참가자 리스트를 DTO로 변환
        List<ParticipantDto> participantDtos = group.getParticipants().stream()
                .map(participant -> {
                    ParticipantDto participantDto = new ParticipantDto();
                    participantDto.setId(participant.getId());
                    participantDto.setName(participant.getName());
                    participantDto.setGroupId(participant.getId());
                    participantDto.setGroupName(group.getName());
                    return participantDto;
                })
                .collect(Collectors.toList());
        groupDto.setParticipants(participantDtos);

        // 그룹의 총 지출 계산
        int totalSpent = group.getDuchPays().stream()
                .mapToInt(DuchPayEntity::getTotalAmount)
                .sum();
        groupDto.setTotalSpent(totalSpent);

        return groupDto;
    }


    public void updateGroup(GroupCreateRequest request, String groupName) throws SQLIntegrityConstraintViolationException {
        GroupEntity group = groupRepository.findByName(groupName)
                .orElseThrow(() -> new IllegalArgumentException("그룹이 존재하지 않습니다."));

        if (groupRepository.existsByname(request.getName())) {
            throw new SQLIntegrityConstraintViolationException("그룹 이름이 중복됩니다.");
        }

        if (group.getName().equals(request.getName())) {
            throw new SQLIntegrityConstraintViolationException("기존 그룹 이름과 동일합니다.");
        }

        group.setName(request.getName());
        groupRepository.save(group);
    }

    public void deleteGroup(String groupName) {
        GroupEntity group = groupRepository.findByName(groupName)
                .orElseThrow(() -> new IllegalArgumentException("그룹이 존재하지 않습니다."));
        groupRepository.delete(group);
    }





}
