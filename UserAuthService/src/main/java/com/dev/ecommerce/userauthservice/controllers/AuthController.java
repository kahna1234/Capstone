package com.dev.ecommerce.userauthservice.controllers;
import com.dev.ecommerce.userauthservice.dtos.LoginRequestDTO;
import com.dev.ecommerce.userauthservice.dtos.SignupRequestDTO;
import com.dev.ecommerce.userauthservice.dtos.UserDTO;
import com.dev.ecommerce.userauthservice.dtos.ValidateTokenDto;
import com.dev.ecommerce.userauthservice.models.User;
import com.dev.ecommerce.userauthservice.pojos.UserToken;
import com.dev.ecommerce.userauthservice.services.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    /*
    bunch of APIs
    1. /signup
        -Type: POST
        -Request type
            -name
            -email
            -password
        -Return type
            -ResponseEntity
            -body: UserDTO
                -UserDTO
                    -name, email
            -200 if registration is success
            -error code if it fails
         -why ResponseEntity<UserDTO> instead of ResponseEntity<User>?
    2. login
        -Type: POST
        -Return type
            -In response, headers
            -ResponseEntity<UserDTO>
                -headers(JWT)
            -name, email, roles
         -Request type
            -email, password

    /login GET
    -you're generating a token as a result of this call and the name
    suggests that you are only feteching some info
    -{
        "username":
        "password":
      }
      Request body / payload
     */

    @Autowired
    private IAuthService authService;

    @PostMapping("/signup")
    ResponseEntity<UserDTO> signup(@RequestBody SignupRequestDTO signupRequestDTO){
        System.out.println("Signup API called with name: " + signupRequestDTO.getName() + ", email: " + signupRequestDTO.getEmail());
        try {
            User user = authService.signup(signupRequestDTO.getName(),
                    signupRequestDTO.getEmail(),
                    signupRequestDTO.getPassword());

            UserDTO userDTO = user.convertToUserDTO();

            return new ResponseEntity<>(userDTO, HttpStatus.CREATED);

        }catch (Exception e){
            return null;
        }

    }

//    @PostMapping("/login")
//    ResponseEntity<UserDTO> login(@RequestBody LoginRequestDTO loginRequestDTO){
//        try{
//            UserToken userToken = authService.login(loginRequestDTO.getEmail(),
//                    loginRequestDTO.getPassword());
//            /*
//            How do we return the token in response?
//            We will add token into headers and we can easily set headers in ResponseEntity
//
//            MultiValueMap is used for representing headers and the key names here
//            should be key against which we will add this token?
//
//            The token i want to send back to client should in form of what in frontend??
//            ---Cookies
//             */
//
//            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
//            /*
//            cookie, token
//            cookie, session id
//            cookie, something else...
//             */
//            headers.add(HttpHeaders.COOKIE, userToken.getToken());
//
//            HttpHeaders httpHeaders = new HttpHeaders(headers);
//
//            return new ResponseEntity<>(userToken.getUser().convertToUserDTO(),
//                    httpHeaders,
//                    HttpStatus.OK);
//
//        } catch (Exception e){
//            System.out.println("Login failed with exception: " + e.getClass().getName() + " - " + e.getMessage());
//            e.printStackTrace();
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }
//
//        /*
//        Input : Token
//        Output: boolean
//        Type: POST because we will send token in request body
//
//
//         */
//
//
//
//
//    }

    @PostMapping("/login")
    ResponseEntity<UserDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            UserToken userToken = authService.login(
                    loginRequestDTO.getEmail(),
                    loginRequestDTO.getPassword()
            );

            ResponseCookie cookie = ResponseCookie.from("token", userToken.getToken())
                    .httpOnly(true)       // prevents JS access
                    .secure(false)        // true in production (HTTPS)
                    .path("/")
                    .maxAge(60 * 60)      // 1 hour
                    .sameSite("Lax")      // CSRF protection
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(userToken.getUser().convertToUserDTO());

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        ResponseCookie cookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(false) // true in production
                .path("/")
                .maxAge(0) // deletes the cookie
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Logged out successfully");
    }

    @PostMapping("/validateToken")
    public ResponseEntity<String> validateToken(@RequestBody ValidateTokenDto validateTokenDto) {
        Boolean result = authService.validateToken(validateTokenDto.getToken());

        if(result == false) {
            return new ResponseEntity<>("Please login again, Inconvenience Regretted", HttpStatus.FORBIDDEN);
            //throw new RuntimeException("Please login again, Inconvenience Regretted");
        }else{
            return new ResponseEntity<>("Token is valid", HttpStatus.OK);
        }
    }





    /*
    Write a controller advice to catch and handle exceptions.
     */
}
