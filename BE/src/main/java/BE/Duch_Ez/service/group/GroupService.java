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

    public void createGroup(GroupCreateRequest request) throws SQLIntegrityConstraintViolationException {
        UserEntity owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid owner ID"));

        // 특정 사용자의 그룹 내에서 이름 중복 검사
        if (groupRepository.existsByNameAndOwner(request.getName(), owner)) {
            throw new SQLIntegrityConstraintViolationException("사용자의 그룹 목록에 중복된 이름이 있습니다.");
        }

        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setName(request.getName());
        groupEntity.setOwner(owner);

        List<ParticipantEntity> participants = new ArrayList<>();
        for (String name : request.getParticipants()) {
            boolean isDuplicate = participants.stream()
                    .anyMatch(participant -> participant.getName().equals(name));
            if (isDuplicate) {
                throw new IllegalArgumentException("그룹 내 중복된 참가자가 있습니다: " + name);
            }

            ParticipantEntity participant = new ParticipantEntity();
            participant.setName(name);
            participant.setGroup(groupEntity);
            participants.add(participant);
        }

        groupEntity.setParticipants(participants);

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


    public void updateGroupName(String currentName, String newName) {
        // 기존 그룹 찾기
        GroupEntity group = groupRepository.findByName(currentName)
                .orElseThrow(() -> new IllegalArgumentException("해당 이름의 그룹이 존재하지 않습니다."));

        // 이름 업데이트
        group.setName(newName);
        groupRepository.save(group); // 변경 내용 저장
    }

    public void deleteGroup(String groupName) {
        // 그룹 조회 및 삭제
        GroupEntity group = groupRepository.findByName(groupName)
                .orElseThrow(() -> new IllegalArgumentException("해당 이름의 그룹이 존재하지 않습니다."));

        groupRepository.delete(group); // 삭제
    }






}
