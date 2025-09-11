package gr.aueb.cf.myreserva.config;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Component
public class AdminWhiteList {

    @Value("#{'${admin.whitelist}'.split(',')}")
    private List<String> whitelist;

    public boolean isAdmin(String email) {
        return whitelist.contains(email);
    }
}
