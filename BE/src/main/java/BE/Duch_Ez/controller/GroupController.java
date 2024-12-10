package BE.Duch_Ez.controller;

import BE.Duch_Ez.dto.group.GroupDto;
import BE.Duch_Ez.service.group.GroupService;
import BE.Duch_Ez.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/group")
public class GroupController {

    private final UserService userService;
    private final GroupService groupService;

    @PostMapping("/create")
    public ResponseEntity<?> CreateGroup(@RequestHeader("Authorization") String authorizationHeader, @Valid @RequestBody GroupDto groupsDto, BindingResult bindingResult) {
        try {


            // 유효성 검사
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body("값이 유효하지 않습니다: " + bindingResult.getFieldError().getDefaultMessage());
            }

            // ownerId를 JWT에서 추출
            Long ownerId = userService.extractUserIdFromToken(authorizationHeader);
            groupsDto.setOwnerId(ownerId);

            // 그룹 생성 로직
            groupService.CreateGroup(groupsDto);
            return ResponseEntity.ok("그룹 생성 완료");

        } catch (SQLIntegrityConstraintViolationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("그룹 생성 오류: " + e.getMessage());
        }
    }

}
