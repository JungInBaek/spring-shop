package com.example.springshop.controller;

import com.example.springshop.dto.CartDetailDto;
import com.example.springshop.dto.CartItemDto;
import com.example.springshop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/cart")
    public String cart(Principal principal, Model model) {
        String email = principal.getName();
        List<CartDetailDto> cartDetailDtoList = cartService.getCartList(email);

        model.addAttribute("cartItems", cartDetailDtoList);

        return "cart/cartList";
    }

    @PostMapping("/cart")
    public @ResponseBody ResponseEntity cart(@RequestBody @Valid CartItemDto cartItemDto,
                                             BindingResult bindingResult, Principal principal) {

        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String email = principal.getName();
        Long cartItemId;
        try {
            cartItemId = cartService.addCart(cartItemDto, email);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    @PatchMapping("/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity updateCartItem(
            @PathVariable("cartItemId") Long cartItemId, int count, Principal principal) {

        if (count <= 0) {
            return new ResponseEntity<String>("최소 1개 이상 담아주세요", HttpStatus.BAD_REQUEST);
        } else if (!cartService.validateCartItem(cartItemId, principal.getName())) {
            return new ResponseEntity<String>("수정 권한이 없습니다", HttpStatus.FORBIDDEN);
        }

        cartService.updateCartItemCount(cartItemId, count);

        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    @DeleteMapping("/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity deleteCartItem(
            @PathVariable("cartItemId") Long cartItemId, Principal principal) {

        if (!cartService.validateCartItem(cartItemId, principal.getName())) {
            return new ResponseEntity<String>("수정 권한이 없습니다", HttpStatus.FORBIDDEN);
        }

        cartService.deleteCartItem(cartItemId);

        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }
}
