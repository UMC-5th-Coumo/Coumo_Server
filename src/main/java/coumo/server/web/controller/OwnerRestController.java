package coumo.server.web.controller;

import coumo.server.apiPayload.ApiResponse;
import coumo.server.converter.OwnerConverter;
import coumo.server.domain.Owner;
import coumo.server.jwt.JWTUtil;
import coumo.server.service.owner.OwnerService;
import coumo.server.web.dto.OwnerRequestDTO;
import coumo.server.web.dto.OwnerResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/owner")
public class OwnerRestController {

    private final OwnerService ownerService;
    private final JWTUtil jwtUtil;

    @PostMapping("/join")
    @Operation(summary = "WEB 회원가입 API",
            description = "WEB 회원가입 API 구현")
    public ApiResponse<OwnerResponseDTO.JoinResultDTO> join(@RequestBody @Valid OwnerRequestDTO.JoinDTO request){
        Owner owner = ownerService.joinOwner(request);
        return ApiResponse.onSuccess(OwnerConverter.toJoinResultDTO(owner));
    }

    @PostMapping("/login")
    @Operation(summary = "WEB 로그인 API",
            description = "WEB 로그인 API 구현")
    public ApiResponse<OwnerResponseDTO.LoginResultDTO> login(@RequestBody @Valid OwnerRequestDTO.LoginDTO request){
        Owner owner = ownerService.loginOwner(request);

        if (owner != null) {
            // 로그인 성공 시 토큰 생성
            String token = jwtUtil.createJwt(request.getLoginId(), 3600000L); //토큰 만료 시간 1시간
            
            // LoginResultDTO를 생성하여 반환
            return ApiResponse.onSuccess(OwnerConverter.toLoginResultDTO(owner, token));
        } else {
            return ApiResponse.onFailure("400", "로그인에 실패했습니다.", null);
        }

    }

    @PostMapping("/join/check-login-id")
    @Operation(summary = "WEB 로그인 ID 중복 확인 API",
            description = "WEB 로그인 ID 중복 확인 API 구현")
    public ApiResponse<OwnerResponseDTO.CheckLoginIdResponseDTO> checkLoginId(@RequestBody @Valid OwnerRequestDTO.CheckLoginIdDTO request){
        boolean isLoginIdAvailable = ownerService.isLoginIdAvailable(request.getLoginId());

        if(isLoginIdAvailable){
            // 로그인 ID가 사용 가능할 경우
            return ApiResponse.onSuccess(OwnerResponseDTO.CheckLoginIdResponseDTO.builder()
                    .loginId(request.getLoginId())
                    .build());
        } else {
            // 로그인 ID가 이미 사용 중인 경우
            return ApiResponse.onFailure("400", "이미 사용 중인 로그인 ID입니다.", null);
        }
    }

    @GetMapping("/mypage/{ownerId}/profile")
    @Operation(summary = "마이페이지 내 프로필 조회 API", description = "로그인한 사장님의 마이페이지 프로필을 조회합니다.")
    @Parameters({
            @Parameter(name = "ownerId", description = "ownerId, path variable 입니다!"),
    })
    public ApiResponse<OwnerResponseDTO.MyPageDTO> myPage(@PathVariable Long ownerId){
        return ownerService.findOwner(ownerId)
                .map(owner -> ApiResponse.onSuccess(OwnerConverter.toMyPageDTO(owner)))
                .orElse(ApiResponse.onFailure("400", "사용자 정보를 찾을 수 없습니다.", null));
    }
}
