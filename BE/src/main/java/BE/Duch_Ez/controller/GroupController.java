package BE.Duch_Ez.controller;


import BE.Duch_Ez.dto.group.DuchPayRequest;
import BE.Duch_Ez.dto.group.DuchPayResponse;
import BE.Duch_Ez.dto.group.GroupCreateRequest;
import BE.Duch_Ez.dto.group.GroupDto;

import BE.Duch_Ez.entity.user.UserEntity;
import BE.Duch_Ez.service.group.DuchPayService;
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
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/group")
public class GroupController {

    private final UserService userService;
    private final GroupService groupService;
    private final DuchPayService duchPayService;

    @PostMapping("/create")
    public ResponseEntity<?> CreateGroup(@RequestHeader("Authorization") String authorizationHeader,
                                         @Valid @RequestBody GroupCreateRequest request,
                                         BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(Map.of("error", "값이 유효하지 않습니다: " + bindingResult.getFieldError().getDefaultMessage()));
            }

            // ownerId를 JWT에서 추출
            Long ownerId = userService.extractUserIdFromToken(authorizationHeader);
            request.setOwnerId(ownerId);

            // 그룹 생성 로직
            groupService.createGroup(request);

            // JSON 형식으로 성공 응답 반환
            return ResponseEntity.ok(Map.of("message", "그룹 생성 완료"));

        } catch (SQLIntegrityConstraintViolationException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "그룹 생성 오류: " + e.getMessage()));
        }
    }

    @GetMapping
    public List<GroupDto> getGroups(@RequestHeader("Authorization") String token) {
        // 토큰에서 사용자 정보 추출 (서비스에서 구현 필요)
        UserEntity currentUser = userService.getUserFromToken(token);

        // 사용자의 그룹만 조회
        return groupService.getGroupsByUser(currentUser);
    }


    @GetMapping("/{groupName}")
    public GroupDto getGroup(@PathVariable String groupName) {
        return groupService.getGroup(groupName); // UUID로 조회
    }

    // 그룹 이름 수정
        @PutMapping("/{groupName}")
        public ResponseEntity<?> updateGroupName(@PathVariable String groupName,
                                                 @RequestBody Map<String, String> updateRequest) {
            try {
                String newName = updateRequest.get("newName");
                if (newName == null || newName.isBlank()) {
                    return ResponseEntity.badRequest().body("새로운 이름을 입력해주세요.");
                }
    
                groupService.updateGroupName(groupName, newName); // 서비스 호출
                return ResponseEntity.ok(Map.of("message", "그룹 이름이 성공적으로 변경되었습니다.", "newName", newName));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
            }
        }
    
        // 그룹 삭제
        @DeleteMapping("/{groupName}")
        public ResponseEntity<?> deleteGroup(@PathVariable String groupName) {
            try {
                groupService.deleteGroup(groupName);
                return ResponseEntity.ok(Map.of("message", "그룹이 성공적으로 삭제되었습니다."));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
            }
        }

    //메인에서 더치페이 항목목록 표시기능 개발
    //
    //
    //
    @GetMapping("/{groupName}/debts")
    public ResponseEntity<?> getDebts(@PathVariable String groupName) {
        try {
            Map<String, Map<String, Integer>> debts = duchPayService.calculateDebts(groupName);
            return ResponseEntity.ok(debts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/{groupName}/duch-pay")
    public ResponseEntity<?> createDuchPay(@PathVariable String groupName, @RequestBody @Valid DuchPayRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("유효하지 않은 값: " + bindingResult.getFieldError().getDefaultMessage());
        }
        try {
            DuchPayResponse response = duchPayService.createDuchPay(groupName, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류 발생: " + e.getMessage());
        }
    }

    @GetMapping("/{groupName}/duch-pay")
    public ResponseEntity<?> getDuchPay(@PathVariable String groupName) {
        try {

            return ResponseEntity.ok(duchPayService.getAllDuchPay(groupName));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
