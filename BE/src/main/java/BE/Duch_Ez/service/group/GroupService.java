package BE.Duch_Ez.service.group;

import BE.Duch_Ez.dto.group.GroupDto;
import BE.Duch_Ez.dto.group.MainGroupsDto;
import BE.Duch_Ez.entity.group.GroupEntity;
import BE.Duch_Ez.entity.group.ParticipantEntity;
import BE.Duch_Ez.entity.user.UserEntity;
import BE.Duch_Ez.repository.group.GroupRepository;
import BE.Duch_Ez.repository.group.ParticipantRepository;
import BE.Duch_Ez.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GroupService {

    private final GroupRepository groupRepository;

    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;

    public void CreateGroup(GroupDto groupsDto) throws SQLIntegrityConstraintViolationException {
        if (groupRepository.existsByname(groupsDto.getName())) {
            throw new SQLIntegrityConstraintViolationException("그룹 이름이 중복됩니다.");
        }
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setName(groupsDto.getName());
        UserEntity owner = userRepository.findById(groupsDto.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid owner ID"));
        groupEntity.setOwner(owner); // 올바르게 설정

        List<ParticipantEntity> participants = groupsDto.getParticipants().stream()
                .map(name -> {
                    ParticipantEntity participantEntity = new ParticipantEntity();
                    participantEntity.setName(name);
                    participantEntity.setGroup(groupEntity); // 여기에서 그룹 설정
                    return participantEntity;
                }).collect(Collectors.toList());

        groupEntity.setParticipants(participants);

        // 그룹과 참가자 저장
        groupRepository.save(groupEntity); // cascade 설정으로 participants도 저장됨
    }

    public List<MainGroupsDto> getAllGroups() {
        List<GroupEntity> groups = groupRepository.findAll();

        return groups.stream()
                .map(groupEntity -> {
                    MainGroupsDto mainGroupsDto = new MainGroupsDto();
                    mainGroupsDto.setName(groupEntity.getName());
                    mainGroupsDto.setParticipants(
                            groupEntity.getParticipants().stream()
                                    .map(ParticipantEntity::getName)
                                    .collect(Collectors.toList())
                    );
                    return mainGroupsDto;
                })
                .collect(Collectors.toList());

    }

}