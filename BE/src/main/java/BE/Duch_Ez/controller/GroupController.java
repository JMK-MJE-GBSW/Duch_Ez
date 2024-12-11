package BE.Duch_Ez.controller;

import BE.Duch_Ez.dto.group.GroupCreateRequest;
import BE.Duch_Ez.dto.group.GroupDto;
import BE.Duch_Ez.service.group.GroupService;
import BE.Duch_Ez.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/group")
public class GroupController {

    private final UserService userService;
    private final GroupService groupService;

    @PostMapping("/create")
    public ResponseEntity<?> CreateGroup(@RequestHeader("Authorization") String authorizationHeader, @Valid @RequestBody GroupCreateRequest request, BindingResult bindingResult) {
        try {


            // 유효성 검사
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body("값이 유효하지 않습니다: " + bindingResult.getFieldError().getDefaultMessage());
            }

            // ownerId를 JWT에서 추출
            Long ownerId = userService.extractUserIdFromToken(authorizationHeader);
            request.setOwnerId(ownerId);

            // 그룹 생성 로직
            groupService.createGroup(request);
            return ResponseEntity.ok("그룹 생성 완료");

        } catch (SQLIntegrityConstraintViolationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("그룹 생성 오류: " + e.getMessage());
        }
    }

    @GetMapping
    public List<GroupDto> getGroups () {
        return groupService.getAllGroups();

    }

    @GetMapping("/{id}")
    public GroupDto getGroup(@PathVariable Long id){
        return groupService.getGroup(id); //조회한 그룹 아이디가 존재하지 않을 때 처리

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGroup(@PathVariable Long id ,@RequestBody @Valid GroupCreateRequest request, BindingResult bindingResult) {
        
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("값이 유효하지 않습니다");
        }
        try {
            groupService.updateGroup(request, id);

            return ResponseEntity.ok("그룹 수정 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable Long id){
        try {
            groupService.deleteGroup(id);

            return ResponseEntity.ok("그룹 삭제 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
