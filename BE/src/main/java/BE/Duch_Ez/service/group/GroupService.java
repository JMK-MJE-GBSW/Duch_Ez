package BE.Duch_Ez.service.group;

import BE.Duch_Ez.dto.group.GroupCreateRequest;
import BE.Duch_Ez.dto.group.GroupDto;
import BE.Duch_Ez.dto.group.ParticipantDto;
import BE.Duch_Ez.entity.group.DuchPayEntity;
import BE.Duch_Ez.entity.group.DuchPayParticipantEntity;
import BE.Duch_Ez.entity.group.GroupEntity;
import BE.Duch_Ez.entity.group.ParticipantEntity;
import BE.Duch_Ez.entity.user.UserEntity;
import BE.Duch_Ez.repository.group.GroupRepository;
import BE.Duch_Ez.repository.group.ParticipantRepository;
import BE.Duch_Ez.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;

    // 그룹 생성
    public void createGroup(GroupCreateRequest request) throws SQLIntegrityConstraintViolationException {
        if (groupRepository.existsByname(request.getName())) {
            throw new SQLIntegrityConstraintViolationException("그룹 이름이 중복됩니다.");
        }

        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setName(request.getName());
        UserEntity owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid owner ID"));
        groupEntity.setOwner(owner);

        // 참가자 리스트를 엔티티로 변환
        List<ParticipantEntity> participants = request.getParticipants().stream()
                .map(name -> {
                    ParticipantEntity participant = new ParticipantEntity();
                    participant.setName(name);
                    participant.setGroup(groupEntity); // 그룹 설정
                    return participant;
                })
                .collect(Collectors.toList());

        groupEntity.setParticipants(participants);

        // 그룹 저장 (Cascade 옵션에 따라 참가자도 저장됨)
        groupRepository.save(groupEntity);
    }

    // 그룹 리스트 조회
    public List<GroupDto> getAllGroups() {
        return groupRepository.findAll().stream()
                .map(group -> {
                    GroupDto groupDto = new GroupDto();
                    groupDto.setName(group.getName());
                    groupDto.setOwnerId(group.getOwner().getId());
                    groupDto.setName(group.getName());
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

    public GroupDto getGroup(String GroupName) {
        GroupEntity group = groupRepository.findByName(GroupName)
                .orElseThrow(() -> new IllegalArgumentException("그룹이 존재하지 않습니다."));

        GroupDto groupDto = new GroupDto();
        groupDto.setName(group.getName());
        groupDto.setId(group.getId());

        List<ParticipantDto> participantDtos = group.getParticipants().stream()
                .map(participant -> {
                    ParticipantDto participantDto = new ParticipantDto();
                    participantDto.setId(participant.getId());
                    participantDto.setName(participant.getName());
                    participantDto.setGroupId(participant.getId());
                    participantDto.setGroupName(group.getName()); // 그룹 ID 설정

                    return participantDto;
                })
                .collect(Collectors.toList());

        groupDto.setParticipants(participantDtos);
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



    ///


}
