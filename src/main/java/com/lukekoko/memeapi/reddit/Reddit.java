package com.lukekoko.memeapi.reddit;

import com.lukekoko.memeapi.util.AccessToken;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class Reddit {
    private AccessToken accessToken;
    private String clientId;
    private String clientSecret;
    private String userAgent;
}
