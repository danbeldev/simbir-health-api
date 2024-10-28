package ru.simbir.health.timetableservice.features.user.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import ru.simbir.health.timetableservice.common.security.user.models.UserRole;
import ru.simbir.health.timetableservice.features.user.client.models.UserModel;

import java.util.Collection;

@FeignClient(name = "user-service", url = "${feign.clients.account.url}")
public interface UserServiceClient {

    @GetMapping("/Authentication/Validate")
    UserModel validationAccessToken(@RequestParam String accessToken);

    @GetMapping("/Accounts/{id}/Is-Exists")
    boolean exists(@PathVariable Long id, @RequestParam Collection<UserRole> roles, @RequestParam boolean requireAll, @RequestHeader("Authorization") String accessToken);
}
