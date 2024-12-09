package BE.Duch_Ez.service.group;

import BE.Duch_Ez.dto.group.GroupDto;
import BE.Duch_Ez.entity.group.GroupEntity;
import BE.Duch_Ez.entity.group.ParticipantEntity;
import BE.Duch_Ez.entity.user.UserEntity;
import BE.Duch_Ez.repository.group.GroupRepository;
import BE.Duch_Ez.repository.group.ParticipantRepository;
import BE.Duch_Ez.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GroupService {

    private final GroupRepository groupRepository;

    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;

    public void CreateGroup(GroupDto groupsDto) {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setName(groupsDto.getName());
        UserEntity owner = userRepository.findById(groupsDto.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid owner ID"));
        groupEntity.setOwner(owner); // 올바르게 설정

        // 참가자 생성 및 그룹 설정
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

}
