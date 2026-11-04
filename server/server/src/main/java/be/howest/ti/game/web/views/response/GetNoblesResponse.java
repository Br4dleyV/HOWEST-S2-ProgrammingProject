package be.howest.ti.game.web.views.response;

import java.util.List;

import be.howest.ti.game.logic.classes.Noble;

public class GetNoblesResponse extends AbstractResponseWithHiddenStatus {
    private final List<Noble> nobles;

    public GetNoblesResponse(List<Noble> nobles) {
        super(200);
        this.nobles = nobles;
    }

    public List<Noble> getNobles() {
        return nobles;
    }
}
