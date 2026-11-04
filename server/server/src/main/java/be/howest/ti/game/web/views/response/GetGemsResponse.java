package be.howest.ti.game.web.views.response;

import java.util.List;

import be.howest.ti.game.logic.enums.GemType;

public class GetGemsResponse extends AbstractResponseWithHiddenStatus{
    public GetGemsResponse() {
        super(200);
    }

    public List<GemType> getGems() {
        return List.of(GemType.values());
    }
}
