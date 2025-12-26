/*
 * package com.xmedia.social.user.controller;
 * 
 * import java.util.Map;
 * 
 * import org.springframework.http.ResponseEntity; import
 * org.springframework.web.bind.annotation.PostMapping; import
 * org.springframework.web.bind.annotation.RequestBody; import
 * org.springframework.web.bind.annotation.RequestMapping; import
 * org.springframework.web.bind.annotation.RestController;
 * 
 * import com.xmedia.social.user.dto.MessageResponse; import
 * com.xmedia.social.user.entity.PasswordResetConfirmRequest; import
 * com.xmedia.social.user.services.PasswordResetService;
 * 
 * import jakarta.validation.Valid; import lombok.RequiredArgsConstructor;
 * 
 * @RestController
 * 
 * @RequestMapping("/api/auth")
 * 
 * @RequiredArgsConstructor public class PasswordResetController {
 * 
 * private final PasswordResetService passwordResetService;
 * 
 * // 1️⃣ Request reset token
 * 
 * @PostMapping("/password/reset/request") public ResponseEntity<?>
 * requestPasswordReset(@RequestBody Map<String, String> body) { String email =
 * body.get("email"); String message =
 * passwordResetService.createPasswordResetToken(email); return
 * ResponseEntity.ok(new MessageResponse(message)); }
 * 
 * // 2️⃣ Confirm reset with token
 * 
 * @PostMapping("/password/reset/confirm") public ResponseEntity<?>
 * confirmPasswordReset(@Valid @RequestBody PasswordResetConfirmRequest request)
 * { try { String message =
 * passwordResetService.resetPassword(request.getToken(),
 * request.getNewPassword()); return ResponseEntity.ok(new
 * MessageResponse(message)); } catch (RuntimeException e) { return
 * ResponseEntity.badRequest().body(new MessageResponse(e.getMessage())); } } }
 * 
 */