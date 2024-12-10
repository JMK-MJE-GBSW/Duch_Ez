package BE.Duch_Ez.controller;

import BE.Duch_Ez.dto.group.GroupDto;
import BE.Duch_Ez.dto.group.MainGroupsDto;
import BE.Duch_Ez.entity.group.GroupEntity;
import BE.Duch_Ez.service.group.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/main")
public class MainController {

    private final GroupService groupService;

    @GetMapping
    public List<MainGroupsDto> getGroups() {

        return groupService.getAllGroups();
    }


}
