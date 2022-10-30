package com.github.geugen.voting.to;

import com.github.geugen.voting.HasIdAndEmail;
import com.github.geugen.voting.model.MenuItem;
import com.github.geugen.voting.util.validation.NoHtml;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;


@Value
@EqualsAndHashCode(callSuper = true)
public class VoteRestaurantTo extends NamedTo implements HasIdAndEmail {
    @Email
    @NotBlank
    @Size(max = 128)
    @NoHtml  // https://stackoverflow.com/questions/17480809
    String email;

    List<MenuItem> menuItems;

    boolean voteMark;

    public VoteRestaurantTo(Integer id, String name, String email, List<MenuItem> menuItems, boolean voteMark) {
        super(id, name);
        this.email = email;
        this.menuItems = menuItems;
        this.voteMark = voteMark;
    }

    @Override
    public String toString() {
        return "VoteRestaurantTo:" + id + '[' + email + ']';
    }
}
